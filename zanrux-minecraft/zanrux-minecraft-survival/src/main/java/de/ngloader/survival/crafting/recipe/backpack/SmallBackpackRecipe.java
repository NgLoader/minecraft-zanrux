package de.ngloader.survival.crafting.recipe.backpack;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

import de.ngloader.survival.crafting.IRecipe;
import de.ngloader.survival.item.type.ItemBackpack;
import de.ngloader.survival.util.NamespacedKeys;

public class SmallBackpackRecipe extends ShapedRecipe implements IRecipe {

	public SmallBackpackRecipe() {
		super(NamespacedKey.minecraft(NamespacedKeys.ITEM_BACKPACK_SIZE.getKey() + ".0"), ItemBackpack.ITEM_BACKPACK_SMALL);

		this.shape("LSL", "ECE", "LLL");
		this.setIngredient('L', Material.LEATHER);
		this.setIngredient('S', Material.STICK);
		this.setIngredient('E', Material.LEAD);
		this.setIngredient('C', Material.CHEST);
	}

	@Override
	public NamespacedKey getNamespaceKey() {
		return this.getKey();
	}
}
