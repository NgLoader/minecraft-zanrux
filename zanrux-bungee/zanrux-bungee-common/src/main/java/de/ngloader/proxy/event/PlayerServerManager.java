package de.ngloader.proxy.event;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import de.ngloader.proxy.Proxy;
import de.ngloader.synced.database.Database;
import de.ngloader.synced.database.controller.PlayerController;
import de.ngloader.synced.database.model.PlayerModel;
import de.ngloader.synced.database.model.PlayerSessionModel;
import de.ngloader.synced.database.model.PlayerSessionModel.PlayerSessionAction;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerServerManager implements Listener {

	private final Database database;
	private final Map<ProxiedPlayer, Long> playtime = new HashMap<>();

	public PlayerServerManager(Proxy proxy) {
		this.database = proxy.getDatabase();

		ProxyServer.getInstance().getPluginManager().registerListener(proxy, this);
	}

	@EventHandler
	public void onServerSwitch(ServerSwitchEvent event) {
		ProxiedPlayer player = event.getPlayer();
		String serverName = player.getServer().getInfo().getName();

		player.sendMessage(new TextComponent(String.format("%s§7Du §7betrittst §7nun §7denn §7Server §a%s§8.",
				Proxy.PREFIX, serverName.substring(0, 1).toUpperCase() + serverName.substring(1).toLowerCase())));

		if (event.getFrom() != null) {
			this.database.transaction().thenAccept(session -> {
				PlayerModel playerModel = PlayerController.getPlayer(session, player.getUniqueId());
				if (playerModel != null) {
					session.save(new PlayerSessionModel(playerModel, Timestamp.from(Instant.now()),
							PlayerSessionAction.SWITCH, serverName));
				}
			});
		} else {
			this.playtime.put(player, System.currentTimeMillis());
			this.database.transaction().thenAccept(session -> {
				PlayerModel playerModel = PlayerController.getPlayer(session, player.getUniqueId(), player.getName(),
						serverName);
				session.save(new PlayerSessionModel(playerModel, Timestamp.from(Instant.now()),
						PlayerSessionAction.JOIN, serverName));
			});
		}
	}

	@EventHandler
	public void onServerQuit(PlayerDisconnectEvent event) {
		ProxiedPlayer player = event.getPlayer();
		Long time = this.playtime.remove(player);

		this.database.transaction().thenAccept(session -> {
			PlayerModel playerModel = PlayerController.getPlayer(session, player.getUniqueId());
			if (playerModel != null) {
				if (time != null) {
					playerModel.addPlaytime(System.currentTimeMillis() - time);
					session.update(playerModel);
				}

				session.save(new PlayerSessionModel(playerModel, Timestamp.from(Instant.now()),
						PlayerSessionAction.QUIT, player.getServer().getInfo().getName()));
			}
		});
	}
}