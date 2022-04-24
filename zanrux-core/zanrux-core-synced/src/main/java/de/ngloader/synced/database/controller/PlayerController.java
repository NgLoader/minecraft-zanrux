package de.ngloader.synced.database.controller;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;

import com.sun.istack.NotNull;

import de.ngloader.synced.database.model.PlayerModel;

public class PlayerController {

	@NotNull
	public static PlayerModel getPlayer(Session session, UUID uuid) {
		List<PlayerModel> result = session.createQuery("from PlayerModel where uuid=:uuid", PlayerModel.class)
				.setParameter("uuid", uuid).getResultList();
		return result.isEmpty() ? null : result.get(0);
	}

	@NotNull
	public static PlayerModel getPlayer(Session session, UUID uuid, String name, String skin) {
		PlayerModel playerModel = PlayerController.getPlayer(session, uuid);
		if (playerModel != null) {
			return playerModel;
		}

		playerModel = new PlayerModel(uuid, Timestamp.from(Instant.now()), name, skin);
		session.save(playerModel);
		return playerModel;
	}
}