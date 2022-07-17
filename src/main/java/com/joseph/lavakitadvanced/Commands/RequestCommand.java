package com.joseph.lavakitadvanced.Commands;

import com.joseph.lavakitadvanced.Utils.Items;
import com.joseph.lavakitadvanced.LavaKitAdvanced;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class RequestCommand implements CommandExecutor, Listener {
    public static ArrayList<Player> requests;

    public RequestCommand() {
        requests = new ArrayList<>();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (HostCommand.host != null) {
                if (!player.equals(HostCommand.host)) {
                    if (HostCommand.gameLevel == 1) {
                        if (!requests.contains(player)) {
                            Items.success(player, "You sended a host request to " + ChatColor.AQUA + HostCommand.host.getName() + ChatColor.GREEN + "!");
                            TextComponent text = new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "You have got a host request from " + ChatColor.AQUA + player.getName() + ChatColor.YELLOW + ". "));
                            TextComponent accept = new TextComponent(TextComponent.fromLegacyText(ChatColor.GREEN + "[ACCEPT] "));
                            TextComponent decline = new TextComponent(TextComponent.fromLegacyText(ChatColor.RED + "[DECLINE]"));
                            requests.add(player);
                            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + player.getName()));
                            decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/decline " + player.getName()));
                            HostCommand.host.spigot().sendMessage(text, accept, decline);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (requests.contains(player)) {
                                        requests.remove(player);
                                        Items.warning(player, "Your request to " + ChatColor.YELLOW + HostCommand.host.getName() + ChatColor.RED + " is removed!");
                                    }
                                }
                            }.runTaskLater(LavaKitAdvanced.plugin, 2400L);
                        }else {
                            Items.warning(player, "You have sended a request already!");
                        }
                    }else {
                        Items.warning(player, "You can't send a request right now!");
                    }
                } else {
                    Items.warning(player, "You are already the host!");
                }
            }else {
                Items.warning(player, "There is no host in the game!");
            }
        }else {
            System.out.println("You have to be a player to execute this command!");
        }
        return true;
    }
}
