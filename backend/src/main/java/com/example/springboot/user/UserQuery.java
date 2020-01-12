package com.example.springboot.user;

import java.util.List;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;
import graphql.schema.DataFetchingEnvironment;

@GraphQLName("query")
public class UserQuery {
	private final static UserDatabase userDatabase = new UserDatabase();

	@GraphQLField
	public static User getUser(DataFetchingEnvironment env, @GraphQLName("id") long id) {
		return userDatabase.getAll().stream().filter((user) -> {
			return user.id == id;
		}).findFirst().orElse(null);
	}

	@GraphQLField
	public static List<User> allUsers(DataFetchingEnvironment env) {
		return userDatabase.getAll();
	}
}
