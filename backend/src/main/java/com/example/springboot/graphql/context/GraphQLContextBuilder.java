package com.example.springboot.graphql.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;

import com.example.springboot.graphql.loader.GraphQLDataLoaderLoader;

import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import graphql.kickstart.execution.context.DefaultGraphQLContext;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.servlet.context.DefaultGraphQLServletContext;
import graphql.servlet.context.DefaultGraphQLWebSocketContext;
import graphql.servlet.context.GraphQLServletContextBuilder;

@Component
public class GraphQLContextBuilder implements GraphQLServletContextBuilder {
	private final DataLoaderRegistry dataLoaderRegistry;

	public GraphQLContextBuilder() throws Exception {
		GraphQLDataLoaderLoader dataLoader = new GraphQLDataLoaderLoader();
		this.dataLoaderRegistry = dataLoader.getDataLoaderRegistry();
	}

	@Override
	public GraphQLContext build(HttpServletRequest req, HttpServletResponse response) {
		return DefaultGraphQLServletContext.createServletContext(this.dataLoaderRegistry, null)
			.with(req)
			.with(response)
			.build();
	}

	@Override
	public GraphQLContext build() {
		return new DefaultGraphQLContext(this.dataLoaderRegistry, null);
	}

	@Override
	public GraphQLContext build(Session session, HandshakeRequest request) {
		return DefaultGraphQLWebSocketContext.createWebSocketContext(this.dataLoaderRegistry, null)
			.with(session)
			.with(request)
			.build();
	}
}
