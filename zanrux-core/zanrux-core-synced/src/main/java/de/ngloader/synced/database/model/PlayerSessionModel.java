package de.ngloader.synced.database.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "session")
public class PlayerSessionModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;

	@ManyToOne()
	@JoinColumn(name = "player_id")
	private PlayerModel player;

	@Column(name = "time", nullable = false)
	private Timestamp time;

	@Column(name = "action", nullable = false)
	private PlayerSessionAction action;

	@Column(name = "server", nullable = false)
	private String server;

	public PlayerSessionModel() { }

	public PlayerSessionModel(PlayerModel player, Timestamp time, PlayerSessionAction action, String server) {
		this.player = player;
		this.time = time;
		this.action = action;
		this.server = server;
	}

	public long getId() {
		return this.id;
	}

	public PlayerModel getPlayer() {
		return this.player;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public PlayerSessionAction getAction() {
		return this.action;
	}

	public String getServer() {
		return this.server;
	}

	public enum PlayerSessionAction {
		JOIN, QUIT, SWITCH, AFK, NO_AFK
	}
}