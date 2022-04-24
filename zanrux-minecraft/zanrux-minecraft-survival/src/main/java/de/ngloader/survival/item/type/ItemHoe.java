package de.ngloader.survival.item.type;

import java.util.EnumSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import de.ngloader.survival.Survival;
import de.ngloader.survival.item.Item;

public class ItemHoe extends Item implements Listener {

	private final EnumSet<Material> hoeTypes = EnumSet.noneOf(Material.class);
	private final EnumSet<Material> grassTypes = EnumSet.of(Material.GRASS, Material.TALL_GRASS);

	private final int range = 4;

	public ItemHoe(Survival core) {
		super(core, new ItemStack(Material.WOODEN_HOE));

		for (Material material : Material.values()) {
			if (material.name().endsWith("_HOE")) {
				hoeTypes.add(material);
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if (this.hoeTypes.contains(item.getType()) && this.grassTypes.contains(block.getType())) {
			Location location = block.getLocation();
			while (this.grassTypes.contains(location.clone().subtract(0, 1, 0).getBlock().getType())) {
				location.subtract(0, 1, 0);
			}

			for (int x = -this.range + 1; x < this.range; x++) {
				for (int z = -this.range + 1; z < this.range; z++) {
					Block type = location.clone().add(x, 0, z).getBlock();
					if (this.grassTypes.contains(type.getType())) {
						type.getWorld().spawnParticle(Particle.BLOCK_CRACK, type.getLocation().add(.5, .5, .5), 15, 0.5, 0.5, 0.5, type.getBlockData());
						type.breakNaturally(item);
					}
				}
			}
		}
	}
}