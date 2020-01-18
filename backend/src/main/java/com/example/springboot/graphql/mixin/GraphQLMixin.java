package com.example.springboot.graphql.mixin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface GraphQLMixin {
	Class<?> value();
};
