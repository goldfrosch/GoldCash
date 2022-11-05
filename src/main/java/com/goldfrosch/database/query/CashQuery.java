package com.goldfrosch.database.query;

import com.goldfrosch.object.entity.CashDAO;
import java.sql.Connection;
import org.bukkit.ChatColor;

import java.sql.SQLException;
import org.bukkit.entity.Player;

public class CashQuery {

  public void addCash(CashDAO cashDAO, Connection conn) {
    var player = cashDAO.getPlayer();
    try (
        var stmt = conn.prepareStatement(
            "INSERT INTO player_cash_log SET uuid  = ?, cash = ? ON DUPLICATE KEY UPDATE cash = cash + ?;")
    ) {
      var uuid = player.getUniqueId();
      var amount = cashDAO.getAmount();
      stmt.setString(1, uuid.toString());
      stmt.setLong(2, amount);
      stmt.setLong(3, amount);
      stmt.execute();
    } catch (SQLException e) {
      player.sendMessage(ChatColor.RED + "에러가 발생하였습니다. 관리자에게 문의하세요");
    }
  }

//  public void takeCash(Player player, long amount) {
//    try (var conn = conn(); var stmt = conn.prepareStatement(
//        "UPDATE player_cash SET cash = IF(cash >= ?, cash - ?, 0) where uuid = ?;"
//    )) {
//      stmt.setLong(1, amount);
//      stmt.setLong(2, amount);
//      stmt.setString(3, player.getUniqueId().toString());
//      int updated = stmt.executeUpdate();
//    } catch (SQLException e) {
//      player.sendMessage(ChatColor.RED + "에러가 발생하였습니다. 관리자에게 문의하세요");
//      logSQLError("Could not take cash from player.", e);
//    }
//  }
//
//  public void setCash(Player player, long amount) {
//    try (var conn = conn(); var stmt = conn.prepareStatement(
//        "REPLACE player_cash(uuid, cash)  VALUES(?,?);"
//    )) {
//      stmt.setString(1, player.getUniqueId().toString());
//      stmt.setLong(2, amount);
//      stmt.execute();
//    } catch (SQLException e) {
//      player.sendMessage(ChatColor.RED + "에러가 발생하였습니다. 관리자에게 문의하세요");
//      logSQLError("Could not set cash", e);
//    }
//  }
//
  public Long getCash(Player player, Connection conn) {
    try (var stmt = conn.prepareStatement(
        "select cash from player_cash where uuid = ?;"
    )) {
      stmt.setString(1, player.getUniqueId().toString());
      var resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        return resultSet.getLong("cash");
      }
    } catch (SQLException e) {
      player.sendMessage(ChatColor.RED + "에러가 발생하였습니다. 관리자에게 문의하세요");
    }
    return 0L;
  }
}
