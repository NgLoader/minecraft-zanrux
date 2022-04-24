package de.ngloader.survival.crafting.recipe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import de.ngloader.core.util.ItemFactory;
import de.ngloader.survival.crafting.IRecipe;
import de.ngloader.survival.util.NamespacedKeys;

public class MobileCraftingTableRecipe extends ShapedRecipe implements IRecipe {

	public static final ItemStack MOBILE_CRATING_TABLE_ITEMSTACK = new ItemFactory(Material.CRAFTING_TABLE)
			.setDisplayName("Mobile Crafting Table")
			.addCanPlaceOn(Material.AIR)
			.addFlag(ItemFlag.HIDE_PLACED_ON)
			.setCustomModelData(1)
			.build();

	public MobileCraftingTableRecipe() {
		super(NamespacedKeys.RECIPE_MOBILE_CRATING_TABLE, MOBILE_CRATING_TABLE_ITEMSTACK);

		this.shape("SWS", "WCW", "SWS");
		this.setIngredient('S', new RecipeChoice.MaterialChoice(Material.OAK_LOG));
		this.setIngredient('W', new RecipeChoice.MaterialChoice(Material.PHANTOM_MEMBRANE));
		this.setIngredient('C', new RecipeChoice.ExactChoice(new ItemStack(Material.CRAFTING_TABLE)));
	}

	@Override
	public NamespacedKey getNamespaceKey() {
		return this.getKey();
	}
}