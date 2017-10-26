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

``bash
all output going to: /usr/local/var/log/mongodb/mongo.log
```

### Maven

In order to build using Maven simply run

```bash
mvn package
```

## Deploy

After having built the deployment artifact using Maven as described above you can deploy by simply running

```bash
mvn exec:java
```

The expected result should be similar to:

```bash
2017-10-26 14:36:26.828  INFO 10686 --- [lication.main()] ication$$EnhancerBySpringCGLIB$$eea4fab7 : welcomePageHandlerMapping
2017-10-26 14:36:26.837  INFO 10686 --- [lication.main()] com.bon.application.Application          : Started Application in 13.036 seconds (JVM running for 18.967)
```

## Usage

You can now browse Swager UI to perform HTTP requests using a graphical interface:

- [SwagerUI](http://localhost:9000/swagger-ui.html#/) The application runs the Swager-UI

Or you can perform it using your CLI, for instance:

```bash
curl https://XXXXXXX.execute-api.us-east-1.amazonaws.com/dev/ping
```

The expected result should be similar to:

```bash
{"message": "Hello, the current time is Wed Jan 04 23:44:37 UTC 2017"}%  
```

## ToDo

By default, AWS Lambda limits the total concurrent executions across all functions within a given region to 100. The default limit is a safety limit that protects you from costs due to potential runaway or recursive functions during initial development and testing. To increase this limit above the default, follow the steps in [To request a limit increase for concurrent executions](http://docs.aws.amazon.com/lambda/latest/dg/concurrent-executions.html#increase-concurrent-executions-limit).
