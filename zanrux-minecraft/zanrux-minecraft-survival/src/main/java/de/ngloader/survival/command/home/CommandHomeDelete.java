package de.ngloader.survival.command.home;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import de.ngloader.survival.Survival;
import de.ngloader.survival.handler.home.HomeHandler;
import de.ngloader.synced.database.model.survival.HomeModel;
import de.ngloader.synced.util.ArrayUtil;

public class CommandHomeDelete implements CommandExecutor, TabExecutor {

	private final Survival core;

	public CommandHomeDelete(Survival core) {
		this.core = core;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Survival.PREFIX + "§7Die Console kann kein §cHome §7löschen§8.");
			return true;
		}
		Player player = (Player) sender;

		if (!player.hasPermission("wuffy.home.delete")) {
			player.sendMessage(Survival.PREFIX + "§7Du hast keine §cRechte §7um diesen §cCommand §7zu nutzen§8.");
			return true;
		}

		if (args.length == 1) {
			HomeHandler handler = this.core.getHomeHandler();
			String homeName = args[0].toLowerCase();
			HomeModel home = handler.get(player.getUniqueId(), homeName);

			if (home == null) {
				player.sendMessage(Survival.PREFIX + "§7Dieser §cHome §7exestiert nicht§8.");
				return true;
			}

			handler.delete(player.getUniqueId(), home);
			player.sendMessage(Survival.PREFIX + "§7Du hast erfolgreich dein §aHome §8\"§a" + home.getName() + "§8\" §cgelöscht§8.");
		} else {
			player.sendMessage(Survival.PREFIX + "§7/DeleteHome §8<§7name§8>");
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			return this.core.getHomeHandler().getHomesOfPlayer(((Player) sender).getUniqueId()).stream()
					.map(home -> home.getName())
					.collect(Collectors.toList());
		} else if (args.length == 1) {
			String search = args[0].toLowerCase();

			return this.core.getHomeHandler().getHomesOfPlayer(((Player) sender).getUniqueId()).stream()
					.map(home -> home.getName())
					.filter(home -> home.toLowerCase().startsWith(search))
					.collect(Collectors.toList());
		}
		return ArrayUtil.EMPTY_ARRAY_LIST;
	}
}
