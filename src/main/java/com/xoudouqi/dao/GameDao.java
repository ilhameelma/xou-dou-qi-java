package com.xoudouqi.dao;

import com.xoudouqi.model.Game;
import com.xoudouqi.model.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameDao {

    public void saveGame(Game game) {
        String sql = "INSERT INTO games (player1_id, player2_id, winner_id, moves, start_time, end_time) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, game.getPlayer1().getId());
            pstmt.setInt(2, game.getPlayer2().getId());
            if (game.getWinner() != null) {
                pstmt.setInt(3, game.getWinner().getId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setString(4, game.getMovesHistory());
            pstmt.setTimestamp(5, new Timestamp(game.getStartDate().getTime()));
            pstmt.setTimestamp(6, new Timestamp(game.getEndDate().getTime()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Game> getGamesByPlayer(int playerId) {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT g.*, " +
                     "p1.id AS p1_id, p1.username AS p1_username, " +
                     "p2.id AS p2_id, p2.username AS p2_username, " +
                     "w.id AS winner_id, w.username AS winner_username " +
                     "FROM games g " +
                     "JOIN players p1 ON g.player1_id = p1.id " +
                     "JOIN players p2 ON g.player2_id = p2.id " +
                     "LEFT JOIN players w ON g.winner_id = w.id " +
                     "WHERE g.player1_id = ? OR g.player2_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerId);
            pstmt.setInt(2, playerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Player p1 = new Player(rs.getInt("p1_id"), rs.getString("p1_username"));
                Player p2 = new Player(rs.getInt("p2_id"), rs.getString("p2_username"));
                Player winner = null;

                int winnerId = rs.getInt("winner_id");
                if (!rs.wasNull()) {
                    winner = new Player(winnerId, rs.getString("winner_username"));
                }

                Game game = new Game(p1, p2);
                game.setWinner(winner);
                game.setMovesHistory(rs.getString("moves"));
                game.setStartDate(new Date(rs.getTimestamp("start_time").getTime()));
                game.setEndDate(new Date(rs.getTimestamp("end_time").getTime()));

                games.add(game);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }
}
