package com.goldfrosch.plugin.commands;

import com.goldfrosch.plugin.MainPlugin;
import com.goldfrosch.plugin.database.query.CashQuery;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.sql.DataSource;
import java.util.List;

public class Commands extends AbstractCommand{
  public Commands(MainPlugin plugin, String Command, DataSource dataSource) {
    super(plugin,Command, dataSource);
  }

  private CashQuery cashQuery = new CashQuery(plugin, this.getDataSource());

  @Override
  public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
    return null;
  }

  @Override
  public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
    String prefix = plugin.getConfig().getString("message.prefix").replace("&", "§");
    if (sender instanceof Player) {
      Player player = (Player) sender;
      if(label.equalsIgnoreCase("cash")){
        if(args.length == 0){
          player.sendMessage(prefix + "현재 나의 캐시: " + cashQuery.getCash(player) + "원");
        }
        else if(args[0].equalsIgnoreCase("help")){
          player.sendMessage(ChatColor.GRAY + "==================================================");
          player.sendMessage(ChatColor.AQUA + "/cash" + ChatColor.WHITE + " : 현재 나의 캐시를 확인합니다");
          player.sendMessage(ChatColor.GREEN + "/cash help" + ChatColor.WHITE + " : 이 플러그인의 도움말을 출력합니다.");
          player.sendMessage(ChatColor.GREEN + "/cash get [playerName]" + ChatColor.WHITE + " : [playerName]의 캐시 보유량을 보여줍니다");
          player.sendMessage(ChatColor.GRAY + "==================================================");
        } else {
//          if(args[0].equalsIgnoreCase("admin")) {
//
//          }
          if(args[0].equalsIgnoreCase("get")) {
            if(args[1].isEmpty()) {
              player.sendMessage(prefix + "현재 나의 캐시: " + cashQuery.getCash(player) + "원");
            }
          }
        }
      }
    }
    return false;
  }
}
