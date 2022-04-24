package de.ngloader.survival.item.type;

import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.ngloader.survival.Survival;
import de.ngloader.survival.crafting.recipe.MobileCraftingTableRecipe;
import de.ngloader.survival.item.Item;

public class ItemMobileCraftingTable extends Item implements Listener {

	public ItemMobileCraftingTable(Survival core) {
		super(core, MobileCraftingTableRecipe.MOBILE_CRATING_TABLE_ITEMSTACK);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		if (item != null && item.hasItemMeta() && item.getType() == Material.CRAFTING_TABLE && item.getItemMeta().getCustomModelData() == 1) {
			event.setCancelled(true);
			event.setUseInteractedBlock(Result.DENY);
			event.setUseItemInHand(Result.DENY);
			event.getPlayer().openWorkbench(event.getPlayer().getLocation(), true);
		}
	}
}