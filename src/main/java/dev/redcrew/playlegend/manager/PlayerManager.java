package dev.redcrew.playlegend.manager;

import dev.redcrew.playlegend.DatabaseManager;
import dev.redcrew.playlegend.entitiy.Group;
import dev.redcrew.playlegend.entitiy.Player;
import dev.redcrew.playlegend.entitiy.PlayerGroupAssigment;
import dev.redcrew.playlegend.entitiy.PlayerGroupAssignmentId;
import lombok.Getter;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

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
public final class PlayerManager {

    private PlayerManager() { }

    /**
     * Registers a player in the system.
     *
     * @param player The Player object to register. Must not be null.
     * @throws IllegalArgumentException if a player with the same UUID already exists in the system.
     */
    public static void registerPlayer(@NotNull Player player) throws IllegalArgumentException {
        if(getPlayerByUUID(player.getId()) != null) throw new IllegalArgumentException("Player already exists!");
        Session session = DatabaseManager.getSession();

        try {
            session.beginTransaction();
            session.persist(player);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    /**
     * Unregisters a player from the system by removing it from the database.
     *
     * @param player The Player object to unregister. Must not be null.
     */
    public static void unregisterPlayer(@NotNull Player player) {
        Session session = DatabaseManager.getSession();

        try {
            session.beginTransaction();
            Player p = session.get(Player.class, player.getId());
            if(p != null) session.remove(p);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    /**
     * Retrieves a player based on the specified name.
     *
     * @param uuid The UUID of the player to retrieve.
     * @return The Player object corresponding to the specified name, or null if no player is found.
     */
    public static Player getPlayerByUUID(@NotNull UUID uuid) {
        Session session = DatabaseManager.getSession();
        
        try {
            session.beginTransaction();
            return session.createQuery("from Player where id = :uuid", Player.class)
                    .setParameter("uuid", uuid)
                    .uniqueResult();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    /**
     * Updates the name of the specified player.
     *
     * @param player The player object whose name is to be updated. Must not be null.
     * @param name The new name to assign to the player. Must not be null.
     * @throws IllegalArgumentException if the player does not exist.
     */
    public static void updatePlayerName(@NotNull Player player, @NotNull String name) throws IllegalArgumentException {
        if(getPlayerByUUID(player.getId()) == null) throw new IllegalArgumentException("Player not exists!");
        Session session = DatabaseManager.getSession();

        try {
            session.beginTransaction();
            Player p = session.get(Player.class, player.getId());
            if(p != null) {
                p.setName(name);
                session.merge(p);
            }
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    /**
     * Assigns a player to a specified group with an optional expiration date.
     *
     * @param player The player to assign to the group. Must not be null.
     * @param group The group to assign the player to. Must not be null.
     * @param expiresAt The expiration date for the group assignment, can be null for no expiration.
     * @throws IllegalArgumentException if the player or group does not exist in the system.
     */
    public static void assignGroup(@NotNull Player player, @NotNull Group group, LocalDateTime expiresAt) throws IllegalArgumentException {
        if(getPlayerByUUID(player.getId()) == null) throw new IllegalArgumentException("Player not exists!");
        if(GroupManager.getGroupByName(group.getName()) == null) throw new IllegalArgumentException("Group not exists!");

        //todo

    }

}
