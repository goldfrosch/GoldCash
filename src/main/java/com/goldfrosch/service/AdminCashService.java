package com.goldfrosch.service;

import com.goldfrosch.database.query.CashLogQuery;
import com.goldfrosch.database.query.CashQuery;
import com.goldfrosch.object.entity.CashDAO;
import com.goldfrosch.utils.PluginDataHolder;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.bukkit.plugin.Plugin;

public class AdminCashService extends PluginDataHolder {

  private final CashQuery cashQuery;
  private final CashLogQuery cashLogQuery;

  public AdminCashService(Plugin plugin, DataSource source, CashQuery cashQuery,
      CashLogQuery cashLogQuery) {
    super(plugin, source);
    this.cashQuery = cashQuery;
    this.cashLogQuery = cashLogQuery;
  }

  public void addCash(CashDAO cashDAO) {
    try {
      conn().setAutoCommit(false);
      cashQuery.addCash(cashDAO, conn());
      cashLogQuery.addCashLog(cashDAO, conn());

      conn().commit();
    } catch (SQLException e) {
      var errorMsg =
          cashDAO.getPlayer().getUniqueId() + " 유저 캐시 추가중 롤백 실패. 금액: " + cashDAO.getAmount()
              + ", 충전 타입: " + cashDAO.getCashChargeType().getType() + ", 처리자: "
              + cashDAO.getManager().getDisplayName();
      rollback(errorMsg);
    }
  }

  public void takeCash(CashDAO cashDAO) {
    try {
      conn().setAutoCommit(false);
      cashQuery.takeCash(cashDAO, conn());
      cashLogQuery.takeCashLog(cashDAO, conn());

      conn().commit();
    } catch (SQLException e) {
      var errorMsg =
          cashDAO.getPlayer().getUniqueId() + " 유저 캐시 회수중 롤백 실패. 금액: " + cashDAO.getAmount()
              + ", 처리자: " + cashDAO.getManager().getDisplayName();
      rollback(errorMsg);
    }
  }
}
