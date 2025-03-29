package dev.redcrew.playlegend.listener;

import dev.redcrew.playlegend.entitiy.Player;
import dev.redcrew.playlegend.events.GroupUpdateEvent;
import dev.redcrew.playlegend.events.PlayerGroupAssignedEvent;
import dev.redcrew.playlegend.events.PlayerGroupExpiredEvent;
import dev.redcrew.playlegend.events.PlayerGroupUnassignedEvent;
import dev.redcrew.playlegend.manager.DisplayManager;
import dev.redcrew.playlegend.manager.PlayerManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
public final class GroupListener implements Listener {

    @EventHandler
    public void onAssignGroup(PlayerGroupAssignedEvent event) {
        updateDisplay(event.getAssigment().getPlayer());
    }

    @EventHandler
    public void onUnassignGroup(PlayerGroupUnassignedEvent event) {
        updateDisplay(event.getAssigment().getPlayer());
    }

    @EventHandler
    public void onExpiredGroup(PlayerGroupExpiredEvent event) {
        updateDisplay(event.getAssigment().getPlayer());
    }

    @EventHandler
    public void onGroupUpdate(GroupUpdateEvent event) {
        PlayerManager.getPlayers().forEach(this::updateDisplay);
    }

    private void updateDisplay(Player player) {
        DisplayManager.getDisplaysForPlayer(player).forEach(DisplayManager::updateDisplay);
    }

}
