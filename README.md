# GraphQL Experiments
Experiments syncing and typing code-first GraphQL between a Java Spring Boot backend & ReactJS frontend.

The entire GraphQL schema is typed via annotations on the Java-side. From there, we accept command line options passed into the Java executable to output the schema file. The frontend consumes that and transforms the schema into TypeScript definition files. Finally, the frontend creates queries and sends them over to the server.

# Building
To run the backend, run:
```bash
cd backend
./gradlew bootRun
```

To run the frontend, run:
```bash
cd frontend
npm start
```
