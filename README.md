# E-Commerce Application

This is a simple e-commerce application with proper authentication and authorization controls so users can only access their data, and that data can only be accessed in a secure way. 

## Packages and Classes

This project has 5 packages, as follows:

- demo - This package contains the main method which runs the application

- model.persistence - This package contains the data models that Hibernate persists to H2. There are 4 models: 
    - Cart: for holding a User's items 
    - Item: for defining new items
    - User: to hold appUser account information
    - UserOrder: to hold information about submitted orders.

- model.persistence.repositories - These contain a `JpaRepository` interface for each of our models. This allows Hibernate to connect them with our database so we can access data in the code, as well as define certain convenience methods.

- model.requests - This package contains the request models. The request models will be transformed by Jackson from JSON to these models as requests are made. We have two kinds of requests:
    - CreateUserRequest: As the name suggest, it creates the new user request which is passed to the createUser api.  
    - ModifyCartRequest: It creates new cart request to add/delete items in a user's cart. It is passed to addToCart/removeFromCart api.

- controllers - These contain the api endpoints for  our app, one per model:
    - CartController: Contains addToCart and removeFromCart api implementation
    - ItemController: Gets items available in our database by id/name.  
    - OrderController: Submits a user's cart. Also, contains the api for fetching user order history.
    - UserController: Creates a new user and fetches the user by name/id
    
- security - This package contains classes that implement proper authentication and authorization controls so users can only access their data, and that data can only be accessed in a secure way. We have used username and password for authentication and JWT (Json Web Tokens) for authorization. The included files are:
    - JWTAuthenticationFilter: It is a subclass of `UsernamePasswordAuthenticationFilter` for taking the username and password from a login request and logging in. This, upon successful authentication, should hand back a valid JWT in the `Authorization` header 
    - JWTAuthorizationFilter: It is a subclass of `BasicAuthenticationFilter`. It implements private getAuthentication method which reads the JWT from the Authorization header, and then uses JWT to validate the token. If everything is in place, we set the user in the SecurityContext and allow the request to move on.
    - SecurityConstants: It consists of some constants that we use in the security package.
    - UserDetailsServiceImpl: It implements `UserDetailsService` interface. which takes a username and returns a `org.springframework.security.core.userdetails.User` instance with the application username and hashed password from user repository.
    - WebSecurityConfiguration: It is a subclass of `WebSecurityConfigurerAdapter`. This should attach your app
    user details service implementation to Spring's  `AuthenticationManager`. It also handles session management
    and what endpoints are secured. For us, we manage the session
    so session management should be disabled. Your filters should
    be added to the authentication chain and every endpoint but 1
    should have security required. The one that should not is the
    one responsible for creating new users.

- resources/data.sql - Its a sql file that populates our database with couple of items. Spring will run this file every time the application starts

### Example:
To create a new appUser for example, you would send a POST request to:
http://localhost:8080/api/appUser/create with an example body like 

```
{
    "username": "test"
}
```


and this would return
```
{
    "id" 1,
    "username": "test"
}
```

We can use Spring's default /login endpoint to login like so

```
POST /login 
{
    "username": "test",
    "password": "somepassword"
}
```

and that should, if those are valid credentials, return a 200 OK with an Authorization header which looks like "Bearer <data>" this "Bearer <data>" is a JWT and must be sent as a Authorization header for all other requests. If it's not present, endpoints will return 401 Unauthorized. If it's present and valid, the endpoints will function as normal.

