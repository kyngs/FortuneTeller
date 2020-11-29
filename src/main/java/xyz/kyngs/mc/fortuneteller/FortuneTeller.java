package xyz.kyngs.mc.fortuneteller;

import xyz.kyngs.mc.fortuneteller.cache.PlayerCache;
import xyz.kyngs.mc.fortuneteller.commands.OpenMainMenu;
import xyz.kyngs.mc.fortuneteller.database.DatabaseHandler;
import xyz.kyngs.mc.fortuneteller.wins.WinManager;
import fr.minuskube.inv.InventoryManager;
import me.realized.tokenmanager.TokenManagerPlugin;
import me.realized.tokenmanager.api.TokenManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.kyngs.mc.fortuneteller.utils.MessageUtil;

import java.io.File;
import java.sql.SQLException;

public final class FortuneTeller extends JavaPlugin {

    private TokenManager tokenAPI;
    private YamlConfiguration configuration;
    private InventoryHandler inventoryHandler;
    private PlayerCache playerCache;
    private WinManager winManager;

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    private DatabaseHandler databaseHandler;

    public TokenManager getTokenAPI() {
        return tokenAPI;
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void onEnable() {
        MessageUtil.console("Zapínání!");
        tokenAPI = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");
        MessageUtil.console(String.format("Detekován TokenManager verze: %s", ((TokenManagerPlugin) tokenAPI).getDescription().getVersion()));
        InventoryManager inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        inventoryHandler = new InventoryHandler(this, inventoryManager);

        File config = new File(getDataFolder(),"config.yaml");

        if (!config.exists()){
            saveResource("config.yaml",false );
        }

        configuration = YamlConfiguration.loadConfiguration(config);

        MessageUtil.console("Připojování k databázi!");

        try {
            databaseHandler = new DatabaseHandler(this);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }

        MessageUtil.console("Připojeno!");

        getCommand("vestkyne").setExecutor(new OpenMainMenu(this));
        playerCache = new PlayerCache(this);

        MessageUtil.console("Načítání možných výher a kol!");

        winManager = new WinManager(this);

        MessageUtil.console(String.format("Načteno %s výher a %s kol!", winManager.getLoadedItems().size()+1, winManager.getRounds().length));

        MessageUtil.console("Zapnuto!");
    }

    @Override
    public void onDisable() {
        MessageUtil.console("Vypínání");
        MessageUtil.console("Ukládání všech dat!");

        try {
            playerCache.saveAll(databaseHandler.getMysql().sync().get());
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.console("Nastala chyba při ukládání dat! Data budou ztracena!");
        }

        MessageUtil.console("Uloženo!");
        databaseHandler.getMysql().close();
        MessageUtil.console("Vypnuto!");
    }

    public InventoryHandler getInventoryHandler() {
        return inventoryHandler;
    }

    public WinManager getWinManager() {
        return winManager;
    }

    public PlayerCache getPlayerCache() {
        return playerCache;
    }
}
