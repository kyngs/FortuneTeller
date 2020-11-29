package xyz.kyngs.mc.fortuneteller.inventories;

import xyz.kyngs.mc.fortuneteller.FortuneTeller;
import xyz.kyngs.mc.fortuneteller.cache.PlayerProfile;
import xyz.kyngs.mc.fortuneteller.utils.ItemUtil;
import xyz.kyngs.mc.fortuneteller.wins.Round;
import xyz.kyngs.mc.fortuneteller.wins.RoundSpecificWinnableItem;
import xyz.kyngs.mc.fortuneteller.wins.WinnableItem;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static xyz.kyngs.mc.fortuneteller.Messages.*;

import java.util.concurrent.ThreadLocalRandom;

public class GameResultInventory implements InventoryProvider {

    private final FortuneTeller fortuneTeller;

    public GameResultInventory(FortuneTeller fortuneTeller) {
        this.fortuneTeller = fortuneTeller;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));
        Round round = fortuneTeller.getWinManager().getRounds()[fortuneTeller.getPlayerCache().get(player).getRound() - 1];
        RoundSpecificWinnableItem[] items = new RoundSpecificWinnableItem[100];
        int chanceSum = 0;
        for (RoundSpecificWinnableItem specificItem : round.getSpecificItems()) {
            for (int i = 0; i < specificItem.getChance(); i++) {
                items[chanceSum] = specificItem;
                chanceSum++;
            }
        }

        RoundSpecificWinnableItem whatHeWon = items[ThreadLocalRandom.current().nextInt(101)];
        
        Material circleMaterial;
        Material lookMaterial;
        int count;
        String itemName;

        if (whatHeWon == null){
            fortuneTeller.getTokenAPI().addTokens(player, round.getPixelCount());
            circleMaterial = fortuneTeller.getWinManager().getPixelCircleMaterial();
            lookMaterial = fortuneTeller.getWinManager().getPixelLookMaterial();
            count = 1;
            itemName = YOU_WON_PIXELS.replace("%%count%%", String.valueOf(round.getPixelCount()));
        }else {
            WinnableItem winnableItem = whatHeWon.getWinnableItem();
            circleMaterial = winnableItem.getCircleMaterial();
            lookMaterial = winnableItem.getLook();
            count = whatHeWon.getCount();
            itemName = YOU_WON_ITEM.replace("%%count%%", String.valueOf(count)).replace("%%item%%", ItemUtil.nameFor(lookMaterial));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), winnableItem.getCommandToExecute().replace("%%player%%", player.getName()).replace("%%count%%", String.valueOf(count)));
        }

        ItemStack look = new ItemStack(lookMaterial, count);
        ItemMeta lookMeta = look.getItemMeta();
        lookMeta.setDisplayName(itemName);
        look.setItemMeta(lookMeta);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(BACK_TO_MAIN_MENU);
        back.setItemMeta(backMeta);

        contents.fillRect(SlotPos.of(1, 3), SlotPos.of(3, 5), ClickableItem.empty(new ItemStack(circleMaterial)));
        contents.set(SlotPos.of(2, 4), ClickableItem.empty(look));
        contents.set(SlotPos.of(4, 4), ClickableItem.of(back, inventoryClickEvent -> fortuneTeller.getInventoryHandler().get("main").open((Player) inventoryClickEvent.getWhoClicked())));

        PlayerProfile pp = fortuneTeller.getPlayerCache().get(player);
        pp.setRound(pp.getRound()+1);

        @SuppressWarnings("DuplicatedCode")
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = book.getItemMeta();
        bookMeta.setDisplayName(BOOK_NAME);
        bookMeta.setLore(BOOK_LORE);
        book.setItemMeta(bookMeta);

        contents.set(SlotPos.of(5, 8), ClickableItem.empty(book));

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
