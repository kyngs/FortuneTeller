package xyz.kyngs.mc.fortuneteller.wins;

import org.bukkit.Material;

public class WinnableItem {

    private final Material look;
    private final Material circleMaterial;
    private final String commandToExecute;

    public WinnableItem(Material look, Material circleMaterial, String commandToExecute) {
        this.look = look;
        this.circleMaterial = circleMaterial;
        this.commandToExecute = commandToExecute;
    }

    public Material getLook() {
        return look;
    }

    public Material getCircleMaterial() {
        return circleMaterial;
    }

    public String getCommandToExecute() {
        return commandToExecute;
    }
}
