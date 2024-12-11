import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class Student extends User {
    Scanner sc = new Scanner(System.in);

    public Student(int id, String username, String password) {
        super(id, username, password);
    }

    public void displayMenu() {
        int choice;
        System.out.println("Welcome to School System Student");
        do {
            System.out.println("1 - View All Available Assignments");
            System.out.println("2 - View Grades");
            System.out.println("3 - Submit Assignment");
            System.out.println("4 - Update Submitted Assignment");
            System.out.println("5 - Show My Profile");
            System.out.println("6 - Exit");
            choice = doMenuCommand();
        } while (choice != 6);
    }

    public int doMenuCommand() {
        int choice;
        do {
            System.out.print("Enter your choice: ");
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

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
                    updateAssignment();
                    break;
                case 5:
                    showMyProfile();
                    break;
                case 6:
                    System.out.println("You have logged out.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        } while (choice < 1 || choice > 6);
        return choice;
    }

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
        } catch (SQLException e) {
            System.out.println("Error retrieving assignments: " + e.getMessage());
        }
    }

    public void showGrades() {
        String sql = "SELECT s.assignment_id, a.description, s.grade FROM students_assignments s " +
                "JOIN assignments a ON s.assignment_id = a.id " +
                "WHERE s.student_id = ?";

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("Your Grades:");
                while (rs.next()) {
                    int assignmentId = rs.getInt("assignment_id");
                    String description = rs.getString("description");
                    int grade = rs.getInt("grade");
                    System.out.println("Assignment ID: " + assignmentId);
                    System.out.println("Description: " + description);
                    System.out.println("Grade: " + (grade == 0 ? "Not graded yet" : grade));
                    System.out.println("--------------");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving grades: " + e.getMessage());
        }
    }

    public void submitAssignment() {
        System.out.print("Enter the assignment ID: ");
        int assignmentId = sc.nextInt();
        sc.nextLine(); // Consume newline

        System.out.print("Enter your submission (text): ");
        String submission = sc.nextLine();

        String sql = "INSERT INTO students_assignments(student_id, assignment_id, submission) VALUES (?, ?, ?)";

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setInt(2, assignmentId);
            pstmt.setString(3, submission);

            pstmt.executeUpdate();
            System.out.println("Assignment submitted successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to submit assignment: " + e.getMessage());
        }
    }

    public void updateAssignment() {
        System.out.print("Enter the assignment ID you want to update: ");
        int assignmentId = sc.nextInt();
        sc.nextLine(); // Consume newline

        System.out.print("Enter your updated submission (text): ");
        String updatedSubmission = sc.nextLine();

        String sql = "UPDATE students_assignments SET submission = ? WHERE student_id = ? AND assignment_id = ?";

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, updatedSubmission);
            pstmt.setInt(2, id);
            pstmt.setInt(3, assignmentId);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Assignment updated successfully.");
            } else {
                System.out.println("No matching assignment found to update.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to update assignment: " + e.getMessage());
        }
    }

    public void showMyProfile() {
        String sql = "SELECT username, name FROM students WHERE id = ?";

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("username");
                    String name = rs.getString("name");
                    System.out.println("Profile:");
                    System.out.println("Username: " + username);
                    System.out.println("Name: " + name);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving profile: " + e.getMessage());
        }
    }
}
