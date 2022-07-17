package com.joseph.lavakitadvanced.Events;

import com.joseph.lavakitadvanced.Commands.HostCommand;
import com.joseph.lavakitadvanced.Game;
import com.joseph.lavakitadvanced.LavaKitAdvanced;
import com.joseph.lavakitadvanced.Utils.Items;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class MoveEvent implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!player.equals(HostCommand.host) && HostCommand.gameLevel != 2) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', "&7(&a+&7) &e" + player.getName()));
        if (!Items.autoggs.containsKey(player)) {
            Items.autoggs.put(player, false);
        }
        if (HostCommand.gameLevel != 2) {
            if (HostCommand.gameLevel != 3) {
                if (HostCommand.gameLevel == 1) {
                    boolean stat = true;
                    for (int i = 0; i < player.getInventory().getSize(); i++) {
                        if (player.getInventory().getItem(i) != null) {
                            if (player.getInventory().getItem(i).equals(Items.getAutoGG(player))) {
                                stat = false;
                                break;
                            }
                        }
                    }
                    if (stat) {
                        player.getInventory().addItem(Items.getAutoGG(player));
                    }
                }
                PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS, 60, 50, true, false, false);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (HostCommand.gameLevel != 2 || HostCommand.gameLevel != 3) {
                            if (HostCommand.host != null) {
                                if (!player.equals(HostCommand.host)) {
                                    player.addPotionEffect(effect);
                                    player.setInvulnerable(true);
                                } else {
                                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                                }
                            } else {
                                player.addPotionEffect(effect);
                                player.setInvulnerable(true);
                            }
                        } else {
                            this.cancel();
                        }
                    }
                }.runTaskTimer(LavaKitAdvanced.plugin, 0L, 20L);
            }else {
                player.setGameMode(GameMode.SPECTATOR);
                player.sendTitle(ChatColor.RED + "This Game is Over", ChatColor.YELLOW + "Wait for host to make another round!");
            }
        }else {
            if (!Game.gameMembers.contains(player)) {
                player.kickPlayer(ChatColor.RED + "There is already a started game! Come again when it's over!");
            }
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', "&7(&c-&7) &e" + player.getName()));
    }

    @EventHandler
    public void noNether(PlayerPortalEvent event) {
        if (event.getTo().getWorld().getEnvironment().equals(World.Environment.NETHER) || event.getTo().getWorld().getEnvironment().equals(World.Environment.THE_END)) {
            event.setCancelled(true);
        }
    }
}
