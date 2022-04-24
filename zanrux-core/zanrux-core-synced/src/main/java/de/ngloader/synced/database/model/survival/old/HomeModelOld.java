package de.ngloader.synced.database.model.survival.old;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "survival_homes_old")
public class HomeModelOld {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "home_id", nullable = false)
	public long home_id;

	@Column(name = "player_id")
	public int player_id;

	@Column(name = "name", nullable = false)
	public String name;

	@Column(name = "description")
	public String description;

	@Column(name = "world", nullable = false)
	public String world;

	@Column(name = "x", nullable = false)
	public double x;

	@Column(name = "y", nullable = false)
	public double y;

	@Column(name = "z", nullable = false)
	public double z;

	@Column(name = "yaw", nullable = false)
	public float yaw;

	@Column(name = "pitch", nullable = false)
	public float pitch;
}
