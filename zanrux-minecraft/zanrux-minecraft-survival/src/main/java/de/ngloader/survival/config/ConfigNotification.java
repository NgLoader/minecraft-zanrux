package de.ngloader.survival.config;

import java.util.Arrays;
import java.util.List;

import de.ngloader.survival.Survival;
import de.ngloader.synced.config.Config;

@Config(path = Survival.CONFIG_FOLDER, name = "notification")
public class ConfigNotification {

	public long delay = 1000 * 60 * 30;
	public List<String> messages = Arrays.asList("&aHier könnte ihre werbung stehen");
}
