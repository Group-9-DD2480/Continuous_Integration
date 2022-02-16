# Continuous Integration
A Continuouses Integration Server that will clone your project build it and then email the result.

### Requirments
- Java JDK 17
- Maven 4.0.0
- Java Unit Tests 5.7.2

### How to use
#### Step 1
Clone or download the repository.\
Set up webhooks to the project you want to feed the CI server.\
#### Step 2
Download Ngrok and start it by using ```./ngrock.exe http 8080``` copy the forwarding URL into your Webhook.\
Start the project by writing ```mvn compile``` and then ```mvn exec:java -D"exec.mainClass"="ContinuousIntegrationServer"``` \
Push the project to trigger the CI Server to compile and build your project.\
#### Testing
Run ```mvn test``` to run the tests.

### Statement of contrubutions

#### Thea Nöteberg
- Set up Maven and Webhooks
- Created the function that Emails the user with Oscar
- Made unit tests for the Email function

#### Jonathan Hedin

#### Oscar Ingels

#### Edvard Aldor

#### Sida Sun
