package de.ngloader.survival.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ngloader.survival.Survival;
import de.ngloader.survival.enchantment.EnchantmentList;

public class CommandTreeFeller implements CommandExecutor {

	public CommandTreeFeller(Survival core) { }

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Survival.PREFIX + "§7Die Console kann diesen §cCommand §7nicht nutzen§8.");
			return true;
		}
		Player player = (Player) sender;

		if (args.length == 3) {
			EnchantmentList.TELEKINESIS.applyOn(player.getInventory().getItemInMainHand(), 1);
			return true;
		}
		if (player.hasPermission("wuffy.admin")) {
			EnchantmentList.TREE_FELLER.applyOn(player.getInventory().getItemInMainHand(), args.length + 1);
		}
		return true;
	}
}