package de.ngloader.survival.item;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import de.ngloader.survival.Survival;
import de.ngloader.survival.item.type.ItemBackpack;
import de.ngloader.survival.item.type.ItemHoe;
import de.ngloader.survival.item.type.ItemMobCatcher;
import de.ngloader.survival.item.type.ItemMobileCraftingTable;
import de.ngloader.survival.item.type.ItemTreefeller;
import de.ngloader.synced.IHandler;

public class ItemManager extends IHandler<Survival> implements Listener {

	private final Set<Item> items = new HashSet<>();

	public ItemManager(Survival core) {
		super(core);
	}

	@Override
	public void onEnable() {
		this.addItem(new ItemMobCatcher(this.core));
		this.addItem(new ItemBackpack(this.core));
		this.addItem(new ItemTreefeller(this.core));
		this.addItem(new ItemHoe(this.core));
		this.addItem(new ItemMobileCraftingTable(this.core));

		Bukkit.getServer().getPluginManager().registerEvents(this, this.core);
	}

	@EventHandler
	public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
		Player player = event.getPlayer();
		Bukkit.getConsoleSender().sendMessage(Survival.PREFIX + " ResourcePack -> Spieler: " + player.getName() + " Status: " + event.getStatus().name());
		switch (event.getStatus()) {
		case ACCEPTED:
			player.sendMessage(Survival.PREFIX + "§aResourcepack §7wird gesendet§8.");
			return;
		case DECLINED:
			player.kickPlayer(Survival.PREFIX + "Bitte §aaktiviere §7das §aServer Resourcepack §7um alle features voll nutzen zu können§8.");
			return;
		case FAILED_DOWNLOAD:
			player.kickPlayer(Survival.PREFIX + "Es ist ein §cFehler §7beim laden des §aResourcePack §7aufgetreten§8.");
			return;
		case SUCCESSFULLY_LOADED:
			player.sendMessage(Survival.PREFIX + "Das §aResourcePack §7wurde §aerfolgreich §7geladen§8.");
			return;
		}
	}

	public void addItem(Item item) {
		this.items.add(item);

		if (item instanceof Listener) {
			Bukkit.getPluginManager().registerEvents((Listener) item, this.core);
		}
	}
}
