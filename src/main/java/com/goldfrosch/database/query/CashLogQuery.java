package com.goldfrosch.database.query;

import com.goldfrosch.object.entity.CashDAO;
import java.sql.Connection;
import java.sql.SQLException;
import org.bukkit.ChatColor;

public class CashLogQuery {

  public void addCashLog(CashDAO cashDAO, Connection conn) {
    var player = cashDAO.getPlayer();
    try (
        var stmt = conn.prepareStatement(
            "INSERT INTO player_cash_log SET uuid  = ?, amount = ?, cash_charge_type = ?, log_status = ?, manager = ?;")
    ) {
      var money = cashDAO.getAmount();
      stmt.setString(1, player.getUniqueId().toString());
      stmt.setLong(2, money);
      stmt.setString(3, cashDAO.getCashChargeType().toString());
      stmt.setString(4, cashDAO.getCashUseStatus().toString());
      stmt.setString(5, cashDAO.getManager().getUniqueId().toString());
    } catch (SQLException e) {
      player.sendMessage(ChatColor.RED + "에러가 발생하였습니다. 관리자에게 문의하세요");
    }
  }

  public void takeCashLog(CashDAO cashDAO, Connection conn) {
    var player = cashDAO.getPlayer();
    try (
        var stmt = conn.prepareStatement(
            "INSERT INTO player_cash_log SET uuid  = ?, amount = ?, log_status = ?, manager = ?;")
    ) {
      var money = cashDAO.getAmount();
      stmt.setString(1, player.getUniqueId().toString());
      stmt.setLong(2, money);
      stmt.setString(3, cashDAO.getCashUseStatus().toString());
      stmt.setString(4, cashDAO.getManager().getUniqueId().toString());
    } catch (SQLException e) {
      player.sendMessage(ChatColor.RED + "에러가 발생하였습니다. 관리자에게 문의하세요");
    }
  }
}
