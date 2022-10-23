package com.goldfrosch.database;

import com.goldfrosch.config.object.Database;

import org.bukkit.plugin.Plugin;
import org.mariadb.jdbc.MariaDbPoolDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConverter {
    public static DataSource initMariaDBDataSource(Plugin plugin, Database database) throws SQLException  {
        //MariaDB 연동 선언
        var dataSource = new MariaDbPoolDataSource();

        //MariaDB 연결 설정
        dataSource.setServerName(database.getHost());
        dataSource.setPassword(database.getPassword());
        dataSource.setPortNumber(database.getPort());
        dataSource.setDatabaseName(database.getDatabase());
        dataSource.setUser(database.getUser());

        //최대 이용 코어 사이즈 설정(나중에 알아볼 필요 있음)
        dataSource.setMaxPoolSize(8);

        //연결 테스트 코드
        testDataSource(plugin, dataSource);

        //키키루키루
        return dataSource;
    }

    public static void testDataSource(Plugin plugin, DataSource dataSource) throws SQLException {
        try (var conn = dataSource.getConnection()) {
            if(!conn.isValid(1000)) {
                throw new SQLException("데이터베이스 연결 실패");
            }
        }
        if(plugin != null) {
            plugin.getLogger().info("데이터베이스 연결 성공");
        }
    }
}
