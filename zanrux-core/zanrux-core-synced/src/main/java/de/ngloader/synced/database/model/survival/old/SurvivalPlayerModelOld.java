package de.ngloader.synced.database.model.survival.old;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "survival_players_old")
public class SurvivalPlayerModelOld {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	public int id;


	@Column(name = "uuid", nullable = false)
	public String uuid;

	@Column(name = "username")
	public String username;
}
