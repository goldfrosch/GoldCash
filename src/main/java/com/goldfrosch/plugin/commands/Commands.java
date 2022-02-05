package com.goldfrosch.plugin.commands;

import com.goldfrosch.plugin.GoldCash;
import com.goldfrosch.plugin.database.query.CashQuery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.sql.DataSource;
import java.util.List;

public class Commands extends AbstractCommand{
  public Commands(GoldCash plugin, String Command, DataSource dataSource) {
    super(plugin, Command, dataSource);
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
          cashQuery.getCash(player).queue(cash -> { player.sendMessage(prefix + "현재 나의 캐시: " + cash.getAsLong() + "원"); });
        }
        else if(args[0].equalsIgnoreCase("help")){
          player.sendMessage(ChatColor.GRAY + "==================================================");
          player.sendMessage(ChatColor.AQUA + "/cash" + ChatColor.WHITE + " : 현재 나의 캐시를 확인합니다");
          player.sendMessage(ChatColor.GREEN + "/cash help" + ChatColor.WHITE + " : 이 플러그인의 도움말을 출력합니다.");
          player.sendMessage(ChatColor.GREEN + "/cash get [playerName]" + ChatColor.WHITE + " : [playerName]의 캐시 보유량을 보여줍니다");
          player.sendMessage(ChatColor.GRAY + "==================================================");
        } else {
          if(args[0].equalsIgnoreCase("admin")) {
            if(args[1].equalsIgnoreCase("add")) {
              if(args[3].isEmpty()) {
                if(args[2].matches("[+-]?\\d*(\\.\\d+)?")) {
                  cashQuery.addCash(player, Integer.parseInt(args[2]));
                } else {
                  player.sendMessage(prefix + "숫자를 입력해주세요");
                }
              }
              else {
                if(args[3].matches("[+-]?\\d*(\\.\\d+)?")) {
                  if(Bukkit.getPlayer(args[2]).isOnline()){
                    cashQuery.addCash(Bukkit.getPlayer(args[2]), Integer.parseInt(args[3]));
                    player.sendMessage(prefix + "성공적으로 캐시를 추가했습니다!");
                  } else {
                    player.sendMessage(prefix + "플레이어가 온라인이 아니거나 존재하지 않습니다.");
                  }
                } else {
                  player.sendMessage(prefix + "숫자를 입력해주세요");
                }
              }
            }
          }
        }
      }
    }
    return false;
  }
}
