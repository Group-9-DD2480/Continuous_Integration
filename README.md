# Continuous_Integration

# Assessment P1

## Implementation of compilation (P1)
P1 is implemented in projectBuilder, using maven invoker. The documentation of invoker can be found at:
( https://maven.apache.org/shared/maven-invoker/ ). In the ContinuousInteGrationServer.java, the function compileRepository calls to ProjectBuilder. The unit test(which can be found in the test folder, CITest.java) runs ProjectBuilder and checks if a class has been created.

## Implementation of testing (P2)

P2 is implemented the same way as P1. The unit test can also be found at the same test class as P1.

## Run the tests
Go to the pom.xml file in the terminal, use mvn compile and mvn test , to compile and run the tests.