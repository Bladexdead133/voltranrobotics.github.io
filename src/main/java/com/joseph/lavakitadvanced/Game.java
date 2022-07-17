package com.joseph.lavakitadvanced;

import com.joseph.lavakitadvanced.Commands.HostCommand;
import com.joseph.lavakitadvanced.Events.onDeath;
import com.joseph.lavakitadvanced.Utils.BarPreparation;
import com.joseph.lavakitadvanced.Utils.Items;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import javax.xml.bind.ValidationEvent;
import java.util.ArrayList;

public class Game {
    public static ArrayList<Player> gameMembers;
    static FileConfiguration config = LavaKitAdvanced.config;

    static BarPreparation barp = new BarPreparation();
    public static int lavaLevel = config.getInt("lavaLevel");

    public Game() {
        gameMembers = new ArrayList<>();
    }
    public static void startGame(World world) {
        HostCommand.gameLevel = 2;
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setPVP(false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        world.getWorldBorder().setCenter(HostCommand.host.getLocation().getBlockX(), HostCommand.host.getLocation().getBlockZ());
        world.getWorldBorder().setSize(config.getInt("borderSize"));
        world.getWorldBorder().setDamageAmount(100);
        barp.createBar();
        for (Player pl:world.getPlayers()) {
            if (config.getString("starterKit").equals("Active")) {
                pl.getInventory().addItem(Items.kit);
            }
            int prepareMinutes = (int) config.getInt("prepareTime") / 60;
            int prepareSeconds = config.getInt("prepareTime") % 60;
            int lavaMinutes = (int) config.getInt("lavaTime") / 60;
            int lavaSeconds = config.getInt("lavaTime") % 60;
            pl.sendMessage(Items.prefix() + ChatColor.translateAlternateColorCodes('&', "&aThe game is started! Lava will start rising in &e" + prepareMinutes + " &aminutes and &e" + prepareSeconds + " &aseconds. After that, lava will rise &e" + config.getInt("lavaAmount") + " &alayer in &e" + lavaMinutes + " &aminutes and &e" + lavaSeconds + " &aseconds!"));
            pl.playSound(pl.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
            pl.setBedSpawnLocation(safer(HostCommand.host.getLocation()));
            barp.addPlayer(pl);
            gameMembers.add(pl);
            onDeath.alivePlayers.add(pl);
            pl.setAllowFlight(false);
            pl.setInvulnerable(false);
            pl.teleport(safer(HostCommand.host.getLocation()));
        }
        barp.cast();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (HostCommand.gameLevel == 2) {
                    world.setGameRule(GameRule.KEEP_INVENTORY, false);
                    world.setDifficulty(Difficulty.NORMAL);
                    world.setPVP(true);
                    for (Player pl : world.getPlayers()) {
                        Items.success(pl, "Lava is starting to increase! Good luck <3");
                        pl.playSound(pl.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0F, 1.0F);
                    }
                    new BukkitRunnable() {
                        World world = HostCommand.host.getWorld();
                        @Override
                        public void run() {
                            if (HostCommand.gameLevel == 2) {
                                WorldBorder border = world.getWorldBorder();
                                for (int x = border.getCenter().getBlockX() - (config.getInt("borderSize") / 2); x <= border.getCenter().getBlockX() + (config.getInt("borderSize") / 2); x++) {
                                    for (int y = 0; y <= lavaLevel; y++) {
                                        for (int z = border.getCenter().getBlockZ() - (config.getInt("borderSize") / 2); z <= border.getCenter().getBlockZ() + (config.getInt("borderSize") / 2); z++) {
                                            if (world.getBlockAt(x,y,z).getType().isAir()) {
                                                world.getBlockAt(x,y,z).setType(Material.LAVA);
                                            }
                                        }
                                    }
                                }
                                lavaLevel += config.getInt("lavaAmount");
                            }else {
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(LavaKitAdvanced.plugin, 0L, (long) config.getInt("lavaTime") * 20);
                }
            }
        }.runTaskLater(LavaKitAdvanced.plugin, (long) config.getInt("prepareTime") * 20);
    }

    public static Location safer(Location location) {
        Location safe = location;
        for (int y = safe.getWorld().getMaxHeight(); y >= 0; y--) {
            safe.setY(y);
            if (!safe.getBlock().getType().isAir()) {
                safe.setY(y + 1);
                return safe;
            }
        }
        throw new IllegalArgumentException("There is no safe block in this y axis!");
    }
}
