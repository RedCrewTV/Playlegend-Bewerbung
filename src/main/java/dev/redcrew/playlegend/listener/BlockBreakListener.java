package dev.redcrew.playlegend.listener;

import dev.redcrew.playlegend.entitiy.PlayerDisplay;
import dev.redcrew.playlegend.manager.DisplayManager;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

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
public final class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getBlock().getState() instanceof Sign) {
            PlayerDisplay display = DisplayManager.getDisplayForLocation(event.getBlock().getLocation());
            if (display != null) {
                DisplayManager.unregisterDisplay(display.getPlayer(), event.getBlock().getLocation());
            }
        }
    }

}
