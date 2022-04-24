package de.ngloader.survival.event.bukkit;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import de.ngloader.survival.Survival;
import de.ngloader.survival.handler.VanishHandler;
import de.ngloader.survival.handler.event.EventListener;

public class FoodLevelChangeEventListener extends EventListener {

	private VanishHandler vanishHandler;

	public FoodLevelChangeEventListener(Survival core) {
		super(core);
	}

	@Override
	public void onInit() {
		this.vanishHandler = this.getCore().getVanishHandler();
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			if (this.vanishHandler.isVanish((Player) event.getEntity())) {
				event.setCancelled(true);
			}
		}
	}
}