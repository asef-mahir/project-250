# QUICK START - Fix Database Connection Error

## The Problem
```
Error: cannot invoke "java.sql.connection.prepareStatement(String)" 
because "c.c" is null
```

## The Root Cause
🔴 **Missing MySQL JDBC Driver** - The `mysql-connector-java.jar` file is not in the project's `lib` folder.

## The Solution (3 Steps)

### ✅ STEP 1: Download MySQL Connector/J
1. Visit: https://dev.mysql.com/downloads/connector/j/
2. Select version **8.0.33** (or latest 8.x)
3. Download **Platform Independent** ZIP
4. Extract the ZIP file

### ✅ STEP 2: Add JAR to Your Project
1. Locate: `mysql-connector-java-8.0.33.jar` from the extracted folder
2. Copy it to: `University Management System/lib/`
3. Final path should be: `University Management System/lib/mysql-connector-java-8.0.33.jar`

### ✅ STEP 3: Rebuild & Run
**Option A - Using NetBeans:**
- Right-click project → **Clean and Build**
- Right-click project → **Run**

**Option B - Using Command Line:**
```bash
cd "University Management System"
ant clean
ant build
java -jar dist/University_Management_System.jar
```

---

## Verify It Works

When you start the app, you should see in console:
```
Database connected successfully!
```

Then the login/registration window appears without errors ✅

---

## Database Setup (Choose One)

### For Local MySQL
```bash
mysql -u root -p
CREATE DATABASE universitymanagementsystem;
```

Create file: `University Management System/db.properties`
```properties
db.url=jdbc:mysql://localhost:3306/universitymanagementsystem?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=root
=
```

### For Railway Cloud (Alternative)
1. Create account at https://railway.app/
2. Create MySQL database in Railway dashboard
3. Copy connection credentials from Railway
4. Create `db.properties` with Railway credentials:
```properties
db.url=jdbc:mysql://mysql-XXXXX.e.aivencloud.com:XXXXX/universitymanagementsystem?useSSL=true&requireSSL=true&verifyServerCertificate=false&serverTimezone=UTC
db.user=avnadmin
=
```

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| Still says "JDBC Driver not found" | ❌ JAR not in right location. Check: `University Management System/lib/mysql-connector-java-8.0.33.jar` |
| "Connection refused" | ❌ MySQL server not running or wrong credentials in `db.properties` |
| "Unknown database" | ❌ Create database: `CREATE DATABASE universitymanagementsystem;` |
| "Access denied" | ❌ Update password in `db.properties` |

---

## Still Stuck?

Read the detailed guides:
- 📖 `TROUBLESHOOT_DATABASE_ERROR.md` - Comprehensive troubleshooting
- 📖 `FIX_DATABASE_CONNECTION.md` - Detailed setup instructions
- 🔧 Run setup script: `.\setup_database.ps1` or `setup_database.bat`

---

**That's it! 🎉 Your app should now connect to the database properly.**
