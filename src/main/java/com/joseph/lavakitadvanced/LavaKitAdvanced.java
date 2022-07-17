package com.joseph.lavakitadvanced;

import com.joseph.lavakitadvanced.Commands.*;
import com.joseph.lavakitadvanced.Events.MoveEvent;
import com.joseph.lavakitadvanced.Events.configuratorGUI;
import com.joseph.lavakitadvanced.Events.onBlockBreak;
import com.joseph.lavakitadvanced.Events.onDeath;
import com.joseph.lavakitadvanced.Utils.Items;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class LavaKitAdvanced extends JavaPlugin {
    public static FileConfiguration config;
    public static Plugin plugin;
    @Override
    public void onEnable() {
        plugin = this;
        config = getConfig();
        Items items = new Items();
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        HostCommand hc = new HostCommand();
        RequestCommand rc = new RequestCommand();
        Game game = new Game();
        getServer().getPluginManager().registerEvents(new MoveEvent(), this);
        getServer().getPluginManager().registerEvents(new configuratorGUI(), this);
        getServer().getPluginManager().registerEvents(new onBlockBreak(), this);
        getServer().getPluginManager().registerEvents(new onDeath(), this);
        getCommand("host").setExecutor(hc);
        getCommand("request").setExecutor(rc);
        getCommand("accept").setExecutor(new HostAccept());
        getCommand("decline").setExecutor(new HostDecline());
        getCommand("stopGame").setExecutor(new GameStop());
        Bukkit.getServer().getWorlds().get(0).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        Bukkit.getServer().getWorlds().get(0).setDifficulty(Difficulty.PEACEFUL);
        Bukkit.getServer().getWorlds().get(0).setPVP(false);

    }

    @Override
    public void onDisable() {
        while (Bukkit.getServer().getBossBars().hasNext()) {
            Bukkit.getServer().getBossBars().next().removeAll();
        }
    }
}
