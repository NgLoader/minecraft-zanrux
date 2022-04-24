package de.ngloader.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.plugin.java.JavaPlugin;

import de.ngloader.core.typeadapter.TypeAdapterBlockFace;
import de.ngloader.core.typeadapter.TypeAdapterDamageCause;
import de.ngloader.core.typeadapter.TypeAdapterMaterial;
import de.ngloader.synced.ICore;
import de.ngloader.synced.IHandler;
import de.ngloader.synced.config.ConfigService;
import de.ngloader.synced.database.Database;

public abstract class MCCore extends JavaPlugin implements ICore {

	public static final List<Advancement> ADVANCEMENT_LIST;

	static {
		List<Advancement> advancements = new ArrayList<>();
		for (Iterator<Advancement> advancement = Bukkit.advancementIterator(); advancement.hasNext();) {
			advancements.add(advancement.next());
		}
		ADVANCEMENT_LIST = Collections.unmodifiableList(advancements);

		IHandler.setMessageAdapter((message) -> Bukkit.getConsoleSender().sendMessage(MCCore.PREFIX + message));

		ConfigService.addTypeAdapter(new TypeAdapterBlockFace());
		ConfigService.addTypeAdapter(new TypeAdapterMaterial());
		ConfigService.addTypeAdapter(new TypeAdapterDamageCause());
	}

	private Database database;

	protected void setDatabase(Database database) {
		this.database = database;
	}

	public Database getDatabase() {
		return this.database;
	}
}