package de.ngloader.core.portal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.ngloader.core.MCCore;
import de.ngloader.synced.IHandler;

public class PortalHandler extends IHandler<MCCore> implements Listener {

	protected final List<Portal> portals = new ArrayList<>();

	public PortalHandler(MCCore core) {
		super(core);

	}

	@Override
	public void onEnable() {
		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this.core, "BungeeCord");

		Bukkit.getServer().getPluginManager().registerEvents(this, this.core);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location location = player.getLocation();

		for (Portal portal : this.portals) {
			if (portal.canUsePortal(player) && portal.isInPortal(location)) {
				portal.getAction().execute(player);
				break;
			}
		}
	}

	public List<Portal> getPortals() {
		return this.portals;
	}
}