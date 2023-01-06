package com.canabrix.wordle;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.*;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class WordleGraphQL {

    private static final Logger logger = LoggerFactory.getLogger(WordleGraphQL.class);
    private static final String GRAPHQL_SCHEMA_FILE = "classpath:wordle.graphqls";

    private GraphQL graphQL;

    @Bean
    public GraphQL graphQL() {
        return this.graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        File f = ResourceUtils.getFile(GRAPHQL_SCHEMA_FILE);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(f)
                ))) {
            StringBuilder sb = new StringBuilder();
            String l = reader.readLine();
            while (l != null) {
                sb.append(l);
                l = reader.readLine();
            }
            GraphQLSchema graphQLSchema = buildSchema(sb.toString());
            logger.info("GraphQL Initialized");
            this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
        }
    }

    @Autowired
    WordleDataFetchers graphQLDataFetchers;

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema schema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
        logger.info("Schema created");
        return schema;
    }

    private RuntimeWiring buildWiring() {
        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("wordle", graphQLDataFetchers.getWordlePossibilities()))
                .build();
        logger.info("GraphQL wiring built.");
        return wiring;
    }

}
