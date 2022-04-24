package de.ngloader.core.inventory;

import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import de.ngloader.core.MCCore;
import de.ngloader.core.inventory.inventory.CustomInventory;
import de.ngloader.core.inventory.inventory.CustomInventoryPlayer;
import de.ngloader.core.inventory.listener.InventoryFilter;
import de.ngloader.core.inventory.listener.ItemAction;
import de.ngloader.core.inventory.listener.InventoryListener.ListenerType;
import de.ngloader.synced.IHandler;

public class InventorySystem extends IHandler<MCCore> implements Listener {

	private final Map<Player, CustomInventoryPlayer> openInventorys = new WeakHashMap<>();

	public InventorySystem(MCCore plugin) {
		super(plugin);
	}

	@Override
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this.core);
	}

//	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		HumanEntity entity = event.getWhoClicked();
		if (!(entity instanceof Player)) {
			return;
		}

		Player player = (Player) entity;
		InventoryView inventoryView = player.getOpenInventory();
		Inventory topInventory = inventoryView.getTopInventory();

		CustomInventoryPlayer playerInventory = this.openInventorys.get(player);
		if (playerInventory == null || !Objects.equals(playerInventory.getInventory(), topInventory)) {
			this.closeInventory(player);
			return;
		}
		playerInventory.callListener(playerInventory, ListenerType.CLICKED);

		CustomInventory customInventory = playerInventory.getCustomInventory();
		switch (Objects.equals(event.getClickedInventory(), topInventory) ? customInventory.getClickableTop() : customInventory.getClickableBottom()) {
			case NOTHING:
				return;

			case FILTER:
				InventoryFilter filter = customInventory.getItemFilter();
				if (filter != null && !filter.isValied(event.getCurrentItem(), event.getSlot())) {
					event.setCancelled(true);
					playerInventory.callListener(playerInventory, ListenerType.ITEM_FILTER_FAILED);
					return;
				}
				playerInventory.callListener(playerInventory, ListenerType.ITEM_FILTER_SUCCESS);
				break;

			case INVENTORY:
				event.setCancelled(true);
				break;

			default:
				break;
		}

		ItemAction item = customInventory.getAction(event.getSlot());
		if (item == null) {
			return;
		}

		InventoryAction action = event.getAction();
		switch (item.onAction(playerInventory, customInventory, action, event)) {
			case IMMOVABLE:
				event.setCancelled(true);
				break;
	
			case REMOVEABLE:
				switch (action) {
					case PICKUP_ALL:
					case PICKUP_HALF:
					case PICKUP_ONE:
					case PICKUP_SOME:
						event.setCancelled(false);
						break;

					default:
						event.setCancelled(true);
					}
				break;

			case PLACEABLE:
				switch (action) {
				case PLACE_ALL:
				case PLACE_ONE:
				case PLACE_SOME:
					event.setCancelled(false);
					break;

				default:
					event.setCancelled(true);
				}
			break;

			default:
				event.setCancelled(true);
				break;
			}
	}

	//TODO FIX
//	@EventHandler
	public void onInventorySwap(InventoryDragEvent event) {
		HumanEntity entity = event.getWhoClicked();
		if (!(entity instanceof Player)) {
			return;
		}

		Player player = (Player) entity;
		InventoryView inventoryView = player.getOpenInventory();
		Inventory topInventory = inventoryView.getTopInventory();

		CustomInventoryPlayer playerInventory = this.openInventorys.get(player);
		if (playerInventory != null && Objects.equals(playerInventory.getInventory(), topInventory)) {
			event.setCancelled(true);
			return;
		} else {
			playerInventory = this.openInventorys.remove(player);
			if (playerInventory != null) {
				playerInventory.destroy();
			}
		}
	}

//	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		HumanEntity entity = event.getPlayer();
		if (!(entity instanceof Player)) {
			return;
		}

		Player player = (Player) entity;
		Inventory inventory = event.getInventory();
		CustomInventoryPlayer playerInventory = this.openInventorys.get(player);
		if (inventory == null || playerInventory == null || !inventory.equals(playerInventory.getInventory())) {
			return;
		}

		this.closeInventory(player);
	}

	public void openInventory(Player player, CustomInventory inventory) {
		CustomInventoryPlayer playerInventory = this.openInventorys.get(player);

		if (playerInventory == null) {
			playerInventory = new CustomInventoryPlayer(this, player, inventory);
			this.openInventorys.put(player, playerInventory);
		} else {
			playerInventory.open(inventory);
		}
	}

	public void closeInventory(Player player) {
		CustomInventoryPlayer inventory = this.openInventorys.remove(player);
		if (inventory != null) {
			player.closeInventory();
			inventory.destroy();
		}
	}

	public void closeAllInventorys() {
		this.openInventorys.keySet().forEach(this::closeInventory);
	}

	public boolean hasInventory(Player player) {
		return this.openInventorys.containsKey(player);
	}
}