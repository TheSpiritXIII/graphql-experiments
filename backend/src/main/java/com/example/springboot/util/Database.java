package com.example.springboot.util;

import java.util.LinkedList;
import java.util.List;

import com.github.javafaker.Faker;

public abstract class Database<TData> {
	private long id = 0;
	private final List<TData> dataList = new LinkedList<>();

	public Database() {
		Faker faker = new Faker();
		for (int i = 0; i < 5; i += 1) {
			add(generate(faker));
		}
	}

	public List<TData> getAll() {
		return this.dataList;
	}

	public synchronized void add(TData data) {
		setId(data, this.id);
		dataList.add(data);
		this.id += 1;
	}

	protected abstract long getId(TData data);
	protected abstract void setId(TData data, long id);
	protected abstract TData generate(Faker faker);
}
