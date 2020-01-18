package com.example.springboot.user;

import java.util.concurrent.CompletableFuture;

import com.example.springboot.address.Address;
import com.example.springboot.address.AddressQuery;
import com.example.springboot.graphql.loader.GraphQLDataLoader;
import com.example.springboot.graphql.loader.GraphQLDataLoaderHelper;
import com.example.springboot.graphql.loader.GraphQLDataLoaderRegister;

import org.dataloader.DataLoader;

import graphql.annotations.annotationTypes.GraphQLDataFetcher;
import graphql.annotations.annotationTypes.GraphQLField;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class User {
	@GraphQLField
	public Long id;

	@GraphQLField
	public String name;

	@GraphQLField
	public String email;

	public Long addressId;

	@GraphQLField
	@GraphQLDataFetcher(AddressDataFetcher.class)
	public static Address address() {
		return null;
	}

	public static class AddressDataFetcher implements DataFetcher<CompletableFuture<Address>> {
		@Override
		public CompletableFuture<Address> get(DataFetchingEnvironment environment) throws Exception {
			User user = environment.getSource();
			DataLoader<Long, Address> dataLoader = GraphQLDataLoaderHelper.getDataLoader(AddressDataLoader.class, environment);
			return dataLoader.load(user.addressId);
		}
	}

	@GraphQLDataLoaderRegister
	public static class AddressDataLoader implements GraphQLDataLoader<Long, Address> {
		@Override
		public DataLoader<Long, Address> get() {
			return DataLoader.newDataLoader((addressIdList) -> {
				System.out.println("Batching address lookup of length: " + addressIdList.size());
				return CompletableFuture.supplyAsync(() -> {
					return AddressQuery.ADDRESS_DATABASE.getByIdBatch(addressIdList);
				});
			});
		}
	}
}
