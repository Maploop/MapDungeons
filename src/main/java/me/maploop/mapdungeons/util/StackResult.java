package me.maploop.mapdungeons.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 *  Utility class taken from previous code from discord.gg/atlasmc
 */
public class StackResult {
    private final ItemStack stack;

    public StackResult(Material material, int variant) {
        stack = new ItemStack(material, 1, (short) variant);
    }

    public StackResult glow() {
        ItemMeta meta = stack.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.values());
        stack.setItemMeta(meta);
        return this;
    }

    public StackResult glow(boolean flag) {
        if (flag) {
            ItemMeta meta = stack.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.values());
            stack.setItemMeta(meta);
        }
        return this;
    }

    public StackResult lore(String... lore) {
        return lore(Arrays.asList(lore));
    }

    public StackResult lore(List<String> lore) {
        ItemMeta meta = stack.getItemMeta();
        List<String> l = new ArrayList<>();
        for (String s : lore) {
            l.add(SUtil.translateColorWords(s));
        }
        meta.setLore(l);
        stack.setItemMeta(meta);
        return this;
    }

    public StackResult name(String name) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(SUtil.translateColorWords(name));
        stack.setItemMeta(meta);
        return this;
    }

    public StackResult amount(int amount) {
        stack.setAmount(amount);
        return this;
    }



    public ItemStack build() {
        return stack;
    }
}
