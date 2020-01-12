package com.example.springboot.address;

import java.util.List;

import com.example.springboot.graphql.namespace.GraphQLNamespace;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;

@GraphQLNamespace(type = GraphQLNamespace.Type.Query)
public class AddressQuery {
	private final static AddressDatabase addressDatabase = new AddressDatabase();

	public static Address getById(long id) {
		return addressDatabase.getAll().stream().filter((address) -> {
			return address.id == id;
		}).findFirst().orElse(null);
	}

	@GraphQLField
	public static Address getByCountry(@GraphQLName("country") String country) {
		return addressDatabase.getAll().stream().filter((address) -> {
			return country.equals(address.country);
		}).findFirst().orElse(null);
	}

	@GraphQLField
	public static List<Address> getAll() {
		return addressDatabase.getAll();
	}
}
