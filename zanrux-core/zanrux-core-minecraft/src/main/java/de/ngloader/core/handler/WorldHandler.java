package de.ngloader.core.handler;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.WorldCreator;

import de.ngloader.core.MCCore;
import de.ngloader.synced.IHandler;

public class WorldHandler extends IHandler<MCCore> {

	private final Map<String, World> worlds = new HashMap<>();

	public WorldHandler(MCCore core) {
		super(core);
	}

	public void loadConfig() {
		
	}

	public void saveConfig() {
		
	}

//	public World createWorld(String worldName, Environment environment) {

	public World loadWorld(String worldName) {
		World world = this.worlds.get(worldName);

		if (world == null) {
			new WorldCreator(worldName).createWorld();
		}

		return world;
	}

	public void unloadWorld(World world) {
		
	}

	public void removeWorld(World world) {
	}
}