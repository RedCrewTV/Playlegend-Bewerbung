package dev.redcrew.playlegend;

import dev.redcrew.playlegend.command.GroupCMD;
import dev.redcrew.playlegend.entitiy.Group;
import dev.redcrew.playlegend.entitiy.Player;
import dev.redcrew.playlegend.language.Language;
import dev.redcrew.playlegend.listener.PlayerJoinListener;
import dev.redcrew.playlegend.manager.GroupManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Objects;

public final class Playlegend extends JavaPlugin {

    @Getter
    private static Playlegend instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        //Default Group
        GroupManager.deleteExpiredGroupAssignments();
        GroupManager.registerGroup(new Group("default", 0, "&7Spieler &8| &7"));

        //Language
        saveResource("languages/english.yml", false);
        saveResource("languages/german.yml", false);
        Language.loadLanguages();

        //Listener
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);

        //Commands
        Objects.requireNonNull(getCommand("group")).setExecutor(new GroupCMD());
        Objects.requireNonNull(getCommand("group")).setTabCompleter(new GroupCMD());


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DatabaseManager.shutdown();
    }
}
