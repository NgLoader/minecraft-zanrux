package de.ngloader.proxy;

import java.io.IOException;

import de.ngloader.proxy.event.PlayerServerManager;
import de.ngloader.synced.ICore;
import de.ngloader.synced.IHandler;
import de.ngloader.synced.database.Database;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class Proxy extends Plugin implements ICore {

	public static final String PREFIX = "§8[§2Zanrux§8] §7";

	private final Database database;

	public Proxy() {
		this.database = new Database();
		IHandler.setMessageAdapter((message) -> ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(Proxy.PREFIX + message)));
	}

	@Override
	public void onLoad() { }

	@Override
	public void onEnable() {
		this.getProxy().getConsole().sendMessage(new TextComponent(Proxy.PREFIX + "§2Enabled§8!"));

		new PlayerServerManager(this);
	}

	@Override
	public void onDisable() {
		try {
			this.database.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.getProxy().getScheduler().cancel(this);
		this.getProxy().getConsole().sendMessage(new TextComponent(Proxy.PREFIX + "§4Disabled§8!"));
	}

	public Database getDatabase() {
		return this.database;
	}
}