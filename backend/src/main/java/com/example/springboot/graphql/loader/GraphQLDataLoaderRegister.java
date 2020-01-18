package com.example.springboot.graphql.loader;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface GraphQLDataLoaderRegister {
	String name() default "";
};
