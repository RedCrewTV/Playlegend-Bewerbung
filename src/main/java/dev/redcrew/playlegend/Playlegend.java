package dev.redcrew.playlegend;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;


public final class Playlegend extends JavaPlugin {

    @Getter
    private static Playlegend instance;

    @Getter
    private final DatabaseManager databaseManager = new DatabaseManager();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getDatabaseManager().executeFile("/sql/init.sql");


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getDatabaseManager().disconnect();
    }
}
