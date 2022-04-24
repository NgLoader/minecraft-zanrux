package de.ngloader.survival.crafting.recipe;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import de.ngloader.core.util.ItemFactory;
import de.ngloader.survival.crafting.IRecipe;

public class ConcreteRecipe extends ShapedRecipe implements IRecipe {

	private static final List<Material> CONCRETE_COLOR_LIST = new ArrayList<>();

	static {
		for (Material material : Material.values()) {
			if (material.name().endsWith("_CONCRETE")) {
				CONCRETE_COLOR_LIST.add(material);
			}
		}
	}

	public ConcreteRecipe(Material result, Material dyeColor) {
		super(NamespacedKey.minecraft("recipe.concrete.recolor." + dyeColor.name().toLowerCase()), new ItemFactory(result).setNumber(8).build());

		this.shape("CCC", "CDC", "CCC");
		this.setIngredient('C', new RecipeChoice.MaterialChoice(CONCRETE_COLOR_LIST));
		this.setIngredient('D', dyeColor);
		this.setGroup("concrete");
	}

	@Override
	public NamespacedKey getNamespaceKey() {
		return this.getKey();
	}
}