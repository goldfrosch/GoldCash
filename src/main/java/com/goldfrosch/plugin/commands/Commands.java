package com.goldfrosch.plugin.commands;

import com.goldfrosch.plugin.GoldCash;
import com.goldfrosch.plugin.database.query.CashQuery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.sql.DataSource;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Commands extends AbstractCommand{
  public Commands(GoldCash plugin, String Command, DataSource dataSource) {
    super(plugin, Command, dataSource);
  }

  private CashQuery cashQuery = new CashQuery(plugin, this.getDataSource());
  String prefix = plugin.getConfig().getString("message.prefix").replace("&", "§");

  private void adminCashSet(String type, String money, Player player) {
    switch (type.toUpperCase(Locale.ROOT)) {
      case "ADD":
        cashQuery.addCash(player, Integer.parseInt(money));
        break;
      case "TAKE":
        cashQuery.takeCash(player, Integer.parseInt(money));
        break;
      case "SET":
        cashQuery.setCash(player, Integer.parseInt(money));
        break;
      default:
        player.sendMessage(prefix + "잘못된 명령어 입니다.");
        break;
    }
  }

  private void adminCashSetting(String[] args, Player player) {
    if (args.length == 3) {
      if (args[2].matches("[+-]?\\d*(\\.\\d+)?")) {
        adminCashSet(args[1], args[2], player);
        player.sendMessage(prefix + "현재 나의 캐시: " + cashQuery.getCash(player) + "원");
      } else {
        player.sendMessage(prefix + "숫자를 입력해주세요");
      }
    } else if (args.length == 4) {
      if (args[3].matches("[+-]?\\d*(\\.\\d+)?")) {
        if (Objects.requireNonNull(Bukkit.getPlayer(args[2])).isOnline()) {
          adminCashSet(args[1], args[3], Objects.requireNonNull(Bukkit.getPlayer(args[2])));
          player.sendMessage(prefix + "성공적으로 캐시를 설정했습니다!");
        } else {
          player.sendMessage(prefix + "플레이어가 온라인이 아니거나 존재하지 않습니다.");
        }
      } else {
        player.sendMessage(prefix + "숫자를 입력해주세요");
      }
    } else {
      player.sendMessage(prefix + "잘못된 명령어 입니다.");
    }
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
    return null;
  }

  @Override
  public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      if (label.equalsIgnoreCase("cash")) {
        if (args.length == 0) {
          player.sendMessage(prefix + "현재 나의 캐시: " + cashQuery.getCash(player) + "원");
        } else if (args[0].equalsIgnoreCase("help")) {
          player.sendMessage(ChatColor.GRAY + "==================================================");
          player.sendMessage(ChatColor.AQUA + "/cash" + ChatColor.WHITE + " : 현재 나의 캐시를 확인합니다");
          player.sendMessage(ChatColor.GREEN + "/cash help" + ChatColor.WHITE + " : 이 플러그인의 도움말을 출력합니다.");
          player.sendMessage(ChatColor.GREEN + "/cash get [playerName]" + ChatColor.WHITE + " : [playerName]의 캐시 보유량을 보여줍니다");
          player.sendMessage(ChatColor.GRAY + "==================================================");
        } else if (args[0].equalsIgnoreCase("admin")) {
          if (!player.hasPermission("cash.admin")) {
            player.sendMessage(prefix + "권한이 없습니다!");
          } else {
            if (args.length == 1) {
              player.sendMessage(ChatColor.GRAY + "==================================================");
              player.sendMessage(ChatColor.AQUA + "/cash admin" + ChatColor.WHITE + " : 어드민 관련 명령어를 확인합니다.");
              player.sendMessage(ChatColor.GREEN + "/cash admin add <플레이어> [숫자]" + ChatColor.WHITE + "<플레이어>에게 캐시를 <숫자>만큼 부여합니다. <플레이어>생략 가능");
              player.sendMessage(ChatColor.GREEN + "/cash admin take <플레이어> [숫자]" + ChatColor.WHITE + "<플레이어>에게 캐시를 <숫자>만큼 차감합니다. <플레이어>생략 가능");
              player.sendMessage(ChatColor.GREEN + "/cash admin set <플레이어> [숫자]" + ChatColor.WHITE + "<플레이어>에게 캐시를 <숫자>로 설정합니다. <플레이어>생략 가능");
              player.sendMessage(ChatColor.GRAY + "==================================================");
            }
            else {
              adminCashSetting(args, player);
            }
          }
        }
        else {
          player.sendMessage(prefix + "존재하지 않는 명령어 입니다.");
        }
      }
    }
    return false;
  }
}
