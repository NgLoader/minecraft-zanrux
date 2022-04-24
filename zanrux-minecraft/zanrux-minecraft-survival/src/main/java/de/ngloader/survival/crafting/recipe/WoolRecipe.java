package de.ngloader.survival.crafting.recipe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import de.ngloader.core.util.ItemFactory;
import de.ngloader.survival.crafting.IRecipe;
import de.ngloader.survival.util.NamespacedKeys;

public class WoolRecipe extends ShapelessRecipe implements IRecipe {

	public static final ItemStack STRING_ITEMSTACK = new ItemFactory(Material.WHITE_WOOL).build();

	public WoolRecipe() {
		super(NamespacedKeys.RECIPE_WOOL, STRING_ITEMSTACK);

		this.addIngredient(4, Material.STRING);
		this.setGroup("wool");
	}

	@Override
	public NamespacedKey getNamespaceKey() {
		return this.getKey();
	}
}