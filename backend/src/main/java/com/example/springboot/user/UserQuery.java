package com.example.springboot.user;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.example.springboot.graphql.loader.GraphQLDataLoader;
import com.example.springboot.graphql.loader.GraphQLDataLoaderHelper;
import com.example.springboot.graphql.loader.GraphQLDataLoaderRegister;
import com.example.springboot.graphql.namespace.GraphQLNamespace;

import org.dataloader.DataLoader;

import graphql.annotations.annotationTypes.GraphQLDataFetcher;
import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.context.DefaultGraphQLServletContext;

@GraphQLNamespace(type = GraphQLNamespace.Type.Query)
public class UserQuery {
	public final static UserDatabase USER_DATABASE = new UserDatabase();

	@GraphQLField
	public static List<User> getAll() {
		return USER_DATABASE.getAll();
	}

	@GraphQLField
	@GraphQLDataFetcher(UserDataFetcher.class)
	public static User get(@GraphQLName("id") long id) {
		return null;
	}

	public static class UserDataFetcher implements DataFetcher<CompletableFuture<User>> {
		@Override
		public CompletableFuture<User> get(DataFetchingEnvironment environment) throws Exception {
			// As defined in GraphQLContextBuilder.
			final DefaultGraphQLServletContext servletContext = environment.getContext();
			final HttpServletRequest request = servletContext.getHttpServletRequest();
			// Must be false because getSession(true) is not thread safe.
			final HttpSession session = request.getSession(false);
			System.out.println("Fetching user for sessionId: " + (session != null ? session.getId() : null));

			DataLoader<Long, User> dataLoader = GraphQLDataLoaderHelper.getDataLoader(UserDataLoader.class, environment);
			Long id = environment.getArgument("id");
			System.out.println("Finding user id: " + id);
			return dataLoader.load(id);
		}
	}

	@GraphQLDataLoaderRegister
	public static class UserDataLoader implements GraphQLDataLoader<Long, User> {
		@Override
		public DataLoader<Long, User> get() {
			return DataLoader.newDataLoader((userIdList) -> {
				System.out.println("Batching user lookup of length: " + userIdList.size());
				return CompletableFuture.supplyAsync(() -> {
					return USER_DATABASE.getByIdBatch(userIdList);
				});
			});
		}
	}
}
