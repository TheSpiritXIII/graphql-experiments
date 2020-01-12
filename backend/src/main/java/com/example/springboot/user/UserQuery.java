package com.example.springboot.user;

import java.util.List;

import com.example.springboot.graphql.namespace.GraphQLNamespace;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;

@GraphQLNamespace(type = GraphQLNamespace.Type.Query)
public class UserQuery {
	private final static UserDatabase userDatabase = new UserDatabase();

	@GraphQLField
	public static User get(@GraphQLName("id") long id) {
		return userDatabase.getAll().stream().filter((user) -> {
			return user.id == id;
		}).findFirst().orElse(null);
	}

	@GraphQLField
	public static List<User> getAll() {
		return userDatabase.getAll();
	}
}
