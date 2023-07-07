package org.thanhmagics.afkshards;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDataSave {

    private Connection connection;


    public void init(String url,String username,String pass) {
//        try (Connection connection = DriverManager.getConnection(url, username, pass)){
//            this.connection = connection;
//            Statement statement = connection.createStatement();
//            statement.executeUpdate("CREATE TABLE saves (uuid VARCHAR(32) NOT NULL, shards VARCHAR(64) NOT NULL, PRIMARY KEY (uuid)");
//            statement.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        File file = new File(AFKShards.getInstance().getDataFolder(),"database.db");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("JDBC:SQLITE:" + file);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Statement s = connection.createStatement();
            s.executeUpdate("CREATE TABLE IF NOT EXISTS saves (uuid VARCHAR(32) NOT NULL, shards VARCHAR(64) NOT NULL, PRIMARY KEY (uuid))");
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<PPlayer> initPlayer() {
        List<PPlayer> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM saves")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PPlayer pPlayer = new PPlayer(UUID.fromString(rs.getString("uuid")));
                pPlayer.setShards(BigDecimal.valueOf(Long.parseLong(rs.getString("shards"))));
                list.add(pPlayer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public BigDecimal getShards(PPlayer player) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM saves WHERE uuid = ?")) {
            ps.setString(1, player.getUuid().toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new BigDecimal(rs.getString("shards"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void setShards(PPlayer player,BigDecimal decimal) {
        try (PreparedStatement ps = connection.prepareStatement("REPLACE INTO saves (uuid, shards) VALUES (?, ?)")) {
            ps.setString(1, player.getUuid().toString());
            ps.setString(2, String.valueOf(decimal.intValue()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveAll() {
        try (PreparedStatement ps = connection.prepareStatement("REPLACE INTO saves (uuid, shards) VALUES (?, ?)")) {
            for (PPlayer player : AFKShards.getInstance().getPlayerMap().values()) {
                ps.setString(1, player.getUuid().toString());
                ps.setString(2, String.valueOf(player.getShards().intValue()));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
