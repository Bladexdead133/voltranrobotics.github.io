package com.joseph.lavakitadvanced.Events;

import com.joseph.lavakitadvanced.Commands.HostCommand;
import com.joseph.lavakitadvanced.LavaKitAdvanced;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class onBlockBreak implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (HostCommand.gameLevel == 2) {
            Material breakblock = event.getBlock().getType();
            switch (breakblock) {
                case IRON_ORE:
                case DEEPSLATE_IRON_ORE:
                    event.setDropItems(false);
                    player.getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT));
                    break;
                case GOLD_ORE:
                case DEEPSLATE_GOLD_ORE:
                    event.setDropItems(false);
                    player.getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT));
                    break;
                case ANCIENT_DEBRIS:
                    event.setDropItems(false);
                    player.getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.NETHERITE_SCRAP));
                    break;
            }
        }else {
            if (HostCommand.host != null) {
                if (!player.equals(HostCommand.host)) {
                    event.setCancelled(true);
                }
            }else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (HostCommand.gameLevel != 2) {
            if (HostCommand.host != null) {
                if (!player.equals(HostCommand.host)) {
                    event.setCancelled(true);
                    player.getInventory().addItem(new ItemStack(event.getBlock().getType()));
                }
            }else {
                event.setCancelled(true);
            }
        }
    }
}
