package me.itzmarcus.azuriteams.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

/**
 * Created by marcus on 09-07-2016.
 */
public class CustomItems {

    public static ItemStack newItem(Material material) {
        ItemStack item = new ItemStack(material, 1);

        return item;
    }

    public static ItemStack newItem(Material material, int amount) {
        ItemStack item = new ItemStack(material, amount);

        return item;
    }

    public static ItemStack newItem(Material material, short data) {
        ItemStack item = new ItemStack(material, 1, data);

        return item;
    }

    public static ItemStack newItem(Material material, String displayename) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayename);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack newItem(Material material, String displayename, Map<Enchantment, Integer> enchantmentIntegerMap) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayename);
        for (Enchantment e : enchantmentIntegerMap.keySet()) {
            meta.addEnchant(e, enchantmentIntegerMap.get(e), false);
        }
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack newItem(Material material, int data, String displayename) {
        ItemStack item = new ItemStack(material, 1, (short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayename);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack newItem(Material material, String displayename, List<String> lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayename);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack newItem(Material material, int data, String displayename, List<String> lore) {
        ItemStack item = new ItemStack(material, 1, (short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayename);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }
}
