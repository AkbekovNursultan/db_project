import java.sql.*;
import java.util.Scanner;

public class MyJDBC {


    private static final String url = "jdbc:postgresql://localhost:5434/project";

    private static final String user = "postgres";

    private static final String password = "1234";



    public static Connection getConnection() throws SQLException {
       return DriverManager.getConnection(url , user , password);

    }




}
