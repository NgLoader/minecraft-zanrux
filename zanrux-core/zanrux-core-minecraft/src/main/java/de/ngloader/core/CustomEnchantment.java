package de.ngloader.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.ngloader.core.util.ItemFactory;

public abstract class CustomEnchantment extends Enchantment {

	private static final NavigableMap<Integer, String> ROMAN_SYMBOLS = new TreeMap<>(Map.ofEntries(
			Map.entry(1, "I"), Map.entry(4, "IV"), Map.entry(5, "V"), Map.entry(9, "IX"), Map.entry(10, "X"),
			Map.entry(40, "XL"), Map.entry(50, "L"), Map.entry(90, "XC"), Map.entry(100, "C"), Map.entry(400, "CD"),
			Map.entry(500, "D"), Map.entry(900, "CM"), Map.entry(1000, "M")));

	private final String displayName;
	private final boolean findable;

	public CustomEnchantment(NamespacedKey key, String displayName, boolean findable) {
		super(key);

		this.displayName = displayName;
		this.findable = findable;
	}

	public void applyOn(ItemStack item, int level) {
		item.addUnsafeEnchantment(this, level);

		ItemMeta meta = item.getItemMeta();
		if (!meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
			List<String> lores = new ArrayList<>();
			lores.add(ChatColor.GRAY + this.displayName + " " + this.getLevelName(level));

			if (meta.hasLore()) {
				for (String lore : meta.getLore()) {
					if (!lore.startsWith(ChatColor.GRAY + this.displayName + " ")) {
						lores.add(lore);
					}
				}
			}
			meta.setLore(lores);
			item.setItemMeta(meta);
		}
	}

	public void applyOn(ItemFactory item, int level) {
		item.addUnsafeEnchantment(this, level);

		ItemMeta meta = item.getEditingItemMeta();
		if (!meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
			List<String> lores = new ArrayList<>();
			lores.add(ChatColor.GRAY + this.displayName + " " + this.getLevelName(level));

			if (meta.hasLore()) {
				for (String lore : meta.getLore()) {
					if (!lore.startsWith(ChatColor.GRAY + this.displayName + " ")) {
						lores.add(lore);
					}
				}
			}
			meta.setLore(lores);
			item.setEditingItemMeta(meta);
		}
	}

	public boolean hasEnchant(ItemStack item) {
		return item != null && (item.containsEnchantment(this) || (item.hasItemMeta() && item.getItemMeta().hasEnchant(this)));
	}

	public int getEnchantLevel(ItemStack item) {
		return item != null && item.hasItemMeta() ? item.getItemMeta().getEnchantLevel(this) : 0;
	}

	public String getLevelName(int level) {
		StringBuilder stringBuilder = new StringBuilder();
		while (level > 0) {
			Entry<Integer, String> entry = ROMAN_SYMBOLS.floorEntry(level);
			level -= entry.getKey();
			stringBuilder.append(entry.getValue());
		}
		return stringBuilder.toString();
	}

	public int getMinCost(int level) {
		return 1 + level * 10;
	}

	public int getMaxCost(int level) {
		return this.getMinCost(level) + 5;
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		return false;
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	@Override
	public boolean isCursed() {
		return false;
	}

	@Override
	public boolean isTreasure() {
		return false;
	}

	@Override
	public String getName() {
		return this.displayName;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public boolean isFindable() {
		return this.findable;
	}
}