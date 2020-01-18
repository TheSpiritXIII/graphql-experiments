package com.example.springboot.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.javafaker.Faker;

public abstract class Database<TData> {
	private final static long seed = 0;

	private long id = 0;
	private final List<TData> dataList = new LinkedList<>();

	public Database() {
		Faker faker = new Faker(new Random(seed));
		for (int i = 0; i < 5; i += 1) {
			add(generate(faker));
		}
	}

	public List<TData> getAll() {
		return this.dataList;
	}

	public List<TData> getByIdBatch(List<Long> idList) {
		return findByBatch(idList.stream().map((id) -> {
			return getIdPredicate(id);
		}).collect(Collectors.toList()));
	}

	public TData getById(long id) {
		return findBy(getIdPredicate(id));
	}

	public TData findBy(Predicate<? super TData> predicate) {
		return this.dataList.stream().filter(predicate).findFirst().orElse(null);
	}

	public List<TData> findByBatch(List<Predicate<? super TData>> predicateList) {
		return predicateList.stream().map((predicate) -> {
			return findBy(predicate);
		}).collect(Collectors.toList());
	}

	public synchronized void add(TData data) {
		setId(data, this.id);
		dataList.add(data);
		this.id += 1;
	}

	protected abstract long getId(TData data);
	protected abstract void setId(TData data, long id);
	protected abstract TData generate(Faker faker);

	private Predicate<? super TData> getIdPredicate(long id) {
		return (data) -> {
			return getId(data) == id;
		};
	}
}
