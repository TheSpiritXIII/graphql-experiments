package com.example.springboot.graphql.namespace;

import java.util.LinkedList;
import java.util.List;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public class GraphQLNamespaceLoader {
	public final List<Class<?>> queryClassList = new LinkedList<>();
	public final List<Class<?>> mutationClassList = new LinkedList<>();
	public final List<Class<?>> subscriptionClassList = new LinkedList<>();

	public GraphQLNamespaceLoader() {
		this(new ClassGraph().enableAnnotationInfo());
	}

	public GraphQLNamespaceLoader(ClassGraph classGraph) {
		try (ScanResult scanResult = classGraph.scan()) {
			final String annotationName = GraphQLNamespace.class.getName();
			ClassInfoList classInfoList = scanResult.getClassesWithAnnotation(annotationName);
			for (ClassInfo classInfo : classInfoList) {
				Class<?> classType = classInfo.loadClass();
				GraphQLNamespace namespace = classType.getAnnotation(GraphQLNamespace.class);
				switch (namespace.type()) {
				case Query:
					queryClassList.add(classType);
					break;
				case Mutation:
					mutationClassList.add(classType);
					break;
				case Subscription:
					subscriptionClassList.add(classType);
					break;
				}
			}
		}
	}
}
