package com.example.springboot.graphql.namespace;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface GraphQLNamespace {
	enum Type {
		Query,
		Mutation,
		Subscription
	}

	Type type();
};
