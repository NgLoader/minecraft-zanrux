package de.ngloader.synced.database.model.survival;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "survival_mobcatcher")
public class MobCatcherModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;

	@Lob
	@Column(name = "name", nullable = false, columnDefinition = "BLOB")
	private byte[] nbt;

	public MobCatcherModel() { }

	public MobCatcherModel(byte[] nbt) {
		this.nbt = nbt;
	}

	public byte[] getNbt() {
		return this.nbt;
	}

	public long getId() {
		return this.id;
	}
}
