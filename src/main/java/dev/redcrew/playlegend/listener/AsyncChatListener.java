package dev.redcrew.playlegend.listener;

import dev.redcrew.playlegend.manager.GroupManager;
import dev.redcrew.playlegend.manager.PlayerManager;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
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
public final class AsyncChatListener implements Listener, ChatRenderer.ViewerUnaware {

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        event.renderer(ChatRenderer.viewerUnaware(this));
    }

    @Override
    public Component render(Player source, Component sourceDisplayName, Component message) {
        dev.redcrew.playlegend.entitiy.Player dbPlayer = PlayerManager.getPlayerByUUID(source.getUniqueId());
        return GroupManager.getGroupPrefix(PlayerManager.getHighestPriorityGroupForPlayer(dbPlayer), dbPlayer)
                .appendSpace().append(message);
    }
}
