package de.ngloader.core.portal.action;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.ngloader.core.portal.PortalAction;

public class PortalActionTeleport extends PortalAction {

	private final Location location;

	public PortalActionTeleport(Location location) {
		this.location = location;
	}

	@Override
	public void execute(Player player) {
		player.teleport(location);
	}
}
