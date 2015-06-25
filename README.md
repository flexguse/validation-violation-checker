# Introduction
The validation-violation-checker is a little tool to check Contraint Violations (result of JSR303 Bean Validation) against expectation in JUnit test.

Some may say these JUnit tests are integration tests to test the validation implementations. But in the daily business it is very handsome to be quickly able to test if the validation annotations are set correctly.

# Prerequisites
The validation-violation-checker needs to run

- Java 1.7 or newer
- JUnit 4 or newer
- javax.validation.validation-api 1.1.0 or higher
- Apache commons lang3 

# Using validation-violation-checker

## Maven dependency
In case you use Maven just add the dependencies

```xml
	<dependency>
		<groupId>de.flexguse.util.junit</groupId>
		<artifactId>validation-violation-checker</artifactId>
		<version>0.1</version>
		<scope>test</scope>
	</dependency>

``` 

to your Maven dependencies. All required dependencies are added automatically to your project.

## ValidationViolationChecker
Using the validation-violation-checker is quite easy.

Do your Java JSR303 BeanValdation, i.e.

```java
		Set<ConstraintViolation<RemoteSite>> violations = validator
				.validate(RemoteSite.builder().build());
```

and check if the violations contain the validation errors you expected

```java
		ValidationViolationChecker.checkExpectedValidationViolations(violations, Arrays
				.asList("error 1", "error 2", "error 3"));
```

The ValidationViolationChecker uses internally the Junit Assert methods, so calling the method 'checkExpectedValidationViolations' produces a meaningful Junit assertion error.

## i18n and ValidationViolationChecker
If you use resource bundles to make your validation errors multilanguage this is absolutely no problem for ValidationViolationChecker because internally the validation template is used. Just use the curly bracket expression as expected validation violation, i.e.

```java
		ValidationViolationChecker.checkExpectedValidationViolations(violations, Arrays
				.asList("{error.1}", "{error.2}", "{error.3}"));
```

# Building from source
validation-violation-checker is a Maven project, so nothing special is needed here

```bash
mvn clean install
```

should do the job.