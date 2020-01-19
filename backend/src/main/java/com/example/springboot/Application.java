package com.example.springboot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.example.springboot.graphql.schema.GraphQLSchemaComponent;
import com.example.springboot.graphql.schema.GraphQLSchemaController;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import graphql.schema.GraphQLSchema;
import picocli.CommandLine;
import picocli.CommandLine.Option;

@SpringBootApplication
public class Application {
	private static class CommandLineArguments {
		@Option(names = {"-s", "--schema"}, description = "Path to output schema file")
		private File schemaFile;
	}

	public static void main(String[] args) throws Exception {
		final CommandLineArguments commandLineArguments = new CommandLineArguments();
		new CommandLine(commandLineArguments).parseArgs(args);
		if (commandLineArguments.schemaFile != null) {
			final GraphQLSchema schema = new GraphQLSchemaComponent().getSchema();
			final GraphQLSchemaController schemaController = new GraphQLSchemaController(schema);
			try (PrintWriter writer = new PrintWriter(commandLineArguments.schemaFile)) {
				writer.println(schemaController.getSchema());
			} catch (FileNotFoundException ex) {
				System.exit(1);
			}
			System.exit(0);
		}
		SpringApplication.run(Application.class, args);
	}
}
