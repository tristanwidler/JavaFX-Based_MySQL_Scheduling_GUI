# Application Title: SQL Database Manager
Purpose: This program was created to allow easy addition to, and modification of a a MySQL database, while adding other helpful features to aid in the understanding of the data in said database. 

The program was originally created as part of my C195 class at Western Governors University.

## Application Information

### Contact

- Author: **Tristan Widler**
- Contact Information: 
  - Email: tristan.widler@gmail.com

### Versions

- *Application Version*: **1.0**
  - *Creation Date*: **12/15/2021**
- *IDE & Version*: **IntelliJ IDEA 2021.1**
- *MySQL Workbench Version*: **8.0.26**
- *Java FX Version*: **openjfx-11.0.2**
- *Java FX SDK Version*: **javafx-dsk-11.0.2**
- *Java MySQL Connector Version*: **8.0.27** (Located in the mysql-connector-java-8.0.27 folder)

### Project Contribution/Credits
- Source Code
  - src/models/JDBC.java
    - Provided by **Western Governors University**
  - All other source code
    - **Tristan Widler**
- Database design: **Western Governors University**

### Program Starting Procedure

#### Opening the Project

1. Download the project files from this repository.
2. If not already installed, download the JetBrains IntelliJ IDE.
    - **Note:** This project was created in the 2021.1 version of IntelliJ, use other versions or IDE's at your own discretion.
3. From the aformentioned IDE open the project via the "open project" option and select the parenting folder of the downloaded project files.
  
#### Project Setup
Within the IntelliJ IDE:
- Add the MySQL Connector jar to the project library
- Select a project SDK. 
  - **Note:** This project was created with SDK Version 11.0.2. Use a different version at your own discretion.
- In the project configuration, add the vm options statement:
  - "*--module-path ${PATH_TO_FX} --add-modules javafx.fxml,javafx.controls,javafx.graphics*"
  - (This assumes that you have a PATH_TO_FX variable created within IntelliJ that points to a JFX 11.0.2 library)
  
 #### Database Setup
 
 This application expects a locally hosted MySQL database to be running and accessible. The database is expected to have the following attributes:
 - *Location*: **localhost**
 - *Vendor*: **mysql**
 - *Database Name*: **client_schedule**
 - *Connection Username*: **sqlUser**
 - *Connection Password*: **Passw0rd!**
 
 An Enhanced Entity Ralationship Diagram file has been included in this repository. Use MySQL Workbench to open it and forward engineer a locally hosted database with the aforementioned attributes.
 - **Note:** MySQL Workbench V.8.0 was used to create this for the project. User aother versions at your discretion.

The "***src/models/JDBC.java***" file can be modified to fit your connection needs.

## Program Structure

### Javadocs
This project uses JavaDoc to efficiently display its structure. 
To view said documentation:
- Navigate to the JavaDoc folder of the downloaded project files and run the "***index.html***" file. 

Should there be an error, you may generate a new JavaDoc from within IntelliJ via"
- Selecting "**Tools**" and then "**Generate JavaDoc**" from the top menu bar
- Ensuring "**Whole Project**" is selected
- Clicking "**Generate**"

## Program Usage Guide

### General Layout

**Note:**

After the user has logged in, every page will have a navigation bar at the top. Based on the page they are currently using, the add, update, and delete buttons will enable and disable themselves. 
When the user is using an add, update, or delete page, these buttons will not be active, but depending on the page the user is using, the corresponding button will enabled to make it more visible, but will have no function.
If the user wishes to navigate out of said page, the buttons at the bottom of the page, and the function combination box at the top may always be used.
	
#### Log-In Page
The log-In page accepts a username and password which will be checked against the database. 
The user must log into the program before they can begin modifying the database.

**Note:** You may add and remove log-in credentials through MySQL Workbench by modifying the *"users"* table.
	
#### Main Menu

This page serves as a sort of welcome screen. The user can navigate to other screens using the function combo box at the top of the screen. This 'navigation bar' will be at the top of the screen at every page in the program.

#### Reports

The reports page currently offers three different reports:
1. Appointment Totals
    - This report displays the number of appointments currently in the database based on the Type and Month selections which can be changed by using the given combination boxes.
2. Contact Schedules
    - This report allows the user to display all appointments related to a specific contact. The contacts schedule shown can be changed by using the given combination box.
3. Log-In Attempts
    - This report uses the text log-in record file to give the number of log-in attempts based on the type of log-in record selected from the given combination box.
	
#### Scheduling
This page displays all appointments currently in the database. The user can sort these appointments with the radio buttons on the left side of the page.
The user can also add, update, and delete the appointments using the buttons at the top of the page. 
Note that the user must select an appointment from the table before the update page, or delete message will appear.
Within the add and update pages the user may enter or modify the information and can either save their changes or click a cancel button.

#### Records
This page displays all customer records currently in the database. The user can sort these records with the combination boxes on the left side of the page.
Once a selection is made the table will update its contents accordingly. As with the scheduling page, the this page has add, update, and delete buttons along the top of the page.
If the user tries	to delete a record with no associated appointments they will only see a message confirming deletion.
However, if there are associated appointments, a deletion page will appear where the user may delete the associated appointments from a table view before deleting the customer record, should they still choose to do so.
	
