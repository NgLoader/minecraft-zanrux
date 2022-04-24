package de.ngloader.core.util;

import java.util.Collection;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import com.mojang.authlib.GameProfile;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket.PlayerUpdate;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.GameType;

public class PlayerUtil {

	public static final String SERVER_VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(23);

	public static int getPlayerPing(Player player) {
		return ((CraftPlayer) player).getHandle().latency;
	}

	public static void sendPacket(Collection<? extends Player> players, Stream<Packet<?>> packets) {
		for (Player player : players) {
			packets.forEach(packet -> PlayerUtil.sendPacket(player, packet));
		}
	}

	public static void sendPacket(Collection<? extends Player> players, Packet<?>... packets) {
		for (Player player : players) {
			PlayerUtil.sendPacket(player, packets);
		}
	}

	public static void sendPacket(Player[] players, Packet<?>... packets) {
		for (Player player : players) {
			PlayerUtil.sendPacket(player, packets);
		}
	}

	public static void sendPacket(Player player, Packet<?>... packets) {
		ServerGamePacketListenerImpl playerConnection = ((CraftPlayer) player).getHandle().connection;

		for (Packet<?> packet : packets) {
			playerConnection.send(packet);
		}
	}

	public static ClientboundPlayerInfoPacket addToTabList(Player player, String displayName) {
		ClientboundPlayerInfoPacket packet = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER);
		Team team = player.getScoreboard().getEntryTeam(player.getName());

		packet.getEntries().add(new PlayerUpdate(
				PlayerUtil.getGameProfile(player),
				0,
				GameType.SURVIVAL,
				new TextComponent((team != null ? team.getPrefix() + team.getColor() : "") + displayName)));
		return packet;
	}

	public static ClientboundPlayerInfoPacket removeFromTabList(Player player) {
		ClientboundPlayerInfoPacket packet = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER);
		packet.getEntries().add(new PlayerUpdate(PlayerUtil.getGameProfile(player), 0, null, null));
		return packet;
	}

	public static CraftPlayer getCraftPlayer(Player player) {
		return ((CraftPlayer) player);
	}

	public static GameProfile getGameProfile(CraftPlayer player) {
		return player.getProfile();
	}

	public static GameProfile getGameProfile(Player player) {
		return ((CraftPlayer) player).getProfile();
	}

	public static ServerGamePacketListenerImpl getPlayerConnection(CraftPlayer player) {
		return player.getHandle().connection;
	}

	public static ServerGamePacketListenerImpl getPlayerConnection(Player player) {
		return ((CraftPlayer) player).getHandle().connection;
	}
}