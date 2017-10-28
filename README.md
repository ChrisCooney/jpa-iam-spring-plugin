# SHIP - Spring Hibernate IAM Plugin

SHIP is a plugin to enable the simple authentication of spring applications, leveraging IAM roles from within AWS
instead of storing database passwords somewhere in the application configuration. This library is designed for applications
that are running with AWS architecture and, thus, can rely on the instance profile of their machine to determine their
access rights into the database.

I called it **SHIP** because I didn't want to have to keep typing _Spring Hibernate IAM Plugin_

## Installation

*SHIP* will auto configure on by bringing it into your spring boot application, but there are some steps 
to get the end to end solution. Firstly, you'll need to work through the [AWS Documentation](http://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/UsingWithRDS.IAMDBAuth.html)
so your environment has the correct certificates in the correct places.

Once this is done, you'll need to add some values into your application configuration. For the purposes of example,
i'll give an example `application.properties`.

```
rds.auth.database.user=Chris
rds.auth.region.name=eu-west-1
rds.auth.instance.host=my.rds.database.host.name
rds.auth.instance.port=3356
```

Additionally, the application depends on some standard datasource properties being set.

```
spring.datasource.url=jdbc://my.rds.database.host.name
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```

## How it works

Once the app comes up, Spring boot will automatically detect the `dataSource` bean has been declared and won't
declare its own. It will set up this bean, which needs a username and password. Once the bean is invoked,
it will run through the AWS code to generate the auth token and authenticate against the database using this.


