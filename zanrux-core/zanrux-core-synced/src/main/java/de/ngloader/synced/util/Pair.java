package de.ngloader.synced.util;

import java.util.Map;

public class Pair<K, V> implements Map.Entry<K, V> {

	private final K key;

	private V value;

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public K getKey() {
		return this.key;
	}

	@Override
	public V getValue() {
		return this.value;
	}

	@Override
	public V setValue(V value) {
		V old = this.value;
		this.value = value;
		return old;
	}
}