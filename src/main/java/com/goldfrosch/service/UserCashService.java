package com.goldfrosch.service;

import com.goldfrosch.database.query.CashQuery;
import com.goldfrosch.utils.PluginDataHolder;
import java.util.logging.Level;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class UserCashService extends PluginDataHolder {

  private final CashQuery cashQuery;

  public UserCashService(Plugin plugin, DataSource source, CashQuery cashQuery) {
    super(plugin, source);
    this.cashQuery = cashQuery;
  }

  public Long getPlayerCash(Player player) {
    try {
      return cashQuery.getCash(player, conn());
    } catch (SQLException e) {
      logSQLError(Level.SEVERE, "Could not set cash", e);
      return 0L;
    }
  }
}
