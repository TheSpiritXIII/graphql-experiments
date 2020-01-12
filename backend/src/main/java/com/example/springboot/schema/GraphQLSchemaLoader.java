package com.example.springboot.schema;

import java.util.Arrays;
import java.util.List;

import com.example.springboot.address.Address;
import com.example.springboot.address.AddressQuery;
import com.example.springboot.user.User;
import com.example.springboot.user.UserQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.processor.GraphQLAnnotations;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.FieldCoordinates;
import graphql.schema.GraphQLCodeRegistry;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

@Component
public class GraphQLSchemaLoader {
	private static List<Class<?>> QUERY_CLASS = Arrays.asList(UserQuery.class, AddressQuery.class);
	private static final GraphQLSchema schema = createSchema();

	private static GraphQLSchema createSchema() {
		GraphQLAnnotations graphqlAnnotations = new GraphQLAnnotations();
		GraphQLCodeRegistry.Builder codeRegistryBuilder = graphqlAnnotations.getContainer().getCodeRegistryBuilder();
		GraphQLObjectType.Builder queryBuilder = GraphQLObjectType.newObject().name("query");
		QUERY_CLASS.stream().forEach((queryClass) -> {
			final GraphQLObjectType queryObject = graphqlAnnotations.object(queryClass);
			final String name = queryObject.getName();
			final GraphQLFieldDefinition fieldDefinition = GraphQLFieldDefinition.newFieldDefinition().name(name)
					.type(queryObject).build();
			queryBuilder.field(fieldDefinition);
			codeRegistryBuilder.dataFetcher(FieldCoordinates.coordinates("query", name), new DataFetcher<UserQuery>() {
				@Override
				public UserQuery get(DataFetchingEnvironment dataFetchingEnvironment) {
					return new UserQuery();
				}
			});
		});

		GraphQLObjectType query1 = queryBuilder.build();
		GraphQLCodeRegistry codeRegistry = codeRegistryBuilder.build();
		return GraphQLSchema.newSchema().query(query1).codeRegistry(codeRegistry).build();
	}

	@Bean
	public GraphQLSchema getSchemaBean() {
		return schema;
	}

	public static GraphQLSchema getSchema() {
		return schema;
	}
}
