package com.example.springboot.graphql.mixin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import graphql.annotations.annotationTypes.GraphQLDescription;
import graphql.annotations.annotationTypes.GraphQLTypeResolver;
import graphql.annotations.processor.ProcessingElementsContainer;
import graphql.annotations.processor.exceptions.GraphQLAnnotationsException;
import graphql.annotations.processor.retrievers.GraphQLExtensionsHandler;
import graphql.annotations.processor.retrievers.GraphQLFieldRetriever;
import graphql.annotations.processor.retrievers.GraphQLInterfaceRetriever;
import graphql.annotations.processor.retrievers.GraphQLObjectInfoRetriever;
import graphql.annotations.processor.searchAlgorithms.SearchAlgorithm;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLTypeReference;

class MixinBuilder {
    private GraphQLObjectInfoRetriever graphQLObjectInfoRetriever;
    private SearchAlgorithm methodSearchAlgorithm;
    private SearchAlgorithm fieldSearchAlgorithm;
    private GraphQLFieldRetriever graphQLFieldRetriever;
    private GraphQLInterfaceRetriever graphQLInterfaceRetriever;
    private GraphQLExtensionsHandler extensionsHandler;

    public MixinBuilder(GraphQLObjectInfoRetriever graphQLObjectInfoRetriever, SearchAlgorithm fieldSearchAlgorithm, SearchAlgorithm methodSearchAlgorithm, GraphQLFieldRetriever graphQLFieldRetriever, GraphQLInterfaceRetriever graphQLInterfaceRetriever, GraphQLExtensionsHandler extensionsHandler) {
        this.graphQLObjectInfoRetriever = graphQLObjectInfoRetriever;
        this.methodSearchAlgorithm = methodSearchAlgorithm;
        this.fieldSearchAlgorithm = fieldSearchAlgorithm;
        this.graphQLFieldRetriever = graphQLFieldRetriever;
        this.graphQLInterfaceRetriever = graphQLInterfaceRetriever;
        this.extensionsHandler = extensionsHandler;
    }

    public GraphQLObjectType.Builder getMixinBuilder(Class<?> object, Class<?> mixin,
            ProcessingElementsContainer container) throws GraphQLAnnotationsException {
        if (!mixin.isInterface() && !Modifier.isAbstract(mixin.getModifiers())) {
            throw new IllegalArgumentException(mixin + " must be an interface or abstract class");
        }

        GraphQLObjectType.Builder builder = GraphQLObjectType.newObject();
        String typeName = graphQLObjectInfoRetriever.getTypeName(object);
        builder.name(typeName);
        GraphQLDescription description = object.getAnnotation(GraphQLDescription.class);
        if (description != null) {
            builder.description(description.value());
        }

        List<String> definedFields = new ArrayList<>();
        for (Method mixinMethod : graphQLObjectInfoRetriever.getOrderedMethods(mixin)) {
            if (mixinMethod.isBridge() || mixinMethod.isSynthetic()) {
                continue;
            }
            Method method;
            try {
                method = object.getMethod(mixinMethod.getName(), mixinMethod.getParameterTypes());
            } catch (NoSuchMethodException | SecurityException e) {
                method = null;
            }

            if (method != null) {
                if (methodSearchAlgorithm.isFound(mixinMethod)) {
                    GraphQLFieldDefinition gqlField = graphQLFieldRetriever.getField(typeName, method, container);
                    definedFields.add(gqlField.getName());
                    builder.field(gqlField);
                }
            } else {
                Field field;
                try {
                    field = object.getField(mixinMethod.getName());
                } catch (NoSuchFieldException | SecurityException e) {
                    field = null;
                }

                if (field == null || Modifier.isStatic(field.getModifiers())) {
                    throw new IllegalArgumentException("Unable to find mixin " + mixin + " method or field " + mixinMethod.getName());
                }

                if (fieldSearchAlgorithm.isFound(field)) {
                    GraphQLFieldDefinition gqlField = graphQLFieldRetriever.getField(typeName, field, container);
                    definedFields.add(gqlField.getName());
                    builder.field(gqlField);
                }
            }
        }

        for (Class<?> iface : object.getInterfaces()) {
            if (iface.getAnnotation(GraphQLTypeResolver.class) != null) {
                String ifaceName = graphQLObjectInfoRetriever.getTypeName(iface);
                if (container.getProcessing().contains(ifaceName)) {
                    builder.withInterface(new GraphQLTypeReference(ifaceName));
                } else {
                    builder.withInterface((GraphQLInterfaceType) graphQLInterfaceRetriever.getInterface(iface, container));
                }
                builder.fields(extensionsHandler.getExtensionFields(iface, definedFields, container));
            }
        }

        builder.fields(extensionsHandler.getExtensionFields(object, definedFields, container));

        return builder;
    }
}
