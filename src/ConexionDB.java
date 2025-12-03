import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String USER = "root";
    private static final String PASSWORD = "rIvPurbpTTAzdqFNUCRMAbxDIYvHoJJL";
    private static final String JDBC_URL = "jdbc:mysql://root:rIvPurbpTTAzdqFNUCRMAbxDIYvHoJJL@tramway.proxy.rlwy.net:11312/railway";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver de MySQL no encontrado.");
            e.printStackTrace();
            throw new SQLException("Error de driver", e);
        }

        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }


}