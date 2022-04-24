package de.ngloader.survival;

import java.io.IOException;

import org.bukkit.Bukkit;

import de.ngloader.core.MCCore;
import de.ngloader.core.handler.ChatHandler;
import de.ngloader.core.help.HelpSystem;
import de.ngloader.core.inventory.InventorySystem;
import de.ngloader.survival.command.CommandEnderchest;
import de.ngloader.survival.command.CommandPing;
import de.ngloader.survival.command.CommandResourcepack;
import de.ngloader.survival.command.CommandSpawn;
import de.ngloader.survival.command.CommandTreeFeller;
import de.ngloader.survival.command.admin.CommandAdminTool;
import de.ngloader.survival.command.admin.CommandFly;
import de.ngloader.survival.command.admin.CommandGameMode;
import de.ngloader.survival.command.admin.CommandInvsee;
import de.ngloader.survival.command.admin.CommandSay;
import de.ngloader.survival.command.admin.CommandSurvival;
import de.ngloader.survival.command.admin.CommandVanish;
import de.ngloader.survival.command.economy.CommandEconomyBalance;
import de.ngloader.survival.command.help.CommandHelp;
import de.ngloader.survival.command.home.CommandHome;
import de.ngloader.survival.command.home.CommandHomeCreate;
import de.ngloader.survival.command.home.CommandHomeDelete;
import de.ngloader.survival.command.home.CommandHomeList;
import de.ngloader.survival.command.tp.CommandTp;
import de.ngloader.survival.command.tp.CommandTpHere;
import de.ngloader.survival.command.tpa.CommandTpa;
import de.ngloader.survival.command.tpa.CommandTpaccept;
import de.ngloader.survival.command.tpa.CommandTpadeny;
import de.ngloader.survival.command.tpa.CommandTpahere;
import de.ngloader.survival.command.tpa.CommandTpalist;
import de.ngloader.survival.command.warp.CommandWarp;
import de.ngloader.survival.command.warp.CommandWarpCreate;
import de.ngloader.survival.command.warp.CommandWarpCreateAlias;
import de.ngloader.survival.command.warp.CommandWarpDelete;
import de.ngloader.survival.command.warp.CommandWarpDeleteAlias;
import de.ngloader.survival.command.warp.CommandWarpList;
import de.ngloader.survival.crafting.CraftingRecipeManager;
import de.ngloader.survival.enchantment.EnchantmentList;
import de.ngloader.survival.handler.BoatHandler;
import de.ngloader.survival.handler.ChairHandler;
import de.ngloader.survival.handler.DeathMessageHandler;
import de.ngloader.survival.handler.HelpHandler;
import de.ngloader.survival.handler.MOTDHandler;
import de.ngloader.survival.handler.SleepHandler;
import de.ngloader.survival.handler.SurvivalNotificationHandler;
import de.ngloader.survival.handler.VanishHandler;
import de.ngloader.survival.handler.event.EventHandler;
import de.ngloader.survival.handler.home.HomeHandler;
import de.ngloader.survival.handler.tpa.TPAHandler;
import de.ngloader.survival.handler.warp.WarpHandler;
import de.ngloader.survival.item.ItemManager;
import de.ngloader.synced.IHandler;
import de.ngloader.synced.database.Database;
import de.ngloader.synced.database.model.survival.HomeModel;
import de.ngloader.synced.database.model.survival.MobCatcherModel;
import de.ngloader.synced.database.model.survival.SurvivalPlayerModel;

public class Survival extends MCCore {

	public static final String CONFIG_FOLDER = "Zanrux";
	public static final String PREFIX = "§8[§2Zanrux§8] §7";

	private final InventorySystem inventorySystem;
	private final HelpSystem helpSystem;
//	private final ScoreboardHandler scoreboardHandler;
	private final ChatHandler chatHandler;
	private final WarpHandler warpHandler;
	private final HomeHandler homeHandler;
	private final VanishHandler vanishHandler;
//	private final InventoryHandler inventoryHandler;
	private final SurvivalNotificationHandler notificationHandler;
//	private final StorageHandler storageHandler;
	private final EventHandler eventHandler;
	private final TPAHandler tpaHandler;
	private final ChairHandler chairHandler;
	private final SleepHandler sleepHandler;
	private final BoatHandler boatHandler;
	private final MOTDHandler motdHandler;
	private final HelpHandler helpHandler;
	private final DeathMessageHandler deathMessageHandler;

	private final CraftingRecipeManager craftingRecipeManager;
	private final ItemManager itemManager;

	public Survival() {
		Bukkit.setWhitelist(true);
		this.setDatabase(new Database(SurvivalPlayerModel.class, HomeModel.class, MobCatcherModel.class));

		this.inventorySystem = new InventorySystem(this);
		this.helpSystem = new HelpSystem(this, PREFIX);
//		this.scoreboardHandler = new ScoreboardHandler(this);
		this.chatHandler = new ChatHandler(this);
		this.warpHandler = new WarpHandler(this);
		this.homeHandler = new HomeHandler(this);
		this.vanishHandler = new VanishHandler(this);
//		this.inventoryHandler = new InventoryHandler(this);
		this.notificationHandler = new SurvivalNotificationHandler(this);
//		this.storageHandler = new StorageHandler(this);
		this.eventHandler = new EventHandler(this);
		this.tpaHandler = new TPAHandler(this);
		this.chairHandler = new ChairHandler(this, PREFIX);
		this.sleepHandler = new SleepHandler(this, PREFIX);
		this.boatHandler = new BoatHandler(this);
		this.motdHandler = new MOTDHandler(this);
		this.helpHandler = new HelpHandler(this);
		this.deathMessageHandler = new DeathMessageHandler(this);

		this.craftingRecipeManager = new CraftingRecipeManager(this);
		this.itemManager = new ItemManager(this);
	}

	@Override
	public void onLoad() {
		IHandler.getHandlers().forEach(IHandler::init);
	}

	@Override
	public void onEnable() {
		EnchantmentList.init(this);
		IHandler.getHandlers().forEach(IHandler::enable);

		this.registerCommands();

		Bukkit.getConsoleSender().sendMessage(Survival.PREFIX + "§2Enabled§8!");

		Bukkit.setWhitelist(false);
	}

	@Override
	public void onDisable() {
		Bukkit.setWhitelist(true);

		IHandler.destroy();
		try {
			this.getDatabase().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bukkit.getScheduler().cancelTasks(this);

		Bukkit.getConsoleSender().sendMessage(Survival.PREFIX + "§4Disabled§8!");
	}

	private void registerCommands() {
		getCommand("ping").setExecutor(new CommandPing());
		getCommand("spawn").setExecutor(new CommandSpawn());
		getCommand("treefeller").setExecutor(new CommandTreeFeller(this));
		getCommand("enderchest").setExecutor(new CommandEnderchest());

		getCommand("survival").setExecutor(new CommandSurvival(this));
		getCommand("gamemode").setExecutor(new CommandGameMode());
		getCommand("admintool").setExecutor(new CommandAdminTool());
		getCommand("invsee").setExecutor(new CommandInvsee());
		getCommand("vanish").setExecutor(new CommandVanish(this));
		getCommand("say").setExecutor(new CommandSay());
		getCommand("fly").setExecutor(new CommandFly());

		getCommand("home").setExecutor(new CommandHome(this));
		getCommand("homelist").setExecutor(new CommandHomeList(this));
		getCommand("homecreate").setExecutor(new CommandHomeCreate(this));
		getCommand("homedelete").setExecutor(new CommandHomeDelete(this));

		getCommand("warp").setExecutor(new CommandWarp(this));
		getCommand("warpcreate").setExecutor(new CommandWarpCreate(this));
		getCommand("warpdelete").setExecutor(new CommandWarpDelete(this));
		getCommand("warplist").setExecutor(new CommandWarpList(this));
		getCommand("warpcreatealias").setExecutor(new CommandWarpCreateAlias(this));
		getCommand("warpdeletealias").setExecutor(new CommandWarpDeleteAlias(this));

		getCommand("help").setExecutor(new CommandHelp(this));

		getCommand("balance").setExecutor(new CommandEconomyBalance(this));

		getCommand("tp").setExecutor(new CommandTp());
		getCommand("tphere").setExecutor(new CommandTpHere());
		getCommand("tpa").setExecutor(new CommandTpa(this));
		getCommand("tpaccept").setExecutor(new CommandTpaccept(this));
		getCommand("tpadeny").setExecutor(new CommandTpadeny(this));
		getCommand("tpahere").setExecutor(new CommandTpahere(this));
		getCommand("tpalist").setExecutor(new CommandTpalist(this));

		getCommand("resourcepack").setExecutor(new CommandResourcepack());
	}

	public InventorySystem getInventorySystem() {
		return this.inventorySystem;
	}

	public HelpSystem getHelpSystem() {
		return this.helpSystem;
	}

//	public ScoreboardHandler getScoreboardHandler() {
//		return this.scoreboardHandler;
//	}

	public ChatHandler getChatHandler() {
		return this.chatHandler;
	}

	public WarpHandler getWarpHandler() {
		return this.warpHandler;
	}

	public HomeHandler getHomeHandler() {
		return this.homeHandler;
	}

	public VanishHandler getVanishHandler() {
		return this.vanishHandler;
	}

//	public InventoryHandler getInventoryHandler() {
//		return this.inventoryHandler;
//	}

	public SurvivalNotificationHandler getNotificationHandler() {
		return this.notificationHandler;
	}

//	public StorageHandler getStorageHandler() {
//		return this.storageHandler;
//	}

	public EventHandler getEventHandler() {
		return this.eventHandler;
	}

	public TPAHandler getTpaHandler() {
		return this.tpaHandler;
	}

	public ChairHandler getChairHandler() {
		return this.chairHandler;
	}

	public SleepHandler getSleepHandler() {
		return this.sleepHandler;
	}

	public BoatHandler getBoatHandler() {
		return this.boatHandler;
	}

	public MOTDHandler getMotdHandler() {
		return this.motdHandler;
	}

	public HelpHandler getHelpHandler() {
		return this.helpHandler;
	}

	public DeathMessageHandler getDeathMessageHandler() {
		return this.deathMessageHandler;
	}

	public CraftingRecipeManager getCraftingRecipeManager() {
		return this.craftingRecipeManager;
	}

	public ItemManager getItemManager() {
		return this.itemManager;
	}
}