# Sample Java Codex Review Project

This is a sample Spring Boot application designed to test the Codex AI code review system.

## Overview

This project intentionally contains various code issues to demonstrate code review capabilities:

- **Security vulnerabilities** (plain text passwords, SQL injection, weak hashing)
- **Logic bugs** (null pointer exceptions, missing validations)
- **Performance issues** (N+1 query problems)
- **Style issues** (poor error handling, information leakage)

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### API Endpoints

- `POST /api/users` - Create a new user
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `POST /api/users/login` - Login endpoint
- `GET /api/users/search?email={email}` - Search users by email

## Testing Code Review

To test the code review system:

```bash
# Initialize git repository if not already done
git init
git add .
git commit -m "Initial commit with intentional issues"

# Run the review script
cd .reviews
./review_last_commit.sh
```

## Expected Review Findings

The code review should identify:

1. **Security Issues:**
   - Plain text password storage
   - MD5 hashing (weak algorithm)
   - SQL injection vulnerability
   - Exposing sensitive data in API responses
   - No authentication/authorization

2. **Logic Bugs:**
   - Missing null checks (NPE potential)
   - No duplicate username validation
   - No input validation
   - Poor error handling

3. **Performance Issues:**
   - Potential N+1 query problems

4. **Style/Best Practices:**
   - Weak password validation
   - Information leakage in error messages
   - Missing proper HTTP status codes

## License

MIT License - This is a sample project for testing purposes.

