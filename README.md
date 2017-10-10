Marvel Api Wrapper
==================

A sample API, including automatically generated, runnable API documentation,
that wraps the Marvel api

## Build & Run ##

Clone the repository
```sh
$ git clone https://github.com/jonvallet/marvel-api-wrapper.git
```

Create a config.properties and add the neccessary api keys
```sh
$ cd marvel-api-wrapper/src/main/resources
$ cp config.properties.sample config.properties
$ vim config.properties
```

Start the local server
```sh
$ cd ../../../
$ chmod +x sbt
$ ./sbt ~jetty:start
```

You can see the marvel APIs specification at [http://localhost:8080/api-docs/swagger.json](http://localhost:8080/api-docs/swagger.json).
You can see Swagger-UI at [https://editor.swagger.io/](https://editor.swagger.io/) or [https://swagger.io/swagger-ui/](https://swagger.io/swagger-ui/).

## Test ##

To run the tests suite, simply execute
```sh
$ ./sbt test

There is also a postman collection you can import to test it locally in the postman folder