package xyz.kyngs.mc.fortuneteller.wins;

import xyz.kyngs.mc.fortuneteller.FortuneTeller;
import xyz.kyngs.mc.fortuneteller.utils.MessageUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class WinManager {

    private final Map<String, WinnableItem> loadedItems;
    private final Round[] rounds;
    private final Material pixelLookMaterial;
    private final Material pixelCircleMaterial;

    public Material getPixelLookMaterial() {
        return pixelLookMaterial;
    }

    public Material getPixelCircleMaterial() {
        return pixelCircleMaterial;
    }

    public WinManager(FortuneTeller fortuneTeller){

        YamlConfiguration configuration = fortuneTeller.getConfiguration();

        pixelLookMaterial = Material.valueOf(configuration.getString("pixel_look_material").toUpperCase());
        pixelCircleMaterial = Material.valueOf(configuration.getString("pixel_circle_material").toUpperCase());

        loadedItems = new HashMap<>();

        ConfigurationSection winnableItemsSection = configuration.getConfigurationSection("winnable_items");

        for (String key : winnableItemsSection != null ? winnableItemsSection.getKeys(false) : Collections.<String>emptySet()) {
            try {
                String commandToExecute = configuration.getString(String.format("winnable_items.%s.command", key));
                Material circleMaterial = Material.valueOf(configuration.getString(String.format("winnable_items.%s.circle_material", key)).toUpperCase());
                Material look = Material.valueOf(configuration.getString(String.format("winnable_items.%s.item_material", key)).toUpperCase());
                loadedItems.put(key,new WinnableItem(look, circleMaterial, commandToExecute));
            } catch (Exception e){
                MessageUtil.console(String.format("Nastala chyba při načítání itemu %s! Nezadali jste špatně název materiálu?", key));
                e.printStackTrace();
            }
        }

        List<Round> rounds = new ArrayList<>();

        ConfigurationSection roundsSection = configuration.getConfigurationSection("rounds");

        for (String key : roundsSection != null ? roundsSection.getKeys(false) : Collections.<String>emptySet()){
            int countInRow;
            try {
                countInRow = Integer.parseInt(key);
            } catch (NumberFormatException e) {
                MessageUtil.console(String.format("Nastala chyba při načítání kola %s! Je toto kolo číslo?", key));
                throw new RuntimeException(e);
            }
            int pixelPrice = configuration.getInt(String.format("rounds.%s.price", key));
            int pixelWinChance = configuration.getInt(String.format("rounds.%s.pixels.chance", key));
            int pixelCount = configuration.getInt(String.format("rounds.%s.pixels.count", key));
            Set<RoundSpecificWinnableItem> items = new HashSet<>();
            ConfigurationSection roundItemsSection = configuration.getConfigurationSection(String.format("rounds.%s.winnable_items", key));

            int totalPercentage = pixelWinChance;

            for (String roundItemsSectionKey : roundItemsSection != null ? roundItemsSection.getKeys(false) : Collections.<String>emptySet()) {
                WinnableItem item = loadedItems.get(roundItemsSectionKey);

                if (item == null){
                    MessageUtil.console(String.format("V kole %s byl specifikován item %s, avšak tento item nebyl nalezen. Nezapomněli jste ho definovat? Tento item nebude tudíž načten, avšak toto není fatální chyba.", key, roundItemsSection));
                    continue;
                }

                int itemCount = configuration.getInt(String.format("rounds.%s.winnable_items.%s.count", key, roundItemsSectionKey));
                int itemChance = configuration.getInt(String.format("rounds.%s.winnable_items.%s.chance", key, roundItemsSectionKey));

                if (100 - (totalPercentage + itemChance) < 0){

                    MessageUtil.console(String.format("Součet percentuálních šancí v kole %s přesáhl sto, zkracuji šanci na tento item a přestávám načítat další.",key));
                    itemChance += 100 - (totalPercentage + itemChance);

                }
                totalPercentage += itemChance;

                items.add(new RoundSpecificWinnableItem(item, itemCount, itemChance));

                if (totalPercentage >= 100) {
                    totalPercentage = pixelWinChance;
                    break;
                }

            }

            rounds.add(new Round(pixelPrice, countInRow, pixelWinChance, pixelCount, items));

        }

        this.rounds = rounds.toArray(new Round[0]);


    }

    public Map<String, WinnableItem> getLoadedItems() {
        return loadedItems;
    }

    public Round[] getRounds() {
        return rounds;
    }
}
