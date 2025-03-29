package dev.redcrew.playlegend.manager;

import dev.redcrew.playlegend.DatabaseManager;
import dev.redcrew.playlegend.Playlegend;
import dev.redcrew.playlegend.entitiy.*;
import dev.redcrew.playlegend.events.PlayerGroupAssignedEvent;
import dev.redcrew.playlegend.events.PlayerGroupExpiredEvent;
import jakarta.persistence.NoResultException;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * This file is a JavaDoc!
 * Created: 3/29/2025
 * <p>
 * Belongs to Playlegend
 * <p>
 *
 * @author RedCrew <p>
 * Discord: redcrew <p>
 * Website: <a href="https://redcrew.dev/">https://redcrew.dev/</a>
 */
public final class DisplayManager {

    private DisplayManager() {}

    /**
     * Registers a sign display for the specified player at the given sign location.
     *
     * @param player the player for whom the display should be registered
     * @param signLocation the location of the sign where the display should be registered
     */
    public static void registerDisplay(@NotNull Player player, @NotNull Location signLocation) {
        String world = signLocation.getWorld().getName();
        int x = signLocation.getBlockX();
        int y = signLocation.getBlockY();
        int z = signLocation.getBlockZ();

        Session session = DatabaseManager.getSession();

        try {
            session.beginTransaction();

            Player managedPlayer = session.get(Player.class, player.getId());

            if (managedPlayer == null) throw new IllegalArgumentException("Player not found in the session");

            PlayerDisplayId id = new PlayerDisplayId(managedPlayer.getId(), world, x, y, z);
            PlayerDisplay display = new PlayerDisplay(id, managedPlayer);
            session.persist(display);
            session.getTransaction().commit();
            updateDisplay(display);
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
     * Unregisters a sign display for the specified player at the given sign location.
     *
     * @param player the player for whom the display should be unregistered
     * @param signLocation the location of the sign where the display should be unregistered
     */
    public static void unregisterDisplay(Player player, Location signLocation) {
        String world = signLocation.getWorld().getName();
        int x = signLocation.getBlockX();
        int y = signLocation.getBlockY();
        int z = signLocation.getBlockZ();

        // Create the composite key to locate the entity.
        PlayerDisplayId id = new PlayerDisplayId(player.getId(), world, x, y, z);

        Session session = DatabaseManager.getSession();
        Transaction tx = session.beginTransaction();
        try {
            PlayerDisplay display = session.get(PlayerDisplay.class, id);
            if (display != null) {
                session.remove(display);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Retrieves a list of PlayerDisplay objects associated with the specified player.
     *
     * @param player the player for whom the displays should be retrieved
     * @return a list of PlayerDisplay objects associated with the player
     */
    public static List<PlayerDisplay> getDisplaysForPlayer(Player player) {
        Session session = DatabaseManager.getSession();
        List<PlayerDisplay> displays;
        Transaction tx = session.beginTransaction();
        try {
            displays = session.createQuery(
                            "from PlayerDisplay d where d.player.id = :playerId", PlayerDisplay.class)
                    .setParameter("playerId", player.getId())
                    .getResultList();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return displays;
    }

    /**
     * Retrieves the PlayerDisplay associated with the specified Location.
     *
     * @param location the location to retrieve the PlayerDisplay for
     * @return the PlayerDisplay associated with the specified Location
     */
    public static PlayerDisplay getDisplayForLocation(Location location) {
        Session session = DatabaseManager.getSession();
        Transaction tx = session.beginTransaction();
        PlayerDisplay display;
        try {
            display = session.createQuery(
                            "from PlayerDisplay d where d.id.world = :world AND d.id.x = :x AND d.id.y = :y AND d.id.z = :z", PlayerDisplay.class)
                    .setParameter("world", location.getWorld().getName())
                    .setParameter("x", location.getBlockX())
                    .setParameter("y", location.getBlockY())
                    .setParameter("z", location.getBlockZ())
                    .getSingleResult();
            tx.commit();
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
        return display;
    }

    /**
     * Updates the display for a specified player based on the PlayerDisplay information.
     *
     * @param display the PlayerDisplay object containing the display information to be updated
     */
    public static void updateDisplay(PlayerDisplay display) {
        String worldName = display.getId().getWorld();
        int x = display.getId().getX();
        int y = display.getId().getY();
        int z = display.getId().getZ();

        World world = Bukkit.getWorld(worldName);
        if (world == null) throw new IllegalStateException("World '" + worldName + "' is not loaded.");

        Block block = world.getBlockAt(x, y, z);
        if (!(block.getState() instanceof Sign sign)) {
            unregisterDisplay(display.getPlayer(), block.getLocation());
            return;
        }

        sign.getSide(Side.FRONT).line(0, Component.text(display.getPlayer().getName()));

        List<Group> groups = PlayerManager.getGroupsForPlayer(display.getPlayer()).stream()
                .sorted(Comparator.comparingInt(Group::getPriority).reversed())
                .toList();

        final int maxGroupLines = 3;
        for (int i = 0; i < maxGroupLines; i++) {
            Component line = i < groups.size() ? Component.text(groups.get(i).getName()) : Component.empty();
            sign.getSide(Side.FRONT).line(i + 1, line);
        }

        sign.update();
    }

}
