package com.example.springboot.graphql.namespace;

import java.util.List;

import graphql.annotations.processor.GraphQLAnnotations;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.FieldCoordinates;
import graphql.schema.GraphQLCodeRegistry;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;

public class GraphQLNamespaceBuilder {
	public static GraphQLObjectType.Builder build(
		String groupName,
		GraphQLAnnotations graphQlAnnotations,
		GraphQLCodeRegistry.Builder codeRegistryBuilder,
		List<Class<?>> namespaceClassList
	) {
		GraphQLObjectType.Builder queryBuilder = GraphQLObjectType.newObject().name(groupName);
		namespaceClassList.stream().forEach((namespaceClass) -> {
			final GraphQLObjectType queryObject = graphQlAnnotations.object(namespaceClass);
			final String name = queryObject.getName();

			final GraphQLFieldDefinition fieldDefinition = GraphQLFieldDefinition
				.newFieldDefinition()
				.name(name)
				.type(queryObject)
				.build();
			queryBuilder.field(fieldDefinition);

			codeRegistryBuilder.dataFetcher(FieldCoordinates.coordinates(groupName, name), new DataFetcher<Object>() {
				@Override
				public Object get(DataFetchingEnvironment dataFetchingEnvironment) {
					try {
						return namespaceClass.getConstructor().newInstance();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		});
		return queryBuilder;
	}

	public static GraphQLObjectType.Builder buildQuery(
		GraphQLAnnotations graphQlAnnotations,
		GraphQLCodeRegistry.Builder codeRegistryBuilder,
		List<Class<?>> namespaceClassList
	) {
		return build("query", graphQlAnnotations, codeRegistryBuilder, namespaceClassList);
	}
}
