package com.example.springboot.graphql.loader;

import java.util.HashMap;
import java.util.Map;

import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public class GraphQLDataLoaderLoader {
	private static final Map<Class<?>, String> DATA_LOADER_NAME_CACHE = new HashMap<>();
	private final DataLoaderRegistry dataLoaderRegistry;

	public GraphQLDataLoaderLoader() throws Exception {
		this(new ClassGraph().enableAnnotationInfo());
	}
	
	public GraphQLDataLoaderLoader(ClassGraph classGraph) throws Exception {
		this.dataLoaderRegistry = new DataLoaderRegistry();
		try (ScanResult scanResult = classGraph.scan()) {
			final String annotationName = GraphQLDataLoaderRegister.class.getName();
			ClassInfoList classInfoList = scanResult.getClassesWithAnnotation(annotationName);
			for (ClassInfo classInfo : classInfoList) {
				Class<?> classType = classInfo.loadClass();
				GraphQLDataLoaderRegister register = classType.getAnnotation(GraphQLDataLoaderRegister.class);

				Object instance = classType.getConstructor().newInstance();
				if (!(instance instanceof GraphQLDataLoader<?, ?>)) {
					throw new Exception("Class `" + register.name() + "` must be GraphQLDataLoader");
				}
				DataLoader<?, ?> dataLoader = ((GraphQLDataLoader<?, ?>) instance).get();
				this.dataLoaderRegistry.register(register.name(), dataLoader);
				DATA_LOADER_NAME_CACHE.put(classType, register.name());
			}
		}
	}

	public DataLoaderRegistry getDataLoaderRegistry() {
		return this.dataLoaderRegistry;
	}

	public static String getDataLoaderName(Class<? extends GraphQLDataLoader<?, ?>> loaderClass) {
		return DATA_LOADER_NAME_CACHE.computeIfAbsent(loaderClass, (keyClass) -> {
			return keyClass.getAnnotation(GraphQLDataLoaderRegister.class).name();
		});
	}
}
