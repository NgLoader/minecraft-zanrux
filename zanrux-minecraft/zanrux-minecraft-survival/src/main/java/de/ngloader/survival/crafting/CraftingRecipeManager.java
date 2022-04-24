package de.ngloader.survival.crafting;

import java.util.LinkedHashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import de.ngloader.survival.Survival;
import de.ngloader.survival.crafting.recipe.ClampRecipe;
import de.ngloader.survival.crafting.recipe.ConcreteRecipe;
import de.ngloader.survival.crafting.recipe.InvisibleItemFrameRecipe;
import de.ngloader.survival.crafting.recipe.LeatherRecipe;
import de.ngloader.survival.crafting.recipe.MobileCraftingTableRecipe;
import de.ngloader.survival.crafting.recipe.SaddleRecipe;
import de.ngloader.survival.crafting.recipe.SnowBlockRecipe;
import de.ngloader.survival.crafting.recipe.TerracottaRecipe;
import de.ngloader.survival.crafting.recipe.WebRecipe;
import de.ngloader.survival.crafting.recipe.WoolRecipe;
import de.ngloader.survival.crafting.recipe.WoolRecolorRecipe;
import de.ngloader.survival.crafting.recipe.WoolToStringRecipe;
import de.ngloader.survival.crafting.recipe.backpack.BigBackpackRecipe;
import de.ngloader.survival.crafting.recipe.backpack.MiddleBackpackRecipe;
import de.ngloader.survival.crafting.recipe.backpack.SmallBackpackRecipe;
import de.ngloader.survival.crafting.recipe.mobcatcher.MobCatcherMiddleRecipe;
import de.ngloader.survival.crafting.recipe.mobcatcher.MobCatcherShellRecipe;
import de.ngloader.survival.crafting.recipe.mobcatcher.MobCatcherSingleRecipe;
import de.ngloader.survival.crafting.recipe.mobcatcher.MobCatcherUnlimitedRecipe;
import de.ngloader.synced.IHandler;

public class CraftingRecipeManager extends IHandler<Survival> implements Listener {

	private final Set<IRecipe> recipes = new LinkedHashSet<>();

	public CraftingRecipeManager(Survival core) {
		super(core);
	}

	@Override
	public void onInit() {
		this.addRecipe(new LeatherRecipe());

		this.addRecipe(new InvisibleItemFrameRecipe(this.core));

		this.addRecipe(new MobCatcherShellRecipe());
		this.addRecipe(new MobCatcherSingleRecipe());
		this.addRecipe(new MobCatcherMiddleRecipe());
		this.addRecipe(new MobCatcherUnlimitedRecipe());

		this.addRecipe(new ClampRecipe());
		this.addRecipe(new SaddleRecipe());

		this.addRecipe(new SmallBackpackRecipe());
		this.addRecipe(new MiddleBackpackRecipe());
		this.addRecipe(new BigBackpackRecipe());

		this.addRecipe(new WebRecipe());
		this.addRecipe(new WoolRecipe());
		this.addRecipe(new WoolToStringRecipe());
		this.addRecipe(new SnowBlockRecipe());
		this.addRecipe(new MobileCraftingTableRecipe());

		for (Material material : Material.values()) {
			String name = material.name();
			if (name.endsWith("_DYE")) {
				Material wool = Material.valueOf(name.replace("_DYE", "") + "_WOOL");
				Bukkit.getRecipesFor(new ItemStack(wool)).forEach(recipe -> {
					if (recipe instanceof ShapedRecipe) {
						Bukkit.removeRecipe(((ShapedRecipe) recipe).getKey());
					} else if (recipe instanceof ShapelessRecipe) {
						Bukkit.removeRecipe(((ShapelessRecipe) recipe).getKey());
					}
				});
				this.addRecipe(new WoolRecolorRecipe(material, wool));

				this.addRecipe(new ConcreteRecipe(Material.valueOf(name.replace("_DYE", "_CONCRETE")), material));
				this.addRecipe(new TerracottaRecipe(Material.valueOf(name.replace("_DYE", "_TERRACOTTA")), material));
			}
		}
	}

	public void addRecipe(IRecipe recipe) {
		this.recipes.add(recipe);
	}

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this.core);

		for (IRecipe recipe : this.recipes) {
			try {
				Bukkit.addRecipe(recipe);

				if (recipe instanceof Listener) {
					Bukkit.getPluginManager().registerEvents((Listener) recipe, this.core);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDisable() {
		this.recipes.forEach(recipe -> Bukkit.removeRecipe(recipe.getNamespaceKey()));
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		for (IRecipe recipe : this.recipes) {
			if (recipe.isDefault(player)) {
				player.discoverRecipe(recipe.getNamespaceKey());
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onCraftItem(CraftItemEvent event) {
		IRecipe recipe = this.getCustomRecipe(event.getRecipe());
		if (recipe != null) {
			recipe.onCraft(event.getWhoClicked(), event);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onPrepareItemCraft(PrepareItemCraftEvent event) {
		IRecipe recipe = this.getCustomRecipe(event.getRecipe());
		if (recipe != null) {
			recipe.onPrepareCraft(event.getViewers(), event);
		}
	}

	public IRecipe getCustomRecipe(Recipe recipe) {
		if (recipe != null) {
			ItemStack result = recipe.getResult();
			for (IRecipe customRecipe : this.recipes) {
				if (customRecipe.getResult().equals(result)) {
					return customRecipe;
				}
			}
		}
		return null;
	}
}
