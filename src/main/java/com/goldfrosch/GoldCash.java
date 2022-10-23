package com.goldfrosch;

import com.goldfrosch.commands.Commands;
import com.goldfrosch.config.Configuration;
import com.goldfrosch.config.object.Database;
import com.goldfrosch.database.DBConverter;
import com.goldfrosch.database.DBSetup;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public final class GoldCash extends JavaPlugin {
  private PluginDescriptionFile pdfFile = this.getDescription();
  private String pfName = pdfFile.getName() + " v" + pdfFile.getVersion();

  private Configuration config;
  private DataSource dataSource;

  @Override
  public void onEnable() {
    ConfigurationSerialization.registerClass(Database.class);

    saveDefaultConfig();
    getConfig().options().copyDefaults(true);
    saveConfig();

    config = new Configuration(this);

    try {
      if(config.getDriver().equalsIgnoreCase("mariadb")) {
        dataSource = DBConverter.initMariaDBDataSource(this, config.getDatabase());
        consoleLog("성공적으로 연결되었습니다");
      }
    } catch (SQLException e) {
      consoleDanger("데이터 베이스 연동 실패", e);
      getServer().getPluginManager().disablePlugin(this);
    }

    try {
      DBSetup.initDB(this, dataSource);
    } catch (SQLException | IOException e) {
      consoleDanger("에러 발생", e);
      getServer().getPluginManager().disablePlugin(this);
    }

    //command
    var cmd = new Commands(this,"cmd", dataSource);
    getCommand(cmd.getCommand()).setExecutor(cmd);
    getCommand(cmd.getCommand()).setTabCompleter(cmd);

    consoleLog(pfName+"이 활성화 되었습니다");
    super.onEnable();

  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }

  public void consoleLog(String msg){
    getLogger().info(msg);
  }

  public void consoleDanger(String msg, Exception e) {
    getLogger().log(Level.SEVERE, msg, e);
  }
}
