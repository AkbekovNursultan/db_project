import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.ResultSet;
class Student extends User {
    Scanner sc = new Scanner(System.in);

    public Student(int id, String username, String password) {
        super(id, username, password);

    }
    public void displayMenu() {
        int choice;
        System.out.println("Welcome to School System Student");
        do {
            System.out.println("1-ViewAllAvailableAssignments");
            System.out.println("2-ViewGrades");
            System.out.println("3-SubmitAssignment");
            System.out.println("4-exit");
            choice=doMenuCommand();
        }while(choice != 4);
    }

    public int doMenuCommand() {
        int choice;
        do {
            choice=sc.nextInt();
            switch (choice) {
                case 1:
                    showAllAvailableAssignments();
                    break;
                case 2:
                    showGrades();
                    break;
                case 3:
                    submitAssignment();
                    break;
                case 4:
                    System.out.println("You have logged out.");
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }while (choice < 1 || choice > 4);
        return choice;
    }
    public void showAllAssignments() {}
    public void showAssignmentDetails() {}
    public void showAllAvailableAssignments() {
        String sql = "SELECT id, name, description FROM assignments";

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("Available Assignments:");
            while (rs.next()) {
                int homeworkID = rs.getInt("id");
                String taskName = rs.getString("name");
                String description = rs.getString("description");
                System.out.println("Homework ID: " + homeworkID);
                System.out.println("Name: " + taskName);
                System.out.println("Description: " + description);
                System.out.println("--------------");
            }
            System.out.println();
            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error retrieving assignments: " + e.getMessage());
        }

    }
    public void showGrades() {

    }
    public void submitAssignment() {

    }
    public void updateAssignment() {

    }
    public void showMyProfile() {

    }
}