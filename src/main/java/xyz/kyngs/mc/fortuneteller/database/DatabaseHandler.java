package xyz.kyngs.mc.fortuneteller.database;

import cz.kyngs.easymysql.MySQL;
import cz.kyngs.easymysql.MySQLBuilder;
import xyz.kyngs.mc.fortuneteller.FortuneTeller;
import xyz.kyngs.mc.fortuneteller.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseHandler {

    private final FortuneTeller fortuneTeller;
    private final MySQL mysql;

    public MySQL getMysql() {
        return mysql;
    }

    public DatabaseHandler(FortuneTeller fortuneTeller) throws SQLException {
        this.fortuneTeller = fortuneTeller;

        YamlConfiguration yamlConfiguration = fortuneTeller.getConfiguration();
        MySQLBuilder builder = new MySQLBuilder();
        builder.setUsername(yamlConfiguration.getString("database.username"));
        builder.setPassword(yamlConfiguration.getString("database.password"));
        builder.setJdbcUrl(String.format("jdbc:mysql://%s/%s", yamlConfiguration.getString("database.host_and_port"), yamlConfiguration.getString("database.database")));
        builder.setThreadCount(yamlConfiguration.getInt("database.thread_count"));
        mysql = builder.build();

        PreparedStatement ps = mysql.sync().get().prepareStatement("CREATE TABLE IF NOT EXISTS `vestkyne_player_profiles` ( `id` INT NOT NULL AUTO_INCREMENT , `name` VARCHAR(256) NOT NULL ,`round` INT NOT NULL , last_date_reset DATE NOT NULL, PRIMARY KEY (`id`));");
        ps.execute();

        Bukkit.getScheduler().runTaskTimer(fortuneTeller, bukkitTask -> {
            MessageUtil.console("Ukládání!");
            mysql.async().schedule(fortuneTeller.getPlayerCache()::saveAll);
            MessageUtil.console("Uloženo!");
        }, 600*20, 600*20);

    }

}
