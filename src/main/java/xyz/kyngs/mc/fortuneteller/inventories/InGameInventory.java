package xyz.kyngs.mc.fortuneteller.inventories;

import xyz.kyngs.mc.fortuneteller.FortuneTeller;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static xyz.kyngs.mc.fortuneteller.Messages.BOOK_LORE;
import static xyz.kyngs.mc.fortuneteller.Messages.BOOK_NAME;
import static fr.minuskube.inv.content.SlotPos.of;

public class InGameInventory implements InventoryProvider {

    private final FortuneTeller fortuneTeller;

    public InGameInventory(FortuneTeller fortuneTeller){
        this.fortuneTeller = fortuneTeller;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);

        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = book.getItemMeta();
        bookMeta.setDisplayName(BOOK_NAME);
        bookMeta.setLore(BOOK_LORE);
        book.setItemMeta(bookMeta);

        contents.set(SlotPos.of(5, 8), ClickableItem.empty(book));

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int state = contents.property("state", 0);

        if (state % 16 == 0){
            contents.fillRect(of(1, 3), of(3, 5), ClickableItem.empty(new ItemStack(Material.PINK_STAINED_GLASS_PANE)));
            contents.set(of(2, 4), ClickableItem.empty(new ItemStack(Material.PINK_STAINED_GLASS_PANE)));
        } else if (state % 8 == 0) {
            contents.fillRect(of(1, 3), of(3, 5), ClickableItem.empty(new ItemStack(Material.PURPLE_STAINED_GLASS_PANE)));
            contents.set(of(2, 4), ClickableItem.empty(new ItemStack(Material.PURPLE_STAINED_GLASS_PANE)));
        }


        if (state == 39){
            fortuneTeller.getInventoryHandler().get("gameresult").open(player);
        }else {
            contents.setProperty("state", state + 1);
        }




    }
}
