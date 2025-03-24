package dev.redcrew.playlegend.command;

import dev.redcrew.playlegend.entitiy.Group;
import dev.redcrew.playlegend.language.Language;
import dev.redcrew.playlegend.language.TranslatableText;
import dev.redcrew.playlegend.language.Tuple;
import dev.redcrew.playlegend.manager.GroupManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
public class GroupCMD implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(sender instanceof Player player) {
            if(args.length >= 1) {
                switch (args[0].toLowerCase()) {
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
                        return true;
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
            return List.of("create", "delete", "list", "edit", "help");
        }

        if(args.length == 2 && (args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("delete"))) {
            return GroupManager.getGroups().stream().map(Group::getName).toList();
        }

        if(args.length == 3 && args[0].equalsIgnoreCase("edit")) {
            return List.of("name", "priority", "prefix");
        }

        return null;
    }
}
