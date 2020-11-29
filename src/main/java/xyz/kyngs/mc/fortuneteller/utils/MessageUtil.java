package xyz.kyngs.mc.fortuneteller.utils;

import org.bukkit.Bukkit;

public class MessageUtil {

    public static void console(String message){
        Bukkit.getConsoleSender().sendMessage(String.format("[Vestkyne] %s", message));
    }

}
