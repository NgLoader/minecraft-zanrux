package de.ngloader.core.scoreboard;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.ngloader.core.util.PlayerUtil;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket.Action;
import net.minecraft.world.scores.PlayerTeam;

public class SurvivalTeam {

	private final PlayerTeam team;
	private final List<String> entrys = new ArrayList<String>();

	public SurvivalTeam(PlayerTeam team) {
		this.team = team;

		PlayerUtil.sendPacket(Bukkit.getOnlinePlayers(), ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(this.team, true));
	}

	public void sendCreatePacket(Player player) {
		PlayerUtil.sendPacket(player, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(this.team, true));

		if (!this.entrys.isEmpty()) {
			PlayerUtil.sendPacket(player, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(this.team, true));
			
			//TODO Check if this already called a line above
			this.entrys.forEach(entry -> PlayerUtil.sendPacket(player, ClientboundSetPlayerTeamPacket.createPlayerPacket(this.team, entry, Action.ADD)));
		}
	}

	public void sendRemovePacket(Player player) {
		PlayerUtil.sendPacket(player, ClientboundSetPlayerTeamPacket.createRemovePacket(this.team));
	}

	public void addEntry(List<String> entrys) {
		for (String entry : entrys) {
			if (!this.entrys.contains(entry)) {
				this.entrys.add(entry);
			}
		}

		PlayerUtil.sendPacket(Bukkit.getOnlinePlayers(), entrys.stream().map(entry -> ClientboundSetPlayerTeamPacket.createPlayerPacket(this.team, entry, Action.ADD)));
	}

	public void removeEntry(List<String> entrys) {
		if (this.entrys.removeAll(entrys)) {
			PlayerUtil.sendPacket(Bukkit.getOnlinePlayers(), entrys.stream().map(entry -> ClientboundSetPlayerTeamPacket.createPlayerPacket(this.team, entry, Action.REMOVE)));
		}
	}

	public void removeAll() {
		PlayerUtil.sendPacket(Bukkit.getOnlinePlayers(), this.entrys.stream().map(entry -> ClientboundSetPlayerTeamPacket.createPlayerPacket(this.team, entry, Action.REMOVE)));
		this.entrys.clear();
	}

	public List<String> getEntrys() {
		return this.entrys;
	}

	public PlayerTeam getTeam() {
		return this.team;
	}
}