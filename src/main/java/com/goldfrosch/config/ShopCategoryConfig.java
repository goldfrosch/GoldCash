package com.goldfrosch.config;

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

  private final GoldCash goldCash;

  private static File categoryFile;
  private static FileConfiguration categoryConfig;

  public void createCategoryConfig() {
    categoryFile = new File(goldCash.getDataFolder(), "category.yml");
    try {
      if (!categoryFile.exists()) {
        categoryFile.getParentFile().mkdirs();
        categoryConfig.options().copyDefaults(true);
        categoryConfig.save(categoryFile);
      }
      categoryConfig = new YamlConfiguration();
      categoryConfig.load(categoryFile);
    } catch (IOException | InvalidConfigurationException e) {
      e.printStackTrace();
    }
  }
}
