package de.ngloader.survival.util;

import org.bukkit.NamespacedKey;

public class NamespacedKeys {

	public static final NamespacedKey ITEM_MOBCATCHER = create("item.mobcatcher");
	public static final NamespacedKey ITEM_MOBCATCHER_ID = create("item.mobcatcher.id");
	public static final NamespacedKey ITEM_MOBCATCHER_USE = create("item.mobcatcher.use");
	public static final NamespacedKey ITEM_MOBCATCHER_SHELL = create("item.mobcatcher_shell");

	public static final NamespacedKey ITEM_BACKPACK_SIZE = create("item.backpack");
	public static final NamespacedKey ITEM_BACKPACK_SAVE_ID = create("item.backpack.id");

	public static final NamespacedKey RECIPE_MOBCATCHER = create("recipe.mobcatcher");
	public static final NamespacedKey RECIPE_INVISIBLE_ITEM_FRAME = create("recipe.invisible_item_frame");
	public static final NamespacedKey RECIPE_WEB = create("recipe.web");
	public static final NamespacedKey RECIPE_CLAMP = create("recipe.clamp");
	public static final NamespacedKey RECIPE_MOBILE_CRATING_TABLE = create("recipe.mobile_crafting_table");
	public static final NamespacedKey RECIPE_LEATHER = create("recipe.leather");
	public static final NamespacedKey RECIPE_SADDLE = create("recipe.saddle");
	public static final NamespacedKey RECIPE_WOOL = create("recipe.wool");
	public static final NamespacedKey RECIPE_WOOL_RECOLOR = create("recipe.wool_recolor");
	public static final NamespacedKey RECIPE_WOOL_TO_STRING = create("recipe.string");
	public static final NamespacedKey RECIPE_SNOW_BLOCK = create("recipe.snow_block");
	public static final NamespacedKey RECIPE_CLAY_RECOLOR = create("recipe.clay_recolor");
	public static final NamespacedKey RECIPE_BACKPACK_SMALL = create("recipe.backpack");

	@SuppressWarnings("deprecation")
	private static NamespacedKey create(String name) {
		return new NamespacedKey("survival", name);
	}
}