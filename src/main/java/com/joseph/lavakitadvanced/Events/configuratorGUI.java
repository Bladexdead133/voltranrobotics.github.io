package com.joseph.lavakitadvanced.Events;

import com.joseph.lavakitadvanced.Commands.HostCommand;
import com.joseph.lavakitadvanced.Game;
import com.joseph.lavakitadvanced.Utils.Items;
import com.joseph.lavakitadvanced.LavaKitAdvanced;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class configuratorGUI implements Listener {
    Map<Player, String> waitingForInput;
    FileConfiguration config = LavaKitAdvanced.config;

    public configuratorGUI() {
        waitingForInput = new HashMap<>();
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (HostCommand.host != null) {
            if (player.equals(HostCommand.host) && event.getItemDrop().getItemStack().equals(Items.configurator)) {
                event.setCancelled(true);
            }else if (event.getItemDrop().getItemStack().equals(Items.getAutoGG(player))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onGUI(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (HostCommand.host != null) {
            if (player.getInventory().getItemInMainHand().equals(Items.configurator)) {
                if (player.equals(HostCommand.host)) {
                    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
                        player.openInventory(Items.compassGUI(player));
                    }
                }else {
                    Items.warning(player, "You have to be the host to use this command!");
                }
            }else if (player.getInventory().getItemInMainHand().equals(Items.getAutoGG(player))) {
                if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    int index = -1;
                    for (int i = 0; i < player.getInventory().getSize(); i++) {
                        if (player.getInventory().getItem(i) != null) {
                            if (player.getInventory().getItem(i).equals(Items.getAutoGG(player))) {
                                index = i;
                            }
                        }
                    }
                    player.getInventory().remove(Items.getAutoGG(player));
                    if (index != -1) {
                        if (Items.autoggs.get(player)) {
                            Items.autoggs.put(player, false);
                        } else {
                            Items.autoggs.put(player, true);
                        }
                        player.getInventory().setItem(index, Items.getAutoGG(player));
                        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (ChatColor.stripColor(event.getView().getTitle()).equals("Game Manager")) {
            event.setCancelled(true);
            if (event.getClickedInventory().getContents() != null && event.getCurrentItem() != null) {
                ItemStack[] conts = event.getClickedInventory().getContents();
                ItemStack item = event.getCurrentItem();
                if (item.equals(conts[10])) {
                    if (config.getString("starterKit").equals("Active")) {
                        config.set("starterKit", "Deactive");
                    } else {
                        config.set("starterKit", "Active");
                    }
                    player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
                    player.closeInventory();
                    player.openInventory(Items.compassGUI(player));
                } else if (item.equals(conts[12])) {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
                    player.sendMessage(Items.prefix() + ChatColor.YELLOW + "Please enter the new value of prepare time: ");
                    waitingForInput.put(player, "prepareTime");
                } else if (item.equals(conts[14])) {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
                    player.sendMessage(Items.prefix() + ChatColor.YELLOW + "Please enter the new value of lava increase time: ");
                    waitingForInput.put(player, "lavaTime");
                } else if (item.equals(conts[16])) {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
                    player.sendMessage(Items.prefix() + ChatColor.YELLOW + "Please enter the new value of lava increase amount: ");
                    waitingForInput.put(player, "lavaAmount");
                } else if (item.equals(conts[31])) {
                    player.closeInventory();
                    HostCommand.host.getInventory().remove(Items.configurator);
                    if (player.getWorld().getPlayers().size() != 1) {
                        for (Player pl:player.getWorld().getPlayers()) {
                            pl.sendTitle(ChatColor.YELLOW + "New game starts in:", "");
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (Player pl:player.getWorld().getPlayers()) {
                                    pl.sendTitle(ChatColor.GREEN + "5", "");
                                    pl.playSound(pl.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
                                }
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        for (Player pl:player.getWorld().getPlayers()) {
                                            pl.sendTitle(ChatColor.GREEN + "4", "");
                                            pl.playSound(pl.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
                                        }
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                for (Player pl:player.getWorld().getPlayers()) {
                                                    pl.sendTitle(ChatColor.YELLOW + "3", "");
                                                    pl.playSound(pl.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
                                                }
                                                new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        for (Player pl:player.getWorld().getPlayers()) {
                                                            pl.sendTitle(ChatColor.YELLOW + "2", "");
                                                            pl.playSound(pl.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
                                                        }
                                                        new BukkitRunnable() {
                                                            @Override
                                                            public void run() {
                                                                for (Player pl:player.getWorld().getPlayers()) {
                                                                    pl.sendTitle(ChatColor.RED + "1", "");
                                                                    pl.playSound(pl.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
                                                                }
                                                                new BukkitRunnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        for (Player pl:player.getWorld().getPlayers()) {
                                                                            pl.sendTitle(ChatColor.GREEN + "GO!", "");
                                                                            pl.playSound(pl.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
                                                                            pl.getInventory().remove(Items.getAutoGG(pl));
                                                                        }
                                                                        Game.startGame(HostCommand.host.getWorld());
                                                                    }
                                                                }.runTaskLater(LavaKitAdvanced.plugin, 20L);
                                                            }
                                                        }.runTaskLater(LavaKitAdvanced.plugin, 20L);
                                                    }
                                                }.runTaskLater(LavaKitAdvanced.plugin, 20L);
                                            }
                                        }.runTaskLater(LavaKitAdvanced.plugin, 20L);
                                    }
                                }.runTaskLater(LavaKitAdvanced.plugin, 20L);
                            }
                        }.runTaskLater(LavaKitAdvanced.plugin, 20L);
                    }else {
                        Items.warning(player, "There has to be more than 1 people to start the game!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInput(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (waitingForInput != null && waitingForInput.containsKey(player)) {
            if (isInt(message)) {
                event.setCancelled(true);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        config.set(waitingForInput.get(player), Integer.parseInt(message));
                        waitingForInput.remove(player);
                        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
                        player.openInventory(Items.compassGUI(player));
                    }
                }.runTask(LavaKitAdvanced.plugin);
            }else {
                Items.warning(player, "You've typed a wrong argument, try again!");
            }
        }
    }

    private boolean isInt(String s) {
        try {
            int i = Integer.parseInt(s);
            return true;
        }catch (NumberFormatException e) {
            return false;
        }
    }
}
/*
Configurables:
    - Prepare time
    - Lava increase time
    - Lava increase amount
    - Starter kit
                        new BukkitRunnable() {
                            @Override
                            public void run() {

                            }
                        }.runTaskLater(LavaKitAdvanced.plugin, 20L);
 */
