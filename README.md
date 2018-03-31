[<img src="https://img.shields.io/travis/playframework/play-java-starter-example.svg"/>](https://travis-ci.org/playframework/play-java-starter-example)

# Enterprise serial bus

This is a starter application that shows how Play works.  Please see the documentation at https://www.playframework.com/documentation/latest/Home for more details.

## Running

Run this using [sbt](http://www.scala-sbt.org/).  If you downloaded this project from http://www.playframework.com/download then you'll find a prepackaged version of sbt in the project directory:

```
sbt run
```

And then go to http://localhost:9000 to see the running web application.

# Api elements

## Users

### Add new user

```
curl -H "Content-Type: application/json" -X POST -d '{"firstName":"Janek","lastName":"Kowalski","email": "jan@kowalski.pl", "phone":"1", "password": "alpha", "isAdmin": "true"}' http://localhost:9000/api/v1/users/add
```

### Update user

```
curl -H "Content-Type: application/json" -X PUT -d '{"id": 1, "firstName":"Janek","lastName":"Kowalski","email":"janek1@kowalsk1i.pl", "phone":"12", "password": "alpha", "isAdmin": true}' http://localhost:9000/api/v1/users/edit/1
```

### Get all users

```
curl http://localhost:9000/api/v1/users/all
```

### Get user by e-mail

```
curl http://localhost:9000/api/v1/users/getByEmail/janek1@kowalski.pl
```

### Get user by phone

```
curl http://localhost:9000/api/v1/users/getByPhone/1
```

### Get user by email and password

```
curl -H "Content-Type: application/json" -X POST -d '{"email":"janek1@kowalsk1i.pl", "password": "alpha"}' http://localhost:9000/api/v1/users/getByEmailAndPassword
```

### GET user by phone and password

```
curl -H "Content-Type: application/json" -X POST -d '{"phone":"1", "password": "alpha"}' http://localhost:9000/api/v1/users/getByPhoneAndPassword
```

### Delete user

```
curl -H "Content-Type: application/json" -X DELETE http://localhost:9000/api/v1/users/delete/1
```