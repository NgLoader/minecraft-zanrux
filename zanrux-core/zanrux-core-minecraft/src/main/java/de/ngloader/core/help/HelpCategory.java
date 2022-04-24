package de.ngloader.core.help;

import org.bukkit.inventory.ItemStack;

import de.ngloader.core.util.ItemFactory;

public class HelpCategory {

	private final String title;
	private final ItemStack item;

	public HelpCategory(ItemFactory item) {
		this.title = item.getDisplayName();
		this.item = item.build();
	}

	public String getTitle() {
		return this.title;
	}

	public ItemStack getItem() {
		return this.item;
	}
}