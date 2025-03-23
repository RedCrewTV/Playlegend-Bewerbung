package dev.redcrew.playlegend;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * This file is a JavaDoc!
 * Created: 3/23/2025
 * <p>
 * Belongs to Playlegend
 * <p>
 *
 * @author RedCrew <p>
 * Discord: redcrew <p>
 * Website: <a href="https://redcrew.dev/">https://redcrew.dev/</a>
 */
@Data
public class DatabaseManager {

    @NotNull
    private final HikariDataSource dataSource;

    public DatabaseManager() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://db:5432/playlegend");
        config.setUsername("legend");
        config.setPassword("legendPW");
        config.setMaximumPoolSize(10);
        config.setDriverClassName("org.postgresql.Driver");

        //config.addDataSourceProperty("", ""); // MISC settings to add
        this.dataSource = new HikariDataSource(config);
    }

    public void disconnect() {
        this.dataSource.close();
    }

    public void executeFile(String pathToFile) {
        try (Connection connection = dataSource.getConnection()) {
            try (InputStream inputStream = Playlegend.getInstance().getClass().getResourceAsStream(pathToFile)) {
                if(inputStream == null) throw new IllegalArgumentException("File not found: " + pathToFile);

                String sql = new BufferedReader(new InputStreamReader(inputStream))
                        .lines()
                        .collect(Collectors.joining("\n"));

                for (String sqlStmt : sql.split(";")) {
                    if(sqlStmt.trim().isEmpty()) continue;
                    try (PreparedStatement statement = connection.prepareStatement(sqlStmt.trim())) {
                        statement.executeUpdate();
                    }
                }

            }
        } catch (Exception e) {
            Playlegend.getInstance().getLogger().log(Level.SEVERE, "Failed to execute SQL file: " + pathToFile, e);
        }
    }

}
