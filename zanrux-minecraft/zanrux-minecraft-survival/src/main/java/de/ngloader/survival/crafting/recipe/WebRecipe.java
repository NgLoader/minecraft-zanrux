package de.ngloader.survival.crafting.recipe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import de.ngloader.core.util.ItemFactory;
import de.ngloader.survival.crafting.IRecipe;
import de.ngloader.survival.util.NamespacedKeys;

public class WebRecipe extends ShapedRecipe implements IRecipe {

	public static final ItemStack WEB_ITEMSTACK = new ItemFactory(Material.COBWEB).build();

	public WebRecipe() {
		super(NamespacedKeys.RECIPE_WEB, WEB_ITEMSTACK);

		this.shape("SSS", "SSS", "SSS");
		this.setIngredient('S', Material.STRING);
	}

	@Override
	public NamespacedKey getNamespaceKey() {
		return this.getKey();
	}
}