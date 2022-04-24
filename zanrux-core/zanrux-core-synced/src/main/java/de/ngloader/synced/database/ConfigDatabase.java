package de.ngloader.synced.database;

import de.ngloader.synced.config.Config;

@Config(path = "Core", name = "database")
public class ConfigDatabase {
	public String driverClass = "org.mariadb.jdbc.Driver";

	public String url = "127.0.0.1";

	public String databaseName = "database";

	public String username = "username";
	public String password = "password";

	public String dialect = "org.hibernate.dialect.MariaDBDialect";

	public String prefix = "core_";

	public int poolSize = 10;
	public boolean autoCommit = true;
}
