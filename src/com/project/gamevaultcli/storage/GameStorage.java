package com.project.gamevaultcli.storage;

import com.project.gamevaultcli.entities.Game;
import com.project.gamevaultcli.interfaces.StorageInterface;
import com.project.gamevaultcli.utils.DBUtil;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GameStorage implements StorageInterface<Game, Integer>{

    @Override
    public Game findById(Integer gameId) {
        String sql = "SELECT * FROM Games WHERE gameId = ?";
        try {
            List<Game> games = DBUtil.executeQuery(sql, rs -> mapResultSetToGame(rs), gameId);
            return games.isEmpty() ? null : games.get(0);
        } catch (SQLException | IOException e) {
            System.err.println("Error finding game by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Game> findAll() {
        String sql = "SELECT * FROM Games";
        try {
            return DBUtil.executeQuery(sql, rs -> mapResultSetToGame(rs));
        } catch (SQLException | IOException e) {
            System.err.println("Error finding all games: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void save(Game game) {
        String sql = "INSERT INTO Games (title, description, developer, platform, price, releaseDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (ResultSet generatedKeys = DBUtil.executeInsert(sql, game.getTitle(), game.getDescription(), game.getDeveloper(), game.getPlatform(), game.getPrice(), new Date(game.getReleaseDate().getTime()))) {

            if (generatedKeys.next()) {
                game.setGameId(generatedKeys.getInt(1));
            }
        } catch (SQLException | IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    @Override
    public void update(Game game) {
        String sql = "UPDATE Games SET title = ?, description = ?, developer = ?, platform = ?, price = ?, releaseDate = ? WHERE gameId = ?";
        try {
            DBUtil.executeUpdate(sql, game.getTitle(), game.getDescription(), game.getDeveloper(), game.getPlatform(), game.getPrice(), new Date(game.getReleaseDate().getTime()), game.getGameId());
        } catch (SQLException | IOException e) {
            System.err.println("Error updating game: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer gameId) {
        String sql = "DELETE FROM Games WHERE gameId = ?";
        try {
            DBUtil.executeUpdate(sql, gameId);
        } catch (SQLException | IOException e) {
            System.err.println("Error deleting game: " + e.getMessage());
        }
    }

    public Game mapResultSetToGame(ResultSet rs) throws SQLException {
        return new Game(
                rs.getInt("gameId"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("developer"),
                rs.getString("platform"),
                rs.getFloat("price"),
                rs.getDate("releaseDate")
        );
    }
}