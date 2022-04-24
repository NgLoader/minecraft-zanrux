package de.ngloader.survival.enchantment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

import de.ngloader.core.CustomEnchantment;
import de.ngloader.survival.Survival;
import de.ngloader.synced.util.ReflectionUtil;

public class EnchantmentList implements Listener {

	private static final Set<CustomEnchantment> ENCHANTMENTS = new HashSet<>();

	public static final CustomEnchantment TREE_FELLER = new TreeFellerEnchantment();
	public static final CustomEnchantment INVISIBLE_ITEM_FRAME = new InvisibleItemFrameEnchantment();
	public static final CustomEnchantment TELEKINESIS = new TelekinesisEnchantment();

	public static void init(Survival plugin) {
		try {
			if (!Enchantment.isAcceptingRegistrations()) {
				Field field = ReflectionUtil.getField(Enchantment.class, "acceptingNew");
				try {
					field.set(null, true);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
					return;
				}
			}

			registerEnchantment(TREE_FELLER);
			registerEnchantment(INVISIBLE_ITEM_FRAME);
			registerEnchantment(TELEKINESIS);

			Enchantment.stopAcceptingRegistrations();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Bukkit.getPluginManager().registerEvents(new EnchantmentList(), plugin);
	}

	public static Set<CustomEnchantment> getEnchantments() {
		return Collections.unmodifiableSet(ENCHANTMENTS);
	}

	private static void registerEnchantment(CustomEnchantment enchantment) {
		if (enchantment instanceof Listener) {
			Bukkit.getPluginManager().registerEvents((Listener) enchantment, Bukkit.getPluginManager().getPlugin("Survival"));
		}

		ENCHANTMENTS.add(enchantment);

		if (!Arrays.stream(Enchantment.values()).anyMatch(enchant -> enchant.getKey().getKey().equals(enchantment.getKey().getKey()))) {
			try {
				Enchantment.registerEnchantment(enchantment);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@EventHandler
	public void onPrepareAnvilEvent(PrepareAnvilEvent event) {
		ItemStack[] contents = event.getInventory().getContents();
		ItemStack input1 = contents[0];
		ItemStack input2 = contents[1];

		if (input1 != null && input1.getType() != Material.AIR && input2 != null && input2.getType() != Material.AIR) {
			ItemStack result = event.getResult();

			if (result == null || result.getType() == Material.AIR) {
				if (input2.hasItemMeta()) {
					result = input1.clone();
					boolean apply = false;
					for (CustomEnchantment custom : getEnchantments()) {
						if (custom.hasEnchant(input1) && custom.canEnchantItem(result)) {
							custom.applyOn(result, custom.getEnchantLevel(input1));
							apply = true;
						} else if (custom.hasEnchant(input2) && custom.canEnchantItem(result)) {
							custom.applyOn(result, custom.getEnchantLevel(input2));
							apply = true;
						}
					}
					if (apply) {
						event.setResult(result);
					}
				}
			} else {
				for (CustomEnchantment custom : getEnchantments()) {
					if (custom.hasEnchant(input1) && custom.canEnchantItem(result)) {
						custom.applyOn(result, custom.getEnchantLevel(input1));
					} else if (custom.hasEnchant(input2) && custom.canEnchantItem(result)) {
						custom.applyOn(result, custom.getEnchantLevel(input2));
					}
				}
				event.setResult(result);
			}
		}
	}
}