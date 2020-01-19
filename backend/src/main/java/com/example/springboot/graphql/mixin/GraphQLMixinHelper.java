package com.example.springboot.graphql.mixin;

import graphql.annotations.directives.DirectiveWirer;
import graphql.annotations.directives.DirectiveWiringMapRetriever;
import graphql.annotations.processor.GraphQLAnnotations;
import graphql.annotations.processor.ProcessingElementsContainer;
import graphql.annotations.processor.exceptions.CannotCastMemberException;
import graphql.annotations.processor.exceptions.GraphQLAnnotationsException;
import graphql.annotations.processor.retrievers.GraphQLExtensionsHandler;
import graphql.annotations.processor.retrievers.GraphQLFieldRetriever;
import graphql.annotations.processor.retrievers.GraphQLInterfaceRetriever;
import graphql.annotations.processor.retrievers.GraphQLObjectHandler;
import graphql.annotations.processor.retrievers.GraphQLObjectInfoRetriever;
import graphql.annotations.processor.retrievers.GraphQLTypeRetriever;
import graphql.annotations.processor.searchAlgorithms.BreadthFirstSearch;
import graphql.annotations.processor.searchAlgorithms.ParentalSearch;
import graphql.annotations.processor.searchAlgorithms.SearchAlgorithm;
import graphql.schema.GraphQLDirectiveContainer;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLType;
import graphql.schema.GraphQLTypeReference;

public class GraphQLMixinHelper {
	public static void register(GraphQLAnnotations annotations, Class<?> classType, Class<?> mixin) {
		try {
			graphQLObjectHandlerGetGraphQLType(annotations.getExtensionsHandler(), annotations.getObjectHandler(), classType, mixin, annotations.getContainer());
		} catch (GraphQLAnnotationsException e) {
			annotations.getContainer().getProcessing().clear();
			annotations.getTypeRegistry().clear();
			throw e;
		}
	}

	private static <T extends GraphQLOutputType> T graphQLObjectHandlerGetGraphQLType(GraphQLExtensionsHandler extensionsHandler, GraphQLObjectHandler graphQLObjectHandler, Class<?> object, Class<?> mixin, ProcessingElementsContainer container) throws GraphQLAnnotationsException, CannotCastMemberException {
		GraphQLOutputType type = (GraphQLOutputType) typeRetrieverGetGraphQLType(extensionsHandler, graphQLObjectHandler.getTypeRetriever(), object, mixin, container, false);
		try {
			return (T) type;
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot cast type " + type.getClass().getSimpleName());
		}
	}

	private static GraphQLType typeRetrieverGetGraphQLType(GraphQLExtensionsHandler extensionsHandler, GraphQLTypeRetriever typeRetriever, Class<?> object, Class<?> mixin, ProcessingElementsContainer container, boolean isInput) throws GraphQLAnnotationsException, CannotCastMemberException {
		final GraphQLObjectInfoRetriever graphQLObjectInfoRetriever = typeRetriever.getGraphQLObjectInfoRetriever();
		final SearchAlgorithm fieldSearchAlgorithm = new ParentalSearch(graphQLObjectInfoRetriever);
		final SearchAlgorithm methodSearchAlgorithm = new BreadthFirstSearch(graphQLObjectInfoRetriever);
		final GraphQLFieldRetriever graphQLFieldRetriever = typeRetriever.getGraphQLFieldRetriever();
		final GraphQLInterfaceRetriever graphQLInterfaceRetriever = typeRetriever.getGraphQLInterfaceRetriever();

		String typeName = graphQLObjectInfoRetriever.getTypeName(object);
		GraphQLType type;

		if (isInput) {
			typeName = container.getInputPrefix() + typeName + container.getInputSuffix();
		}

		if (container.getProcessing().contains(typeName)) {
			return new GraphQLTypeReference(typeName);
		}

		type = container.getTypeRegistry().get(typeName);
		if (type != null) return type;

		container.getProcessing().push(typeName);
		type = new MixinBuilder(graphQLObjectInfoRetriever, fieldSearchAlgorithm, methodSearchAlgorithm,
			graphQLFieldRetriever, graphQLInterfaceRetriever, extensionsHandler).getMixinBuilder(object, mixin, container).build();

		DirectiveWirer directiveWirer = new DirectiveWirer();

		// wire the type with the directives and change the original type
		type = directiveWirer.wire((GraphQLDirectiveContainer) type,
				new DirectiveWiringMapRetriever().getDirectiveWiringMap(object, container),
				container.getCodeRegistryBuilder(), null);

		container.getTypeRegistry().put(type.getName(), type);
		container.getProcessing().pop();

		return type;
	}
}
