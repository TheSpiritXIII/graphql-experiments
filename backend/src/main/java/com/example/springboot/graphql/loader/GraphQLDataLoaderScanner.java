package com.example.springboot.graphql.loader;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public class GraphQLDataLoaderScanner {
	private static final Map<Class<?>, String> DATA_LOADER_NAME_CACHE = new HashMap<>();
	private final DataLoaderRegistry dataLoaderRegistry;

	public GraphQLDataLoaderScanner() throws Exception {
		this(new ClassGraph().enableAnnotationInfo());
	}
	
	public GraphQLDataLoaderScanner(ClassGraph classGraph) throws Exception {
		this.dataLoaderRegistry = new DataLoaderRegistry();
		try (ScanResult scanResult = classGraph.scan()) {
			final String annotationName = GraphQLDataLoaderRegister.class.getName();
			final ClassInfoList classInfoList = scanResult.getClassesWithAnnotation(annotationName);
			for (ClassInfo classInfo : classInfoList) {
				final Class<?> classType = classInfo.loadClass();
				final GraphQLDataLoaderRegister register = classType.getAnnotation(GraphQLDataLoaderRegister.class);

				final String dataLoaderName;
				if (!register.name().isEmpty()) {
					dataLoaderName = register.name();
				} else {
					dataLoaderName = classType.getCanonicalName();
				}

				final Object instance = classType.getConstructor().newInstance();
				if (!(instance instanceof GraphQLDataLoader<?, ?>)) {
					final String template = "Class `{0}` must be GraphQLDataLoader";
					throw new Exception(MessageFormat.format(template, classType));
				}
				final DataLoader<?, ?> dataLoader = ((GraphQLDataLoader<?, ?>) instance).get();
				if (dataLoader == null) {
					final String template = "Class `{0}` missing data loader";
					throw new NullPointerException(MessageFormat.format(template, classType));
				}
				this.dataLoaderRegistry.register(dataLoaderName, dataLoader);
				String name = DATA_LOADER_NAME_CACHE.put(classType, dataLoaderName);
				if (name != null) {
					final String template = "Class `{0}` registered twice ({1} vs {2})";
					throw new Exception(MessageFormat.format(template, classType, name, dataLoaderName));
				}
			}
		}
		final String template = "Processed {0} data loaders";
		System.out.print(MessageFormat.format(template, DATA_LOADER_NAME_CACHE.size()));
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
