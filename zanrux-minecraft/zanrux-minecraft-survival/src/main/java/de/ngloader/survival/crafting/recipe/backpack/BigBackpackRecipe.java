package de.ngloader.survival.crafting.recipe.backpack;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

import de.ngloader.survival.crafting.IRecipe;
import de.ngloader.survival.item.type.ItemBackpack;
import de.ngloader.survival.util.NamespacedKeys;

public class BigBackpackRecipe extends ShapedRecipe implements IRecipe {

	public BigBackpackRecipe() {
		super(NamespacedKey.minecraft(NamespacedKeys.ITEM_BACKPACK_SIZE.getKey() + ".2"), ItemBackpack.ITEM_BACKPACK_BIG);

		this.shape("LCL", "ECE", "DCD");
		this.setIngredient('L', Material.LEATHER);
		this.setIngredient('E', Material.LEAD);
		this.setIngredient('C', Material.CHEST);
		this.setIngredient('D', Material.DIAMOND);
	}

	@Override
	public NamespacedKey getNamespaceKey() {
		return this.getKey();
	}
}
