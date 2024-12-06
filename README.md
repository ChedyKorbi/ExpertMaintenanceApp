Mobile Application for "Expert Maintenance" Case Study
Project Overview
Objective
The objective of this project is to develop an Android mobile application for Expert Maintenance, a company specializing in IT maintenance services. The mobile application will allow employees to track and manage interventions on various client sites. It will also enable automatic synchronization of data between the company's web-based application and the mobile application.

The company uses XAMPP (version 3.2.4) on their server to manage a MySQL database.

Application Features
The application provides the following key features:

Authentication:

Upon startup, the application presents a login screen where the technician enters their credentials (username and password).
Data Synchronization:

Once logged in, the app will automatically synchronize data between the remote MySQL database and the local SQLite database on the mobile device. This updates the list of interventions and parameters to be completed by the employee.
Main Interface:

After synchronization, the first interface displays the current day's schedule.
The main screen shows the interventions assigned for the day.
Project Architecture
Frontend
The frontend of the application is developed using Android Studio. It provides a clean and user-friendly interface for the technician to interact with the system. The mobile application will provide the following functionalities:

Login screen
Intervention schedule
Synchronization status
Intervention details
Backend
The backend is based on MySQL, with data stored and managed via a web application that synchronizes with the mobile app. The backend handles:

User Authentication
Intervention data
Synchronization with mobile devices
Data Management
Database Synchronization
The app will use a REST API to fetch and push data between the mobile app and the backend. This API ensures that all data in the local SQLite database is synchronized with the central MySQL database.
Local Database
The mobile app uses SQLite to store intervention data locally, enabling offline functionality. Once the device is online, the data is automatically synchronized with the MySQL database.
User Interface
Login Screen
Fields: Username and Password
Button: Login
Main Screen
Displays the intervention list for the current day.
Tabs: Upcoming, Ongoing, Completed
Navigation: Allows users to view detailed information about each intervention.
Intervention Details Screen
Information displayed:
Client name
Date and time
Description of the problem
Technician notes
Option to mark the intervention as completed
Technologies Used
Android Studio: The development environment used to build the mobile application.
Java: The primary programming language used in this project.
SQLite: Used for local data storage on the mobile device.
MySQL: The relational database used on the backend server.
XAMPP: A cross-platform web server solution for managing the MySQL database.
Challenges Faced
Data Synchronization: Ensuring seamless synchronization between the local SQLite database and the central MySQL database, especially during offline use.
Offline Functionality: Handling situations where the mobile device does not have an internet connection.
User Interface Design: Ensuring a smooth and intuitive user experience.
Future Improvements
Push Notifications: Implementing notifications to alert technicians about new or updated interventions.
Offline Mode Improvements: Enhancing the offline functionality to handle larger datasets and improve synchronization efficiency.
User Profiles: Adding a user profile feature for technicians to view their historical interventions and performance.
Conclusion
The Expert Maintenance mobile application aims to provide an efficient solution for technicians to manage their daily interventions. The app ensures real-time synchronization with the backend, allowing employees to stay updated on their tasks, even when offline.
