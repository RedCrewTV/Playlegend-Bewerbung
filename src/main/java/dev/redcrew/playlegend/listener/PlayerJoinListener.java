package dev.redcrew.playlegend.listener;

import dev.redcrew.playlegend.manager.GroupManager;
import dev.redcrew.playlegend.manager.PlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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

        event.joinMessage(LegacyComponentSerializer.legacyAmpersand()
                .deserialize(PlayerManager.getHighestPriorityGroupForPlayer(
                        PlayerManager.getPlayerByUUID(player.getUniqueId())).getPrefix())
                .append(Component.text(player.getName())));



    }

}
