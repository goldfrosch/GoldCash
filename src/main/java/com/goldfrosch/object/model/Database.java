package com.goldfrosch.object.model;

import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@SerializableAs("GoldCash")
public class Database implements ConfigurationSerializable {
    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;

    public Database() {
        host = "localhost";
        port = 3306;
        database = "mariadb";
        user = "root";
        password = "1234";
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("host", host);
        map.put("port", port);
        map.put("database", database);
        map.put("user", user);
        map.put("password", password);
        return map;
    }
}
