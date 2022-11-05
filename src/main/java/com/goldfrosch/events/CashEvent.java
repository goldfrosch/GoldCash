package com.goldfrosch.events;

import com.goldfrosch.GoldCash;
import com.goldfrosch.database.query.CashQuery;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.sql.DataSource;

public class CashEvent implements Listener {
  private final CashQuery cashQuery;

  public CashEvent(GoldCash plugin, DataSource dataSource){
    this.cashQuery = new CashQuery(plugin, dataSource);
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onPlayerJoinEvent(PlayerJoinEvent e) {
    cashQuery.addCash(e.getPlayer(), 0);
  }
}
