# SkyStream Airline Operations System

SkyStream is an Integrated Airline Operations System developed in Java using a modern, dark-themed Swing GUI. It manages flights, crew assignments, and incident reporting.

## 🚀 How to Run the Application

The system can be run from any standard terminal or IDE. To run it from the command line:

1. **Compile the code:**
   Ensure you are in the project root directory and run:
   ```bash
   javac -d out src/model/*.java src/utils/*.java src/service/*.java src/gui/*.java src/Main.java
   ```

2. **Run the application:**
   ```bash
   java -cp out Main
   ```

## 🔐 User Roles & Login Credentials

The system is role-based. Depending on your role, you have different access levels. The following accounts are pre-seeded in the database:

| Role | Email | Password | Access Level |
|---|---|---|---|
| **Admin** | `admin@sky.com` | `admin123` | Full access. Can add/edit/delete flights, assign/remove crew, and delete incident reports. |
| **Dispatcher** | `dispatch@sky.com` | `disp123` | High access. Can add/edit/delete flights and assign crew. View-only access to incident reports. |
| **Pilot** | `pilot@sky.com` | `pilot123` | Read-only access to flights and crew assignments. Can submit incident reports. |
| **Pilot** | `sara@sky.com` | `sara123` | Same as above. |

*Note: You can also use the **"CREATE ACCOUNT"** button on the login screen to register a new user of any role.*

## ⚙️ Core Modules & Functionality

### 1. Authentication (`AuthService`, `LoginFrame`, `SignupDialog`)
- **Working:** Handles user login and registration. It verifies credentials against the in-memory `DataStore`. The new registration feature allows you to select a role and automatically generates necessary dummy data (like a Pilot's license number) behind the scenes.

### 2. Flight Management (`FlightService`, `FlightManagementFrame`)
- **Working:** A CRUD (Create, Read, Update, Delete) module for flights. 
- **Roles:** Admins and Dispatchers can manage the schedule (add/edit/delete). Pilots only view the flights.
- **Data:** Flights track Origin, Destination, Departure/Arrival times, Status (Scheduled, Departed, etc.), and the assigned Aircraft.

### 3. Crew Assignments (`CrewService`, `CrewAssignmentFrame`)
- **Working:** Allows operations to link Pilots and Dispatchers to specific Flights.
- **Roles:** Admins and Dispatchers can assign or remove crew. Pilots can only see who is flying what.

### 4. Incident Reporting (`IncidentService`, `IncidentFrame`)
- **Working:** A safety logging module where issues during flights are reported.
- **Roles:** Pilots (and Admins) can submit reports. Admins can delete reports. Dispatchers have view-only access.
- **Data:** Tracks Flight ID, severity (color-coded as Low/Medium/High), date, and a description.

## 🏗️ Architecture & Design Patterns

- **Language:** 100% Java.
- **UI Framework:** Java Swing, customized extensively via `UIFactory` and `AppColors` for a premium dark mode aesthetic.
- **Layered Architecture:**
  - `model`: POJOs representing the domain (User, Flight, Aircraft, etc.).
  - `service`: Business logic handling CRUD operations.
  - `gui`: All visual components.
  - `utils`: Helpers like the `DataStore` and UI Factories.
- **Design Patterns Used:**
  - **Singleton:** `DataStore` and `AuthService` ensure only one instance holds data globally.
  - **Factory:** `UIFactory` creates consistent UI elements (buttons, tables, labels).
  - **Inheritance:** `Admin`, `Pilot`, and `Dispatcher` all extend the abstract `User` class.
