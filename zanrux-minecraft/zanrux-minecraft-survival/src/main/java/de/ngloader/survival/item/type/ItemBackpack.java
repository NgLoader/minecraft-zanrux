package de.ngloader.survival.item.type;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import de.ngloader.core.util.ItemFactory;
import de.ngloader.core.util.NBTUtil;
import de.ngloader.survival.Survival;
import de.ngloader.survival.item.Item;
import de.ngloader.survival.util.NamespacedKeys;

public class ItemBackpack extends Item implements Listener {

	public static final ItemStack ITEM_BACKPACK_SMALL = new ItemFactory(Material.CHEST)
			.setDisplayName("§bSmall Backpack").addFlag(ItemFlag.HIDE_PLACED_ON)
			.setCustomMeta(NamespacedKeys.ITEM_BACKPACK_SIZE, PersistentDataType.INTEGER, 1).setCustomModelData(1)
			.build();

	public static final ItemStack ITEM_BACKPACK_MIDDLE = new ItemFactory(Material.CHEST)
			.setDisplayName("§bMiddle Backpack").addFlag(ItemFlag.HIDE_PLACED_ON)
			.setCustomMeta(NamespacedKeys.ITEM_BACKPACK_SIZE, PersistentDataType.INTEGER, 2).setCustomModelData(2)
			.build();

	public static final ItemStack ITEM_BACKPACK_BIG = new ItemFactory(Material.CHEST).setDisplayName("§bBig Backpack")
			.addFlag(ItemFlag.HIDE_PLACED_ON)
			.setCustomMeta(NamespacedKeys.ITEM_BACKPACK_SIZE, PersistentDataType.INTEGER, 3).setCustomModelData(3)
			.build();

	private final Map<Inventory, ItemStack> openInventorys = new WeakHashMap<>();

	public ItemBackpack(Survival core) {
		super(core, ITEM_BACKPACK_SMALL);
	}

	@EventHandler
	public void onInventory(InventoryCloseEvent event) {
		if (!(event.getPlayer() instanceof Player)) {
			return;
		}

		Inventory inventory = event.getInventory();
		ItemStack item = this.openInventorys.remove(inventory);
		if (item != null && item.hasItemMeta()) {
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.getPersistentDataContainer().set(NamespacedKeys.ITEM_BACKPACK_SAVE_ID,
					PersistentDataType.BYTE_ARRAY, NBTUtil.itemstackToBinary(inventory.getContents()));
			item.setItemMeta(itemMeta);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}

		ItemStack item = this.openInventorys.get(event.getWhoClicked().getOpenInventory().getTopInventory());
		ItemStack clickedItem = event.getCurrentItem();
		if (item != null && clickedItem != null && clickedItem.hasItemMeta() && clickedItem.getItemMeta()
				.getPersistentDataContainer().has(NamespacedKeys.ITEM_BACKPACK_SIZE, PersistentDataType.INTEGER)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		ItemStack item = event.getItemDrop().getItemStack();
		if (item != null && item.getType() == Material.CHEST && item.hasItemMeta()) {
			PersistentDataContainer dataContainer = item.getItemMeta().getPersistentDataContainer();
			if (dataContainer.has(NamespacedKeys.ITEM_BACKPACK_SAVE_ID, PersistentDataType.BYTE_ARRAY)) {
				InventoryView inventoryView = event.getPlayer().getOpenInventory();
				ItemStack backpackItem = this.openInventorys.get(inventoryView.getTopInventory());
				if (backpackItem != null) {
					inventoryView.close();
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		if (item != null && item.getType() == Material.CHEST && item.hasItemMeta()) {
			PersistentDataContainer dataContainer = item.getItemMeta().getPersistentDataContainer();
			if (dataContainer.has(NamespacedKeys.ITEM_BACKPACK_SIZE, PersistentDataType.INTEGER)) {
				Player player = event.getPlayer();
				Inventory inventory = Bukkit.createInventory(player,
						9 * dataContainer.get(NamespacedKeys.ITEM_BACKPACK_SIZE, PersistentDataType.INTEGER),
						item.getItemMeta().getDisplayName());

				if (dataContainer.has(NamespacedKeys.ITEM_BACKPACK_SAVE_ID, PersistentDataType.BYTE_ARRAY)) {
					byte[] data = dataContainer.get(NamespacedKeys.ITEM_BACKPACK_SAVE_ID,
							PersistentDataType.BYTE_ARRAY);
					List<ItemStack> items = NBTUtil.binaryToItemStack(data);
					inventory.setContents(items.toArray(new ItemStack[items.size()]));
				}

				this.openInventorys.put(inventory, item);
				player.openInventory(inventory);

				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		ItemStack item = event.getItemInHand();
		if (item != null && item.hasItemMeta() && item.getType() == Material.CHEST
				&& item.getItemMeta().getCustomModelData() > 0) {
			event.setCancelled(true);
		}
	}
}
