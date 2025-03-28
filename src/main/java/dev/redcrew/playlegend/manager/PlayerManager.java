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
     * Retrieves a player based on the specified name.
     *
     * @param name The name of the player to retrieve. Must not be null.
     * @return The Player object corresponding to the specified name, or null if no player is found.
     */
    public static Player getPlayerByName(@NotNull String name) {
        Session session = DatabaseManager.getSession();

        try {
            session.beginTransaction();
            return session.createQuery("from Player where LOWER(name) = LOWER(:name)", Player.class)
                    .setParameter("name", name)
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
     * Assigns a player to a group with an expiration date.
     *
     * @param player The Player object to assign to the group. Must not be null.
     * @param group The Group object to assign the player to. Must not be null.
     * @param expiresAt The LocalDateTime when the assignment expires.
     * @throws IllegalArgumentException If the player or group does not exist, or if an error occurs during the assignment process.
     */
    public static void assignGroup(@NotNull Player player, @NotNull Group group, LocalDateTime expiresAt) throws IllegalArgumentException {
        if(getPlayerByUUID(player.getId()) == null) throw new IllegalArgumentException("Player not exists!");
        if(GroupManager.getGroupByName(group.getName()) == null) throw new IllegalArgumentException("Group not exists!");

        Session session = DatabaseManager.getSession();

        try {
            session.beginTransaction();

            Player managedPlayer = session.get(Player.class, player.getId());
            Group managedGroup = session.get(Group.class, group.getId());

            if (managedPlayer == null || managedGroup == null) throw new IllegalArgumentException("Player or Group not found in the session");

            PlayerGroupAssignmentId id = new PlayerGroupAssignmentId(managedPlayer.getId(), managedGroup.getId());
            PlayerGroupAssigment assignment = new PlayerGroupAssigment(id, LocalDateTime.now(), expiresAt, managedPlayer, managedGroup);

            session.persist(assignment);
            session.getTransaction().commit();
        } catch(Exception e) {
            if(session.getTransaction().isActive()){
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Removes the assignment of a player from a specified group.
     *
     * @param player The Player object to unassign. Must not be null.
     * @param group The Group object from which to unassign the player. Must not be null.
     * @throws IllegalArgumentException if no assignment exists for the given player and group, or if any error occurs during the unassignment process.
     */
    public static void unassignGroup(@NotNull Player player, @NotNull Group group) throws IllegalArgumentException {
        if(getPlayerByUUID(player.getId()) == null) throw new IllegalArgumentException("Player not exists!");
        if(GroupManager.getGroupByName(group.getName()) == null) throw new IllegalArgumentException("Group not exists!");

        Session session = DatabaseManager.getSession();

        try {
            session.beginTransaction();

            PlayerGroupAssignmentId id = new PlayerGroupAssignmentId(player.getId(), group.getId());
            PlayerGroupAssigment assignment = session.get(PlayerGroupAssigment.class, id);

            if (assignment == null) throw new IllegalArgumentException("No assignment exists for the given player and group.");

            session.remove(assignment);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }


}
