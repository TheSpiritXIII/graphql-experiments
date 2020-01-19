package com.example.springboot.graphql.schema;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.ScalarInfo;
import graphql.schema.idl.SchemaPrinter;

@RestController
public class GraphQLSchemaController {
	private final GraphQLSchema schema;

	public GraphQLSchemaController(GraphQLSchema schema) {
		this.schema = schema;
	}

	@RequestMapping(path = "/schema", produces = { MediaType.TEXT_PLAIN_VALUE })
	public String getSchema() {
		final StringBuilder builder = new StringBuilder();
		for (GraphQLScalarType scalar : ScalarInfo.STANDARD_SCALARS) {
			if (!ScalarInfo.isGraphqlSpecifiedScalar(scalar)) {
				builder.append("scalar ");
				builder.append(scalar.getName());
				builder.append('\n');
			}
		}
		builder.append('\n');

		final SchemaPrinter schemaPrinter = new SchemaPrinter();
		builder.append(schemaPrinter.print(this.schema));
		return builder.toString();
	}
}
