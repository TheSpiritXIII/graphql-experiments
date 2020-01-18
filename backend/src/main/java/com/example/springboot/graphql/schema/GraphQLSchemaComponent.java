package com.example.springboot.graphql.schema;

import java.util.List;

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

	public GraphQLSchemaComponent() {
		final GraphQLAnnotations graphQlAnnotations = new GraphQLAnnotations();
		final GraphQLCodeRegistry.Builder codeRegistryBuilder = graphQlAnnotations
			.getContainer()
			.getCodeRegistryBuilder();

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
