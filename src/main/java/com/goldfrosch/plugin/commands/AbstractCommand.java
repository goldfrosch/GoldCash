package com.goldfrosch.plugin.commands;

import com.goldfrosch.plugin.MainPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import javax.sql.DataSource;
import java.util.List;

public abstract class AbstractCommand implements TabExecutor {
  protected MainPlugin plugin;
  private String Command;
  private DataSource dataSource;

  public AbstractCommand(MainPlugin plugin,String Command, DataSource dataSource) {
    this.plugin = plugin;
    this.Command = Command;
    this.dataSource = dataSource;
  }

  public String getCommand() {
    return Command;
  }

  public DataSource getDataSource() { return dataSource; }

  public abstract List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args);
  public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);
}
