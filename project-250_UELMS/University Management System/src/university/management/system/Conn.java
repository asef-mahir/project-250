
package university.management.system;

import java.sql.*;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Conn implements AutoCloseable {
    public Connection c;
    public Statement s;
    private boolean initialized = false;
    
    public Conn() {
        c = null;
        s = null;
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Load database configuration from properties file or environment variables
            Properties props = loadDbProperties();
            
            String url = propOrSysEnv(props, "db.url", "DB_URL");
            String user = propOrSysEnv(props, "db.user", "DB_USER");
            String password = propOrSysEnv(props, "", "DB_PASS");
            
            // Fallback to default localhost if no configuration found
            if (url == null || url.isEmpty()) {
                url = "jdbc:mysql://localhost:3306/universitymanagementsystem?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                System.out.println("Warning: Using default localhost connection. Set DB_URL/DB_USER/DB_PASS or a local db.properties file.");
            }
            if (user == null || user.isEmpty()) {
                user = "root";
            }
            if (password == null) {
                password = "";
            }
            
            // Establish connection
            c = DriverManager.getConnection(url, user, password);
            s = c.createStatement();
            initialized = true;
            
            System.out.println("Database connected successfully!");
            
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
            String errorMsg = "MySQL JDBC Driver (mysql-connector-java) not found in classpath!\n\n" +
                "To fix this:\n" +
                "1. Download mysql-connector-java-8.0.28.jar\n" +
                "2. Place it in the 'lib' folder of your project\n" +
                "3. Rebuild the project\n" +
                "4. Ensure the JAR is in your classpath when running\n\n" +
                "Alternative: Ensure mysql-connector-java.jar is in the classpath.";
            System.err.println(errorMsg);
            showErrorDialog(errorMsg);
            throw new IllegalStateException("MySQL JDBC Driver not found. Please install mysql-connector-java.jar", e);
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
            String errorMsg = "Failed to connect to database!\n\nError: " + e.getMessage() + 
                "\n\nPlease check:\n1. Database server is running\n2. db.properties or DB_URL/DB_USER/DB_PASS are configured correctly\n3. Network connection is available\n4. Database credentials are correct\n5. Firewall is not blocking the connection";
            System.err.println(errorMsg);
            showErrorDialog(errorMsg);
            throw new IllegalStateException("Failed to connect to database: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            showErrorDialog("Unexpected error: " + e.getMessage());
            throw new IllegalStateException("Unexpected error creating database connection", e);
        }
    }
    
    private static Properties loadDbProperties() {
        Properties props = new Properties();
        
        // 1) Explicit path via -Ddb.config system property
        String cfgPath = System.getProperty("db.config");
        if (cfgPath != null && !cfgPath.isEmpty()) {
            if (tryLoad(props, cfgPath)) {
                System.out.println("Loaded db.properties from: " + cfgPath);
                return props;
            }
        }
        
        // 2) Next to the running JAR: db.properties
        File jarDir = getJarDir();
        if (jarDir != null) {
            File nextToJar = new File(jarDir, "db.properties");
            if (tryLoad(props, nextToJar.getAbsolutePath())) {
                System.out.println("Loaded db.properties from: " + nextToJar.getAbsolutePath());
                return props;
            }
        }
        
        // 3) In project root directory
        File projectRoot = new File("db.properties");
        if (tryLoad(props, projectRoot.getAbsolutePath())) {
            System.out.println("Loaded db.properties from: " + projectRoot.getAbsolutePath());
            return props;
        }
        
        // 4) User home fallback: ~/.uems/db.properties
        String home = System.getProperty("user.home");
        if (home != null) {
            File homeFile = new File(new File(home, ".uems"), "db.properties");
            if (tryLoad(props, homeFile.getAbsolutePath())) {
                System.out.println("Loaded db.properties from: " + homeFile.getAbsolutePath());
                return props;
            }
        }
        
        // 5) Classpath fallback (for development)
        try (InputStream in = Conn.class.getResourceAsStream("/db.properties")) {
            if (in != null) {
                props.load(in);
                System.out.println("Loaded db.properties from classpath");
                return props;
            }
        } catch (Exception ignored) {}
        
        return props; // May be empty if no config found
    }
    
    private static boolean tryLoad(Properties props, String path) {
        if (path == null || path.isEmpty()) return false;
        try {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    props.load(fis);
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load properties from: " + path);
        }
        return false;
    }
    
    private static String propOrSysEnv(Properties props, String propKey, String envKey) {
        // First try system property
        String value = System.getProperty(propKey);
        if (value != null && !value.isEmpty()) return value;
        
        // Then try properties file
        value = props.getProperty(propKey);
        if (value != null && !value.isEmpty()) return value;
        
        // Finally try environment variable
        value = System.getenv(envKey);
        return (value != null && !value.isEmpty()) ? value : null;
    }
    
    private static File getJarDir() {
        try {
            String url = Conn.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            File f = new File(url);
            return f.isFile() ? f.getParentFile() : f;
        } catch (Exception e) {
            return null;
        }
    }
    
    private static void showErrorDialog(String message) {
        try {
            javax.swing.JOptionPane.showMessageDialog(null, message, 
                "Database Connection Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception ignored) {
            // If Swing is not available, just print to console
        }
    }
    
    // Add method to check if connection is valid
    public boolean isConnected() {
        try {
            return initialized && c != null && !c.isClosed() && c.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
    
    // Add method to close connection properly
    @Override
    public void close() {
        try {
            if (s != null) {
                s.close();
            }
            if (c != null && !c.isClosed()) {
                c.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}








