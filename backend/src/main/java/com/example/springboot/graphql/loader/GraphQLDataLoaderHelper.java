package com.example.springboot.graphql.loader;

import org.dataloader.DataLoader;

import graphql.schema.DataFetchingEnvironment;

public class GraphQLDataLoaderHelper {
	public static <K, V> DataLoader<K, V> getDataLoader(Class<? extends GraphQLDataLoader<K, V>> loaderClass, DataFetchingEnvironment environment) {
		return environment.getDataLoader(GraphQLDataLoaderLoader.getDataLoaderName(loaderClass));
	}
}
