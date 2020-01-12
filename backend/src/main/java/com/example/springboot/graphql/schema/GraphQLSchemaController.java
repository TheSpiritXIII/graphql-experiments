package com.example.springboot.graphql.schema;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaPrinter;

@RestController
class SchemaController {
	private final GraphQLSchema schema;

	public SchemaController(GraphQLSchema schema) {
		this.schema = schema;
	}

	@RequestMapping(path = "/schema", produces = { MediaType.TEXT_PLAIN_VALUE })
	String getSchema() {
		final var schemaPrinter = new SchemaPrinter();
		return schemaPrinter.print(this.schema);
	}
}
