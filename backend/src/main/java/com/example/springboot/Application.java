package com.example.springboot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.example.springboot.schema.GraphQLSchemaLoader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import graphql.schema.idl.SchemaPrinter;
import picocli.CommandLine;
import picocli.CommandLine.Option;

@SpringBootApplication
public class Application {
	private static class CommandLineArguments {
		@Option(names = {"-s", "--schema"}, description = "Path to output schema file")
		private File schemaFile;
	}

	public static void main(String[] args) {
		final var commandLineArguments = new CommandLineArguments();
		new CommandLine(commandLineArguments).parseArgs(args);
		if (commandLineArguments.schemaFile != null) {
			try (PrintWriter writer = new PrintWriter(commandLineArguments.schemaFile)) {
				final var schemaPrinter  = new SchemaPrinter();
				writer.println(schemaPrinter.print(GraphQLSchemaLoader.getSchema()));
			} catch (FileNotFoundException ex) {
				System.exit(1);
			}
		}
		SpringApplication.run(Application.class, args);
	}
}
