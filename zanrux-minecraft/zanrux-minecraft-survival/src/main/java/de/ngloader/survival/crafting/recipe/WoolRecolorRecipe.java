package de.ngloader.survival.crafting.recipe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;

import de.ngloader.core.util.ItemFactory;
import de.ngloader.survival.crafting.IRecipe;

public class WoolRecolorRecipe extends ShapelessRecipe implements IRecipe {

	public WoolRecolorRecipe(Material dyeColor, Material woolColor) {
		super(NamespacedKey.minecraft("recipe.wool." + woolColor.name().toLowerCase()), new ItemFactory(woolColor).build());

		this.addIngredient(dyeColor);
		this.addIngredient(new RecipeChoice.MaterialChoice(Tag.WOOL));
		this.setGroup("wool");
	}

	@Override
	public NamespacedKey getNamespaceKey() {
		return this.getKey();
	}
}