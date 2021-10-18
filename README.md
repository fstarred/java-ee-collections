# java-ee-collections Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/java-ee-collections-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

## Related Guides

- RESTEasy JAX-RS ([guide](https://quarkus.io/guides/rest-json)): REST endpoint framework implementing JAX-RS and more

## Provided Code

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)

## Custom class collections

### QueryParamValidator

This annotation manage query params policy, which is checked by **ParamRequestFilter** provider class.
There are 2 types of validations:

1. XorGroupParam
2. AnyGroupParam

#### XorGroupParam

Each group of query param annotated with XorGroupParam got a check with the XOR operator criteria.
Thus, there must be one and only one parameter valorized for one group.

XOR

| A | B | R |
|---|---|---|
| V | V | F |
| V | F | V |
| F | V | V |
| F | F | F |

#### AnyGroupParam

Each group of query param annotated with AnyGroupParam got a check with the OR operator criteria.
Thus, there must be at least one parameter valorized for one group.


OR

| A | B | R |
|---|---|---|
| V | V | V |
| V | F | V |
| F | V | V |
| F | F | F |