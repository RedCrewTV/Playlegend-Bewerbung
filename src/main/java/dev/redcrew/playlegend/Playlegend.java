package dev.redcrew.playlegend;

import dev.redcrew.playlegend.entitiy.Group;
import dev.redcrew.playlegend.entitiy.Player;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public final class Playlegend extends JavaPlugin {

    @Getter
    private static Playlegend instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DatabaseManager.shutdown();
    }
}
