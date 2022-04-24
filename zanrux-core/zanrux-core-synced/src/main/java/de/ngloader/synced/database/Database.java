package de.ngloader.synced.database;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import de.ngloader.synced.config.ConfigService;
import de.ngloader.synced.database.model.PlayerModel;
import de.ngloader.synced.database.model.PlayerSessionModel;
import de.ngloader.synced.database.model.survival.old.HomeModelOld;
import de.ngloader.synced.database.model.survival.old.SurvivalPlayerModelOld;

public class Database implements Closeable {

	private final SessionFactory sessionFactory;
	private final ExecutorService executor = new ForkJoinPool(Runtime.getRuntime().availableProcessors(), pool -> {
		ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
		worker.setName("mc-zanrux-pool-db-" + worker.getPoolIndex());
		return worker;
	}, null, true);

	public Database(Class<?>... entities) {
		ConfigDatabase config = ConfigService.reloadConfig(ConfigDatabase.class);
		Configuration configuration = new Configuration()
				.setProperty("hibernateconnection.provider_class", "org.hibernate.c3p0.internal.C3P0ConnectionProvider")
				.setProperty("hibernate.connection.driver_class", config.driverClass)
				.setProperty("hibernate.connection.url", config.url)
				.setProperty("hibernate.connection.username", config.username)
				.setProperty("hibernate.connection.password", config.password)
				.setProperty("hibernate.connection.pool_size", Integer.toString(config.poolSize))
				.setProperty("hibernate.connection.autocommit", Boolean.toString(config.autoCommit))
				.setProperty("hibernate.dialect", config.dialect)
				.setProperty("hibernate.hbm2ddl.auto", "update")
				.setProperty("hibernate.c3p0.min_size", "5")
				.setProperty("hibernate.c3p0.max_size", "20")
				.setProperty("hibernate.c3p0.timeout", "300")
				.setProperty("hibernate.c3p0.acquire_increment", "5")
				.setProperty("hibernate.c3p0.idle_test_period", "100")
				.setProperty("connection.autoReconnect", "true")
				.setProperty("connection.autoReconnectForPools", "true")
				.setProperty("connection.is-connection-validation-required", "true")
				.addAnnotatedClass(PlayerModel.class)
				.addAnnotatedClass(PlayerSessionModel.class)
				.addAnnotatedClass(SurvivalPlayerModelOld.class)
				.addAnnotatedClass(HomeModelOld.class);
		configuration.setPhysicalNamingStrategy(new DatabaseNamingStrategy(config.prefix));

		for (Class<?> entity : entities) {
			configuration.addAnnotatedClass(entity);
		}

		this.sessionFactory = configuration.buildSessionFactory();
	}

	public CompletableFuture<Session> transaction() {
		CompletableFuture<Session> future = new CompletableFuture<>();
		this.executor.execute(() -> {
			try {
				Session session = this.sessionFactory.openSession();
				Transaction transaction = session.beginTransaction();

				try {
					future.complete(session);
					transaction.commit();
				} catch (Exception e) {
					transaction.rollback();
					future.completeExceptionally(e);
				} finally {
					session.close();
				}
			} catch (Exception e) {
				future.completeExceptionally(e);
			}
		});
		return future;
	}

	@Override
	public void close() throws IOException {
		this.sessionFactory.close();
	}
}