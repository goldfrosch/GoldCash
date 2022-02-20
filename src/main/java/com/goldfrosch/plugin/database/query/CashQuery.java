package com.goldfrosch.plugin.database.query;

import com.goldfrosch.plugin.database.utils.PluginDataHolder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CashQuery extends PluginDataHolder {
    public CashQuery(Plugin plugin, DataSource source) {
        super(plugin, source);
    }

    public Boolean addCash(Player player, long amount) {
        String query = "INSERT INTO player_cash SET uuid  = ?, cash = ? ON DUPLICATE KEY UPDATE cash = cash + ?;";
        try (
                Connection conn = conn();
                PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setLong(2, amount);
            stmt.setLong(3, amount);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            logSQLError("Could not add cash", e);
        }
        return false;
    }

    public Boolean takeCash(Player player, long amount) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "UPDATE player_cash SET cash = IF(cash >= ?, cash - ?, 0) where uuid = ?;"
        )) {
            stmt.setLong(1, amount);
            stmt.setLong(2, amount);
            stmt.setString(3, player.getUniqueId().toString());
            int updated = stmt.executeUpdate();
            return updated == 1;
        } catch (SQLException e) {
            logSQLError("Could not take cash from player.", e);
        }
        return false;
    }

    public Boolean setCash(Player player, long amount) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "REPLACE player_cash(uuid, cash)  VALUES(?,?);"
        )) {
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setLong(2, amount);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            logSQLError("Could not set cash", e);
        }
        return false;
    }

    public Long getCash(Player player) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "select cash from player_cash where uuid = ?;"
        )) {
            stmt.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("cash");
            }
        } catch (SQLException e) {
            logSQLError("Could not retrieve player cash.", e);
        }
        return Long.valueOf(0);

    }
}
