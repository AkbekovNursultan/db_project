import java.sql.*;
import java.util.Scanner;

class Teacher extends User {
    private final Scanner sc = new Scanner(System.in);

    public Teacher(int id, String username, String password) {
        super(id, username, password);
    }

    public void displayMenu() {
        int choice;
        System.out.println("Welcome to School System Teacher");

        do {
            System.out.println("\nPlease select an option:");
            System.out.println("1 - Add Assignment");
            System.out.println("2 - Grade Assignment");
            System.out.println("3 - Show My Tasks");
            System.out.println("4 - Show My Tasks With No Submissions");
            System.out.println("5 - Show Submissions");
            System.out.println("6 - Exit");
            choice = getValidatedMenuChoice();

            switch (choice) {
                case 1 -> addAssignment();
                case 2 -> gradeStudent();
                case 3 -> showMyTasks();
                case 4 -> showMyTasksWithNoSubmissions();
                case 5 -> showSubmissions();
                case 6 -> System.out.println("You have logged out.");
                default -> System.out.println("Invalid option, please try again.");
            }
        } while (choice != 6);
    }

    private int getValidatedMenuChoice() {
        while (true) {
            try {
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                if (choice >= 1 && choice <= 6) {
                    return choice;
                } else {
                    System.out.println("Invalid choice! Please select between 1 and 6.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a numeric choice.");
                sc.next(); // Clear invalid input
            }
        }
    }

    public void addAssignment() {
        String sql = "INSERT INTO assignments (name, description, teacher_id) VALUES (?, ?, ?)";

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            System.out.print("Enter the task name: ");
            sc.nextLine(); // Consume newline
            String taskName = sc.nextLine();

            System.out.print("Enter the Description: ");
            String desc = sc.nextLine();

            if (taskName.isEmpty() || desc.isEmpty()) {
                System.out.println("Task name and description cannot be empty.");
                return;
            }

            pstmt.setString(1, taskName);
            pstmt.setString(2, desc);
            pstmt.setInt(3, id);

            pstmt.executeUpdate();
            System.out.println("You successfully added homework to the system.");
        } catch (SQLException e) {
            System.out.println("Failed to add the homework: " + e.getMessage());
        }
    }

    public void gradeStudent() {
        System.out.print("Enter the assignment ID: ");
        int assignmentId = sc.nextInt();

        System.out.print("Enter the student ID: ");
        int studentId = sc.nextInt();

        System.out.print("Enter the grade: ");
        int grade = sc.nextInt();

        String sql = "UPDATE students_assignments SET grade = ? WHERE assignment_id = ? AND student_id = ?";

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, grade);
            pstmt.setInt(2, assignmentId);
            pstmt.setInt(3, studentId);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Grade updated successfully.");
            } else {
                System.out.println("No matching submission found to update.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to update grade: " + e.getMessage());
        }
    }

    public void showMyTasks() {
        String sql = "SELECT * FROM assignments WHERE teacher_id = ?";

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("My Assignments:");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") + ", Description: " + rs.getString("description"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve tasks: " + e.getMessage());
        }
    }

    public void showMyTasksWithNoSubmissions() {
        String sql = """
                SELECT a.id, a.description
                FROM assignments a
                LEFT JOIN students_assignments sa ON a.id = sa.assignment_id
                WHERE sa.assignment_id IS NULL AND a.teacher_id = ?
                """;

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("Assignments with No Submissions:");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") + ", Description: " + rs.getString("description"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve tasks with no submissions: " + e.getMessage());
        }
    }

    public void showSubmissions() {
        System.out.print("Enter the assignment ID: ");
        int assignmentId = sc.nextInt();

        String sql = """
                SELECT sa.student_id, sa.submission, sa.grade
                FROM students_assignments sa
                WHERE sa.assignment_id = ?
                """;

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, assignmentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("Submissions for Assignment ID " + assignmentId + ":");
                while (rs.next()) {
                    System.out.println("Student ID: " + rs.getInt("student_id") +
                            ", Submission: " + rs.getString("submission") +
                            ", Grade: " + rs.getInt("grade"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve submissions: " + e.getMessage());
        }
    }
}
