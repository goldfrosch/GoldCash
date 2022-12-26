package com.goldfrosch.config;

import static com.goldfrosch.GoldCash.plugin;

import com.goldfrosch.GoldCash;
import java.io.File;
import java.io.IOException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

@Getter
@RequiredArgsConstructor
public class ShopCategoryConfig {

  public static File categoryFile;
  public static FileConfiguration categoryConfig;

  public void createCategoryConfig() {
    try {
      categoryFile = new File(plugin.getDataFolder(), "category.yml");
      categoryConfig = new YamlConfiguration();
      if (!categoryFile.exists()) {
        categoryFile.getParentFile().mkdirs();
        plugin.saveResource("category.yml", false);
        categoryConfig.save(categoryFile);
      }
      categoryConfig.load(categoryFile);
      categoryConfig.options().copyDefaults(true);
    } catch (IOException | InvalidConfigurationException e) {
      e.printStackTrace();
    }
  }
}
