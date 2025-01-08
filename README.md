# Financial Management App

## Overview
This project is an academic Android application developed in a small team during college. The primary purpose of the app is to help users manage their finances effectively by tracking income, expenses, and financial goals. The app integrates with a Spring Boot server using RESTful APIs to store and manage data in an H2 database. Additionally, there is a web-based administrative interface for managing users, goals, and financial data.

## Features
### Android App
1. **User Authentication**
   - Login with email and password.
   - Registration for new users.

2. **Goal Management**
   - Add financial goals with the following details:
     - Title (Objective)
     - Target amount
     - Target date
   - Remove goals when needed.

3. **Expense vs Income Analytics**
   - View graphical representations of:
     - Income vs Expenses.
     - Percentage breakdown of bank usage (e.g., Santander 60%, Bradesco 40%).

### Server Features
1. **Data Storage**
   - The server is built with Spring Boot and stores data in an H2 database.

2. **Admin Web Interface**
   - Admin can:
     - View all registered users.
     - View users' income, expenses, and goals.
     - Update or remove user data.
     - Add new users, goals, expenses, or income manually.

3. **Port Configuration**
   - The server runs on port `8081`.

### Integration
- The Android app communicates with the server using RESTful APIs.
- The app requires the server's IP address to be updated in its configuration to ensure both devices are on the same network.

## Requirements
### Android App
- Android Studio
- Java Development Kit (JDK 17)
- Internet connection to connect with the server.

### Server
- Java 17
- Spring Boot framework
- H2 Database

### Web Interface
- A modern web browser to access the admin interface.

## Getting Started
### Setting Up the Server
1. Clone the server repository:
   ```bash
   git clone https://github.com/ViniciusCastellani/Gestao-Financeira-Server.git
   ```
2. Navigate to the server directory:
   ```bash
   cd Gestao-Financeira-Server
   ```
3. Build and run the server:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Access the H2 database console at: `http://localhost:8081/h2-console`
5. To access the web interface, open the file `PessoaListar.html` in a browser.

### Configuring the Android App
1. Open the Android project in Android Studio.
2. Update the server URL in the app's configuration to match the server's IP address:
   ```java
   String serverUrl = "http://<server-ip>:8081";
   ```
3. Build and run the app on an Android device or emulator.

## Usage
### Android App
1. Register or log in using your email and password.
2. Add financial goals, specifying the target amount and date.
3. View your income and expense analytics.

### Admin Web Interface
1. Access the admin panel by opening the file `PessoaListar.html` in a browser.
2. Manage users, goals, and financial data as needed.

## Project Structure
### Android App
- **Java**: Contains all the logic for activities, fragments, and REST API integration.
- **XML**: Layout files for UI.
- **Volley**: For HTTP requests to the server.

### Server
- **Spring Boot**: Backend logic and REST endpoints.
- **H2 Database**: For data storage.

## Contributors
This project was developed by a small team of college students as an academic exercise.

## Repository Links
- [Server Repository](https://github.com/ViniciusCastellani/Gestao-Financeira-Server)

## Notes
- Ensure both the Android device and server are on the same network for proper communication.
- Update the server URL in the Android app whenever the server's IP address changes.

