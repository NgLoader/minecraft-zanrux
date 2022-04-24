package de.ngloader.synced.database.model.survival;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.sun.istack.Nullable;

import de.ngloader.synced.database.model.PlayerModel;

@Entity
@Table(name = "survival_player")
public class SurvivalPlayerModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;

	@OneToOne()
	@JoinColumn(name = "player_id")
	private PlayerModel player;

	@Column(name = "joined_date")
	private Timestamp joinedDate;

	@OneToMany(mappedBy = "player")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<HomeModel> homes;

	public SurvivalPlayerModel() { }

	public SurvivalPlayerModel(PlayerModel playerModel, Timestamp joinedDate) {
		this.player = playerModel;
		this.joinedDate = joinedDate;
	}

	public long getId() {
		return this.id;
	}

	public PlayerModel getPlayer() {
		return this.player;
	}

	public Timestamp getJoinedDate() {
		return this.joinedDate;
	}

	@Nullable
	public List<HomeModel> getHomes() {
		return this.homes;
	}
}
