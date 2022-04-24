package de.ngloader.survival.crafting.recipe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

import de.ngloader.core.util.ItemFactory;
import de.ngloader.survival.crafting.IRecipe;
import de.ngloader.survival.util.NamespacedKeys;

public class LeatherRecipe extends FurnaceRecipe implements IRecipe {

	public static final ItemStack LEATHER_ITEMSTACK = new ItemFactory(Material.LEATHER).build();

	public LeatherRecipe() {
		super(NamespacedKeys.RECIPE_LEATHER, LEATHER_ITEMSTACK, Material.ROTTEN_FLESH, .15F, 20 * 15);
	}

	@Override
	public NamespacedKey getNamespaceKey() {
		return this.getKey();
	}
}