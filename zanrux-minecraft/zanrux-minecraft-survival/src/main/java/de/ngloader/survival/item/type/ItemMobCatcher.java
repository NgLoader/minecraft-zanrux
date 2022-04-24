package de.ngloader.survival.item.type;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

import de.ngloader.core.util.ItemFactory;
import de.ngloader.core.util.NBTUtil;
import de.ngloader.survival.Survival;
import de.ngloader.survival.item.Item;
import de.ngloader.survival.util.NamespacedKeys;
import de.ngloader.synced.database.Database;
import de.ngloader.synced.database.model.survival.MobCatcherModel;
import de.ngloader.synced.util.StringUtil;

public class ItemMobCatcher extends Item implements Listener {

	public static final ItemStack ITEM_MOBCATCHER_SHELL = new ItemFactory(Material.SNOWBALL)
			.setDisplayName("§bMobCatcher Shell")
			.setCustomModelData(1)
			.build();
	public static final ItemStack ITEM_MOBCATCHER_EMPTY = new ItemFactory(Material.SNOWBALL)
			.setDisplayName("§cMobCatcher")
			.setCustomModelData(2)
			.build();
	public static final ItemStack ITEM_MOBCATCHER = new ItemFactory(Material.SNOWBALL)
			.setDisplayName("§cMobCatcher")
			.setCustomModelData(3)
			.build();

	public static final ItemFactory applyLoreEmpty(ItemFactory factory, int uses) {
		return factory
				.addLore("§7Benutzungen übrig§8: §b" + (uses == -1 ? "§cUnbeschrenkt" : "" + uses));
	}

	public static final ItemFactory applyLoreUse(ItemFactory factory, LivingEntity entity, int uses) {
		factory.setDisplayName("§cMobCatcher §7- §b" + StringUtil.justify(entity.getType().name()));
		factory.addLore("§7Gefangen§8: §b" + StringUtil.justify(entity.getType().name()));
		factory.addLore("§7Leben§8: §b" + (int) entity.getHealth() + "§7/§b" + (int) entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		if (entity.getCustomName() != null) {
			factory.addLore("§7Name§8: §b" + entity.getCustomName());
		}
		Collection<PotionEffect> activePotionEffects = entity.getActivePotionEffects();
		if (!activePotionEffects.isEmpty()) {
			factory.addLore(" ").addLore("§7Effecte§8:");
			int count = 0;
			for (PotionEffect potionEffect : activePotionEffects) {
				if (count > 3) {
					break;
				}
				count++;

				int seconds = (potionEffect.getDuration() / 20) % 60;
				int minutes = (potionEffect.getDuration() / (20 * 60)) % 60;
				int hours = (potionEffect.getDuration() / (20 * 60 * 60)) % 24;
				factory.addLore("  §7- §b" + StringUtil.justify(potionEffect.getType().getName()) + " §8(§b" +
						(hours != 0 ? hours + "§7St.§b" : "") +
						(minutes != 0 ? (hours != 0 ? " " : "") + minutes + "§7Min.§b" : "") +
						(seconds != 0 ? (hours != 0 || minutes != 0 ? " " : "") + seconds + "§7Sek.§b" : "") +
						"§8)");
			}
			if (count < activePotionEffects.size()) {
				int diff = activePotionEffects.size() - count;
				factory.addLore("      §7Und §b" + diff + (diff == 1 ? " §7weitern effect" : " §7weiter effecte"));
			}
		}
		factory
			.addLore(" ")
			.addLore("§7Benutzungen übrig§8: §b" + (uses == -1 ? "§cUnbeschränkt" : "" + uses));
		return factory;
	}

	private final Set<EntityType> blacklist = new HashSet<>(Arrays.asList(EntityType.PLAYER, EntityType.ENDER_DRAGON, EntityType.WITHER));
	private final Database database;

	public ItemMobCatcher(Survival core) {
		super(core, ITEM_MOBCATCHER_EMPTY);
		this.database = core.getDatabase();
	}

	@EventHandler
	public void onItemSpawn(ItemSpawnEvent event) {
		ItemStack item = event.getEntity().getItemStack();
		if (this.isItem(ITEM_MOBCATCHER, item) || this.isItem(ITEM_MOBCATCHER_EMPTY, item)) {
			ItemFactory.setFireProof(item, true);
		}
	}

	@EventHandler
	public void onDespawn(ItemDespawnEvent event) {
		ItemStack item = event.getEntity().getItemStack();
		if (this.isItem(ITEM_MOBCATCHER, item)) {
			PersistentDataContainer persistentDataContainer = item.getItemMeta().getPersistentDataContainer();
			Long id = persistentDataContainer.get(NamespacedKeys.ITEM_MOBCATCHER_ID, PersistentDataType.LONG);
			if (id == null) {
				return;
			}

			this.database.transaction().thenAccept(session -> {
				MobCatcherModel model = session.find(MobCatcherModel.class, id);
				if (model != null) {
					session.delete(model);
				}
			});
		}
	}

	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof ThrowableProjectile) {
			ItemStack item = ((ThrowableProjectile) entity).getItem();
			if (this.isItem(ITEM_MOBCATCHER_SHELL, item)) {
				if (((ThrowableProjectile) entity).getShooter() instanceof Player) {
					event.setCancelled(true);
				}

//				if (((ThrowableProjectile) entity).getShooter() instanceof BlockProjectileSource) {
//					BlockProjectileSource source = (BlockProjectileSource) ((ThrowableProjectile) entity).getShooter();
//					BlockState state = source.getBlock().getState();
//
//					if (state instanceof InventoryHolder) {
//						((InventoryHolder) state).getInventory().addItem(new ItemFactory(item).setNumber(1).build());
//					}
//				}
			}
		}
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		if (projectile instanceof ThrowableProjectile) {
			ItemStack item = ((ThrowableProjectile) projectile).getItem();
			if (!item.hasItemMeta()) {
				return;
			}

			PersistentDataContainer persistentDataContainer = item.getItemMeta().getPersistentDataContainer();
			if (this.isItem(ITEM_MOBCATCHER_EMPTY, item)) {
				Entity entity = event.getHitEntity();
				if (entity == null || !(entity instanceof LivingEntity) || this.blacklist.contains(entity.getType())) {
					projectile.getLocation().getWorld().dropItemNaturally(projectile.getLocation(), item);
					return;
				}

				byte[] binaryEntity = NBTUtil.entityToBinary(entity);
				Location location = entity.getLocation().add(0, .2, 0);
				entity.remove();
				projectile.remove();

				this.database.transaction().thenAccept(session -> {
					MobCatcherModel model = new MobCatcherModel(binaryEntity);
					session.save(model);

					int uses = persistentDataContainer.get(NamespacedKeys.ITEM_MOBCATCHER_USE, PersistentDataType.INTEGER);
					int usesLeft = uses != -1 ? uses -1 : -1;

					ItemStack newItem = new ItemFactory(ITEM_MOBCATCHER)
							.handle((factory) -> applyLoreUse(factory, (LivingEntity) entity, usesLeft))
							.setCustomMeta(NamespacedKeys.ITEM_MOBCATCHER_ID, PersistentDataType.LONG, model.getId())
							.setCustomMeta(NamespacedKeys.ITEM_MOBCATCHER_USE, PersistentDataType.INTEGER, usesLeft)
							.build();

					try {
						this.runTask(() -> location.getWorld().dropItemNaturally(location, newItem));
					} catch (Exception e) {
						newItem.setType(Material.AIR);
						throw e;
					}
				}).exceptionally(error -> {
					error.printStackTrace();
					this.runTask(() -> {
						NBTUtil.binaryToEntity(binaryEntity, projectile.getLocation().add(0, .5, 0));
						location.getWorld().dropItemNaturally(location, item);
					});
					return null;
				});
			} else if (this.isItem(ITEM_MOBCATCHER, item)) {
				Long id = persistentDataContainer.get(NamespacedKeys.ITEM_MOBCATCHER_ID, PersistentDataType.LONG);
				if (id == null) {
					return;
				}

				projectile.remove();
				this.database.transaction().thenAccept(session -> {
					MobCatcherModel model = session.find(MobCatcherModel.class, id);

					int uses = persistentDataContainer.get(NamespacedKeys.ITEM_MOBCATCHER_USE, PersistentDataType.INTEGER);
					if (uses != 0) {
						ItemStack newItem = new ItemFactory(ITEM_MOBCATCHER_EMPTY)
								.handle((factory) -> applyLoreEmpty(factory, uses))
								.setCustomMeta(NamespacedKeys.ITEM_MOBCATCHER_USE, PersistentDataType.INTEGER, uses)
								.build();
						this.runTask(() -> projectile.getLocation().getWorld().dropItemNaturally(projectile.getLocation().add(0, .5, 0), newItem));
					}

					if (model == null) {
						item.setType(Material.AIR);
						return;
					}

					session.delete(model);
					this.runTask(() -> NBTUtil.binaryToEntity(model.getNbt(), projectile.getLocation().add(0, 1.5, 0)));
				});
			}
		}
	}
}