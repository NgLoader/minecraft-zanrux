package de.ngloader.survival.event.bukkit;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import de.ngloader.survival.Survival;
import de.ngloader.survival.handler.VanishHandler;
import de.ngloader.survival.handler.event.EventListener;

public class EntityDamageEventListener extends EventListener {

	private BlockData REDSTONE_BLOCK_DATA_REDSTONE_BLOCK = Bukkit.createBlockData(Material.REDSTONE_BLOCK);
	private BlockData REDSTONE_BLOCK_DATA_RED_WOOL = Bukkit.createBlockData(Material.RED_WOOL);

	private VanishHandler vanishHandler;

	public EntityDamageEventListener(Survival core) {
		super(core);
	}

	@Override
	public void onInit() {
		this.vanishHandler = this.getCore().getVanishHandler();
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			if (this.vanishHandler.isVanish((Player) event.getEntity())) {
				event.setCancelled(true);
			}
		} else if (event.getEntity() instanceof Cat cat
				&& cat.getOwner() != null
				&& cat.getOwner().getUniqueId().equals(UUID.fromString("6dafd062-79b3-4bf3-8d2d-1ad9e0124771"))) {
			World world = cat.getWorld();
			Location location = cat.getLocation().add(0, 0.2, 0);
			world.spawnParticle(Particle.BLOCK_CRACK, location, 5, 0.2, 0.2, 0.2, REDSTONE_BLOCK_DATA_REDSTONE_BLOCK);
			world.spawnParticle(Particle.BLOCK_CRACK, location, 5, 0.2, 0.2, 0.2, REDSTONE_BLOCK_DATA_RED_WOOL);
			event.setCancelled(true);

			if (event.getCause() == DamageCause.VOID) {
				cat.teleport(world.getHighestBlockAt(location).getLocation().add(0, 0.5, 0));
			}
		}
	}
}