package de.ngloader.core.help;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.ngloader.core.util.ItemFactory;

public class HelpBuilder {

	private static final String DEFAULT_NAME = "undefinied";
	private static final String DEFAULT_DESCRIPTION = "";
	private static final ItemFactory DEFAULT_ITEM_FACTORY = new ItemFactory(Material.BARRIER).addAllFlag();

	private String name = HelpBuilder.DEFAULT_NAME;

	private String description = HelpBuilder.DEFAULT_DESCRIPTION;
	private ItemFactory displayItem = HelpBuilder.DEFAULT_ITEM_FACTORY;

	public HelpBuilder() { }

	public HelpBuilder(String name) {
		this.setName(name);
	}

	public HelpBuilder(String name, String description) {
		this(name);
		this.setDescription(description);
	}

	public HelpBuilder(String name, String description, ItemFactory displayItem) {
		this(name, description);
		this.setDisplayItem(displayItem);
	}

	public HelpBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public HelpBuilder setDescription(String description) {
		this.description = description;
		return this;
	}

	public HelpBuilder setDisplayItem(ItemFactory displayItem) {
		this.displayItem = displayItem;
		return this;
	}

	public IHelp build() {
		return new IHelp() {
			
			@Override
			public String getName() {
				return HelpBuilder.this.name;
			}
			
			@Override
			public ItemStack getDisplayItem() {
				return HelpBuilder.this.displayItem.build();
			}
			
			@Override
			public String getDescription() {
				return HelpBuilder.this.description;
			}
		};
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public ItemFactory getDisplayItem() {
		return this.displayItem;
	}
}