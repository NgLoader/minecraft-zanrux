package de.ngloader.survival.config;

import java.util.Arrays;
import java.util.List;

import org.bukkit.block.BlockFace;

import de.ngloader.survival.Survival;
import de.ngloader.synced.config.Config;

@Config(path = Survival.CONFIG_FOLDER, name = "chair")
public class ConfigChair {

	public long delay = 500;

	public boolean allowSneaking = false;
	public boolean allowMainHand = false;
	public boolean allowOffHand = true;

	public List<Chair> chairs = Arrays.asList(
			new Chair("_STAIRS", .5, BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH),
			new Chair("_CARPET", 0.85));

	public class Chair {
		public String suffix;
		public double height;
		public BlockFace[] blockFaces;

		public Chair(String suffix, double height, BlockFace... blockFaces) {
			this.suffix = suffix;
			this.height = height;
			this.blockFaces = blockFaces;
		}
	}
}