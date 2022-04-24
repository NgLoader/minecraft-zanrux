package de.ngloader.survival.handler.event;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.ngloader.survival.Survival;
import de.ngloader.survival.event.bukkit.AsyncPlayerChatEventListener;
import de.ngloader.survival.event.bukkit.EntityDamageEventListener;
import de.ngloader.survival.event.bukkit.EntityPickupItemEventListener;
import de.ngloader.survival.event.bukkit.FoodLevelChangeEventListener;
import de.ngloader.survival.event.bukkit.PlayerJoinEventListener;
import de.ngloader.survival.event.bukkit.PlayerQuitEventListener;
import de.ngloader.survival.event.luckperms.GroupDataRecalculateEventListener;
import de.ngloader.survival.event.luckperms.UserDataRecalculateEventListener;
import de.ngloader.survival.event.luckperms.UserPromoteEventListener;
import de.ngloader.synced.IHandler;

public class EventHandler extends IHandler<Survival> {

	private final List<EventListener> events = new LinkedList<EventListener>();

	public EventHandler(Survival core) {
		super(core);
	}

	@Override
	public void onInit() {
		// LuckPerms
		this.events.add(new UserPromoteEventListener(this.getCore()));
		this.events.add(new UserDataRecalculateEventListener(this.getCore()));
		this.events.add(new GroupDataRecalculateEventListener(this.getCore()));

		// Bukkit
		this.events.add(new AsyncPlayerChatEventListener(this.getCore()));
		this.events.add(new PlayerJoinEventListener(this.getCore()));
		this.events.add(new PlayerQuitEventListener(this.getCore()));
		this.events.add(new EntityDamageEventListener(this.getCore()));
		this.events.add(new EntityPickupItemEventListener(this.getCore()));
		this.events.add(new FoodLevelChangeEventListener(this.getCore()));

		this.events.stream().forEach(EventListener::init);
	}

	@Override
	public void onEnable() {
		this.events.stream().filter(event -> !event.isEnabled()).forEach(EventListener::enable);
	}

	@Override
	public void onDisable() {
		this.events.stream().filter(EventListener::isEnabled).forEach(EventListener::disable);
	}

	public void add(EventListener listener) {
		this.events.add(listener);

		if (this.isEnabled()) {
			listener.onInit();
			listener.onEnable();
		}
	}

	public List<EventListener> getEvents() {
		return Collections.unmodifiableList(this.events);
	}
}
