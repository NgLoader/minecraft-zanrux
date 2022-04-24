package de.ngloader.survival.config;

import de.ngloader.survival.Survival;
import de.ngloader.synced.config.Config;

@Config(path = Survival.CONFIG_FOLDER, name = "sleep")
public class ConfigSleep {

	public int sleepPercent = 60;
}