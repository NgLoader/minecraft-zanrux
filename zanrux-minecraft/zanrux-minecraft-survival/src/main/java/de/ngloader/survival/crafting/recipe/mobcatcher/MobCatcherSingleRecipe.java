package de.ngloader.survival.crafting.recipe.mobcatcher;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.persistence.PersistentDataType;

import de.ngloader.core.util.ItemFactory;
import de.ngloader.survival.crafting.IRecipe;
import de.ngloader.survival.item.type.ItemMobCatcher;
import de.ngloader.survival.util.NamespacedKeys;

public class MobCatcherSingleRecipe extends ShapedRecipe implements IRecipe {

	public MobCatcherSingleRecipe() {
		super(NamespacedKey.minecraft("recipe.mobcatcher.single"), new ItemFactory(ItemMobCatcher.ITEM_MOBCATCHER_EMPTY)
				.setCustomMeta(NamespacedKeys.ITEM_MOBCATCHER_USE, PersistentDataType.INTEGER, 1)
				.handle((factory) -> ItemMobCatcher.applyLoreEmpty(factory, 1))
				.build());

		this.shape("CDC", "DND", "CDC");
		this.setIngredient('C', new RecipeChoice.ExactChoice(ItemMobCatcher.ITEM_MOBCATCHER_SHELL));
		this.setIngredient('D', Material.DIAMOND);
		this.setIngredient('N', Material.NETHERITE_SCRAP);
	}

	@Override
	public NamespacedKey getNamespaceKey() {
		return this.getKey();
	}
}