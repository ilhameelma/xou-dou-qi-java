package com.xoudouqi.dao;

import com.xoudouqi.model.Player;
import java.sql.*;
import java.util.Optional;

public class PlayerDao {
    public Optional<Player> findByUsername(String username) {
        String sql = "SELECT * FROM players WHERE username = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Player player = new Player();
                player.setId(rs.getInt("id"));
                player.setUsername(rs.getString("username"));
                player.setPassword(rs.getString("password"));
                player.setWins(rs.getInt("wins"));
                player.setLosses(rs.getInt("losses"));
                player.setDraws(rs.getInt("draws"));
                return Optional.of(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean create(Player player) {
        String sql = "INSERT INTO players (username, password) VALUES (?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, player.getUsername());
            pstmt.setString(2, player.getPassword());
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        player.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateStats(Player player) {
        String sql = "UPDATE players SET wins = ?, losses = ?, draws = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, player.getWins());
            pstmt.setInt(2, player.getLosses());
            pstmt.setInt(3, player.getDraws());
            pstmt.setInt(4, player.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}