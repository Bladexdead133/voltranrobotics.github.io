package com.joseph.lavakitadvanced.Commands;

import com.joseph.lavakitadvanced.Utils.Items;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HostCommand implements CommandExecutor {
    public static int gameLevel;

    public static Player host;

    public HostCommand() {
        gameLevel = 0;
        host = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                if (gameLevel == 0) {
                    host = player;
                    gameLevel = 1;
                    for (Player pl:player.getWorld().getPlayers()) {
                        pl.getInventory().addItem(Items.getAutoGG(pl));
                    }
                    Items.success(player, "Now you are the host of game! You can configure game options with your compass!");
                    player.setAllowFlight(true);
                    player.setInvulnerable(true);
                    player.getInventory().addItem(Items.configurator);
                } else if (gameLevel == 1) {
                    if (host != null) {
                        if (host.equals(player)) {
                            Items.warning(player, "You are already the host of the running game!");
                        } else {
                            Items.warning(player, "There is already a started game! Current host is " + host.getName() + ". Send a host request with /request");
                        }
                    }
                } else {
                    if (host != null) {
                        Items.warning(player, "There is already a started game! Current host is " + host.getName() + ".");
                    }
                }
            }else {
                Items.warning(player, "You have to be an operator to use this command!");
            }
        }else {
            System.out.println("You have to be a player to execute this command!");
        }
        return true;
    }
}
/*
gameLevel 0 = Game is not started yet
gameLevel 1 = Host is selected, but game is not started
gameLevel 2 = Game is started, and host cannot changeable
 */