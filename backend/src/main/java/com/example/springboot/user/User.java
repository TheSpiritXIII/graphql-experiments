package com.example.springboot.user;

import com.example.springboot.address.Address;
import com.example.springboot.address.AddressQuery;

import graphql.annotations.annotationTypes.GraphQLField;

public class User {
	@GraphQLField
	public Long id;

	@GraphQLField
	public String name;

	@GraphQLField
	public String email;

	public Long addressId;

	@GraphQLField
	public Address address() {
		return AddressQuery.getById(this.addressId);
	}
}
