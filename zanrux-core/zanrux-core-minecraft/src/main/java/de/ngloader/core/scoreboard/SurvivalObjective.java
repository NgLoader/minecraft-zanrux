package de.ngloader.core.scoreboard;

import java.util.UUID;

import org.bukkit.entity.Player;

import de.ngloader.core.util.PlayerUtil;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.world.scores.Objective;

public class SurvivalObjective {

	private final Objective objective;

	public final Player player;
	public final UUID uuid;
	public final String identifier;
	private String scoreValue;
	private int score;

	public SurvivalObjective(Objective objective, Player player, String identifier, String scoreValue, int score) {
		this.objective = objective;
		this.player = player;
		this.identifier = identifier;
		this.scoreValue = scoreValue;
		this.score = score;

		this.uuid = this.player.getUniqueId();
	}

	public void updateScoreValue(String scoreValue) {
		this.remove();
		this.scoreValue = scoreValue;
		this.update();
	}

	public void updateScore(int score) {
		this.remove();
		this.score = score;
		this.update();
	}

	public void update() {
		ClientboundSetScorePacket packetPlayOutScoreboardScore = new ClientboundSetScorePacket(ServerScoreboard.Method.CHANGE, this.objective.getName(), this.scoreValue, this.score);
		PlayerUtil.sendPacket(player, packetPlayOutScoreboardScore);
	}

	public void remove() {
		ClientboundSetScorePacket packetPlayOutScoreboardScore = new ClientboundSetScorePacket(ServerScoreboard.Method.REMOVE, this.objective.getName(), this.scoreValue, this.score);
		PlayerUtil.sendPacket(player, packetPlayOutScoreboardScore);
	}

	public String getScoreValue() {
		return this.scoreValue;
	}

	public int getScore() {
		return this.score;
	}
}