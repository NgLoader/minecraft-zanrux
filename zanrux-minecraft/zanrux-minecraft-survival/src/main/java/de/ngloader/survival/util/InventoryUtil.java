package de.ngloader.survival.util;

import java.util.Arrays;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {

	public static final int DEFAULT_MAX_STACK_SIZE = 64;

	public static InventoryAddResult giveItem(Player player, ItemStack[] item, boolean dropWhenFull) {
		return InventoryUtil.giveItem(player.getInventory(), item, dropWhenFull, player.getEyeLocation());
	}

	public static InventoryAddResult giveItem(Player player, ItemStack[] item, boolean dropWhenFull, Location dropLocation) {
		return InventoryUtil.giveItem(player.getInventory(), item, dropWhenFull, dropLocation);
	}

	public static InventoryAddResult giveItem(Inventory inventory, ItemStack[] item, boolean dropWhenFull, Location dropLocation) {
//		int amount = item.getAmount();
//		boolean addedToInventory = false;

//		while (amount != 0) {
			Map<Integer, ItemStack> unstored = inventory.addItem(item);
			if (unstored.isEmpty()) {
				return InventoryAddResult.ADDED;
			} else if (dropWhenFull) {
				int totalCount = Arrays.stream(item).mapToInt(ItemStack::getAmount).sum();
				int itemCount = 0;

				World world = dropLocation.getWorld();
				for (ItemStack unstoredItem : unstored.values()) {
					world.dropItemNaturally(dropLocation, unstoredItem);
					itemCount += unstoredItem.getAmount();
				}
				return itemCount != totalCount ? InventoryAddResult.ADDED_AND_DROPPED : InventoryAddResult.DROPPED;
			}
//			int slot = InventoryUtil.getItemOrFreeSlot(inventory, item, true);
//			if (slot != -1) {
//				ItemStack inventoryItem = inventory.getItem(slot);
//				int inventoryItemAmount = inventoryItem != null ? inventoryItem.getAmount() : 0;
//				int inventoryItemMaxStackSize = inventoryItem != null ?
//						inventoryItem.getMaxStackSize() != -1 ? inventoryItem.getMaxStackSize()
//								: item.getMaxStackSize() != -1 ? item.getMaxStackSize() : DEFAULT_MAX_STACK_SIZE
//						: item.getMaxStackSize() != -1 ? item.getMaxStackSize() : DEFAULT_MAX_STACK_SIZE;
//
//				addedToInventory = true;
//
//				int amountNew = 0;
//				if (inventoryItemAmount + amount >= inventoryItemMaxStackSize) {
//					amountNew = inventoryItemMaxStackSize;
//					amount = inventoryItemAmount + amount - inventoryItemMaxStackSize;
//				} else {
//					amountNew = inventoryItemAmount + amount;
//					amount = 0;
//				}
//
//				if (inventoryItem != null) {
//					inventoryItem.setAmount(amountNew);
//					continue;
//				}
//
//				ItemStack copyItem = item.clone();
//				copyItem.setAmount(amountNew);
//				inventory.setItem(slot, copyItem);
//			} else if (dropWhenFull) {
//				item.setAmount(amount);
//				dropLocation.getWorld().dropItemNaturally(dropLocation, item);
//				return addedToInventory ? InventoryAddResult.ADDED_AND_DROPPED : InventoryAddResult.DROPPED;
//			} else {
//				break;
//			}
//		}
		return /* addedToInventory ? InventoryAddResult.ADDED : */ InventoryAddResult.VOIDED;
	}

	public static int getItemOrFreeSlot(Inventory inventory, ItemStack item, boolean canStack) {
		ItemStack[] content = inventory.getStorageContents();
		int freeSlot = -1;
		for (int slot = 0; slot < content.length; slot++) {
			ItemStack contentItem = content[slot];
			if (contentItem != null
					&& contentItem.isSimilar(item)
					&& (!canStack || contentItem.getAmount() != contentItem.getMaxStackSize())) {
				return slot;
			} else if (contentItem == null || contentItem.getType() == Material.AIR) {
				freeSlot = slot;
			}
		}
		return freeSlot;
	}

	public enum InventoryAddResult {
		ADDED,
		ADDED_AND_DROPPED,
		DROPPED,
		VOIDED
	}
}