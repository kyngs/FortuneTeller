package xyz.kyngs.mc.fortuneteller.commands;

import xyz.kyngs.mc.fortuneteller.FortuneTeller;
import xyz.kyngs.mc.fortuneteller.cache.PlayerCache;
import xyz.kyngs.mc.fortuneteller.utils.DurationUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalTime;

import static xyz.kyngs.mc.fortuneteller.Messages.*;

public class OpenMainMenu implements CommandExecutor {
    private final FortuneTeller fortuneTeller;

    public OpenMainMenu(FortuneTeller fortuneTeller) {
        this.fortuneTeller = fortuneTeller;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        LocalTime now = LocalTime.now();
        PlayerCache cache = fortuneTeller.getPlayerCache();
        if (now.isAfter(cache.getCloseStart()) && now.isBefore(cache.getTimeToCompleteReset())){
            Duration duration = Duration.between(now, cache.getTimeToCompleteReset());
            player.sendMessage(CLOSED.replace("%%min%%", String.valueOf(DurationUtil.toMinutePart(duration))).replace("%%sec%%", String.valueOf(DurationUtil.toSecondsPart(duration))));
            return true;
        }

        fortuneTeller.getInventoryHandler().get("main").open(player);

        return true;
    }
}
