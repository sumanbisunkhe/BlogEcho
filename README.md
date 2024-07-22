---

# BlogEcho Application

BlogEcho is a backend web application designed for managing blogs and user accounts, emphasizing robust authentication and authorization mechanisms.

## Introduction

BlogEcho provides a comprehensive platform for users to create, manage, and share blogs securely. Built using Java and Spring technologies, it ensures efficient data management and user interaction through RESTful APIs. The application integrates PostgreSQL for data storage and employs JWT (JSON Web Token) for secure user authentication.

### Key Features

- **Blog Management**: Allows users to create, update, retrieve, and delete blogs.
- **User Management**: Facilitates user registration, login, profile management, and deletion.
- **Comment Management**: Users can add, update, view, and delete comments on blogs.
- **Authentication**: Implements JWT-based authentication for secure user sessions.
- **Validation**: Utilizes Bean Validation (JSR 380) for input validation ensuring data integrity.
- **Error Handling**: Provides detailed error messages for validation errors, resource not found exceptions, and internal server errors.
- **API Documentation**: Utilizes Swagger (OpenAPI 3) for interactive API documentation and testing.

### Technologies Used

- **Backend**: Java, Spring Boot, Spring Security, Spring Data JPA
- **Database**: PostgreSQL
- **Authentication**: JWT (JSON Web Token)
- **Validation**: Bean Validation (JSR 380)
- **API Documentation**: Swagger (OpenAPI 3)

## APIs

### Blog APIs

#### Create Blog

- **Endpoint**: `POST /api/blogs/create`
- **Description**: Creates a new blog with validated input data.
- **Request Body**: `BlogDto`
- **Response**: Returns a success message and created blog data.

#### Update Blog

- **Endpoint**: `PUT /api/blogs/update/{id}`
- **Description**: Updates an existing blog identified by `{id}`.
- **Request Body**: `BlogDto`
- **Response**: Returns a success message and updated blog data.

#### Get Blog by ID

- **Endpoint**: `GET /api/blogs/{id}`
- **Description**: Retrieves a blog by its unique identifier `{id}`.
- **Response**: Returns blog details if found, or a "Not Found" message.

#### Get All Blogs

- **Endpoint**: `GET /api/blogs/all`
- **Description**: Retrieves a list of all blogs.
- **Response**: Returns a list of blog data.

#### Delete Blog by ID

- **Endpoint**: `DELETE /api/blogs/delete/{id}`
- **Description**: Deletes a blog identified by `{id}`.
- **Response**: Returns a success message upon successful deletion.

### User Management APIs

#### Create User

- **Endpoint**: `POST /api/user/create`
- **Description**: Registers a new user with optional OTP verification.
- **Request Body**: `UserDto`
- **Response**: Returns a success message and user data.

#### User Login

- **Endpoint**: `POST /api/user/login`
- **Description**: Authenticates a user and returns a JWT for accessing protected APIs.
- **Request Body**: `AuthenticationRequest`
- **Response**: Returns a JWT token for authenticated user sessions.

#### Get All Users

- **Endpoint**: `GET /api/user/all`
- **Description**: Retrieves a list of all registered users.
- **Response**: Returns a list of user data.

#### Get User by ID

- **Endpoint**: `GET /api/user/id/{id}`
- **Description**: Retrieves user details by their unique identifier `{id}`.
- **Response**: Returns user details if found, or a "Not Found" message.

#### Get User by Username

- **Endpoint**: `GET /api/user/username/{username}`
- **Description**: Retrieves user details by their username `{username}`.
- **Response**: Returns user details if found, or a "Not Found" message.

#### Delete User by ID

- **Endpoint**: `DELETE /api/user/delete/id/{id}`
- **Description**: Deletes a user identified by `{id}`.
- **Response**: Returns a success message upon successful deletion.

#### Delete User by Username

- **Endpoint**: `DELETE /api/user/delete/username/{username}`
- **Description**: Deletes users identified by `{username}`.
- **Response**: Returns a success message upon successful deletion.

#### Generate OTP

- **Endpoint**: `POST /api/user/generate-otp`
- **Description**: Generates and sends an OTP to the user's registered email.
- **Request Parameter**: `email`
- **Response**: Returns a success message confirming OTP generation.

#### Verify OTP

- **Endpoint**: `POST /api/user/verify-otp`
- **Description**: Verifies the OTP sent to the user's email for account activation.
- **Request Body**: `{ "email": "user@example.com", "otp_code": "123456" }`
- **Response**: Returns a success message upon successful OTP verification.

### Comment APIs

#### Create Comment

- **Endpoint**: `POST /api/comments/create`
- **Description**: Creates a new comment.
- **Request Body**: 
  ```json
  {
    "content": "This is a comment.",
    "authorId": 1,
    "blogId": 1
  }
  ```
- **Response**: Returns a success message and created comment data.

#### Update Comment

- **Endpoint**: `PUT /api/comments/update/{id}`
- **Description**: Updates an existing comment identified by `{id}`.
- **Request Body**: 
  ```json
  {
    "content": "This is an updated comment."
  }
  ```
- **Response**: Returns a success message and updated comment data.

#### Get All Comments

- **Endpoint**: `GET /api/comments/all`
- **Description**: Retrieves a list of all comments.
- **Response**: Returns a list of comment data.

#### Get Comment by ID

- **Endpoint**: `GET /api/comments/{id}`
- **Description**: Retrieves a comment by its unique identifier `{id}`.
- **Response**: Returns comment details if found, or a "Not Found" message.

#### Get Comments by Blog ID

- **Endpoint**: `GET /api/comments/blog/{blogId}`
- **Description**: Retrieves comments by blog ID `{blogId}`.
- **Response**: Returns a list of comments for the specified blog.

#### Get Comments by Author ID

- **Endpoint**: `GET /api/comments/author/{authorId}`
- **Description**: Retrieves comments by author ID `{authorId}`.
- **Response**: Returns a list of comments by the specified author.

#### Delete Comment

- **Endpoint**: `DELETE /api/comments/delete/{id}`
- **Description**: Deletes a comment identified by `{id}`.
- **Response**: Returns a success message upon successful deletion.

### Error Handling

The application provides detailed error handling for various scenarios, ensuring informative responses for validation errors, resource not found exceptions, and internal server errors. This helps in debugging and improving user experience.

### Setup

1. **Database**: Ensure PostgreSQL is installed and configured.
2. **Environment Variables**: Set database credentials and JWT secret key.
3. **Build and Run**: Use Maven or an IDE (e.g., IntelliJ IDEA, Eclipse) to build and run the Spring Boot application.

### Usage

1. Use tools like Postman or Swagger UI to interact with the APIs.
2. Include proper authentication headers (JWT) for accessing protected APIs.
3. Explore and test the functionality provided by each API endpoint.

### Contributors

- [Suman Bisunkhe](https://github.com/sumanbisunkhe) - Developer

### License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

This README.md provides an extensive overview of the BlogEcho application, detailing its features, APIs, technologies used, setup instructions, and more. Adjust the content further based on your project's specific details and requirements.
