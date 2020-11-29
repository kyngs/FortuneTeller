package xyz.kyngs.mc.fortuneteller.inventories;

import xyz.kyngs.mc.fortuneteller.FortuneTeller;
import xyz.kyngs.mc.fortuneteller.cache.PlayerProfile;
import xyz.kyngs.mc.fortuneteller.utils.DurationUtil;
import xyz.kyngs.mc.fortuneteller.utils.skull.Skull;
import xyz.kyngs.mc.fortuneteller.wins.Round;
import xyz.kyngs.mc.fortuneteller.wins.WinManager;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

import static xyz.kyngs.mc.fortuneteller.Messages.*;

public class MainInventory implements InventoryProvider {

    private final FortuneTeller fortuneTeller;

    public MainInventory(FortuneTeller fortuneTeller){
        this.fortuneTeller = fortuneTeller;
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        PlayerProfile pp = fortuneTeller.getPlayerCache().get(player);
        WinManager winManager = fortuneTeller.getWinManager();

        int roundNum = pp.getRound();

        contents.fill(ClickableItem.empty(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));
        contents.fillRect(SlotPos.of(1, 3), SlotPos.of(3, 5), ClickableItem.empty(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));

        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = book.getItemMeta();
        bookMeta.setDisplayName(BOOK_NAME);
        bookMeta.setLore(BOOK_LORE);
        book.setItemMeta(bookMeta);

        contents.set(SlotPos.of(5, 8), ClickableItem.empty(book));

        if (roundNum > winManager.getRounds().length){
            LocalTime now = LocalTime.now();
            if ((now.isAfter(fortuneTeller.getPlayerCache().getTimeToCompleteReset()) || now.equals(fortuneTeller.getPlayerCache().getTimeToCompleteReset())) && LocalDate.now().isAfter(pp.getLastResetDate())){
                pp.setRound(1);
                pp.setLastResetDate(LocalDate.now());

            }else {

                Duration dur = Duration.between(now, fortuneTeller.getPlayerCache().getTimeToCompleteReset());

                if (dur.isNegative()){
                    dur = dur.plusDays(1);
                }

                ItemStack ball = new ItemStack(NO_ROUNDS_MATERIAL);
                ItemMeta ballMeta = ball.getItemMeta();
                ballMeta.setDisplayName(ALL_ROUNDS_GONE.replace("%%min%%", String.valueOf(DurationUtil.toMinutePart(dur))).replace("%%hour%%", String.valueOf(DurationUtil.toHoursPart(dur))));
                ball.setItemMeta(ballMeta);

                contents.set(SlotPos.of(2, 4), ClickableItem.empty(ball));

                return;
            }
        }

        Round round = fortuneTeller.getWinManager().getRounds()[roundNum-1];

        ItemStack ball = Skull.getCustomSkull("http://textures.minecraft.net/texture/3a5a0715c62122ded65af4eae0969f23f571b0afa50cf93fc9ee2af4c7b34e12");
        ItemMeta ballMeta = ball.getItemMeta();
        ballMeta.setDisplayName(BALL);
        ball.setItemMeta(ballMeta);

        ItemStack start = new ItemStack(Material.MAGMA_CREAM);
        ItemMeta startMeta = start.getItemMeta();
        startMeta.setDisplayName(START);
        List<String> lore = new ArrayList<>();
        lore.add(START_LORE_PIXEL_COUNT.replace("%%count%%", String.valueOf(round.getPixelCount())));
        lore.add(START_LORE_PIXEL_PRICE.replace("%%count%%", String.valueOf(round.getPixelPrice())));
        startMeta.setLore(lore);
        start.setItemMeta(startMeta);

        contents.set(SlotPos.of(2, 4), ClickableItem.empty(ball));
        OptionalLong optional = fortuneTeller.getTokenAPI().getTokens(player);
        long playerTokens = optional.isPresent() ? optional.getAsLong() : 0;
        
        if (playerTokens < round.getPixelPrice()){
            
            ItemStack cantAfford = new ItemStack(Material.BARRIER);
            ItemMeta cantAffordMeta = cantAfford.getItemMeta();
            cantAffordMeta.setDisplayName(CANT_AFFORD_ROUND.replace("%%count%%", String.valueOf(round.getPixelPrice() - playerTokens)));
            cantAfford.setItemMeta(cantAffordMeta);
            
            contents.set(SlotPos.of(4, 4), ClickableItem.empty(cantAfford));
        } else{
            contents.set(SlotPos.of(4, 4), ClickableItem.of(start, inventoryClickEvent -> {
                fortuneTeller.getInventoryHandler().get("ingame").open((Player) inventoryClickEvent.getWhoClicked());
                fortuneTeller.getTokenAPI().removeTokens(player, round.getPixelPrice());
            }));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
