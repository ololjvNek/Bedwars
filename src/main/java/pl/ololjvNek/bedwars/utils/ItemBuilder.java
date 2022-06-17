package pl.ololjvNek.bedwars.utils;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemBuilder
{
    private ItemStack is;
    
    public ItemBuilder(final Material m) {
        this(m, 1);
    }
    
    public ItemBuilder(final ItemStack is) {
        this.is = is;
    }
    
    public ItemBuilder(final Material m, final int amount) {
        this.is = new ItemStack(m, amount);
    }
    
    public ItemBuilder(final Material m, final int amount, final byte durability) {
        this.is = new ItemStack(m, amount, (short)durability);
    }
    
    public ItemBuilder clone() {
        return new ItemBuilder(this.is);
    }
    
    public ItemBuilder setDurability(final short dur) {
        this.is.setDurability(dur);
        return this;
    }
    
    public ItemBuilder setName(final String name) {
        final ItemMeta im = this.is.getItemMeta();
        im.setDisplayName(name);
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder addUnsafeEnchantment(final Enchantment ench, final int level) {
        this.is.addUnsafeEnchantment(ench, level);
        return this;
    }
    
    public ItemBuilder removeEnchantment(final Enchantment ench) {
        this.is.removeEnchantment(ench);
        return this;
    }
    
    public ItemBuilder setSkullOwner(final String owner) {
        try {
            final SkullMeta im = (SkullMeta)this.is.getItemMeta();
            im.setOwner(owner);
            this.is.setItemMeta((ItemMeta)im);
        }
        catch (ClassCastException ex) {}
        return this;
    }
    
    public ItemBuilder addEnchant(final Enchantment ench, final int level) {
        final ItemMeta im = this.is.getItemMeta();
        im.addEnchant(ench, level, true);
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder addEnchantments(final Map<Enchantment, Integer> enchantments) {
        this.is.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilder addUnsafeEnchantments(final Map<Enchantment, Integer> enchantments) {
        this.is.addUnsafeEnchantments(enchantments);
        return this;
    }
    
    public ItemBuilder setInfinityDurability() {
        ItemMeta meta = this.is.getItemMeta();
        meta.spigot().setUnbreakable(true);
        this.is.setItemMeta(meta);
        return this;
    }
    
    public ItemBuilder setLore(final String... lore) {
        final ItemMeta im = this.is.getItemMeta();
        im.setLore(Arrays.asList(lore));
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder setLore(final List<String> lore) {
        final ItemMeta im = this.is.getItemMeta();
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder removeLoreLine(final String line) {
        final ItemMeta im = this.is.getItemMeta();
        final List<String> lore = new ArrayList<String>(im.getLore());
        if (!lore.contains(line)) {
            return this;
        }
        lore.remove(line);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder removeLoreLine(final int index) {
        final ItemMeta im = this.is.getItemMeta();
        final List<String> lore = new ArrayList<String>(im.getLore());
        if (index < 0 || index > lore.size()) {
            return this;
        }
        lore.remove(index);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder addLoreLine(final String line) {
        final ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<String>();
        if (im.hasLore()) {
            lore = new ArrayList<String>(im.getLore());
        }
        lore.add(line);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder addLoreLine(final String line, final int pos) {
        final ItemMeta im = this.is.getItemMeta();
        final List<String> lore = new ArrayList<String>(im.getLore());
        lore.set(pos, line);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }
    
    @SuppressWarnings("deprecation")
	public ItemBuilder setDyeColor(final DyeColor color) {
        this.is.setDurability((short)color.getData());
        return this;
    }
    
    @Deprecated
    public ItemBuilder setWoolColor(final DyeColor color) {
        if (!this.is.getType().equals((Object)Material.WOOL)) {
            return this;
        }
        this.is.setDurability((short)color.getData());
        return this;
    }
    
    public ItemBuilder setLeatherArmorColor(final Color color) {
        try {
            final LeatherArmorMeta im = (LeatherArmorMeta)this.is.getItemMeta();
            im.setColor(color);
            this.is.setItemMeta((ItemMeta)im);
        }
        catch (ClassCastException ex) {}
        return this;
    }
    
    public ItemStack toItemStack() {
        return this.is;
    }
}
