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
    } catch (SQLException e) {
      player.sendMessage(ChatColor.RED + "에러가 발생하였습니다. 관리자에게 문의하세요");
    }
  }
}
