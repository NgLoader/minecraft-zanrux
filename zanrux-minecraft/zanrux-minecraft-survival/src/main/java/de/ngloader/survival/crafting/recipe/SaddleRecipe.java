package de.ngloader.survival.crafting.recipe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import de.ngloader.core.util.ItemFactory;
import de.ngloader.survival.crafting.IRecipe;
import de.ngloader.survival.util.NamespacedKeys;

public class SaddleRecipe extends ShapedRecipe implements IRecipe {

	public static final ItemStack SADDLE_ITEMSTACK = new ItemFactory(Material.SADDLE).build();

	public SaddleRecipe() {
		super(NamespacedKeys.RECIPE_SADDLE, SADDLE_ITEMSTACK);

		this.shape("EEE", "LAL", "IAI");
		this.setIngredient('A', Material.AIR);
		this.setIngredient('E', Material.LEATHER);
		this.setIngredient('L', Material.LEAD);
		this.setIngredient('I', new RecipeChoice.ExactChoice(ClampRecipe.CLAMP_ITEMSTACK));
	}

	@Override
	public NamespacedKey getNamespaceKey() {
		return this.getKey();
	}
}