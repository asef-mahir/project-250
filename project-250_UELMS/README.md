# University Result Management System

This is a comprehensive desktop application developed using Java and MySQL, designed to manage university grading, student results, certificate generation, and dashboard functionalities for Students, Teachers, and Administrators.

## Important: How to Run the Project (For Evaluation)

This project connects to a live **Aiven Cloud Database** by default, meaning no local database setup is necessary!
**To start the project, simply go to `University Management System/dist` and double-click the `University_Management_System.jar` file.**

---

### Fallback: Running on a Local Database (If Cloud is Offline)

If the cloud database ever goes offline or becomes unreachable, follow these steps to use your local MySQL server instead:

#### Step 1: Import the Database
1. Open your local database tool (e.g., **MySQL Workbench** or **XAMPP / phpMyAdmin**).
2. Create a new database or simply import the provided SQL dump.
3. Import the `university_local_dump.sql` file located in the root folder of this project. 
*(This file contains the complete database backup required to run the application).*

#### Step 2: Configure Database Credentials
1. Open the file `University Management System/dist/db.properties` in any text editor.
2. Comment out the Aiven Cloud `db.url`, `db.user`, and `db.pass` lines (add a `#` at the beginning of those lines).
3. Uncomment the "Backup Localhost Connection" lines (remove the `#`).
4. If your local MySQL `root` user has a password, update the `db.pass=` field to match your password. *(Example: `db.pass=root123`)*

#### Step 3: Run the Application
1. Double-click the `University_Management_System.jar` file again.

---
*Note: The codebase contains the full Java source code in the `src` directory, necessary template files, and resources directly mapped to meet the coursework requirements.*
