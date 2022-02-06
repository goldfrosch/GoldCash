package com.goldfrosch.plugin;

import com.goldfrosch.plugin.commands.Commands;
import com.goldfrosch.plugin.config.Configuration;
import com.goldfrosch.plugin.config.object.Database;
import com.goldfrosch.plugin.database.DBConverter;
import com.goldfrosch.plugin.database.DBSetup;

import com.goldfrosch.plugin.events.CashEvent;
import com.goldfrosch.plugin.utils.placeholder.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

public class GoldCash extends JavaPlugin implements Listener {
  private PluginDescriptionFile pdfFile = this.getDescription();
  private String pfName = pdfFile.getName() + " v" + pdfFile.getVersion();

  private Configuration config;
  private DataSource dataSource;

  @Override
  public void onEnable(){
    //Config DB 등록하기
    ConfigurationSerialization.registerClass(Database.class);
    
    saveDefaultConfig();
    getConfig().options().copyDefaults(true);
    saveConfig();

    config = new Configuration(this);

    //sql convert
    try {
      if(config.getDriver().equalsIgnoreCase("mariadb")) {
        dataSource = DBConverter.initMariaDBDataSource(this, config.getDatabase());
        consoleLog("성공적으로 연결되었습니다");
      }
    } catch (SQLException e) {
      consoleDanger("데이터 베이스 연동 실패", e);
      getServer().getPluginManager().disablePlugin(this);
      return;
    }

    //sql check table
    try {
      DBSetup.initDB(this, dataSource);
    } catch (SQLException | IOException e) {
      consoleDanger("에러 발생", e);
      getServer().getPluginManager().disablePlugin(this);
    }

    //command
    Commands cmd = new Commands(this,"cash", dataSource);
    getCommand(cmd.getCommand()).setExecutor(cmd);
    getCommand(cmd.getCommand()).setTabCompleter(cmd);

    //event
    register(dataSource);

    //placeholderAPI
    if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
      new Placeholders(this, dataSource).register();
    }

    consoleLog(pfName+"이 활성화 되었습니다");
    super.onEnable();
  }

  @Override
  public void onDisable(){
    consoleLog(pfName+"이 비활성화 되었습니다");
    super.onDisable();
  }

  public void consoleLog(String msg){
    getLogger().info(msg);
  }

  public void consoleDanger(String msg, Exception e) {
    getLogger().log(Level.SEVERE, msg, e);
  }

  public void register(DataSource dataSource) {
    new CashEvent(this, dataSource);
  }
}
