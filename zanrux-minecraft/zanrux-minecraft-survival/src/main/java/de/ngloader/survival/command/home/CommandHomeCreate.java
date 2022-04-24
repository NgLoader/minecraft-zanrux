package de.ngloader.survival.command.home;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ngloader.core.util.GroupUtil;
import de.ngloader.survival.Survival;
import de.ngloader.survival.handler.home.HomeHandler;
import de.ngloader.synced.database.model.survival.HomeModel;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class CommandHomeCreate implements CommandExecutor {

	private final Survival core;

	public CommandHomeCreate(Survival core) {
		this.core = core;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Survival.PREFIX + "§7Die Console kann kein §cHome §7erstellen§8.");
			return true;
		}
		Player player = (Player) sender;

		if (!player.hasPermission("wuffy.home.create")) {
			player.sendMessage(Survival.PREFIX + "§7Du hast keine §cRechte §7um diesen §cCommand §7zu nutzen§8.");
			return true;
		}

		if (args.length >= 1) {
			HomeHandler handler = this.core.getHomeHandler();
			String homeName = args[0];
			LuckPerms luckPermsApi = LuckPermsProvider.get();
			int homes = Integer.valueOf(GroupUtil.getGroupMetaSorted(luckPermsApi.getGroupManager().getGroup(luckPermsApi.getUserManager().getUser(player.getUniqueId()).getPrimaryGroup()), "max-homes", "10"));

			if (!player.hasPermission("wuffy.home.nohomelimit") && handler.getHomesOfPlayer(player.getUniqueId()).size() >= homes) {
				player.sendMessage(Survival.PREFIX + "§7Du darfst nur maximal §c" + homes + " §7Homes §cbesitzen§8.");
				return true;
			}

			List<String> description = new ArrayList<>();
			for (int i = 1; i < args.length; i++)
				description.add(args[i]);

			if (handler.exist(player.getUniqueId(), homeName.toLowerCase())) {
				player.sendMessage(Survival.PREFIX + "§7Dieser §cHome §7exestiert schon§8.");
				return true;
			}

			player.sendMessage(Survival.PREFIX + "§7Dein home wird §aerstellt§8.");
			CompletableFuture<HomeModel> future = handler.create(player.getUniqueId(), homeName, String.join(" ", description), player.getLocation());
			future.thenAccept(home -> {
				player.sendMessage(Survival.PREFIX + "§7Du hast erfolgreich dein §aHome §8\"§a" + home.getName() + "§8\" §aerstellt§8.");
			}).exceptionally(error -> {
				error.printStackTrace();
				player.sendMessage(Survival.PREFIX + "§7Es ist ein fehler beim erstellen deines §cHomes §7passiert§8.");
				return null;
			});
		} else {
			player.sendMessage(Survival.PREFIX + "§7/SetHome §8<§7name§8> §8[§7beschreibung§8]");
		}
		return true;
	}
}
