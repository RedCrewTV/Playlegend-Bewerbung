package dev.redcrew.playlegend.manager;

import dev.redcrew.playlegend.entitiy.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

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
public final class TablistManager {

    private TablistManager() {}

    private final static Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

    public static void setPlayerTeam(@NotNull Player player) {
        PlayerManager.getGroupsForPlayer(PlayerManager.getPlayerByUUID(player.getUniqueId())).forEach(g -> getTeam(g).removeEntry(player.getName()));
        getTeam(PlayerManager.getHighestPriorityGroupForPlayer(PlayerManager.getPlayerByUUID(player.getUniqueId()))).addEntry(player.getName());
        player.playerListName(GroupManager.getGroupPrefix(PlayerManager.getHighestPriorityGroupForPlayer(PlayerManager.getPlayerByUUID(player.getUniqueId())),
                PlayerManager.getPlayerByUUID(player.getUniqueId())));
    }

    private static @NotNull Team getTeam(Group group) {
        Team finalTeam = board.getTeam(group.getPriority() + "-" + group.getName());
        if(finalTeam == null) {
            for (Team team : board.getTeams()) {
                if(team.getName().split("-")[1].equals(group.getName())) {
                    team.unregister();
                    break;
                }
            }
            finalTeam = board.registerNewTeam(group.getPriority() + "-" + group.getName());
        }
        return finalTeam;
    }

}
