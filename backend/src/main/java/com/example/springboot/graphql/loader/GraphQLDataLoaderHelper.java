package com.example.springboot.graphql.loader;

import org.dataloader.DataLoader;

import graphql.schema.DataFetchingEnvironment;

public class GraphQLDataLoaderHelper {
	public static <K, V> DataLoader<K, V> getDataLoader(Class<? extends GraphQLDataLoader<K, V>> loaderClass, DataFetchingEnvironment environment) {
		String dataLoaderName = GraphQLDataLoaderScanner.getDataLoaderName(loaderClass);
		if (dataLoaderName == null) {
			throw new NullPointerException("Unable to find data loader for class: " + loaderClass);
		}
		return environment.getDataLoader(dataLoaderName);
	}
}
