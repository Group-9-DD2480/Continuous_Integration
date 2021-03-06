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

### P+
#### P9
We linked every pull request to an issue, that was the standard we established last project.

#### P8
Without having any prior knowledge in either using or creating a continuous integration server a lot of hours were spent figuring out just what to do, and more importantly how to do it. This made everyone have to adapt to a new working environment, and even though we were put under quite a lot of stress, and one member not being able to attend due to personal issues, everyone stayed proffessional and cordial, and we believe that we are now more than ever ready to tackle the future assignments. We believe that learning to adapt to new work environments in a proficient manner is worth at least something. So doing this project in this amount of time made us evolve in these areas:
- Communication between team members
- Better understanding of maven
- Better understanding of using APIs (Git actions, maven invoker)
This made us proud of the project we were able to create.

### Statement of contrubutions

#### Thea Nöteberg
- Set up Maven and Webhooks
- Created the function that Emails the user with Oscar
- Made unit tests for the Email function

#### Jonathan Hedin
- Enabled the automatic compilation and testing together with Edvard.
- Made unit tests for the ProjectBuilder.
- Various bugfixes related to the automatic compilation.

#### Oscar Ingels
- Tried to work on P7, but unsuccessfully.
- Helped Thea with Email notification
- Crated javadoc documentation.

#### Edvard Aldor
- Set up Skeleton template
- Created the function that clones git repositorys.
- Created ProjectBuilder with Jonathan
- Implemented ProjectBuilder with ContinouousIntegrationServer

#### Sida Sun
- Participate in group meeetings


## Way of working
We evaluate that our team is on the level "Collaborating". The checklist for each previous stage are mostly fulfilled. An exception can be made for a group member who had to be absent for a part of the work due to personal reasons, which puts the items "The team is working as one cohesive unit" and "Team members are accepting work" on somewhat shaky ground, however with their inclusion we feel that all checklist items up until that point are met, especially the items "Communication within the team is open and honest" and "The team is focused on achieving the team mission". It is due to this exception that we do not feel that we are at the "Performing" level yet, however, our work in Assignment 2 has been to a standard where most items in the "Performing" checklist are satisfied, and we might reach this level in the near future.

