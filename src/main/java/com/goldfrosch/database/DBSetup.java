package com.goldfrosch.database;

import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class DBSetup {
    public static void initDB(Plugin plugin, DataSource dataSource) throws SQLException, IOException {
        //DB 쿼리 선언문
        String setup = "";

        //특정 이름의 파일 값을 클래스에 로드하는 방법
        try(InputStream inputStream = DBSetup.class.getClassLoader().getResourceAsStream("setup.sql")) {
            //파일을 불러와서 엔터마다 한줄로 조인시키는 듯함
            setup = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "DB 셋업 파일을 인식할 수 없습니다.", e);
        }

        String[] queries = setup.split(";");
        try(Connection conn = dataSource.getConnection()) {
            //모든 sql문이 개별 트렌젝션으로 실행되는 가?
            //create table의 경우에는 그럴 필요가 없음
            conn.setAutoCommit(false);
            for(String query: queries) {
                if(query.isEmpty()) continue;
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    plugin.getLogger().info(query);
                    stmt.execute();
                }
            }
            conn.commit();
        } catch(SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "쿼리 인식 과정에 문제 발생", e);
        }
        
        plugin.getLogger().info("DB 테이블 세팅이 완료되었습니다.");
    }
}
