package de.ngloader.survival.crafting.recipe;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import de.ngloader.core.util.ItemFactory;
import de.ngloader.survival.crafting.IRecipe;
import de.ngloader.survival.util.NamespacedKeys;

public class SnowBlockRecipe extends ShapedRecipe implements IRecipe {

	public static final ItemStack ITEMSTACK = new ItemFactory(Material.SNOW_BLOCK).build();

	public SnowBlockRecipe() {
		super(NamespacedKeys.RECIPE_SNOW_BLOCK, ITEMSTACK);

		this.shape("SS", "SS");
		this.setIngredient('S', new RecipeChoice.ExactChoice(new ItemStack(Material.SNOWBALL)));

		Bukkit.getRecipesFor(ITEMSTACK).forEach(recipe -> Bukkit.removeRecipe(((ShapedRecipe) recipe).getKey()));
	}

	@Override
	public NamespacedKey getNamespaceKey() {
		return this.getKey();
	}
}