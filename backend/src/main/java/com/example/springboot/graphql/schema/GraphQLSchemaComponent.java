package com.example.springboot.graphql.schema;

import java.util.List;
import java.util.Map.Entry;

import com.example.springboot.graphql.mixin.GraphQLMixinHelper;
import com.example.springboot.graphql.mixin.GraphQLMixinScanner;
import com.example.springboot.graphql.namespace.GraphQLNamespaceBuilder;
import com.example.springboot.graphql.namespace.GraphQLNamespaceScanner;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import graphql.annotations.processor.GraphQLAnnotations;
import graphql.schema.GraphQLCodeRegistry;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

@Component
public class GraphQLSchemaComponent {
	private final GraphQLSchema schema;

	public GraphQLSchemaComponent() throws Exception {
		final GraphQLAnnotations graphQlAnnotations = new GraphQLAnnotations();
		final GraphQLCodeRegistry.Builder codeRegistryBuilder = graphQlAnnotations.getContainer()
				.getCodeRegistryBuilder();

		for (Entry<Class<?>, Class<?>> mixinEntry : GraphQLMixinScanner.scan().entrySet()) {
			GraphQLMixinHelper.register(graphQlAnnotations, mixinEntry.getKey(), mixinEntry.getValue());
		}

		final GraphQLNamespaceScanner namespaceLoader = new GraphQLNamespaceScanner();
		final List<Class<?>> queryClassList = namespaceLoader.queryClassList;
		GraphQLObjectType query = GraphQLNamespaceBuilder
			.buildQuery(graphQlAnnotations, codeRegistryBuilder, queryClassList)
			.build();

		final GraphQLCodeRegistry codeRegistry = codeRegistryBuilder.build();
		this.schema = GraphQLSchema.newSchema().query(query).codeRegistry(codeRegistry).build();
	}

	@Bean
	public GraphQLSchema getSchema() {
		return schema;
	}
}
