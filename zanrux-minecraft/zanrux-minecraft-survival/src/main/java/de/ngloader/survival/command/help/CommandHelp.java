package de.ngloader.survival.command.help;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ngloader.core.help.HelpSystem;
import de.ngloader.core.inventory.InventorySystem;
import de.ngloader.survival.Survival;

public class CommandHelp implements CommandExecutor {

	private final HelpSystem helpSystem;
	private final InventorySystem inventorySystem;

	public CommandHelp(Survival core) {
		this.helpSystem = core.getHelpSystem();
		this.inventorySystem = core.getInventorySystem();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Survival.PREFIX + "§7Die Console hat keinen §4Ping§8.");
			return true;
		}

		this.inventorySystem.openInventory((Player) sender, this.helpSystem.getInventory());
		return true;
	}
}
