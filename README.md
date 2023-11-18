# PBKDF2_SHA256

## Overview

This repository contains a Java application designed for user registration and login processes, emphasizing secure password handling. The application employs PBKDF2 with SHA-256 for password hashing, ensuring robust security for stored user credentials.

## Features

- **User Registration**: Facilitates the registration of new users, capturing username and password details.
- **Secure Password Hashing**: Implements PBKDF2 with SHA-256 for robust password hashing.
- **User Login**: Allows users to log in using their credentials.
- **Swing GUI**: Provides a graphical user interface for registration and login using Java Swing.

## Components

- `UserRegistrationApp.java`: Main application class that sets up the Swing GUI for user registration and login.
- `PBKDF2_SHA256.java`: Utility class for hashing passwords using PBKDF2 with SHA-256.
- `PasswordHashResult.java`: Class representing the result of a password hashing operation.

## Requirements

- Java Development Kit (JDK) 8 or above.
- Java Runtime Environment (JRE) for running the application.
- PostgreSQL Database for storing user data.

## Database Configuration

- The application uses a PostgreSQL database to store user credentials.
- The database table should have three columns:
  - `username`: Stores the username of the user.
  - `password_hash`: Stores the hashed password.
  - `password_salt`: Stores the salt used in the hashing process. The salt is essential for authenticating the user during the login process.

Ensure you have PostgreSQL installed and create a table with the above structure. Configure the database connection details in `UserRegistrationApp.java`.

## Setup and Execution

1. Clone the repository to your local machine.
2. Open the project in your preferred Java IDE (e.g., IntelliJ IDEA, Eclipse).
3. Ensure JDK is properly set up in the IDE.
4. Run `UserRegistrationApp.java` to start the application.
5. Use the GUI to register a new user or log in with existing credentials.
