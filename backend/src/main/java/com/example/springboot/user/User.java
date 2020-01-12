package com.example.springboot.user;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import com.example.springboot.address.Address;
import com.example.springboot.address.AddressQuery;

import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;

import graphql.annotations.annotationTypes.GraphQLBatched;
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
			DataLoader<Long, Address> dataLoader = environment.getDataLoader("userAddress");
			return dataLoader.load(user.addressId);
		}
	}

	public static class AddressBatchLoader implements BatchLoader<Long, Address> {
		@Override
		public CompletionStage<List<Address>> load(List<Long> addressIdList) {
			return CompletableFuture.supplyAsync(() -> {
				return addressIdList.stream().map((addressId) -> {
					return AddressQuery.getById(addressId);
				}).collect(Collectors.toList());
			});
		}
	};
}
