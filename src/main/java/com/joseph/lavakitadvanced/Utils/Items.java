package com.joseph.lavakitadvanced.Utils;

import com.joseph.lavakitadvanced.LavaKitAdvanced;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class Items {
    public static ItemStack configurator;
    public static HashMap<Player, Boolean> autoggs;
    public static ItemStack[] kit;

    public Items() {
        autoggs = new HashMap<>();
        for (Player pl:Bukkit.getServer().getOnlinePlayers()) {
            autoggs.put(pl, false);
        }
        FileConfiguration config = LavaKitAdvanced.config;
        configurator = new ItemStack(Material.COMPASS);
        setItem(configurator, ChatColor.translateAlternateColorCodes('&', config.getString("managerTitleColor") + config.getString("managerTitle")), true, " ");
        ItemStack pickaxe = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta pickMeta = pickaxe.getItemMeta();
        pickMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("pickaxeNameColor") + config.getString("pickaxeName")));
        pickMeta.addEnchant(Enchantment.DURABILITY, 5, true);
        pickMeta.addEnchant(Enchantment.MENDING, 1, true);
        pickMeta.addEnchant(Enchantment.DIG_SPEED, 5, true);
        pickMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3, true);
        pickaxe.setItemMeta(pickMeta);
        ItemStack beef = new ItemStack(Material.COOKED_BEEF, 64);
        ItemStack apples = new ItemStack(Material.APPLE, 10);
        kit = new ItemStack[]{pickaxe, beef, apples};
    }

    public static void setItem(ItemStack item, String name, boolean isGlowing, String lore) {
        ItemMeta meta = item.getItemMeta();
        ArrayList<String> loreList = new ArrayList<>();
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        if (isGlowing) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
        }
        if (!lore.equals(" ")) {
            loreList.add(lore);
            meta.setLore(loreList);
        }
        item.setItemMeta(meta);
    }

    public static String prefix() {
        return ChatColor.translateAlternateColorCodes('&', "&7[&6Lava&4Kit&7] &f");
    }
    public static ItemStack getAutoGG(Player player) {
        ItemStack autogg = new ItemStack(Material.CLOCK);
        String lore = ChatColor.YELLOW + "Status: ";
        if (Items.autoggs.get(player)) {
            lore += ChatColor.GREEN + "On";
        }else {
            lore += ChatColor.RED + "Off";
        }
        setItem(autogg, ChatColor.DARK_BLUE + "Auto-GG", true, lore);
        return autogg;
    }
    public static void warning(Player player, String message) {
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
        player.sendMessage(prefix() + ChatColor.RED + message);
    }
    public static void success(Player player, String message) {
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
        player.sendMessage(prefix() + ChatColor.GREEN + message);
    }

    public static Inventory compassGUI(Player player) {
        FileConfiguration config = LavaKitAdvanced.config;
        Inventory compassGUI = Bukkit.createInventory(player, 45, ChatColor.GREEN + "Game Manager");
        ItemStack black = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        Items.setItem(black, ChatColor.BLACK + " ", false, " ");
        ItemStack starter = new ItemStack(Material.CRAFTING_TABLE);
        if (config.getString("starterKit").equals("Active")) {
            Items.setItem(starter, ChatColor.GREEN + "Starter Kit", true, ChatColor.YELLOW + "Status: " + ChatColor.GREEN + "Active");
        }else {
            Items.setItem(starter, ChatColor.GREEN + "Starter Kit", true, ChatColor.YELLOW + "Status: " + ChatColor.RED + "Deactive");
        }
        ItemStack prepareTime = new ItemStack(Material.CLOCK);
        Items.setItem(prepareTime, ChatColor.GOLD + "Prepare Time (Seconds)", true, ChatColor.YELLOW + "Value: " + ChatColor.AQUA + config.getInt("prepareTime"));
        ItemStack increaseTime = new ItemStack(Material.CLOCK);
        Items.setItem(increaseTime, ChatColor.GOLD + "Lava Increase Time (Seconds)", true, ChatColor.YELLOW + "Value: " + ChatColor.AQUA + config.getInt("lavaTime"));
        ItemStack lavaAmount = new ItemStack(Material.LAVA_BUCKET);
        Items.setItem(lavaAmount, ChatColor.GOLD + "Lava Increase Amount", true, ChatColor.YELLOW + "Value: " + ChatColor.AQUA + config.getInt("lavaAmount"));
        ItemStack start = new ItemStack(Material.COMMAND_BLOCK);
        Items.setItem(start, ChatColor.GREEN + "START", true, " ");
        for(int i = 0; i < 45; i++) {
            switch (i) {
                case 10:
                    compassGUI.setItem(i, starter);
                    break;
                case 12:
                    compassGUI.setItem(i, prepareTime);
                    break;
                case 14:
                    compassGUI.setItem(i, increaseTime);
                    break;
                case 16:
                    compassGUI.setItem(i, lavaAmount);
                    break;
                case 31:
                    compassGUI.setItem(i, start);
                    break;
                default:
                    compassGUI.setItem(i, black);
                    break;
            }
        }
        return compassGUI;
    }
}
