package com.goldfrosch.config;

import com.goldfrosch.config.object.Database;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

@Getter
public class Configuration {
    private final Plugin plugin;
    private Database database;
    private String driver;

    public Configuration(Plugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        database = plugin.getConfig().getObject("database", Database.class, new Database());
        driver = plugin.getConfig().getString("driver");
    }
}
