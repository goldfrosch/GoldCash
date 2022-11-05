package com.goldfrosch.placeholder;

import com.goldfrosch.GoldCash;
import com.goldfrosch.database.query.CashQuery;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import javax.sql.DataSource;

public class Placeholders extends PlaceholderExpansion {

  private final GoldCash plugin;
  private final CashQuery cashQuery;

  public Placeholders(GoldCash plugin, DataSource dataSource) {
    this.plugin = plugin;
    cashQuery = new CashQuery(plugin, dataSource);
  }

  @Override
  public boolean canRegister() {
    return plugin != null;
  }

  @Override
  public String getAuthor() {
    return "GoldFrosch";
  }

  @Override
  public String getIdentifier() {
    return "cash";
  }

  @Override
  public String getVersion() {
    return "1.0.0-SNAPSHOT";
  }

  @Override
  public boolean persist() {
    return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
  }

  @Override
  public String onPlaceholderRequest(Player p, String identifier) {
    if (p == null) {
      return "";
    }
    // %cash_amount%
    if (identifier.equalsIgnoreCase("amount")) {
      return String.valueOf(cashQuery.getCash(p));
    }

    return null;
  }
}
