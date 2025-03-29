package dev.redcrew.playlegend.listener;

import dev.redcrew.playlegend.manager.GroupManager;
import dev.redcrew.playlegend.manager.PlayerManager;
import dev.redcrew.playlegend.manager.TablistManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * This file is a JavaDoc!
 * Created: 3/24/2025
 * <p>
 * Belongs to Playlegend
 * <p>
 *
 * @author RedCrew <p>
 * Discord: redcrew <p>
 * Website: <a href="https://redcrew.dev/">https://redcrew.dev/</a>
 */
public final class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        //Register Player and Update Username
        if (PlayerManager.getPlayerByUUID(player.getUniqueId()) == null) {
            PlayerManager.registerPlayer(new dev.redcrew.playlegend.entitiy.Player(player.getUniqueId(), player.getName()));
            PlayerManager.assignGroup(PlayerManager.getPlayerByUUID(player.getUniqueId()), GroupManager.getGroupById(1L), null);
        }else {
            PlayerManager.updatePlayerName(PlayerManager.getPlayerByUUID(player.getUniqueId()), player.getName());
        }

        event.joinMessage(GroupManager.getGroupPrefix(
                PlayerManager.getHighestPriorityGroupForPlayer(PlayerManager.getPlayerByUUID(player.getUniqueId())),
                PlayerManager.getPlayerByUUID(player.getUniqueId())
        ));

        TablistManager.setPlayerTeam(player);



    }

}
