package de.ngloader.survival.handler;

import org.bukkit.ChatColor;

import de.ngloader.core.handler.notification.NotificationHandler;
import de.ngloader.survival.Survival;
import de.ngloader.survival.config.ConfigNotification;
import de.ngloader.synced.config.ConfigService;

public class SurvivalNotificationHandler extends NotificationHandler<Survival> {

	public SurvivalNotificationHandler(Survival core) {
		super(core);
	}

	@Override
	public void onEnable() {
		ConfigNotification config = ConfigService.getConfig(ConfigNotification.class);
		config.messages.forEach(message -> this.messages.add(ChatColor.translateAlternateColorCodes('&', message)));
		this.delay = config.delay;

		super.onEnable();
	}
}