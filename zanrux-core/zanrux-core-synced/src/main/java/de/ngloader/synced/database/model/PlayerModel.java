package de.ngloader.synced.database.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.sun.istack.Nullable;

import de.ngloader.synced.database.model.survival.SurvivalPlayerModel;

@Entity
@Table(name = "player")
public class PlayerModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;

	@Column(name = "uuid", unique = true, nullable = false, columnDefinition = "BINARY(16)")
	private UUID uuid;

	@Column(name = "joined_date", nullable = false)
	private Timestamp joinedDate;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "skin")
	private String skin;

	@Column(name = "playtime")
	private long playtime = 0;

	@OneToMany(mappedBy = "player")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<PlayerSessionModel> sessions;

	@OneToOne(mappedBy = "player")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private SurvivalPlayerModel survivalPlayer;

	public PlayerModel() { }

	public PlayerModel(UUID uuid, Timestamp joinedDate, String username, String skin) {
		this.uuid = uuid;
		this.joinedDate = joinedDate;
		this.username = username;
		this.skin = skin;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSkin() {
		return this.skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public long getPlaytime() {
		return this.playtime;
	}

	public void addPlaytime(long playtime) {
		this.playtime += playtime;
	}

	@Nullable
	public SurvivalPlayerModel getSurvivalPlayer() {
		return this.survivalPlayer;
	}

	public void setSurvivalPlayer(SurvivalPlayerModel survivalPlayer) {
		this.survivalPlayer = survivalPlayer;
	}

	public long getId() {
		return this.id;
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public Timestamp getJoinedDate() {
		return this.joinedDate;
	}

	@Nullable
	public List<PlayerSessionModel> getSessions() {
		return this.sessions;
	}
}