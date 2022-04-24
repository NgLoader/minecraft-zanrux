package de.ngloader.survival.crafting.recipe;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import de.ngloader.core.util.ItemFactory;
import de.ngloader.survival.crafting.IRecipe;

public class TerracottaRecipe extends ShapedRecipe implements IRecipe {

	private static final List<Material> TERRACOTTA_COLOR_LIST = new ArrayList<>();

	static {
		for (Material material : Material.values()) {
			if (material.name().endsWith("_TERRACOTTA")) {
				TERRACOTTA_COLOR_LIST.add(material);
			}
		}
	}

	public TerracottaRecipe(Material result, Material dyeColor) {
		super(NamespacedKey.minecraft("recipe.terracotta.recolor." + dyeColor.name().toLowerCase()), new ItemFactory(result).setNumber(8).build());

		this.shape("CCC", "CDC", "CCC");
		this.setIngredient('C', new RecipeChoice.MaterialChoice(TERRACOTTA_COLOR_LIST));
		this.setIngredient('D', dyeColor);
		this.setGroup("terracotta");
	}

	@Override
	public NamespacedKey getNamespaceKey() {
		return this.getKey();
	}
}