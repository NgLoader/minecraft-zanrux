package de.ngloader.survival.enchantment;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import de.ngloader.core.CustomEnchantment;

public class TelekinesisEnchantment extends CustomEnchantment implements Listener {

	public TelekinesisEnchantment() {
		super(NamespacedKey.minecraft("enchantment.telekinesis"), "Telekinesis", true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if (item != null && item.hasItemMeta() && item.getItemMeta().hasEnchant(EnchantmentList.TELEKINESIS)) {
			Player player = event.getPlayer();
			player.giveExp(event.getExpToDrop());
			event.setExpToDrop(0);
		}
	}

	@EventHandler
	public void onBlockDropItemEvent(BlockDropItemEvent event) {
		PlayerInventory inventory = event.getPlayer().getInventory();
		ItemStack item = inventory.getItemInMainHand();
		if (item != null && item.hasItemMeta() && item.getItemMeta().hasEnchant(EnchantmentList.TELEKINESIS)) {
			List<Item> items = event.getItems();

			Map<Integer, ItemStack> unstored = inventory.addItem(items.stream().map(Item::getItemStack).toArray(ItemStack[]::new));

			Iterator<Item> itemIterator = items.iterator();
			for (int i = 0; itemIterator.hasNext(); i++) {
				ItemStack unstoredItem = unstored.get(i);
				Item drop = itemIterator.next();
				if (unstoredItem != null) {
					drop.setItemStack(unstoredItem);
				} else {
					itemIterator.remove();
				}
			}
		}
	}

	@Override
	public boolean canEnchantItem(ItemStack item) {
		return this.getItemTarget().includes(item);
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.TOOL;
	}

	@Override
	public int getMaxLevel() {
		return 0;
	}
}