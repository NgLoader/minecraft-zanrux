package de.ngloader.synced;

import de.ngloader.synced.database.Database;

public interface ICore {

	public static final String PREFIX = "§8[§cCore§8] §7";
	
	public Database getDatabase();
}