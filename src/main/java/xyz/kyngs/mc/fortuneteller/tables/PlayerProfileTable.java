package xyz.kyngs.mc.fortuneteller.tables;

import xyz.kyngs.mc.fortuneteller.cache.PlayerProfile;

import java.sql.*;

public class PlayerProfileTable {

    public static PlayerProfile get(String name, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM vestkyne_player_profiles WHERE name=?");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new PlayerProfile(rs.getString("name"), rs.getDate("last_date_reset").toLocalDate() ,rs.getInt("round"));
        }
        ps.close();
        return null;
    }

    public static void insert(PlayerProfile pp, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO vestkyne_player_profiles (name, round, last_date_reset) VALUES (?, ?, ?)");
        ps.setString(1, pp.getName());
        ps.setInt(2,pp.getRound());
        ps.setDate(3, Date.valueOf(pp.getLastResetDate()));
        ps.execute();
        ps.close();
    }

    public static void update(PlayerProfile pp, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE vestkyne_player_profiles SET name=?, round=?, last_date_reset=? WHERE name=?");
        ps.setString(1, pp.getName());
        ps.setInt(2,pp.getRound());
        ps.setDate(3, Date.valueOf(pp.getLastResetDate()));
        ps.setString(4, pp.getName());
        ps.execute();
        ps.close();
    }

}
