import java.sql.*;
import java.sql.SQLException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws SQLException {
        // Viewer viewer = new Viewer();
        Scanner scanner = new Scanner(System.in);
        login(scanner);

    }

    public static void login(Scanner scanner) {
        ResultSet rs = null;
        Connection connection = null;
        PreparedStatement stmt = null;
        boolean loggedIn = false;
        while(true){
            try {
                connection = MyJDBC.getConnection();
                System.out.println("Enter your username: \n(Leave blank to exit the program)");
                String username = scanner.nextLine();
                if(username.isBlank()){
                    break;
                }
                System.out.println("Enter your password: ");
                String password = scanner.nextLine();

                String query = "SELECT id, username , password , accountType FROM users WHERE username =? AND password =?";
                stmt = connection.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, password);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    String accountType = rs.getString("accountType");
                    int id = rs.getInt("id");
                    System.out.println("Login successfully");
                    switch (accountType) {
                        case "Admin":
                            System.out.println("Welcome Admin");
                            Admin admin = new Admin(id, username, password);
                            admin.displayMenu();

                            break;
                        case "Teacher":
                            System.out.println("Welcome Teacher");
                            Teacher teacher = new Teacher(id, username, password);
                            teacher.displayMenu();
                            break;
                        case "Student":
                            System.out.println("Welcome Student");
                            Student student = new Student(id, username, password);
                            student.displayMenu();
                            break;
                        default:
                            System.out.println("Invalid account type");
                            break;
                    }
                } else {
                    System.out.println("Invalid username or password");
                }

            } catch (SQLException e) {
                System.out.println("Connection error" + e.getMessage());

            } finally {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                    if (connection != null) connection.close();
                } catch (SQLException ex) {
                    System.out.println("Connection error" + ex.getMessage());
                }
            }
        }
    }

}
