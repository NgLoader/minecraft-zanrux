package de.ngloader.survival.crafting.recipe.backpack;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

import de.ngloader.survival.crafting.IRecipe;
import de.ngloader.survival.item.type.ItemBackpack;
import de.ngloader.survival.util.NamespacedKeys;

public class MiddleBackpackRecipe extends ShapedRecipe implements IRecipe {

	public MiddleBackpackRecipe() {
		super(NamespacedKey.minecraft(NamespacedKeys.ITEM_BACKPACK_SIZE.getKey() + ".1"), ItemBackpack.ITEM_BACKPACK_MIDDLE);

		this.shape("LSL", "ECE", "ICI");
		this.setIngredient('L', Material.LEATHER);
		this.setIngredient('S', Material.STICK);
		this.setIngredient('E', Material.LEAD);
		this.setIngredient('C', Material.CHEST);
		this.setIngredient('I', Material.IRON_INGOT);
	}

//	@Override
//	public void onCraft(HumanEntity entity, CraftItemEvent event) {
//		for (ItemStack item : event.getInventory().getMatrix()) {
//			if (item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer()
//					.has(NamespacedKeys.ITEM_BACKPACK_SIZE, PersistentDataType.INTEGER)) {
//				byte[] data = item.getItemMeta().getPersistentDataContainer().get(NamespacedKeys.ITEM_BACKPACK_SAVE_ID, PersistentDataType.BYTE_ARRAY);
//				if (data != null) {
//					ItemStack newItem = event.getCurrentItem();
//					if (newItem != null && newItem.hasItemMeta()) {
//						ItemMeta meta = newItem.getItemMeta();
//						meta.getPersistentDataContainer().set(NamespacedKeys.ITEM_BACKPACK_SAVE_ID,
//								PersistentDataType.BYTE_ARRAY, data);
//						newItem.setItemMeta(meta);
//					}
//				}
//				break;
//			}
//		}
//	}

	@Override
	public NamespacedKey getNamespaceKey() {
		return this.getKey();
	}
}
