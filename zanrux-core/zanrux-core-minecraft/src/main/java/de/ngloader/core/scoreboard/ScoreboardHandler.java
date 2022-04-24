package de.ngloader.core.scoreboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_18_R2.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;

import de.ngloader.core.MCCore;
import de.ngloader.core.util.GroupUtil;
import de.ngloader.synced.ICore;
import de.ngloader.synced.IHandler;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team.CollisionRule;
import net.minecraft.world.scores.Team.Visibility;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.world.scores.criteria.ObjectiveCriteria.RenderType;

public class ScoreboardHandler extends IHandler<MCCore> {

	private Scoreboard scoreboard;
	private CraftScoreboard craftScoreboard;
	private Objective objective;

	private Map<Player, PlayerScoreboard> playerScoreboards = new HashMap<>();
	private Map<Group, SurvivalTeam> teamScoreboardsByGroup = new HashMap<>();
	private Map<String, SurvivalTeam> teamScoreboardsByName = new HashMap<>();

	public ScoreboardHandler(MCCore core) {
		super(core);
	}

	@Override
	public void onEnable() {
		this.scoreboard = new Scoreboard();
		this.craftScoreboard = (CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard();
		this.objective = new Objective(((CraftScoreboard) this.craftScoreboard).getHandle(),
				"abc",
				ObjectiveCriteria.DUMMY,
				new TextComponent("§2Z§aan§2ru§ax§7.§2de"),
				RenderType.INTEGER);

		this.craftScoreboard.getObjectives().forEach(objective -> objective.unregister());

		LuckPerms luckPerms = LuckPermsProvider.get();

		while(luckPerms.getGroupManager().loadAllGroups().isDone());
		Bukkit.getConsoleSender().sendMessage(ICore.PREFIX + "§7Loaded §a" + luckPerms.getGroupManager().getLoadedGroups().size() + " §7groups§8.");

		this.updateGroups();
	}

	@Override
	public void onDisable() {
		this.removeGroups();
	}

	public PlayerScoreboard getPlayerScoreboard(Player player) {
		PlayerScoreboard scoreboard = this.playerScoreboards.get(player);

		if (scoreboard == null) {
			scoreboard = new PlayerScoreboard(this, player, this.objective);
			this.playerScoreboards.put(player, scoreboard);
		}

		return scoreboard;
	}

	public PlayerScoreboard removePlayerScoreboard(Player player) {
		PlayerScoreboard playerScoreboard = this.playerScoreboards.remove(player);

//		if (playerScoreboard != null) {
//			playerScoreboard.leaveTeam(true);
//			playerScoreboard.removeAllObjectives(false);
//		}

		return playerScoreboard;
	}

	public SurvivalTeam getTeam(String group) {
		return this.teamScoreboardsByName.get(group);
	}

	public SurvivalTeam getTeam(Group group) {
		return this.teamScoreboardsByGroup.get(group);
	}

	public SurvivalTeam createTeam(Group group) {
		SurvivalTeam team = this.teamScoreboardsByGroup.get(group);

		if (team == null) {
			String prefix = ChatColor.translateAlternateColorCodes('&', GroupUtil.getGroupPrefix(group).orElseGet(() -> GroupUtil.getGroupMeta(group, "color", "§a") + group.getFriendlyName()));

			if (!prefix.endsWith(" ")) {
				prefix += " ";
			}

			ChatFormatting color = ChatFormatting.GRAY;
			String colorCode = GroupUtil.getGroupMetaSorted(group, "tablist-color", "a");

			for (ChatFormatting format : ChatFormatting.values()) {
				if (format.code == colorCode.charAt(0)) {
					color = format;
				}
			}

			PlayerTeam scoreboardTeam = new PlayerTeam(this.scoreboard, this.getTeamName(group));
			scoreboardTeam.setPlayerPrefix(new TextComponent(prefix));
			scoreboardTeam.setAllowFriendlyFire(true);
			scoreboardTeam.setCollisionRule(CollisionRule.ALWAYS);
			scoreboardTeam.setSeeFriendlyInvisibles(false);
			scoreboardTeam.setDeathMessageVisibility(Visibility.NEVER);
			scoreboardTeam.setColor(color);

			team = new SurvivalTeam(scoreboardTeam);

			this.teamScoreboardsByGroup.put(group, team);
			this.teamScoreboardsByName.put(group.getName(), team);
		}

		return team;
	}

	public String getTeamName(Group group) {
		String teamName = "§" + "ABCEDFGHIJKLMNOPQRSTUFWXYZ".charAt(25 - group.getWeight().orElseGet(() -> 25)) + "_" + group.getName().toUpperCase();
		if(teamName.length() > 16)
			teamName = teamName.substring(0, 16);
		return teamName;
	}

	public void loadGroups() {
//		for(Group group : LuckPermsProvider.get().getGroupManager().getLoadedGroups()) {
//			this.createTeam(group);
//		}
	}

	public void updateGroups() {
//		UserManager userManager = LuckPermsProvider.get().getUserManager();
//
//		this.removeGroups();
//		this.loadGroups();
//
//		for (Player player : Bukkit.getOnlinePlayers()) {
//			PlayerScoreboard playerScoreboard = this.getPlayerScoreboard(player);
//			userManager.loadUser(playerScoreboard.getPlayer().getUniqueId()).thenAccept(user -> {
//				playerScoreboard.joinTeam(user.getPrimaryGroup());
//			});
//		}
	}

	public void removeGroups() {
//		for (PlayerScoreboard playerScoreboard : this.playerScoreboards.values()) {
//			playerScoreboard.leaveTeam(false);
//		}
//
//		for (SurvivalTeam team : this.teamScoreboardsByGroup.values()) {
//			for (Player player : Bukkit.getOnlinePlayers()) {
//				team.sendRemovePacket(player);
//			}
//		}
//
//		this.teamScoreboardsByGroup.clear();
//		this.teamScoreboardsByName.clear();
	}

	public Collection<SurvivalTeam> getTeams() {
		return this.teamScoreboardsByGroup.values();
	}

	public Objective getObjective() {
		return this.objective;
	}
}