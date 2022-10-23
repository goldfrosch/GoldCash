package com.goldfrosch.database.query;

import com.goldfrosch.database.utils.PluginDataHolder;
import org.bukkit.ChatColor;
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

    public void addCash(Player player, long amount) {
        String query = "INSERT INTO player_cash SET uuid  = ?, cash = ? ON DUPLICATE KEY UPDATE cash = cash + ?;";
        try (
                var conn = conn();
                var stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setLong(2, amount);
            stmt.setLong(3, amount);
            stmt.execute();
        } catch (SQLException e) {
            player.sendMessage(ChatColor.RED + "에러가 발생하였습니다. 관리자에게 문의하세요");
            logSQLError("Could not add cash", e);
        }
    }

    public void takeCash(Player player, long amount) {
        try (var conn = conn(); var stmt = conn.prepareStatement(
                "UPDATE player_cash SET cash = IF(cash >= ?, cash - ?, 0) where uuid = ?;"
        )) {
            stmt.setLong(1, amount);
            stmt.setLong(2, amount);
            stmt.setString(3, player.getUniqueId().toString());
            int updated = stmt.executeUpdate();
        } catch (SQLException e) {
            player.sendMessage(ChatColor.RED + "에러가 발생하였습니다. 관리자에게 문의하세요");
            logSQLError("Could not take cash from player.", e);
        }
    }

    public void setCash(Player player, long amount) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "REPLACE player_cash(uuid, cash)  VALUES(?,?);"
        )) {
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setLong(2, amount);
            stmt.execute();
        } catch (SQLException e) {
            player.sendMessage(ChatColor.RED + "에러가 발생하였습니다. 관리자에게 문의하세요");
            logSQLError("Could not set cash", e);
        }
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
            player.sendMessage(ChatColor.RED + "에러가 발생하였습니다. 관리자에게 문의하세요");
            logSQLError("Could not retrieve player cash.", e);
        }
        return 0L;
    }
}
