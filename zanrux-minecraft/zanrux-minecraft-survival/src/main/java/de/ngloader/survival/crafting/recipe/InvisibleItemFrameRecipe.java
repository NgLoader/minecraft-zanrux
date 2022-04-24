package de.ngloader.survival.crafting.recipe;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.metadata.FixedMetadataValue;

import de.ngloader.core.util.ItemFactory;
import de.ngloader.survival.Survival;
import de.ngloader.survival.crafting.IRecipe;
import de.ngloader.survival.enchantment.EnchantmentList;
import de.ngloader.survival.util.NamespacedKeys;

public class InvisibleItemFrameRecipe extends ShapedRecipe implements IRecipe, Listener {

	public static final ItemStack ITEMSTACK = new ItemFactory(Material.ITEM_FRAME)
			.addSingleEnchantment(EnchantmentList.INVISIBLE_ITEM_FRAME, 1)
			.setDisplayName("Invisible Item Frame")
			.addAllFlag()
			.build();

	public static FixedMetadataValue metadataValue;

	public InvisibleItemFrameRecipe(Survival plugin) {
		super(NamespacedKeys.RECIPE_INVISIBLE_ITEM_FRAME, ITEMSTACK);
		metadataValue = new FixedMetadataValue(plugin, true);

		this.shape("GGG", "GIG", "GGG");
		this.setIngredient('G', new RecipeChoice.MaterialChoice(Stream.of(Material.values())
				.filter(material -> 
				material.name().endsWith("GLASS_PANE"))
				.collect(Collectors.toList())));
		this.setIngredient('I', Material.ITEM_FRAME);
	}

	@EventHandler(ignoreCancelled = true)
	public void onHangingPlace(HangingPlaceEvent event) {
		Player player = event.getPlayer();
		PlayerInventory inventory = player.getInventory();

		ItemStack item = null;
		if (inventory.getItemInMainHand().getType() == Material.ITEM_FRAME) {
			item = inventory.getItemInMainHand();
		} else if (inventory.getItemInOffHand().getType() == Material.ITEM_FRAME) {
			item = inventory.getItemInOffHand();
		}

		if (item instanceof ItemFrame itemFrame && EnchantmentList.INVISIBLE_ITEM_FRAME.hasEnchant(item)) {
			itemFrame.setVisible(false);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onHangingBreak(HangingBreakEvent event) {
		if (event.getEntity() instanceof ItemFrame itemFrame && !itemFrame.isVisible()) {
			event.setCancelled(true);

			ItemStack content = itemFrame.getItem();
			Location dropLocation = itemFrame.getLocation().subtract(itemFrame.getAttachedFace().getDirection().multiply(0.1));
			if (content != null && content.getType() != Material.AIR) {
				itemFrame.getWorld().dropItem(dropLocation, content);
			}

			itemFrame.remove();
			itemFrame.getWorld().dropItem(dropLocation, InvisibleItemFrameRecipe.ITEMSTACK);
		}
	}

	@Override
	public NamespacedKey getNamespaceKey() {
		return this.getKey();
	}
}