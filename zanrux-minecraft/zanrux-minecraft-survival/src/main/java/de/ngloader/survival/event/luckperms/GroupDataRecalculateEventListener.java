package de.ngloader.survival.event.luckperms;

import java.util.function.Consumer;

import org.bukkit.Bukkit;

import de.ngloader.core.handler.ChatHandler;
import de.ngloader.survival.Survival;
import de.ngloader.survival.handler.event.EventListener;
import net.luckperms.api.event.EventSubscription;
import net.luckperms.api.event.group.GroupDataRecalculateEvent;

public class GroupDataRecalculateEventListener extends EventListener implements Consumer<GroupDataRecalculateEvent> {

//	private ScoreboardHandler scoreboardHandler;
	private ChatHandler chatHandler;

	private EventSubscription<GroupDataRecalculateEvent> subscription;

	public GroupDataRecalculateEventListener(Survival core) {
		super(core);
	}

	@Override
	public void onInit() {
//		this.scoreboardHandler = this.getCore().getScoreboardHandler();
		this.chatHandler = this.getCore().getChatHandler();
	}

	@Override
	public void onEnable() {
//		this.subscription = LuckPermsProvider.get().getEventBus().subscribe(GroupDataRecalculateEvent.class, this);
	}

	@Override
	public void onDisable() {
		if (this.subscription != null) {
			this.subscription.close();
		}
	}

	@Override
	public void accept(GroupDataRecalculateEvent event) {
		Bukkit.getScheduler().runTask(this.core, () -> {
//			this.scoreboardHandler.updateGroups();
			this.chatHandler.updateGroups();
		});
	}
}