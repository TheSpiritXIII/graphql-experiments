package com.example.springboot.user;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import graphql.annotations.AnnotationsSchemaCreator;
import graphql.schema.GraphQLSchema;

@Component
public class UserSchema {
	private static final GraphQLSchema schema = AnnotationsSchemaCreator.newAnnotationsSchema().query(UserQuery.class).build();

	@Bean
	public GraphQLSchema getSchema() {
		return schema;
	}
}
