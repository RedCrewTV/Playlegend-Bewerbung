package dev.redcrew.playlegend.events;

import dev.redcrew.playlegend.entitiy.PlayerGroupAssigment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This file is a JavaDoc!
 * Created: 3/28/2025
 * <p>
 * Belongs to Playlegend
 * <p>
 *
 * @author RedCrew <p>
 * Discord: redcrew <p>
 * Website: <a href="https://redcrew.dev/">https://redcrew.dev/</a>
 */
@Getter
@AllArgsConstructor
public class PlayerGroupAssigned extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final PlayerGroupAssigment assigment;

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
