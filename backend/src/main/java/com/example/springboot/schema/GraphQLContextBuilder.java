package com.example.springboot.schema;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;

import com.example.springboot.address.Address;
import com.example.springboot.user.User.AddressBatchLoader;

import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import graphql.kickstart.execution.context.DefaultGraphQLContext;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.servlet.context.DefaultGraphQLServletContext;
import graphql.servlet.context.DefaultGraphQLWebSocketContext;
import graphql.servlet.context.GraphQLServletContextBuilder;

@Component
public class GraphQLContextBuilder implements GraphQLServletContextBuilder {
	@Override
	public GraphQLContext build(HttpServletRequest req, HttpServletResponse response) {
		return DefaultGraphQLServletContext.createServletContext(buildDataLoaderRegistry(), null).with(req)
				.with(response).build();
	}

	@Override
	public GraphQLContext build() {
		return new DefaultGraphQLContext(buildDataLoaderRegistry(), null);
	}

	@Override
	public GraphQLContext build(Session session, HandshakeRequest request) {
		return DefaultGraphQLWebSocketContext.createWebSocketContext(buildDataLoaderRegistry(), null).with(session)
				.with(request).build();
	}

	private DataLoaderRegistry buildDataLoaderRegistry() {
		DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
		DataLoader<Long, Address> characterDataLoader = DataLoader.newDataLoader(new AddressBatchLoader());
		dataLoaderRegistry.register("userAddress", characterDataLoader);
		return dataLoaderRegistry;
	}
}
