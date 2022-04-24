package de.ngloader.synced.database.model.survival;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "survival_home")
public class HomeModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;

	@ManyToOne()
	@JoinColumn(name = "player_id")
	private SurvivalPlayerModel player;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "world", nullable = false)
	private String world;

	@Column(name = "x", nullable = false)
	private double x;

	@Column(name = "y", nullable = false)
	private double y;

	@Column(name = "z", nullable = false)
	private double z;

	@Column(name = "yaw", nullable = false)
	private float yaw;

	@Column(name = "pitch", nullable = false)
	private float pitch;

	public HomeModel() { }

	public HomeModel(SurvivalPlayerModel player, String name, String description, String world, double x, double y, double z, float yaw, float pitch) {
		this.player = player;
		this.name = name;
		this.description = description;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWorld() {
		return this.world;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public double getX() {
		return this.x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return this.y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return this.z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public float getYaw() {
		return this.yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return this.pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public long getId() {
		return this.id;
	}

	public SurvivalPlayerModel getPlayer() {
		return this.player;
	}
}
