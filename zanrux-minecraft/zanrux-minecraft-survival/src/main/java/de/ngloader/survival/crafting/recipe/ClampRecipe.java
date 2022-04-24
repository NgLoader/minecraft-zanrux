package de.ngloader.survival.crafting.recipe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import de.ngloader.core.util.ItemFactory;
import de.ngloader.survival.crafting.IRecipe;
import de.ngloader.survival.util.NamespacedKeys;

public class ClampRecipe extends ShapedRecipe implements IRecipe {

	public static final ItemStack CLAMP_ITEMSTACK = new ItemFactory(Material.IRON_INGOT)
			.setDisplayName("Clamp")
			.setCustomModelData(1)
			.build();

	public ClampRecipe() {
		super(NamespacedKeys.RECIPE_CLAMP, CLAMP_ITEMSTACK);

		this.shape("AIA", "IAI", "III");
		this.setIngredient('A', Material.AIR);
		this.setIngredient('I', Material.IRON_INGOT);
	}

	@Override
	public NamespacedKey getNamespaceKey() {
		return this.getKey();
	}
}