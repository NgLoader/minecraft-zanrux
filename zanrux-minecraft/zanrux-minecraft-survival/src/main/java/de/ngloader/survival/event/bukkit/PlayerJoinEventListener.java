package de.ngloader.survival.event.bukkit;

import java.sql.Timestamp;
import java.time.Instant;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Team;

import de.ngloader.survival.Survival;
import de.ngloader.survival.handler.event.EventListener;
import de.ngloader.survival.handler.home.HomeHandler;
import de.ngloader.synced.database.Database;
import de.ngloader.synced.database.controller.PlayerController;
import de.ngloader.synced.database.model.PlayerModel;
import de.ngloader.synced.database.model.survival.SurvivalPlayerModel;

public class PlayerJoinEventListener extends EventListener {

	private Database database;
	private HomeHandler homeHandler;
//	private VanishHandler vanishHandler;
//	private ScoreboardHandler scoreboardHandler;
//	private LuckPerms luckPerms;
//	private UserManager userManager;
//	private ChatHandler chatHandler;

	public PlayerJoinEventListener(Survival core) {
		super(core);
	}

	@Override
	public void onInit() {
		this.database = this.getCore().getDatabase();
		this.homeHandler = this.getCore().getHomeHandler();
//		this.vanishHandler = this.getCore().getVanishHandler();
//		this.scoreboardHandler = this.getCore().getScoreboardHandler();
//		this.chatHandler = this.getCore().getChatHandler();
	}

	@Override
	public void onEnable() {
//		this.luckPerms = LuckPermsProvider.get();
//		this.userManager = this.luckPerms.getUserManager();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		Team team = player.getScoreboard().getEntryTeam(player.getName());
		if (team == null) {
			player.getScoreboard().getTeam("player").addEntry(player.getName());
		}

		event.setJoinMessage("§8[§a+§8] " + player.getDisplayName());

		this.database.transaction().thenAccept(session -> {
//			this.scoreboardHandler.getTeams().forEach(team -> team.sendCreatePacket(player));
//			this.userManager.loadUser(player.getUniqueId()).thenAcceptAsync(user -> this.scoreboardHandler.getPlayerScoreboard(player).joinTeam(user.getPrimaryGroup()));
//			this.chatHandler.updateMessagePattern(player);

			PlayerModel playerModel = PlayerController.getPlayer(session, player.getUniqueId(), player.getName(), "");
			if (playerModel.getSurvivalPlayer() == null) {
				SurvivalPlayerModel survivalPlayerModel = new SurvivalPlayerModel(playerModel, Timestamp.from(Instant.now()));
				session.save(survivalPlayerModel);
				playerModel.setSurvivalPlayer(survivalPlayerModel);
			}

			this.homeHandler.load(player.getUniqueId(), playerModel.getSurvivalPlayer());

//			if (this.homeHandler.getHomesOfPlayer(player.getUniqueId()).isEmpty()) {
//				List<SurvivalPlayerModelOld> result = session.createQuery("from SurvivalPlayerModelOld where uuid=:uuid", SurvivalPlayerModelOld.class)
//						.setParameter("uuid", player.getUniqueId().toString()).getResultList();
//				if (!result.isEmpty()) {
//					List<HomeModelOld> result2 = session.createQuery("from HomeModelOld where player_id=:id", HomeModelOld.class)
//							.setParameter("id", result.get(0).id).getResultList();
//
//					for(HomeModelOld old : result2) {
//						this.homeHandler.create(player.getUniqueId(), old.name, old.description, new Location(Bukkit.getWorld(old.world), old.x, old.y, old.z, old.yaw, old.pitch)).thenAccept(home -> {
//							player.sendMessage(Survival.PREFIX + "§7Dein Home §8\"§e" + old.name + "§8\" §7wurde §aimportiert§8.");
//						});
//						session.delete(old);
//					}
//					session.delete(result.get(0));
//				}
//			}

			player.sendMessage(Survival.PREFIX + "§aResourcepack §7wird vorbereitet§8.");
			player.setResourcePack("https://www.dropbox.com/s/vmtsfuhozlg7ock/Zanrux.zip?dl=1");
		}).exceptionally(error -> {
			error.printStackTrace();
			player.kickPlayer(Survival.PREFIX + "§7Es ist ein §cfehler §7beim laden deiner §cspielerdaten §7aufgetreten§8.");
			return null;
		});
	}
}