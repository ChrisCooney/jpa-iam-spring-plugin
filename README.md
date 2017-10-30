# JISP - JPA IAM Spring Plugin

JISP is a plugin to enable the simple authentication of spring applications, leveraging IAM roles from within AWS
instead of storing database passwords somewhere in the application configuration. This library is designed for applications
that are running with AWS architecture and, thus, can rely on the instance profile of their machine to determine their
access rights into the database.

I called it **JISP** because I didn't want to have to keep typing _JPA IAM Spring Plugin_

## Why would you want to handle authentication this way?

Containing most of the bits needed to set up _getting a password_ without storing the password means no accidental password
commits to VCS but it also means no static database passwords.

## Installation (It will be going onto Maven central soon!)

While this isn't on maven central, you can try this project out by cloning the repository and running

```
mvn install
```

from the root of the project. This will install it to your local project, then you add the dependency to
your favourite Java dependency manager.

```xml
<dependency>
     <groupId>com.cooney</groupId>
     <artifactId>jisp</artifactId>
     <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### Once you've got it installed

*JISP* will auto configure on by bringing it into your spring boot application, but there are some steps 
to get the end to end solution. Firstly, you'll need to work through the [AWS Documentation](http://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/UsingWithRDS.IAMDBAuth.html)
so your environment has the correct certificates in the correct places.

Once this is done, there are two more steps. Firstly, you'll need to tell your Spring application to use the configuration
inside the library. To do this in a spring boot application, your main class might look like this:

```java
@SpringBootApplication
@Import(JispConfiguration.class) // This is the necessary bit that wires in the custom dataSource bean.
public class TestProject {
    public static void main(String[] args) {
        SpringApplication.run(TestProject.class, args);
    }
}
```

Finally, your configuration will need to set the correct parameters. To do this, you'll need to update your application
configuration file. As an example, here is a complete `application.yml` that would work with this library:

```yml
server:
  port: 5000

spring:
  jpa:
    database: MYSQL

  datasource:
    url:  jdbc:mysql://my-db.eu-west-1.rds.amazonaws.com:3306/db-name?verifyServerCertificate=true&useSSL=true&requireSSL=true
    driver-class-name: com.mysql.jdbc.Driver

rds:
  auth:
    db-user: Chris
    region: eu-west-1
    db-host: my-db.eu-west-1.rds.amazonaws.com
    db-port: 3306
```

## How it works

Once the app comes up, Spring boot will automatically detect the `dataSource` bean has been declared and won't
declare its own. It will set up this bean, which needs a username and password. Once the bean is invoked,
it will run through the AWS code to generate the auth token and authenticate against the database using this.


