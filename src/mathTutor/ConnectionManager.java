package mathTutor;
import java.sql.*;

// Used to provide connection to mysql database
public class ConnectionManager {

   // access credentials
   private static String driver = "com.mysql.jdbc.Driver";
   private static String url = "jdbc:mysql://url/";
   private static String dbName = "mathtutor";
   private static String userName = "";
   private static String password = "";
   private static Connection connection;

   // establish connection to mysql database
   public static Connection getConnection() {
      try {
         Class.forName(driver);
      } catch (ClassNotFoundException ex) {
         System.out.println("Driver not found");
         System.exit(1);
      }
      try {
         connection = DriverManager.getConnection(url + dbName, userName, password);
      } catch (SQLException ex) {
         System.out.println("Database connection failed. Check credentials.");
         System.exit(1);
      }
      return connection;
   }
}
