package com.goldfrosch.commands;

import com.goldfrosch.GoldCash;
import com.goldfrosch.database.query.CashLogQuery;
import com.goldfrosch.database.query.CashQuery;
import com.goldfrosch.gui.CategoryGUI;
import com.goldfrosch.object.entity.CashDAO;
import com.goldfrosch.object.type.CashChargeType;
import com.goldfrosch.object.type.CashUseStatus;
import com.goldfrosch.service.AdminCashService;

import com.goldfrosch.service.UserCashService;
import java.util.Locale;

import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;

public class Commands extends AbstractCommand {

  private final AdminCashService adminCashService;
  private final UserCashService userCashService;


  public Commands(GoldCash plugin, String Command, DataSource dataSource) {
    super(plugin, Command, dataSource);
    this.adminCashService = new AdminCashService(plugin, dataSource, new CashQuery(),
        new CashLogQuery());
    this.userCashService = new UserCashService(plugin, dataSource, new CashQuery());
  }


  String prefix = Objects.requireNonNull(plugin.getConfig().getString("message.prefix"))
      .replace("&", "§");

  private void adminCashSet(String type, CashDAO cashDAO) {
    var player = cashDAO.getManager();
    switch (type.toUpperCase(Locale.ROOT)) {
      case "ADD" -> adminCashService.addCash(cashDAO);
      case "TAKE" -> adminCashService.takeCash(cashDAO);
      default -> player.sendMessage(prefix + "잘못된 명령어 입니다.");
    }
  }

  private void adminCashSetting(String[] args, Player player) {
    if (args.length == 3) {
      if (args[2].matches("[+-]?\\d*(\\.\\d+)?")) {
        var adminCashDAO = CashDAO.builder().player(player).amount(Integer.parseInt(args[2]))
            .cashChargeType(CashChargeType.ADMIN).cashUseStatus(CashUseStatus.ADMIN_CHARGE)
            .manager(player).build();
        adminCashSet(args[1], adminCashDAO);
        player.sendMessage(prefix + "현재 나의 캐시: " + userCashService.getPlayerCash(player) + "원");
      } else {
        player.sendMessage(prefix + "숫자를 입력해주세요");
      }
    } else if (args.length == 6) {
      if (args[3].matches("[+-]?\\d*(\\.\\d+)?")) {
        if (Objects.requireNonNull(Bukkit.getPlayer(args[2])).isOnline()) {
          var userCashDAO = CashDAO.builder()
              .player(Objects.requireNonNull(Bukkit.getPlayer(args[2])))
              .amount(Integer.parseInt(args[3]))
              .cashChargeType(Objects.requireNonNull(CashChargeType.of(args[4])))
              .cashUseStatus(Objects.requireNonNull(CashUseStatus.of(args[5]))).manager(player)
              .build();
          adminCashSet(args[1], userCashDAO);
        } else {
          player.sendMessage(prefix + "플레이어가 온라인이 아니거나 존재하지 않습니다.");
        }
      } else {
        player.sendMessage(prefix + "숫자를 입력해주세요");
      }
    } else {
      player.sendMessage(prefix + "잘못된 명령어 형식 입니다.");
    }
  }

  public void helpCash(Player player) {
    player.sendMessage(ChatColor.GRAY + "==================================================");
    player.sendMessage(ChatColor.AQUA + "/cash" + ChatColor.WHITE + " : 현재 나의 캐시를 확인합니다");
    player.sendMessage(
        ChatColor.GREEN + "/cash help" + ChatColor.WHITE + " : 이 플러그인의 도움말을 출력합니다.");
    player.sendMessage(ChatColor.GREEN + "/cash get [playerName]" + ChatColor.WHITE
        + " : [playerName]의 캐시 보유량을 보여줍니다");
    player.sendMessage(ChatColor.GRAY + "==================================================");
  }

  public void executeCashAdmin(Player player, String[] args) {
    if (!player.hasPermission("cash.admin")) {
      player.sendMessage(prefix + "권한이 없습니다!");
    } else {
      if (args.length == 1) {
        this.helpCashAdmin(player);
      } else {
        this.adminCashSetting(args, player);
      }
    }
  }

  public void helpCashAdmin(Player player) {
    player.sendMessage(
        ChatColor.GRAY + "==================================================");
    player.sendMessage(
        ChatColor.AQUA + "/cash admin" + ChatColor.WHITE + " : 어드민 관련 명령어를 확인합니다.");
    player.sendMessage(ChatColor.GREEN + "/cash admin add <플레이어> [숫자] (충전타입) (충전형태)");
    player.sendMessage(ChatColor.WHITE + "<플레이어>에게 캐시를 <숫자>만큼 부여합니다. <플레이어>생략 가능");
    player.sendMessage(ChatColor.WHITE + "충전타입과 충전 형태는 타인에게 지급 시 필수로 넣어주세요");
    player.sendMessage(ChatColor.GREEN + "충전 타입 종류: " + ChatColor.WHITE + "계좌, 문상, 기타");
    player.sendMessage(ChatColor.GREEN + "충전 형태 종류: " + ChatColor.WHITE + "충전, 이벤트_지급");
    player.sendMessage(
        ChatColor.WHITE + "만약 잘못 입력했을 시에 " + ChatColor.DARK_RED + "꼭" + ChatColor.WHITE
            + " 개발자에게 문의해주세요. 까먹으시면 큰일납니다. 후원 복구시 문제가 됩니다.");
    player.sendMessage(ChatColor.GREEN + "/cash admin take <플레이어> [숫자]" + ChatColor.WHITE
        + "<플레이어>에게 캐시를 <숫자>만큼 차감합니다. <플레이어>생략 가능");
    player.sendMessage(
        ChatColor.WHITE + "회수는 최대한 사용하지 마시고 개발자에게 문의해주세요...! 없으면 쓰고 말씀만 해주시면 좋습니다.");
    player.sendMessage(
        ChatColor.GRAY + "==================================================");
  }

  public void executeCommand(Player player, String[] args) {
    switch (args[0].toUpperCase(Locale.ROOT)) {
      case "HELP" -> this.helpCash(player);
      case "ADMIN" -> this.executeCashAdmin(player, args);
      default -> player.sendMessage(prefix + "존재하지 않는 명령어 입니다.");
    }
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command,
      String alias, String[] args) {
    return null;
  }

  @Override
  public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label,
      String[] args) {
    if (sender instanceof Player player) {
      if (label.equalsIgnoreCase("cash")) {
        if (args.length == 0) {
          player.sendMessage(prefix + "현재 나의 캐시: " + userCashService.getPlayerCash(player) + "원");
        } else {
          this.executeCommand(player, args);
        }
      }
    }
    return false;
  }
}
