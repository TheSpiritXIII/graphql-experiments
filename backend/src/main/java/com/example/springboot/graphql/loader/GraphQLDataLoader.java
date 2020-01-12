package com.example.springboot.graphql.loader;

import java.util.function.Supplier;

import org.dataloader.DataLoader;

public interface GraphQLDataLoader<K, V> extends Supplier<DataLoader<K, V>> {
}
