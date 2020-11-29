package xyz.kyngs.mc.fortuneteller;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Messages {

    public static final String CANT_AFFORD_ROUND;
    public static final String BOOK_NAME;
    public static final String START_LORE_PIXEL_COUNT;
    public static final String START_LORE_PIXEL_PRICE;
    public static final String START;
    public static final String BALL;
    public static final String ALL_ROUNDS_GONE;
    public static final String CLOSED;
    public static final String YOU_WON_PIXELS;
    public static final String YOU_WON_ITEM;
    public static final String MAIN_INVENTORY_NAME;
    public static final String IN_GAME_INVENTORY_NAME;
    public static final String GAME_RESULT_INVENTORY_NAME;
    public static final String BACK_TO_MAIN_MENU;
    public static final List<String> BOOK_LORE;
    public static final Material NO_ROUNDS_MATERIAL;

    static {
        File messages = new File(FortuneTeller.getPlugin(FortuneTeller.class).getDataFolder(), "messages.yaml");

        if (!messages.exists()){
            FortuneTeller.getPlugin(FortuneTeller.class).saveResource("messages.yaml", false);
        }

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(messages);

        CANT_AFFORD_ROUND = configuration.getString("cant_afford_round");
        BOOK_NAME = configuration.getString("book_name");
        START_LORE_PIXEL_COUNT = configuration.getString("start_lore_pixel_count");
        START_LORE_PIXEL_PRICE = configuration.getString("start_lore_pixel_price");
        START = configuration.getString("start");
        BALL = configuration.getString("ball");
        ALL_ROUNDS_GONE = configuration.getString("all_rounds_gone");
        CLOSED = configuration.getString("closed");
        YOU_WON_PIXELS = configuration.getString("you_won_pixels");
        YOU_WON_ITEM = configuration.getString("you_won_item");
        MAIN_INVENTORY_NAME = configuration.getString("main_inventory_name");
        IN_GAME_INVENTORY_NAME = configuration.getString("in_game_inventory_name");
        GAME_RESULT_INVENTORY_NAME = configuration.getString("game_result_inventory_name");
        BACK_TO_MAIN_MENU = configuration.getString("back_to_main_menu");
        BOOK_LORE = configuration.getStringList("book_lore");
        NO_ROUNDS_MATERIAL = Material.valueOf(configuration.getString("no_rounds_material").toUpperCase());

    }

}
