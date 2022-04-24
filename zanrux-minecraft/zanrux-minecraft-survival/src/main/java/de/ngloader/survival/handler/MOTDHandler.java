package de.ngloader.survival.handler;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;

import de.ngloader.survival.Survival;
import de.ngloader.survival.config.ConfigMOTD;
import de.ngloader.synced.IHandler;
import de.ngloader.synced.config.ConfigService;

public class MOTDHandler extends IHandler<Survival> {

	public MOTDHandler(Survival core) {
		super(core);
	}

	@Override
	public void onEnable() {
		ConfigMOTD config = ConfigService.getConfig(ConfigMOTD.class);
		((CraftServer) Bukkit.getServer()).getServer().setMotd(config.getMotd());
		ConfigService.removeConfig(config);
	}
}