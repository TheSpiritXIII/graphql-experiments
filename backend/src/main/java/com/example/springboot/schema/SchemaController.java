package com.example.springboot.schema;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import graphql.schema.idl.SchemaPrinter;

@RestController
class SchemaController {
	private final GraphQLSchemaLoader schemaLoader;

	public SchemaController(GraphQLSchemaLoader schemaLoader) {
		this.schemaLoader = schemaLoader;
	}

	@RequestMapping(path = "/schema", produces = { MediaType.TEXT_PLAIN_VALUE })
	String getSchema() {
		final var schemaPrinter = new SchemaPrinter();
		return schemaPrinter.print(this.schemaLoader.getSchema());
	}
}
