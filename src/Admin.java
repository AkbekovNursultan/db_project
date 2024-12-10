import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

class Admin extends User {
    Scanner sc = new Scanner(System.in);

    public Admin(int id, String username, String password) {
        super(id, username, password);
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("Welcome to School System Administrator");
            System.out.println("1-Add students");
            System.out.println("2-Remove students");
            System.out.println("3-Add courses");
            System.out.println("4-Remove courses");
            System.out.println("5-Exit");
            choice = doMenuCommand();
        } while (choice != 5);
    }

    public int doMenuCommand() {
        int choice;
        do {
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    addUser();
                    break;
                case 2:
                    removeUser();
                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:
                    System.out.println("You have exited the system.");
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        } while (choice < 1 || choice > 5);
        return choice;
    }
    public void showAllUsers() {

    }
    public void showAllStudents() {

    }
    public void showAllTeachers() {

    }
    public void addUser() {


        String sql = "INSERT INTO users(username, password, name, accountType) VALUES (?, ?, ?, ?)";

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            System.out.println("Enter the username:");
            String username = sc.next();

            System.out.println("Enter the password:");
            String password = sc.next();

            System.out.println("Enter the name:");
            String name = sc.next();

            System.out.println("Enter the account type:");
            String accountType = sc.next();

            // Установка параметров запроса
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, name);
            pstmt.setString(4, accountType);

            // Выполнение запроса
            pstmt.executeUpdate();
            System.out.println("You successfully added participant to the system.");
        } catch (SQLException e) {
            System.out.println("You failed to add the participant : " + e.getMessage());
        }

    }
    public void removeUser() {

        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            System.out.println("Enter the ID of the participant to remove:");
            int id = sc.nextInt();

            // Установка параметра запроса
            pstmt.setInt(1, id);

            // Выполнение запроса
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Student successfully removed.");
            } else {
                System.out.println("Student with the specified ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error removing student: " + e.getMessage());
        }
    }
    public void showAllAssignments() {

    }

    public void deleteAssignment() {

    }
}

