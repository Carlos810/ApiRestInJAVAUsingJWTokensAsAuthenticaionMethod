# Project Description – Spring Boot API with JWT Security

This project is a REST API developed in Java using Spring Boot. It implements a stateless authentication system based on JWT (JSON Web Token). This means the server does not store user sessions. Instead, every request must include a valid token.

The security architecture is divided into three main parts: CORS configuration, Spring Security configuration, and a custom JWT filter.

## 1. CORS Configuration

The CorsConfig class defines the Cross-Origin Resource Sharing (CORS) rules. It allows a frontend application (for example, Angular running on http://localhost:4200) to consume the API.

The configuration:
- Allows credentials (such as cookies and authorization headers).
- Allows HTTP methods: GET, POST, PUT, DELETE.
- Allows all headers.
- Applies the CORS rules to all routes in the application.

This ensures controlled communication between the frontend and backend.

## 2. Security Configuration

The SecurityConfig class contains the main Spring Security setup.

It performs the following actions:

- Enables CORS using the defined configuration.
- Disables CSRF protection because the application uses JWT and does not rely on sessions.
- Allows public access only to authentication endpoints ("/api/v1/auth/**").
- Requires authentication for all other endpoints.
- Adds a custom JWT filter before the default authentication filter.
- Configures the application as stateless (SessionCreationPolicy.STATELESS), meaning no HTTP sessions are created.
- Defines a PasswordEncoder using BCrypt for secure password hashing.
- Exposes the AuthenticationManager provided by Spring.

This configuration ensures that only authenticated users can access protected resources.

## 3. JWT Request Filter

The JwtRequestFilter class extends OncePerRequestFilter, which guarantees that the filter runs once per HTTP request.

Its responsibilities are:

1. Read the "Authorization" header.
2. Check if it starts with "Bearer ".
3. Extract the JWT from the header.
4. Extract the username from the token.
5. If there is no authentication in the security context:
    - Load the user from the database using UserDetailsService.
    - Validate the token using JwtUtilService.
6. If the token is valid:
    - Create a UsernamePasswordAuthenticationToken.
    - Store it in the SecurityContextHolder.
7. Continue the filter chain to allow the request to reach the controller.

## Overall Architecture

This project follows a modern security approach for backend applications:

- No server-side sessions.
- Every request must include a valid JWT.
- Passwords are securely hashed with BCrypt.
- Only authentication endpoints are public.
- All other endpoints are protected by token validation.

This design improves scalability, security, and separation between frontend and backend systems.
```