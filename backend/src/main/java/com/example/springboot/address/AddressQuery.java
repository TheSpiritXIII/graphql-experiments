package com.example.springboot.address;

import java.util.List;

import com.example.springboot.graphql.namespace.GraphQLNamespace;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;

@GraphQLNamespace(type = GraphQLNamespace.Type.Query)
public class AddressQuery {
	public final static AddressDatabase ADDRESS_DATABASE = new AddressDatabase();

	public static Address getById(long id) {
		return ADDRESS_DATABASE.getById(id);
	}

	@GraphQLField
	public static Address getByCountry(@GraphQLName("country") String country) {
		return ADDRESS_DATABASE.findBy((address) -> {
			return country.equals(address.country);
		});
	}

	@GraphQLField
	public static List<Address> getAll() {
		return ADDRESS_DATABASE.getAll();
	}
}
