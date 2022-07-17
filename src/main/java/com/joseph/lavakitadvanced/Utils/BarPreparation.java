package com.joseph.lavakitadvanced.Utils;

import com.joseph.lavakitadvanced.Commands.HostCommand;
import com.joseph.lavakitadvanced.Game;
import com.joseph.lavakitadvanced.LavaKitAdvanced;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static com.joseph.lavakitadvanced.Game.gameMembers;

public class BarPreparation {
    private int taskID = -1;
    private int taskID2 = -1;
    private final Plugin plugin = LavaKitAdvanced.plugin;
    private BossBar bar;
    private BossBar bar2;
    private FileConfiguration config = LavaKitAdvanced.config;

    public void addPlayer(Player player) {
        bar.addPlayer(player);
    }

    public BossBar getBar() {
        return bar;
    }

    public void createBar() {
        bar = Bukkit.createBossBar(ChatColor.GOLD + "Preparation Time", BarColor.YELLOW, BarStyle.SOLID);
        bar.setVisible(true);
    }
    private void createBar2() {
        bar2 = Bukkit.createBossBar(ChatColor.GOLD + "Lava Level: " + ChatColor.YELLOW + Game.lavaLevel, BarColor.RED, BarStyle.SOLID);
        for (Player pl:gameMembers) {
            bar2.addPlayer(pl);
        }
        bar2.setVisible(true);
    }

    public void cast() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            double progress = 1.0;
            double time = 1.0 / config.getInt("prepareTime");
            @Override
            public void run() {
                if (HostCommand.gameLevel == 2) {
                    bar.setProgress(progress);
                    progress -= time;
                    if (progress <= 0) {
                        bar.setVisible(false);
                        createBar2();
                        castGame();
                        Bukkit.getScheduler().cancelTask(taskID);
                    }
                }else {
                    Bukkit.getScheduler().cancelTask(taskID);
                    bar.setVisible(false);
                }
            }
        }, 0, 20L);
    }
    private void castGame() {
        taskID2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            double progres = 1.0;
            double time = 1.0 / config.getInt("lavaTime");
            @Override
            public void run() {
                if (HostCommand.gameLevel == 2) {
                    bar2.setProgress(progres);
                    progres -= time;
                    if (progres <= 0) {
                        progres = 1.0;
                        bar2.setTitle(ChatColor.GOLD + "Lava Level: " + ChatColor.YELLOW + Game.lavaLevel);
                    }
                }else {
                    Bukkit.getScheduler().cancelTask(taskID2);
                    bar2.setVisible(false);
                }
            }
        }, 0, 20L);
    }
}
