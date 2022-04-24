package de.ngloader.survival.item.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import de.ngloader.core.util.ItemFactory;
import de.ngloader.survival.Survival;
import de.ngloader.survival.enchantment.EnchantmentList;
import de.ngloader.survival.item.Item;
import de.ngloader.survival.util.InventoryUtil;

public class ItemTreefeller extends Item implements Listener {

	public static final ItemStack ITEM = new ItemFactory(Material.DIAMOND_AXE).addCustomEnchant(EnchantmentList.TREE_FELLER, 1).build();

	private static final Random RANDOM = new Random();
	private static final Set<Block> EMPTY_BLOCK_LIST = Collections.newSetFromMap(new ConcurrentHashMap<>());

	private final Map<Runnable, Integer> runnables = new ConcurrentHashMap<Runnable, Integer>();

	private final EnumSet<BlockFace> blockFaceAround = EnumSet.of(
			BlockFace.SELF, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST,
			BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST);

	private final EnumSet<Material> logTypes = EnumSet.noneOf(Material.class);
	private final Map<Material, Material> leaveTypes = new ConcurrentHashMap<Material, Material>();
	private final EnumSet<Material> axeMaterials = EnumSet.noneOf(Material.class);

	private final Map<Player, Long> countdown = new ConcurrentHashMap<>();

	public ItemTreefeller(Survival core) {
		super(core, ITEM);

		for (Material material : Material.values()) {
			String name = material.name();
			if (!name.startsWith("LEGACY_") && !name.startsWith("STRIPPED_")) {
				if (name.endsWith("_LOG")) {
					this.logTypes.add(material);
					this.leaveTypes.put(material, Material.valueOf(name.replace("_LOG", "") + "_LEAVES"));
				}
			}
			if (name.endsWith("_AXE")) {
				this.axeMaterials.add(material);
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Material blockType = block.getType();
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if (this.logTypes.contains(blockType)
				&& this.axeMaterials.contains(item.getType())
//				&& item.hasItemMeta()
//				&& item.getItemMeta().hasEnchant(EnchantmentList.TREE_FELLER)
				&& item.getItemMeta() instanceof Damageable
				&& !player.isSneaking()
				&& player.getGameMode() == GameMode.SURVIVAL) {

			long countdown = this.countdown.getOrDefault(event.getPlayer(), 0L);
			if (System.currentTimeMillis() < countdown) {
				player.sendMessage(Survival.PREFIX + "Bitte warte noch einen augenblick§8.");
				return;
			}

			Damageable itemMeta = (Damageable) item.getItemMeta();
			int itemDamage = itemMeta.getDamage();
			int canBreak = item.getType().getMaxDurability() - itemDamage - 1;

			if (canBreak > 1) {
//				Set<Block> logs = this.getAllLogs(new HashSet<>(), block, (item.getItemMeta().getEnchantLevel(EnchantmentList.TREE_FELLER) * 5) - 1);
				Set<Block> logs = this.getAllLogs(new HashSet<>(), block, 1000);
				Set<Block> leaves = this.getAllLeaves(logs);
				if (logs.isEmpty() || leaves.isEmpty()) {
					return;
				}

				int applyItemDamage = logs.size();

				int unbreakingLevel = item.getEnchantmentLevel(Enchantment.DURABILITY);
				if (unbreakingLevel > 0) {
					int chance = 100 / (unbreakingLevel + 1);
					for (int i = 0; i < logs.size(); i++) {
						if (RANDOM.nextInt(100) > chance) {
							applyItemDamage--;
						}
					}
				}

				boolean destoryLeaves = true;
				if (canBreak > applyItemDamage) {
					itemMeta.setDamage(itemDamage + applyItemDamage);
				} else {
					int removeFromBreak = applyItemDamage - canBreak;
					
					for (Iterator<Block> iterator = logs.iterator(); iterator.hasNext();) {
						if (removeFromBreak > 0) {
							removeFromBreak--;
							iterator.remove();
						} else {
							break;
						}
					}

					itemMeta.setDamage(itemDamage + canBreak);
					destoryLeaves = false;
				}
				item.setItemMeta((ItemMeta) itemMeta);

				Runnable runnable = this.buildSchedule(block, blockType, player, item, logs, destoryLeaves ? leaves : EMPTY_BLOCK_LIST);
				this.runnables.put(runnable, Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getCore(), runnable, 2, 2));
				this.countdown.put(event.getPlayer(), System.currentTimeMillis() + 2000);

				for (Material material : axeMaterials) {
					player.setCooldown(material, 20 * 2);
				}
			}
		}
	}

	private Runnable buildSchedule(Block block, Material type, Player player, ItemStack item, Set<Block> logs, Set<Block> leaves) {
		List<Block> logSorted = logs.stream().sorted(Comparator.comparingInt(log -> log.getY())).collect(Collectors.toList());
		List<Block> leavesShuffled = new ArrayList<>(leaves);
		Collections.shuffle(leavesShuffled);

		return new Runnable() {

			@Override
			public void run() {
				try {
					Block block;
					if (logSorted.size() > 0) {
						block = logSorted.remove(0);
					} else if (leavesShuffled.size() > 0) {
						block = leavesShuffled.remove(0);
					} else {
						Bukkit.getScheduler().cancelTask(runnables.get(this));
						return;
					}

					if (ItemTreefeller.this.logTypes.contains(block.getType()) || ItemTreefeller.this.leaveTypes.get(type) == block.getType()) {
						block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(.5, .5, .5), 15, 0.5, 0.5, 0.5, block.getBlockData());

						if (ItemTreefeller.this.logTypes.contains(block.getType()) && EnchantmentList.TELEKINESIS.hasEnchant(item)) {
							InventoryUtil.giveItem(player, block.getDrops(item, player).toArray(ItemStack[]::new), true, block.getLocation().add(.5, .5, .5));
							block.setType(Material.AIR, true);
						} else {
							block.breakNaturally();
						}
					}
				} catch(Exception e) {
					e.printStackTrace();
					Bukkit.getScheduler().cancelTask(runnables.get(this));
				}
			}
		};
	}

	public Set<Block> getAllLogs(Set<Block> blocks, Block block, int logLimit) {
		if (logLimit < blocks.size())
			return blocks;

		for (int i = 0; i < 2; i++) {
			Block blockUp = block.getRelative(0, i, 0);

			for (BlockFace blockFace : this.blockFaceAround) {
				Block blockCheck = blockUp.getRelative(blockFace);

				if (blockCheck.getType() == block.getType() && !blocks.contains(blockCheck)) {
					blocks.add(blockCheck);
					this.getAllLogs(blocks, blockCheck, logLimit);
				}
			}
		}

		return blocks;
	}

	public Set<Block> getAllLeaves(Set<Block> blocks) {
		Set<Block> leaves = new HashSet<Block>();
		Material leaveMaterial = this.leaveTypes.get(blocks.iterator().next().getType());
		int startLogY = blocks.stream().map(block -> block.getY()).sorted().findFirst().get();
		int highestLogY = blocks.stream().map(block -> block.getY()).sorted(Comparator.reverseOrder()).findFirst().get();

		for (Block block : blocks) {
			int currentlyLogY = block.getY();
			int distance;

			switch (leaveMaterial) {
				case OAK_LEAVES:
					if (currentlyLogY - startLogY > 5)
						distance = 5;
					else
						distance = 3;
					break;

				case BIRCH_LEAVES:
					distance = 3;
				break;

				case SPRUCE_LEAVES:
					distance = 4;
					break;

				case DARK_OAK_LEAVES:
				case ACACIA_LEAVES:
					if (highestLogY - currentlyLogY < 1)
						distance = 4;
					else
						distance = 3;
					break;

				case JUNGLE_LEAVES:
					if (highestLogY - currentlyLogY < 4)
						distance = 6;
					else
						distance = 4;
					break;

				default:
					distance = 3;
					break;
			}

			this.getLeavesForLog(new HashSet<Block>(), leaveMaterial, block.getLocation(), block, distance).stream()
				.filter(leave -> !leaves.contains(leave))
				.forEach(leave -> leaves.add(leave));
		}

		return leaves;
	}

	public Set<Block> getLeavesForLog(Set<Block> leaves, Material leaveMaterial, Location logLocation, Block lastLeave, int distance) {
		for (int i = -1; i < 2; i++) {
			Block blockUp = lastLeave.getRelative(0, i, 0);

			for (BlockFace blockFace : this.blockFaceAround) {
				Block blockCheck = blockUp.getRelative(blockFace);

				if (blockCheck.getType() == leaveMaterial && !leaves.contains(blockCheck) && blockCheck.getLocation().distance(logLocation) < distance) {
					leaves.add(blockCheck);
					this.getLeavesForLog(leaves, leaveMaterial, logLocation, blockCheck, distance);
				}
			}
		}

		return leaves;
	}
}