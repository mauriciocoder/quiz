# Simple HTTP CRUD Endpoint Example for a Quiz Application

This example demonstrates how to setup a simple HTTP CRUD endpoint using SpringBoot. You can perform any CRUD operation on Question entity. Solution's stack:

[SpringBoot](https://projects.spring.io/spring-boot/) is used to provide a container boot and MVC support.

[Swager](https://swagger.io/) is used for API tooling and REST documentation.

[MongoDB](https://www.mongodb.com/) is used for entity persistence.

[Exec Maven Plugin](http://www.mojohaus.org/exec-maven-plugin/) is used to provide goals for Java application run.

[Jackson](https://github.com/FasterXML/jackson) is used to serialize objects to JSON.

## Use Cases

- CRUD operations on Question entity

## Build

It is required to build prior to deploying. You must build the deployment artifact using Maven.

### Maven

In order to build using Maven simply run

```bash
mvn package
```

Note: you can install Maven with

1. [sdkman](http://sdkman.io/) using "sdk install maven" (yes, use as default)
2. sudo apt-get install mvn
3. brew install maven

If you use Maven to build, then in `serverless.yml` you have to replace

```yaml
package:
  artifact: build/distributions/aws-java-simple-http-endpoint.zip
```
by
```yaml
package:
  artifact: target/aws-java-simple-http-endpoint.jar
```
before deploying.

## Deploy

After having built the deployment artifact using Gradle or Maven as described above you can deploy by simply running

```bash
serverless deploy
```

The expected result should be similar to:

```bash
Serverless: Creating Stack...
Serverless: Checking Stack create progress...
.....
Serverless: Stack create finished...
Serverless: Uploading CloudFormation file to S3...
Serverless: Uploading service .zip file to S3...
Serverless: Updating Stack...
Serverless: Checking Stack update progress...
..............................
Serverless: Stack update finished...
Service Information
service: aws-java-simple-http-endpoint
stage: dev
region: us-east-1
api keys:
  None
endpoints:
  GET - https://XXXXXXX.execute-api.us-east-1.amazonaws.com/dev/ping
functions:
  aws-java-simple-http-endpoint-dev-currentTime: arn:aws:lambda:us-east-1:XXXXXXX:function:aws-java-simple-http-endpoint-dev-currentTime

```

## Usage

You can now invoke the Lambda function directly and even see the resulting log via

```bash
serverless invoke --function currentTime --log
```

The expected result should be similar to:

```bash
{
    "statusCode": 200,
    "body": "{\"message\":\"Hello, the current time is Wed Jan 04 23:44:37 UTC 2017\"}",
    "headers": {
        "X-Powered-By": "AWS Lambda & Serverless",
        "Content-Type": "application/json"
    },
    "isBase64Encoded": false
}
--------------------------------------------------------------------
START RequestId: XXXXXXX Version: $LATEST
2017-01-04 23:44:37 <XXXXXXX> INFO  com.serverless.Handler:18 - received: {}
END RequestId: XXXXXXX
REPORT RequestId: XXXXXXX	Duration: 0.51 ms	Billed Duration: 100 ms 	Memory Size: 1024 MB	Max Memory Used: 53 MB
```

Finally you can send an HTTP request directly to the endpoint using a tool like curl

```bash
curl https://XXXXXXX.execute-api.us-east-1.amazonaws.com/dev/ping
```

The expected result should be similar to:

```bash
{"message": "Hello, the current time is Wed Jan 04 23:44:37 UTC 2017"}%  
```

## Scaling

By default, AWS Lambda limits the total concurrent executions across all functions within a given region to 100. The default limit is a safety limit that protects you from costs due to potential runaway or recursive functions during initial development and testing. To increase this limit above the default, follow the steps in [To request a limit increase for concurrent executions](http://docs.aws.amazon.com/lambda/latest/dg/concurrent-executions.html#increase-concurrent-executions-limit).
