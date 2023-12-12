# 'Stellar Burgers' web-ui-tests

API TESTING A TRAINING SERVICE
[**«STELLAR BURGERS»**](https://stellarburgers.nomoreparties.site)


## Description

**Project stack**
- Java 11
- JUnit 4.13.2
- RestAssured 5.3.0


## Repository cloning
```shell
git clone https://github.com/Sergey8193/Diplom_2.git
```


## Running auto tests

**Running tests and generating allure report** (```mvn clean test```)

Report folder: ```target/allure-results```

**Launching web server with a report** (```mvn allure:serve```)


## Project Tree

```
pom.xml
README.md
.gitignore
src
|-- main
|   |-- java
|   |   |-- praktikum
|   |   |   |-- stellarburgers
|   |   |   |   |-- constants
|   |   |   |   |   |-- RestClient.java
|   |   |   |   |   |-- UserState.java
|   |   |   |   |   |-- UserStatus.java
|   |   |   |   |-- ingredient
|   |   |   |   |   |-- IngredientClient.java
|   |   |   |   |   |-- IngredientData.java
|   |   |   |   |   |-- IngredientsOrderDataGenerator.java
|   |   |   |   |   |-- IngredientsSuccessInfo.java
|   |   |   |   |-- order
|   |   |   |   |   |-- CreateOrderFailureInfo.java
|   |   |   |   |   |-- CreateOrderOrderData.java
|   |   |   |   |   |-- CreateOrderSuccessInfo.java
|   |   |   |   |   |-- GetOrdersOrderData.java
|   |   |   |   |   |-- GetOrdersSuccessInfo.java
|   |   |   |   |   |-- OrderClient.java
|   |   |   |   |   |-- OrderData.java
|   |   |   |   |   |-- OrderDataGenerator.java
|   |   |   |   |-- user
|   |   |   |   |   |-- User
|   |   |   |   |   |-- UserClient.java
|   |   |   |   |   |-- UserContactInfo.java
|   |   |   |   |   |-- UserCredentials.java
|   |   |   |   |   |-- UserDataGenerator.java
|   |   |   |   |   |-- UserFailureInfo.java
|   |   |   |   |   |-- UserRegistrationData.java
|   |   |   |   |   |-- UserSuccessInfo.java
|-- test
|   |-- java
|   |   |-- praktikum
|   |   |   |-- stellarburgers
|   |   |   |   |-- ingredient
|   |   |   |   |   |-- GetIngredientsTest
|   |   |   |   |-- order
|   |   |   |   |   |-- CreateOrderParametersTest
|   |   |   |   |   |-- CreateOrderTest
|   |   |   |   |   |-- GetOrdersTest
|   |   |   |   |-- user
|   |   |   |   |   |-- CreateUserParametersTest
|   |   |   |   |   |-- CreateUserTest
|   |   |   |   |   |-- LoginUserParametersTest
|   |   |   |   |   |-- LoginUserTest
|   |   |   |   |   |-- ModifyUserParametersTest
|   |   |   |   |   |-- ModifyUserTest
|   |   |   |   |-- WebUiTestLauncher.java
```


## Completed tasks

**Created auto API tests of basic functionality**
- user registration
- user authorization
- creating an order
- placing an order on the service
