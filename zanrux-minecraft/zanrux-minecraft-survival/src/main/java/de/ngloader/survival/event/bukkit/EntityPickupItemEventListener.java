package de.ngloader.survival.event.bukkit;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;

import de.ngloader.survival.Survival;
import de.ngloader.survival.handler.VanishHandler;
import de.ngloader.survival.handler.event.EventListener;

public class EntityPickupItemEventListener extends EventListener {

	private VanishHandler vanishHandler;

	public EntityPickupItemEventListener(Survival core) {
		super(core);
	}

	@Override
	public void onInit() {
		this.vanishHandler = this.getCore().getVanishHandler();
	}

	@EventHandler
	public void onEntityPickupItemEvent(EntityPickupItemEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			Player player = (Player) event.getEntity();

			if (this.vanishHandler.isVanish(player)) {
				event.setCancelled(true);
			}
		}
	}
}