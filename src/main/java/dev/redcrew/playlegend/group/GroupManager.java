package dev.redcrew.playlegend.group;

import dev.redcrew.playlegend.Playlegend;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;

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
public final class GroupManager {

    private GroupManager() {}

    public void addGroup(@NotNull Group group) {

    }

    public void removeGroup(@NotNull Group group) {

    }

    public Group getGroupByName(@NotNull String name) {
        try (Connection connection = Playlegend.getInstance().getDatabaseManager().getDataSource().getConnection()) {
            PreparedStatement sql = connection.prepareStatement("SQL");

        } catch (Exception e) {
            Playlegend.getInstance().getLogger().log(Level.SEVERE, "An error occurred while retrieving the group by name: " + name, e);
        }
    }

    public boolean isGroupExists(@NotNull Group group) {

    }

}
