package com.example.springboot.graphql.mixin;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public class GraphQLMixinScanner {
	public static Map<Class<?>, Class<?>> scan() throws Exception {
		return scan(new ClassGraph().enableAnnotationInfo());
	}

	public static Map<Class<?>, Class<?>> scan(ClassGraph classGraph) throws Exception {
		final Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
		try (ScanResult scanResult = classGraph.scan()) {
			final String annotationName = GraphQLMixin.class.getName();
			ClassInfoList classInfoList = scanResult.getClassesWithAnnotation(annotationName);
			for (ClassInfo classInfo : classInfoList) {
				Class<?> classType = classInfo.loadClass();
				GraphQLMixin mixin = classType.getAnnotation(GraphQLMixin.class);
				Class<?> type = mixinMap.put(mixin.value(), classType);
				if (type != null) {
					final String template = "Class `{0}` registered twice ({1} vs {2})";
					throw new Exception(MessageFormat.format(template, mixin.value(), classType, type));
				}
			}
		}
		return mixinMap;
	}
}
