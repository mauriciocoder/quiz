# Simple HTTP CRUD Endpoint Example

This example demonstrates how to setup a simple HTTP CRUD endpoint using SpringBoot. You can perform any CRUD operation on Question entity. Solution's stack:

- [SpringBoot](https://projects.spring.io/spring-boot/) is used to provide container's boot and MVC support.
- [Swager](https://swagger.io/) is used for API tooling and REST documentation.
- [MongoDB](https://www.mongodb.com/) is used for entity persistence.
- [Exec Maven Plugin](http://www.mojohaus.org/exec-maven-plugin/) is used to provide goals for Java application run.
- [Jackson](https://github.com/FasterXML/jackson) is used to serialize objects to JSON.


## Use Cases

- CRUD operations on Question entity


## Build

It is required to build prior to deploying. You must build the deployment artifact using Maven. You can configure the server port and mongoDB properties on file (src/main/resources/application.properties). The default is:

```bash
### application port
server.port: 9000

### management
management.port: 9001
management.address: 127.0.0.1
### bypass security for manageable resources
management.security.enabled=false

### logging
logging.level.org.springframework.web=ERROR
logging.level.com.bon=DEBUG
logging.file=/tmp/quiz.log

### mongodb
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=quiz
```


### MongoDB

In order to perform the use cases, you have to provide a mongodb service running and the correct entries in the application properties file:

```bash
### mongodb
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=quiz
```
- Install and launch MongoDB

If you are using a Mac with homebrew, this is as simple as:

```bash
$ brew install mongodb
```

With MacPorts:

```bash
$ port install mongodb

```

For other systems with package management, such as Redhat, Ubuntu, Debian, CentOS, and Windows, see instructions at http://docs.mongodb.org/manual/installation/.

After you install MongoDB, launch it in a console window. This command also starts up a server process.

```bash
$ mongod
```

You probably won’t see much more than this:

```bash
all output going to: /usr/local/var/log/mongodb/mongo.log
```

### Maven

In order to build using Maven simply run

```bash
mvn package
```


## Deploy

After having built the deployment artifact using Maven and having a mongodb instance configured as described above you can deploy by simply running:

```bash
mvn exec:java
```

The expected result should be similar to:

```bash
2017-10-26 14:36:26.828  INFO 10686 --- [lication.main()] ication$$EnhancerBySpringCGLIB$$eea4fab7 : welcomePageHandlerMapping
2017-10-26 14:36:26.837  INFO 10686 --- [lication.main()] com.bon.Application          : Started Application in 13.036 seconds (JVM running for 18.967)
```


## Usage

Every CRUD operation in the application is conventioned to use the following HTTP methods pattern:

- [HTTP Rest Pattern](https://lh3.googleusercontent.com/-cpYCrP36Nc8/VsWO7emBMRI/AAAAAAAAAyU/0rv7Lnl0aNI/s1600-h/image%25255B5%25255D.png)

You can now browse Swager UI to perform HTTP requests using a graphical interface:

- [SwagerUI](http://localhost:9000/swagger-ui.html#/) The application runs the Swager-UI

Or you can perform it using your CLI, for instance:

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: */*' -d '{ \ 
   "answers": [ \ 
     "Donald Trump", "George Bush", "Barack Obama" \ 
   ], \ 
   "correctAnswerIndex": 0, \ 
   "questioning": "What is the name of the president of the USA?" \ 
 }' 'http://localhost:9000/question'
```

The expected result should be similar to:

```bash
RESPONSE CODE
201
HEADERS:
{
  "location": "/question/59f2151115dd9429be54a8a7",
  "date": "Thu, 26 Oct 2017 17:02:09 GMT",
  "content-length": "0",
  "x-application-context": "application:9000",
  "content-type": null
}
```


## ToDo

It is still in progress the development of a security interface to protect the REST endpoints, also it needs further configurations in order to be containerized in a Docker image.
