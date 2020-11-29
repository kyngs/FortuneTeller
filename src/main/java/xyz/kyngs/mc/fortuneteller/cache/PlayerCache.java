package xyz.kyngs.mc.fortuneteller.cache;

import cz.kyngs.easymysql.connection.sync.SyncConnection;
import xyz.kyngs.mc.fortuneteller.FortuneTeller;
import xyz.kyngs.mc.fortuneteller.tables.PlayerProfileTable;
import xyz.kyngs.mc.fortuneteller.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class PlayerCache implements Listener {

    private final FortuneTeller fortuneTeller;
    private final Map<String, PlayerProfile> cache;
    private final LocalTime timeToCompleteReset;
    private final LocalTime closeStart;

    public PlayerCache(FortuneTeller fortuneTeller){
        this.fortuneTeller = fortuneTeller;

        try {
            String closeTimeStartFromConfig = fortuneTeller.getConfiguration().getString("start_of_reset");
            String[] closeSplit = closeTimeStartFromConfig.split(":");
            closeStart = LocalTime.of(Integer.parseInt(closeSplit[0]), Integer.parseInt(closeSplit[1]));
            String timeFromConfig = fortuneTeller.getConfiguration().getString("end_of_reset");
            String[] split = timeFromConfig.split(":");
            timeToCompleteReset = LocalTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        } catch (Exception e) {
            MessageUtil.console("Failed to load reset time!");
            throw new RuntimeException(e);
        }
        cache = new HashMap<>();
        fortuneTeller.getServer().getPluginManager().registerEvents(this, fortuneTeller);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            try {
                handlePlayer(onlinePlayer.getName());
            } catch (Exception e) {
                onlinePlayer.kickPlayer("Failed to load data!");
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event){
        try {
            handlePlayer(event.getName());
        }catch (Exception e){
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Failed to load data!");
            e.printStackTrace();
        }

    }

    public void handlePlayer(String name) throws Exception{
        SyncConnection sync = fortuneTeller.getDatabaseHandler().getMysql().sync();
        PlayerProfile pp = PlayerProfileTable.get(name, sync.get());
        if (pp == null){
            pp = new PlayerProfile(name, LocalDate.now(),1);
            PlayerProfileTable.insert(pp, sync.get());
        }
        cache.put(name, pp);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        PlayerProfile pp = cache.get(event.getPlayer().getName());
        cache.remove(event.getEventName());
        fortuneTeller.getDatabaseHandler().getMysql().async().schedule(connection -> PlayerProfileTable.update(pp, connection));
    }

    public PlayerProfile get(Player player){
        return cache.get(player.getName());
    }

    public void saveAll(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE vestkyne_player_profiles SET name=?, round=?, last_date_reset=? WHERE name=?");

        for (PlayerProfile pp : cache.values()) {
            ps.setString(1, pp.getName());
            ps.setInt(2,pp.getRound());
            ps.setDate(3, Date.valueOf(pp.getLastResetDate()));
            ps.setString(4, pp.getName());
            ps.addBatch();
        }

        ps.executeBatch();
        ps.close();
    }

    public LocalTime getTimeToCompleteReset() {
        return timeToCompleteReset;
    }

    public LocalTime getCloseStart() {
        return closeStart;
    }
}
