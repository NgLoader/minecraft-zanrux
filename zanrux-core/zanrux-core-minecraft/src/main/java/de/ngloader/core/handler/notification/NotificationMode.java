package de.ngloader.core.handler.notification;

import de.ngloader.core.MCCore;

public abstract class NotificationMode<T extends MCCore> {

	public abstract void onInit();
	public abstract void onEnable();
	public abstract void onDisable();

	public abstract boolean tick();
	public abstract void display(String text, long duration);

	protected final T core;

	public NotificationMode(T core) {
		this.core = core;
	}

	public T getCore() {
		return this.core;
	}
}