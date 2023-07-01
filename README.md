# CRUD com Kotlin + Ktor

Crud of users


#### Technologies and Frameworks:

* Kotlin 1.7.10
* Ktor 2.3.0

#### How to use:

Use this command to build dockerfile
```
docker build -t ktor-crud-users .
```

Start the api and database
```
docker-compose up
```

#### Collection:
You can use the `Users.postman_collection.json` collection located at the root of the project

#### How to Test

* Execute `./gradlew clean test`

To check coverage open the `index.html` files in the folders
* build/reports/jacoco/test/html/index.html
* build/reports/jacoco/tests/test/index.html