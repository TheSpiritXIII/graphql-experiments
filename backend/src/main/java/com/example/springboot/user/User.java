package com.example.springboot.user;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;

@GraphQLName("user")
public class User {
	@GraphQLField
	public Long id;

	@GraphQLField
	public String name;

	@GraphQLField
	public String email;
}
