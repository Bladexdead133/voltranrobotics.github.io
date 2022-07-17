package com.joseph.lavakitadvanced.Events;

import com.joseph.lavakitadvanced.Commands.HostCommand;
import com.joseph.lavakitadvanced.Game;
import com.joseph.lavakitadvanced.Utils.Items;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;

public class onDeath implements Listener {
    public static ArrayList<Player> deathPlayers;
    public static ArrayList<Player> alivePlayers;
    public onDeath() {
        deathPlayers = new ArrayList<>();
        alivePlayers = new ArrayList<>();
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (HostCommand.gameLevel == 2 && event.getEntity().getWorld().getPVP()) {
            deathPlayers.add(player);
            alivePlayers.remove(player);
            if (alivePlayers.size() != 1) {
                for (Player pl : event.getEntity().getWorld().getPlayers()) {
                    pl.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " is dead! Remained players: " + ChatColor.GREEN + alivePlayers.size());
                    pl.playSound(pl.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0F, 1.0F);
                }
            }else {
                HostCommand.gameLevel = 3;
                HostCommand.host = null;
                String board = ChatColor.GREEN + "Leaderboard: " + "\n" + ChatColor.WHITE + "1. " + ChatColor.YELLOW + alivePlayers.get(0).getName();
                int leadNum = 2;
                for (int i = deathPlayers.size() - 1; i >= 0; i--) {
                    board += "\n" + ChatColor.WHITE + leadNum + ". " + ChatColor.YELLOW + deathPlayers.get(i).getName();
                    leadNum++;
                }

                for (Player pl : event.getEntity().getWorld().getPlayers()) {
                    pl.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " is dead! " + ChatColor.GREEN + alivePlayers.get(0).getName() + ChatColor.YELLOW + " is the last survivor! Thanks everyone for playing!");
                    pl.playSound(pl.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F);
                    pl.sendMessage(board);
                }
                alivePlayers.get(0).sendTitle(ChatColor.GREEN + "YOU WIN", ChatColor.YELLOW + "You are the last survivor. Congrats!");
                for (Player pl : event.getEntity().getWorld().getPlayers()) {
                    if (Items.autoggs.get(pl)) {
                        pl.chat(ChatColor.AQUA + "gg");
                    }
                }
                alivePlayers.clear();
                deathPlayers.clear();
                Game.gameMembers.clear();
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (HostCommand.gameLevel == 2) {
            player.sendTitle(ChatColor.RED + "You're Dead", ChatColor.YELLOW + "Wait for this round to end!");
            player.setGameMode(GameMode.SPECTATOR);
        }else if (HostCommand.gameLevel == 3) {
            player.sendTitle(ChatColor.RED + "Game Over", ChatColor.YELLOW + "This round ended! Thanks for playing!");
            player.setGameMode(GameMode.SPECTATOR);
        }
    }
}
