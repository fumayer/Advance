package com.quduquxie.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1905193699692219295L;
	private int maxCapacity;
	private Lock lock = new ReentrantLock();

	public BlockingLinkedHashMap(int maxCapacity) {
		super(maxCapacity);
		this.maxCapacity = maxCapacity;
	}

	@Override
	protected boolean removeEldestEntry(Entry<K, V> eldest) {
		return size() > maxCapacity;
	}

	@Override
	public V get(Object key) {
		try {
			lock.lock();
			return super.get(key);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public V put(K key, V value) {
		try {
			lock.lock();
			return super.put(key, value);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean containsKey(Object key) {
		try {
			lock.lock();
			return super.containsKey(key);
		} finally {
			lock.unlock();
		}
	}

	public int size() {
		try {
			lock.lock();
			return super.size();
		} finally {
			lock.unlock();
		}
	}

	public void clear() {
		try {
			lock.lock();
			super.clear();
		} finally {
			lock.unlock();
		}
	}

	public Collection<Entry<K, V>> getAll() {
		try {
			lock.lock();
			return new ArrayList<>(super.entrySet());
		} finally {
			lock.unlock();
		}
	}

}
