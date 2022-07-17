package com.joseph.lavakitadvanced.Commands;

import com.joseph.lavakitadvanced.Utils.Items;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameStop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (HostCommand.host != null && HostCommand.gameLevel == 2) {
                if (player.equals(HostCommand.host)) {
                    HostCommand.gameLevel = 3;
                    HostCommand.host = null;
                    for (Player pl:player.getWorld().getPlayers()) {
                        Items.success(pl, "The game is ended by the host. That was a good game everyone!");
                    }
                }else {
                    Items.warning(player, "You are not host of the game!");
                }
            }else {
                Items.warning(player, "There is not a started game!");
            }
        }else {
            System.out.println("You have to be a player to use this command!");
        }
        return true;
    }
}
