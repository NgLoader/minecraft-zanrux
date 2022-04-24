package de.ngloader.synced.database;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class DatabaseNamingStrategy extends PhysicalNamingStrategyStandardImpl {

	private static final long serialVersionUID = 1L;

	private final String prefix;

	public DatabaseNamingStrategy(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
		return super.toPhysicalTableName(Identifier.toIdentifier(this.prefix + name.getText(), name.isQuoted()), context);
	}
}