package de.ngloader.survival.handler.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import de.ngloader.survival.Survival;
import de.ngloader.synced.IHandler;
import de.ngloader.synced.database.Database;
import de.ngloader.synced.database.controller.PlayerController;
import de.ngloader.synced.database.model.survival.HomeModel;
import de.ngloader.synced.database.model.survival.SurvivalPlayerModel;

public class HomeHandler extends IHandler<Survival> {

	private Map<UUID, List<HomeModel>> homes = new ConcurrentHashMap<UUID, List<HomeModel>>();

	private final Database database;

	public HomeHandler(Survival core) {
		super(core);

		this.database = this.core.getDatabase();
	}

	@Override
	public void onEnable() {
		this.database.transaction().thenAccept(session -> {
			Bukkit.getOnlinePlayers().forEach(player -> {
				this.load(player.getUniqueId(), PlayerController.getPlayer(session, player.getUniqueId()).getSurvivalPlayer());
			});
		});
	}

	public void load(UUID uuid, SurvivalPlayerModel player) {
		if (this.homes.containsKey(uuid)) {
			this.homes.remove(uuid);
		}

		List<HomeModel> homes = player.getHomes();
		if (homes != null) {
			this.homes.put(uuid, new ArrayList<>(homes));
			return;
		}
		this.homes.put(uuid, new ArrayList<>());
	}

	public HomeModel get(UUID uuid, String name) {
		Optional<HomeModel> found = this.homes.get(uuid).stream().filter(home -> home.getName().equalsIgnoreCase(name)).findFirst();
		if(found.isPresent()) {
			return found.get();
		}
		return null;
	}

	public boolean exist(UUID uuid, String name) {
		return this.homes.containsKey(uuid) && homes.get(uuid).stream().filter(home -> home.getName().equalsIgnoreCase(name)).findAny().isPresent();
	}

	public CompletableFuture<HomeModel> create(UUID uuid, String name, String description, Location location) {
		CompletableFuture<HomeModel> future = new CompletableFuture<>();
		this.database.transaction().thenAccept(session -> {
			SurvivalPlayerModel survivalPlayer = PlayerController.getPlayer(session, uuid).getSurvivalPlayer();
			HomeModel homeModel = new HomeModel(survivalPlayer, name, description, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
			session.save(homeModel);

			this.homes.get(uuid).add(homeModel);
			future.complete(homeModel);
		});
		return future;
	}

	public void delete(UUID uuid, HomeModel home) {
		if (this.homes.containsKey(uuid)) {
			this.homes.get(uuid).remove(home);
		}

		this.database.transaction().thenAccept(session -> session.delete(home));
	}

	public List<HomeModel> getHomesOfPlayer(UUID uuid) {
		if (this.homes.containsKey(uuid))
			return this.homes.get(uuid);
		return new ArrayList<HomeModel>();
	}
}