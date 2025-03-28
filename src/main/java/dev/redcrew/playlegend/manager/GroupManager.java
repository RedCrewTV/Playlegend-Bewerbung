package dev.redcrew.playlegend.manager;

import dev.redcrew.playlegend.DatabaseManager;
import dev.redcrew.playlegend.entitiy.Group;
import dev.redcrew.playlegend.entitiy.Player;
import jakarta.transaction.RollbackException;
import lombok.Getter;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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

    private GroupManager() { }

    /**
     * Registers a group in the system.
     *
     * @param group The Group object to register. Must not be null.
     * @throws IllegalArgumentException if a group with the same name already exists in the system.
     */
    public static void registerGroup(@NotNull Group group) throws IllegalArgumentException {
        if(getGroupByName(group.getName()) != null) throw new IllegalArgumentException("Group already exists!");
        Session session = DatabaseManager.getSession();

        try {
            session.beginTransaction();
            session.persist(group);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    /**
     * Unregisters a group from the system by removing it from the database.
     *
     * @param group The Group object to unregister. Must not be null.
     */
    public static void unregisterGroup(@NotNull Group group) {
        Session session = DatabaseManager.getSession();

        try {
            session.beginTransaction();
            Group g = session.get(Group.class, group.getId());
            if(g != null) session.remove(g);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    /**
     * Retrieves a group based on the specified name.
     *
     * @param name The name of the group to retrieve.
     * @return The Group object corresponding to the specified name, or null if no group is found.
     */
    public static Group getGroupByName(@NotNull String name) {
        Session session = DatabaseManager.getSession();

        try {
            session.beginTransaction();
            return session.createQuery("from Group where LOWER(name) = LOWER(:name)", Group.class)
                    .setParameter("name", name)
                    .uniqueResult();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    /**
     * Retrieves a Group object by its unique identifier.
     *
     * @param id The identifier of the group to retrieve. Must not be null.
     * @return The Group object corresponding to the provided identifier. Returns null if no group is found.
     */
    public static Group getGroupById(@NotNull Long id) {
        Session session = DatabaseManager.getSession();

        try {
            session.beginTransaction();
            return session.createQuery("from Group where id = :id", Group.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    /**
     * Retrieves all groups existing in the system.
     *
     * @return A list of Group objects representing all groups in the system.
     */
    public static List<Group> getGroups() {
        Session session = DatabaseManager.getSession();

        try {
            session.beginTransaction();
            return session.createQuery("from Group", Group.class).getResultList();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    /**
     * Updates the prefix of a group identified by the provided Group object.
     *
     * @param group The Group object representing the group to update. Must not be null.
     * @param prefix The new prefix to be set for the group. Must not be null.
     * @throws IllegalArgumentException if the group does not exist in the system.
     */
    public static void updateGroupPrefix(@NotNull Group group, @NotNull String prefix) throws IllegalArgumentException {
        if(getGroupByName(group.getName()) == null) throw new IllegalArgumentException("Group not exists!");
        Session session = DatabaseManager.getSession();

        try {
            session.beginTransaction();
            Group g = session.get(Group.class, group.getId());
            if(g != null) {
                g.setPrefix(prefix);
                session.merge(g);
            }
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    /**
     * Updates the priority of a group identified by the provided Group object.
     *
     * @param group The Group object representing the group whose priority needs to be updated. Must not be null.
     * @param priority The new priority value to be set for the group.
     * @throws IllegalArgumentException if the group does not exist in the system.
     */
    public static void updateGroupPriority(@NotNull Group group, int priority) throws IllegalArgumentException {
        if(getGroupByName(group.getName()) == null) throw new IllegalArgumentException("Group not exists!");
        Session session = DatabaseManager.getSession();

        try {
            session.beginTransaction();
            Group g = session.get(Group.class, group.getId());
            if(g != null) {
                g.setPriority(priority);
                session.merge(g);
            }
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    /**
     * Updates the name of a Group based on the provided Group object and new name.
     *
     * @param group The Group object representing the group to update. Must not be null.
     * @param name The new name to be set for the group. Must not be null.
     * @throws IllegalArgumentException if the group does not exist in the system or if a group with the new name already exists.
     */
    public static void updateGroupName(@NotNull Group group, @NotNull String name) throws IllegalArgumentException {
        if(getGroupByName(group.getName()) == null) throw new IllegalArgumentException("Group not exists!");
        if(getGroupByName(name) != null) throw new IllegalArgumentException("A Group with this name already exists!");
        Session session = DatabaseManager.getSession();

        try {
            session.beginTransaction();
            Group g = session.get(Group.class, group.getId());
            if(g != null) {
                g.setName(name);
                session.merge(g);
            }
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

}
