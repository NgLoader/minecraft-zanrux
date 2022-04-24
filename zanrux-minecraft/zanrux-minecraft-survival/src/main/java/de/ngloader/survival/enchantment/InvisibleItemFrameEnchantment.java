package de.ngloader.survival.enchantment;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import de.ngloader.core.CustomEnchantment;

public class InvisibleItemFrameEnchantment extends CustomEnchantment {

	public InvisibleItemFrameEnchantment() {
		super(NamespacedKey.minecraft("enchantment.itemframe.invisible"), "Invisible Item Frame", false);
	}

	@Override
	public boolean canEnchantItem(ItemStack item) {
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.TOOL;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}
}