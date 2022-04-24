package de.ngloader.survival.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ngloader.survival.Survival;

public class CommandResourcepack implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Survival.PREFIX + "§7Die Console kann kein §4Resourcepack §7anfragen§8.");
			return true;
		}

		sender.sendMessage(Survival.PREFIX + "§7Sende resourcepack§8.");
		((Player) sender).setResourcePack("https://www.dropbox.com/s/vmtsfuhozlg7ock/Zanrux.zip?dl=1");
		return true;
	}
}