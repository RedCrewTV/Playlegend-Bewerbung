package dev.redcrew.playlegend.command;

import dev.redcrew.playlegend.entitiy.Group;
import dev.redcrew.playlegend.language.Language;
import dev.redcrew.playlegend.language.TranslatableText;
import dev.redcrew.playlegend.language.Tuple;
import dev.redcrew.playlegend.manager.GroupManager;
import dev.redcrew.playlegend.manager.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
public final class GroupCMD implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(sender instanceof Player player) {
            if(args.length >= 1) {
                switch (args[0].toLowerCase()) {
                    case "info" -> {
                        if(args.length >= 2) {
                            dev.redcrew.playlegend.entitiy.Player target = PlayerManager.getPlayerByName(args[1]);

                            if (target == null) {
                                player.sendMessage(TranslatableText.of("playlegend_group_cmd_exists_not_player",
                                        Language.getPreferredLanguage(player), new Tuple<>("%name%", args[1])).getText());
                                return true;
                            }

                            player.sendMessage(TranslatableText.of("playlegend_group_cmd_info_title",
                                    Language.getPreferredLanguage(player), new Tuple<>("%name%", target.getName())).getText());

                            GroupManager.getAssignmentsForPlayer(target).forEach(assignment -> {
                                player.sendMessage(TranslatableText.of("playlegend_group_cmd_info_group_title",
                                        Language.getPreferredLanguage(player), new Tuple<>("%name%", assignment.getGroup().getName())).getText());

                                player.sendMessage(TranslatableText.of("playlegend_group_cmd_info_group_assigned",
                                        Language.getPreferredLanguage(player),
                                        new Tuple<>("%date%", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(assignment.getAssignedAt())))
                                        .getText());

                                if(assignment.getExpiresAt() != null) {
                                    player.sendMessage(TranslatableText.of("playlegend_group_cmd_info_group_expires",
                                                    Language.getPreferredLanguage(player),
                                                    new Tuple<>("%date%", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(assignment.getExpiresAt())))
                                            .getText());
                                }

                            });

                            return true;
                        }
                    }
                    case "create" -> {
                        if(args.length >= 4) {
                            String name = args[1];
                            String prefix = Arrays.stream(args, 3, args.length).collect(Collectors.joining(" "));
                            int priority;

                            try {
                                priority = Integer.parseInt(args[2]);
                            } catch (NumberFormatException ignored) {
                                player.sendMessage(TranslatableText.of("playlegend_group_cmd_not_number", Language.getPreferredLanguage(player)).getText());
                                return true;
                            }


                            try {
                                GroupManager.registerGroup(new Group(name, priority, prefix));
                            } catch (IllegalArgumentException ignored) {
                                player.sendMessage(TranslatableText.of("playlegend_group_cmd_exists_already",
                                        Language.getPreferredLanguage(player), new Tuple<>("%name%", name)).getText());
                                return true;
                            }

                            player.sendMessage(TranslatableText.of("playlegend_group_cmd_create_created",
                                    Language.getPreferredLanguage(player), new Tuple<>("%name%", name)).getText());
                            return true;
                        }
                    }
                    case "delete" -> {
                        if(args.length >= 2) {
                            String name = args[1];
                            Group group = GroupManager.getGroupByName(name);

                            if (group == null) {
                                player.sendMessage(TranslatableText.of("playlegend_group_cmd_exists_not",
                                        Language.getPreferredLanguage(player), new Tuple<>("%name%", name)).getText());
                                return true;
                            }

                            GroupManager.unregisterGroup(group);
                            player.sendMessage(TranslatableText.of("playlegend_group_cmd_delete_deleted",
                                    Language.getPreferredLanguage(player), new Tuple<>("%name%", name)).getText());
                            return true;

                        }
                    }
                    case "edit" -> {
                        if(args.length >= 4) {
                            String name = args[1];
                            String property = args[2];
                            Group group = GroupManager.getGroupByName(name);

                            if (group == null) {
                                player.sendMessage(TranslatableText.of("playlegend_group_cmd_exists_not",
                                        Language.getPreferredLanguage(player), new Tuple<>("%name%", name)).getText());
                                return true;
                            }

                            switch (property.toLowerCase()) {
                                case "name" -> {
                                    String newName = args[3];

                                    try {
                                        GroupManager.updateGroupName(group, newName);
                                    } catch (IllegalArgumentException ignored) {
                                        player.sendMessage(TranslatableText.of("playlegend_group_cmd_exists_already",
                                                Language.getPreferredLanguage(player), new Tuple<>("%name%", newName)).getText());
                                        return true;
                                    }

                                    player.sendMessage(TranslatableText.of("playlegend_group_cmd_edit_updated",
                                            Language.getPreferredLanguage(player), new Tuple<>("%name%", name)).getText());
                                    return true;

                                }
                                case "priority" -> {
                                    int priority;

                                    try {
                                        priority = Integer.parseInt(args[3]);
                                    } catch (NumberFormatException ignored) {
                                        player.sendMessage(TranslatableText.of("playlegend_group_cmd_not_number", Language.getPreferredLanguage(player)).getText());
                                        return true;
                                    }

                                    GroupManager.updateGroupPriority(group, priority);
                                    player.sendMessage(TranslatableText.of("playlegend_group_cmd_edit_updated",
                                            Language.getPreferredLanguage(player), new Tuple<>("%name%", name)).getText());
                                    return true;
                                }
                                case "prefix" -> {
                                    String prefix = Arrays.stream(args, 3, args.length).collect(Collectors.joining(" "));
                                    GroupManager.updateGroupPrefix(group, prefix);
                                    player.sendMessage(TranslatableText.of("playlegend_group_cmd_edit_updated",
                                            Language.getPreferredLanguage(player), new Tuple<>("%name%", name)).getText());
                                    return true;
                                }
                            }

                        }
                    }
                    case "list" -> {
                        player.sendMessage(TranslatableText.of("playlegend_group_cmd_list_title", Language.getPreferredLanguage(player)).getText());
                        GroupManager.getGroups().forEach(group -> {
                            player.sendMessage(TranslatableText.of("playlegend_group_cmd_list",
                                    Language.getPreferredLanguage(player),
                                    new Tuple<>("%name%", group.getName())
                            ).getText());
                        });
                        return true;
                    }
                    case "help" -> {
                        player.sendMessage(TranslatableText.of("playlegend_group_cmd_help_title", Language.getPreferredLanguage(player)).getText());
                        player.sendMessage(TranslatableText.of("playlegend_group_cmd_help_create", Language.getPreferredLanguage(player)).getText());
                        player.sendMessage(TranslatableText.of("playlegend_group_cmd_help_delete", Language.getPreferredLanguage(player)).getText());
                        player.sendMessage(TranslatableText.of("playlegend_group_cmd_help_list", Language.getPreferredLanguage(player)).getText());
                        player.sendMessage(TranslatableText.of("playlegend_group_cmd_help_edit", Language.getPreferredLanguage(player)).getText());
                        player.sendMessage(TranslatableText.of("playlegend_group_cmd_help_unassign", Language.getPreferredLanguage(player)).getText());
                        player.sendMessage(TranslatableText.of("playlegend_group_cmd_help_assign", Language.getPreferredLanguage(player)).getText());
                        player.sendMessage(TranslatableText.of("playlegend_group_cmd_help_info", Language.getPreferredLanguage(player)).getText());
                        player.sendMessage(TranslatableText.of("playlegend_group_cmd_help_display", Language.getPreferredLanguage(player)).getText());
                        return true;
                    }
                    case "assign" -> {
                        if(args.length >= 3) {
                            dev.redcrew.playlegend.entitiy.Player target = PlayerManager.getPlayerByName(args[1]);
                            Group group = GroupManager.getGroupByName(args[2]);
                            LocalDateTime expire = null;

                            if (target == null) {
                                player.sendMessage(TranslatableText.of("playlegend_group_cmd_exists_not_player",
                                        Language.getPreferredLanguage(player), new Tuple<>("%name%", args[1])).getText());
                                return true;
                            }

                            if (group == null) {
                                player.sendMessage(TranslatableText.of("playlegend_group_cmd_exists_not",
                                        Language.getPreferredLanguage(player), new Tuple<>("%name%", args[2])).getText());
                                return true;
                            }

                            if(args.length >= 7) {
                                int days;
                                int hours;
                                int minutes;
                                int seconds;

                                try {
                                    days = Integer.parseInt(args[3]);
                                    hours = Integer.parseInt(args[4]);
                                    minutes = Integer.parseInt(args[5]);
                                    seconds = Integer.parseInt(args[6]);
                                } catch (NumberFormatException ignored) {
                                    player.sendMessage(TranslatableText.of("playlegend_group_cmd_not_number", Language.getPreferredLanguage(player)).getText());
                                    return true;
                                }

                                expire = LocalDateTime.now().plusDays(days).plusHours(hours).plusMinutes(minutes).plusSeconds(seconds);

                            }

                            if(PlayerManager.getGroupsForPlayer(target).contains(group)) {
                                player.sendMessage(TranslatableText.of("playlegend_group_cmd_assign_already_assigned",
                                        Language.getPreferredLanguage(player), new Tuple<>("%name%", args[2])).getText());
                                return true;
                            }

                            PlayerManager.assignGroup(target, group, expire);
                            player.sendMessage(TranslatableText.of("playlegend_group_cmd_assign_assigned",
                                    Language.getPreferredLanguage(player), new Tuple<>("%name%", args[2])).getText());

                            return true;
                        }
                    }
                    case "unassign" -> {
                        if(args.length >= 3) {
                            dev.redcrew.playlegend.entitiy.Player target = PlayerManager.getPlayerByName(args[1]);
                            Group group = GroupManager.getGroupByName(args[2]);
                            LocalDateTime expire = null;

                            if (target == null) {
                                player.sendMessage(TranslatableText.of("playlegend_group_cmd_exists_not_player",
                                        Language.getPreferredLanguage(player), new Tuple<>("%name%", args[1])).getText());
                                return true;
                            }

                            if (group == null) {
                                player.sendMessage(TranslatableText.of("playlegend_group_cmd_exists_not",
                                        Language.getPreferredLanguage(player), new Tuple<>("%name%", args[2])).getText());
                                return true;
                            }

                            if(!PlayerManager.getGroupsForPlayer(target).contains(group)) {
                                player.sendMessage(TranslatableText.of("playlegend_group_cmd_unassign_already_unassigned",
                                        Language.getPreferredLanguage(player), new Tuple<>("%name%", args[2])).getText());
                                return true;
                            }

                            PlayerManager.unassignGroup(target, group);
                            player.sendMessage(TranslatableText.of("playlegend_group_cmd_unassign_unassigned",
                                    Language.getPreferredLanguage(player), new Tuple<>("%name%", args[2])).getText());

                            return true;
                        }
                    }
                    case "display" -> {
                        //todo
                    }
                }
            }

            player.sendMessage(TranslatableText.of("playlegend_group_cmd_usage", Language.getPreferredLanguage(player)).getText());
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length == 1) {
            return List.of("create", "delete", "list", "edit", "help", "assign", "unassign", "info");
        }

        if(args.length == 2 && (args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("delete"))) {
            return GroupManager.getGroups().stream().map(Group::getName).toList();
        }

        if(args.length == 2 && (args[0].equalsIgnoreCase("assign") || args[0].equalsIgnoreCase("unassign"))
                || args[0].equalsIgnoreCase("info")) {
            return PlayerManager.getPlayers().stream().map(dev.redcrew.playlegend.entitiy.Player::getName).toList();
        }

        if(args.length == 3 && (args[0].equalsIgnoreCase("assign") || args[0].equalsIgnoreCase("unassign"))) {
            return GroupManager.getGroups().stream().map(Group::getName).toList();
        }

        if(args.length == 3 && args[0].equalsIgnoreCase("edit")) {
            return List.of("name", "priority", "prefix");
        }

        return null;
    }
}
