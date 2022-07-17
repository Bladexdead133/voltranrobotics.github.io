package com.joseph.lavakitadvanced.Commands;

import com.joseph.lavakitadvanced.Utils.Items;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HostDecline implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (HostCommand.host != null) {
                if (player.equals(HostCommand.host)) {
                    if (args.length == 1) {
                            Player player1 = player.getServer().getPlayer(args[0]);
                            if (RequestCommand.requests != null) {
                                if (RequestCommand.requests.contains(player1)) {
                                    if (HostCommand.gameLevel == 1) {
                                        Items.success(HostCommand.host, "You have declined the request from " + ChatColor.YELLOW + player1.getName() + ChatColor.GREEN + " successfully!");
                                        Items.warning(player1, "Your request declined by the host!");
                                        RequestCommand.requests.remove(player1);
                                    } else {
                                        Items.warning(player, "You can't decline this request anymore!");
                                        RequestCommand.requests.remove(player1);
                                    }
                                } else {
                                    Items.warning(player, "This player don't have a current request!");
                                }
                            }else {
                                Items.warning(player, "This player don't have a current request!");
                            }
                    }else {
                        Items.warning(player, "You have to enter a player's name as argument!");
                    }
                }else {
                    Items.warning(player, "You have to be host to use this command!");
                }
            }
        }else {
            System.out.println("You have to be a player to execute this command!");
        }
        return true;
    }
}
