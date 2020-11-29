package xyz.kyngs.mc.fortuneteller.utils;

import org.bukkit.Material;

public class ItemUtil {

    public static String nameFor(Material material){
        String[] arr = material.name().toLowerCase().split("_");
        for (int i = 0; i < arr.length; i++) {
            char[] nameToTransform = arr[i].toCharArray();
            char firstChar = nameToTransform[0];
            nameToTransform[0] = Character.toUpperCase(firstChar);
            arr[i] = new String(nameToTransform);
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            builder.append(arr[i]);
            if (i == arr.length-1) continue;
            builder.append(' ');
        }

        return builder.toString();
    }

}
