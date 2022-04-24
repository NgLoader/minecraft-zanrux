package de.ngloader.survival.handler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.event.world.TimeSkipEvent.SkipReason;
import org.bukkit.scheduler.BukkitTask;

import de.ngloader.survival.Survival;
import de.ngloader.survival.config.ConfigSleep;
import de.ngloader.synced.IHandler;
import de.ngloader.synced.config.ConfigService;

public class SleepHandler extends IHandler<Survival> implements Listener, Runnable {

	private final VanishHandler vanishHandler;

	private BukkitTask task;
	private Set<World> worlds = new HashSet<>();

	protected String prefix;
	protected int sleepPercent = 60;

	public SleepHandler(Survival plugin, String prefix) {
		super(plugin);
		this.prefix = prefix;

		this.vanishHandler = plugin.getVanishHandler();
	}

	@Override
	public void onEnable() {
		ConfigSleep sleep = ConfigService.getConfig(ConfigSleep.class);
		this.sleepPercent = sleep.sleepPercent;

		Bukkit.getServer().getPluginManager().registerEvents(this, this.core);
	}

	@Override
	public void run() {
		Iterator<World> iterator = this.worlds.iterator();
		while (iterator.hasNext()) {
			World world = iterator.next();
			int needed = this.getNeededCount(this.getPlayersInWorld(world));
			int sleeping = this.getSleepingCount(this.getPlayersInWorld(world));
			int deepSleeping = this.getDeepSleepingCount(this.getPlayersInWorld(world));

			if (sleeping > 0) {
				if (deepSleeping >= needed) {
					world.setTime(0);
					if (world.isThundering()) {
						world.setThundering(false);
						world.setStorm(false);
					}

					world.getPlayers().forEach(player -> {
						player.setStatistic(Statistic.TIME_SINCE_REST, 0);
						player.sendMessage(this.prefix + "Es wird nun §eTag§8.");
					});
					iterator.remove();
				}
				return;
			}

			iterator.remove();
		}	

		if (this.worlds.isEmpty()) {
			this.task.cancel();
			this.task = null;
		}
	}

	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
		if (event.getBedEnterResult() != BedEnterResult.OK) {
			return;
		}

		Player player = event.getPlayer();
		World world = player.getWorld();
		if (this.worlds.add(world)) {
			if (this.task == null || this.task.isCancelled()) {
				this.task = Bukkit.getScheduler().runTaskTimer(this.core, this, 20, 20);
			}
		}

		int needed = this.getNeededCount(this.getPlayersInWorld(world));
		int sleeping = this.getSleepingCount(this.getPlayersInWorld(world)) + 1;
		int difference = needed - sleeping;

		if (difference > 1) {
			this.sendMessage(world, String.format("%s§e%s §7liegt nun im §6Bett§8.\n%sEs m§ssen noch §e%d §aSpieler §7schlafen§8.",
					this.prefix,
					event.getPlayer().getName(),
					this.prefix,
					difference));
		} else if (difference > 0) {
			this.sendMessage(world, String.format("%s§e%s §7liegt nun im §6Bett§8.\n%sEs muss noch §eein §7weiterer §aSpieler §7schlafen§8.",
					this.prefix,
					event.getPlayer().getName(),
					this.prefix,
					difference));
		} else {
			this.sendMessage(world, String.format("%s§e%s §7liegt nun im §6Bett§8.",
					this.prefix,
					event.getPlayer().getName()));
		}
	}

	@EventHandler
	public void onWorldTimeChange(TimeSkipEvent event) {
		if (event.getSkipReason() == SkipReason.NIGHT_SKIP) {
			event.setCancelled(true);
		}
	}

	public int getNeededCount(Stream<Player> players) {
		int count = Math.round(players.count() / 100f * this.sleepPercent);
		return count > 1 ? count : 1;
	}

	public int getSleepingCount(Stream<Player> players) {
		return  (int) players
				.filter(Player::isSleeping)
				.count();
	}

	public int getDeepSleepingCount(Stream<Player> players) {
		return  (int) players
				.filter(player -> player.isSleeping() && player.getSleepTicks() > 99)
				.count();
	}

	public Stream<Player> getPlayersInWorld(World world) {
		return world.getPlayers().stream().filter(player ->
				player.getGameMode() != GameMode.SPECTATOR
				&& player.getGameMode() != GameMode.CREATIVE
				&& !this.vanishHandler.isVanish(player));
	}

	public void sendMessage(World world, String message) {
		world.getPlayers().forEach(player -> player.sendMessage(message));
	}
}