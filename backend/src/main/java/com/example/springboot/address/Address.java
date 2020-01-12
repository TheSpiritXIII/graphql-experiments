package com.example.springboot.address;

import graphql.annotations.annotationTypes.GraphQLField;

public class Address {
	public Long id;

	@GraphQLField
	public String streetNumber;

	@GraphQLField
	public String city;

	@GraphQLField
	public String country;
}
