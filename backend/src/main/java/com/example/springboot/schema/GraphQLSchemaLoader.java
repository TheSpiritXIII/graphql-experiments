package com.example.springboot.schema;

import com.example.springboot.user.UserQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import graphql.annotations.processor.GraphQLAnnotations;
import graphql.schema.GraphQLCodeRegistry;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

@Component
public class GraphQLSchemaLoader {
	private static final GraphQLSchema schema = createSchema();

	private static GraphQLSchema createSchema() {
		GraphQLAnnotations graphqlAnnotations = new GraphQLAnnotations();
		GraphQLObjectType object = graphqlAnnotations.object(UserQuery.class);
		// GraphQLTypeReference.typeRef("UserQuery")
		// GraphQLFieldDefinition fieldDefinition = GraphQLFieldDefinition.newFieldDefinition().name("user").type(object).build();
		GraphQLObjectType query = object;//GraphQLObjectType.newObject().name("query").field(fieldDefinition).build();
		GraphQLCodeRegistry codeRegistry = graphqlAnnotations.getContainer().getCodeRegistryBuilder().build();
		return GraphQLSchema.newSchema().query(query).codeRegistry(codeRegistry).build();
	}

	@Bean
	public GraphQLSchema getSchemaBean() {
		return schema;
	}

	public static GraphQLSchema getSchema() {
		return schema;
	}
}
