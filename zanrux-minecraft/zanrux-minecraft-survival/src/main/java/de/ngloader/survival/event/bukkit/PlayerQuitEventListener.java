package de.ngloader.survival.event.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import de.ngloader.core.handler.ChatHandler;
import de.ngloader.survival.Survival;
import de.ngloader.survival.handler.VanishHandler;
import de.ngloader.survival.handler.event.EventListener;

public class PlayerQuitEventListener extends EventListener {

//	private HomeHandler homeHandler;
//	private ScoreboardHandler scoreboardHandler;
	private ChatHandler chatHandler;
	private VanishHandler vanishHandler;

	public PlayerQuitEventListener(Survival core) {
		super(core);
	}

	@Override
	public void onInit() {
//		this.homeHandler = this.getCore().getHomeHandler();
//		this.scoreboardHandler = this.getCore().getScoreboardHandler();
		this.chatHandler = this.getCore().getChatHandler();
		this.vanishHandler = this.getCore().getVanishHandler();
	}

	@EventHandler
	public void onChat(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		event.setQuitMessage("§8[§c-§8] " + event.getPlayer().getDisplayName());

		this.vanishHandler.onPlayerQuit(player);
//		this.scoreboardHandler.removePlayerScoreboard(player);
		this.chatHandler.removeMessagePattern(player);
	}
}