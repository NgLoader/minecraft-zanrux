package de.ngloader.survival.command.home;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ngloader.survival.Survival;
import de.ngloader.synced.database.model.survival.HomeModel;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class CommandHomeList implements CommandExecutor {

	private final Survival core;

	public CommandHomeList(Survival core) {
		this.core = core;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Survival.PREFIX + "§7Die Console kann keine §cHome §7haben§8.");
			return true;
		}
		Player player = (Player) sender;
		List<HomeModel> homes = this.core.getHomeHandler().getHomesOfPlayer(player.getUniqueId());

		if (homes.size() > 0) {
			player.sendMessage(Survival.PREFIX + "§8[]§7------§a{ §2Home Liste §a}§7------§8[]");
			player.sendMessage(Survival.PREFIX + " ");

			for (HomeModel home : homes) {
				TextComponent textComponent = new TextComponent(Survival.PREFIX + "§8- §a" + home.getName() + (home.getDescription().isEmpty() ? "" : " §8(§7" + home.getDescription() + "§8)"));
				textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aKlick zum §2teleportieren")));
				textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + home.getName()));
				player.spigot().sendMessage(textComponent);
			}
//				sender.sendMessage(Freebuild.PREFIX + "§8- §a" + home.getName() + (home.getDescription().isEmpty() ? "" : " §8(§7" + home.getDescription() + "§8)"));

			player.sendMessage(Survival.PREFIX + " ");
			player.sendMessage(Survival.PREFIX + "§8[]§7------§a{ §2Home Liste §a}§7------§8[]");
		} else
			sender.sendMessage(Survival.PREFIX + "§7Du hast derzeitig keine §chomes §7erstellt§8.");
		return true;
	}
}
