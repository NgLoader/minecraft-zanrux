package de.ngloader.survival.item;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import de.ngloader.survival.Survival;

public abstract class Item {

	final Survival core;

	private ItemStack item;

	public Item(Survival core, ItemStack item) {
		this.core = core;
		this.item = item;
	}

	public boolean isItem(ItemStack second) {
		return this.isItem(this.item, second);
	}

	public boolean isItem(ItemStack first, ItemStack second) {
		if (first.hasItemMeta() && first.getItemMeta().hasCustomModelData() && second.hasItemMeta() && second.getItemMeta().hasCustomModelData()) {
			return first.getType().equals(second.getType()) && first.getItemMeta().getCustomModelData() == second.getItemMeta().getCustomModelData();
		}
		return false;
	}

	public BukkitTask runTask(Runnable runnable) {
		return Bukkit.getServer().getScheduler().runTask(this.core, runnable);
	}

	public ItemStack getItem() {
		return this.item;
	}

	public Survival getCore() {
		return this.core;
	}
}