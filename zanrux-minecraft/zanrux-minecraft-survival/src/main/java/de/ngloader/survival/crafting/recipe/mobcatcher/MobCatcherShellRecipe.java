package de.ngloader.survival.crafting.recipe.mobcatcher;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

import de.ngloader.survival.crafting.IRecipe;
import de.ngloader.survival.item.type.ItemMobCatcher;

public class MobCatcherShellRecipe extends ShapedRecipe implements IRecipe {

	public MobCatcherShellRecipe() {
		super(NamespacedKey.minecraft("recipe.mobcatcher.shell"), ItemMobCatcher.ITEM_MOBCATCHER_SHELL);

		this.shape("LWL", "BCB", "LWL");
		this.setIngredient('L', Material.LEAD);
		this.setIngredient('W', Material.COBWEB);
		this.setIngredient('B', Material.IRON_BARS);
		this.setIngredient('C', Material.CHEST);
	}

	@Override
	public NamespacedKey getNamespaceKey() {
		return this.getKey();
	}
}