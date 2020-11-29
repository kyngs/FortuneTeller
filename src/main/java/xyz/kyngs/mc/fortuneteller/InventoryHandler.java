package xyz.kyngs.mc.fortuneteller;

import xyz.kyngs.mc.fortuneteller.inventories.GameResultInventory;
import xyz.kyngs.mc.fortuneteller.inventories.InGameInventory;
import xyz.kyngs.mc.fortuneteller.inventories.MainInventory;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;

import java.util.HashMap;
import java.util.Map;

public class InventoryHandler {

    private final Map<String, SmartInventory> inventories;

    public InventoryHandler(FortuneTeller fortuneTeller, InventoryManager inventoryManager){
        inventories = new HashMap<>();
        inventories.put("main", SmartInventory.builder()
                .id("main")
                .provider(new MainInventory(fortuneTeller))
                .size(6, 9)
                .title(Messages.MAIN_INVENTORY_NAME)
                .manager(inventoryManager)
                .build()
        );
        inventories.put("ingame", SmartInventory.builder()
                .id("ingame")
                .provider(new InGameInventory(fortuneTeller))
                .size(6, 9)
                .title(Messages.IN_GAME_INVENTORY_NAME)
                .manager(inventoryManager)
                .closeable(false)
                .build()
        );
        inventories.put("gameresult", SmartInventory.builder()
                .id("gameresult")
                .provider(new GameResultInventory(fortuneTeller))
                .size(6, 9)
                .title(Messages.GAME_RESULT_INVENTORY_NAME)
                .manager(inventoryManager)
                .build()
        );
    }

    public SmartInventory get(String name){
        return inventories.get(name);
    }

}
