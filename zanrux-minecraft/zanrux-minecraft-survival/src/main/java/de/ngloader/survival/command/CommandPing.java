package de.ngloader.survival.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ngloader.core.util.PlayerUtil;
import de.ngloader.survival.Survival;

public class CommandPing implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Survival.PREFIX + "§7Die Console hat keinen §4Ping§8.");
			return true;
		}
		sender.sendMessage(Survival.PREFIX + "§7Dein §aPing §7beträgt §a" + PlayerUtil.getPlayerPing((Player) sender) + "§cms§8.");
		return true;
	}
}
