Mobile Application for "Expert Maintenance" Case Study
Project Overview
Objective
The objective of this project is to develop an Android mobile application for Expert Maintenance, a company specializing in IT maintenance services. The mobile application will allow employees to track and manage interventions on various client sites. It will also enable automatic synchronization of data between the company's web-based application and the mobile application.

The company uses XAMPP (version 3.2.4) on their server to manage a MySQL database.

Application Features
Authentication:

At the startup, the application presents a login screen where the technician enters their credentials (username and password).
Data Synchronization:

Once logged in, the application will automatically synchronize data between the remote MySQL database and the local SQLite database on the mobile device. This updates the list of interventions and parameters to be completed by the employee.
Main Interface:

After synchronization, the first interface displays the current day's schedule.
The main screen shows the interventions assigned for the day, displaying key details like:
Client name
Task name
Scheduled date and time
A checkbox to mark the intervention as completed
Intervention Details:

Clicking on an intervention will show additional details including:
Client details
Intervention start and end times
Location details with latitude and longitude
A button to view the intervention site on Google Maps
Calendar Navigation:

Employees can navigate through the calendar to view upcoming interventions by using navigation arrows.
Capture and Manage Images:

The app allows employees to capture images during the intervention and save them to the database.
Modification and Deletion:

Employees can modify or delete interventions. A confirmation prompt will be displayed when attempting to uncheck an intervention as completed.
Syncing Interventions:

Modifications to interventions, including status updates, will be synced with the web-based application.
Google Maps Integration:

The application will integrate Google Maps to allow employees to navigate to the site where the intervention needs to take place.
History of Interventions:

The app also provides access to historical data on interventions performed at a given site.
Data Structure
The application uses the following tables in a MySQL database hosted on the company’s server:

Clients:

Stores client information like name, contact details, and address.
Contracts:

Contains contract details for each client, including the start and end dates, and payment details.
Employees:

Stores employee login credentials and contact details.
Interventions:

Contains intervention details such as the title, scheduled time, and client information.
Tasks:

Stores information about specific tasks involved in each intervention.
Priorities:

Holds the priority levels for interventions.
Sites:

Contains information about the locations where interventions take place.
Images:

Stores images captured during interventions.
Database Synchronization
The mobile application will synchronize data with the web server through the following process:

Fetch data from the web-based MySQL database.
Store it locally in the mobile’s SQLite database.
Update the local database with any changes made in the web database.
Technologies Used
Android SDK: For building the mobile application.
SQLite Database: For local data storage on the mobile device.
XAMPP: For setting up the server environment to manage the MySQL database.
MySQL Database: Used for storing all operational data, including interventions, employees, and clients.
Google Maps API: To help employees navigate to intervention sites.
Image Capture API: For capturing and saving images related to interventions.
Database Schema
The database schema used for this project consists of the following tables:

Clients table to store client data.
Contracts table to store contract details.
Employees table for employee details.
Interventions table to track intervention tasks and statuses.
Tasks table for specific task details linked to interventions.
Images table to store images captured during interventions.
Priorities table to define priority levels for interventions.
Sites table to store information about sites for interventions.
Installation
Set up XAMPP and MySQL:

Download and install XAMPP (version 3.2.4).
Set up MySQL to host the database GEM on the server.
Database Script:

Use the provided SQL dump to create the necessary database structure on MySQL.
Android Application:

Clone the Android repository.
Open the project in Android Studio.
Set up the necessary permissions for accessing the internet and Google Maps.
Build and run the app on a physical Android device or emulator.
Usage
Login: Launch the app and log in with valid employee credentials.
Sync Data: The app will automatically synchronize data with the server when the user logs in.
View Interventions: The employee can view their scheduled interventions for the day and mark them as completed.
Capture Images: Employees can capture and attach images during interventions.
Navigation: Use Google Maps to navigate to the intervention site.
Modify and Delete Interventions: Modify or delete interventions as needed, with confirmation prompts.
Sync Changes: Any changes made on the mobile device will be synced with the server when the next synchronization occurs.
Contributing
If you wish to contribute to this project, please fork the repository, make changes, and submit a pull request. Contributions are welcome!
