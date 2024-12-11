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
        String sql = "SELECT a.id, a.name, a.description FROM assignments a " +
                "LEFT JOIN students_assignments sa ON a.id = sa.assignment_id AND sa.student_id = ? " +
                "WHERE sa.assignment_id IS NULL";  // Only show assignments that have not been submitted

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);  // Set student ID in the query

            try (ResultSet rs = pstmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving assignments: " + e.getMessage());
        }
    }

    public void showGrades() {
        String sql = "SELECT a.id AS assignment_id, a.name AS assignment_name, a.description, " +
                "s.submission, s.grade FROM assignments a " +
                "JOIN students_assignments s ON a.id = s.assignment_id " +
                "WHERE s.student_id = ?";

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("Your Grades:");
                while (rs.next()) {
                    int assignmentId = rs.getInt("assignment_id");
                    String assignmentName = rs.getString("assignment_name");
                    String description = rs.getString("description");
                    String submission = rs.getString("submission");
                    int grade = rs.getInt("grade");

                    System.out.println("Assignment ID: " + assignmentId);
                    System.out.println("Name: " + assignmentName);
                    System.out.println("Description: " + description);
                    System.out.println("Submission: " + (submission == null || submission.isEmpty() ? "No submission yet" : submission));
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

        // First, check if the assignment has already been submitted
        String checkSql = "SELECT * FROM students_assignments WHERE student_id = ? AND assignment_id = ?";

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {

            checkPstmt.setInt(1, id);
            checkPstmt.setInt(2, assignmentId);

            try (ResultSet rs = checkPstmt.executeQuery()) {
                if (rs.next()) {
                    // If entry already exists, update the submission instead of inserting
                    String updateSql = "UPDATE students_assignments SET submission = ? WHERE student_id = ? AND assignment_id = ?";
                    try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                        updatePstmt.setString(1, submission);
                        updatePstmt.setInt(2, id);
                        updatePstmt.setInt(3, assignmentId);

                        int rowsUpdated = updatePstmt.executeUpdate();
                        if (rowsUpdated > 0) {
                            System.out.println("Assignment updated successfully.");
                        }
                    }
                } else {
                    // If no existing entry, insert a new submission
                    String insertSql = "INSERT INTO students_assignments(student_id, assignment_id, submission) VALUES (?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                        pstmt.setInt(1, id);
                        pstmt.setInt(2, assignmentId);
                        pstmt.setString(3, submission);
                        pstmt.executeUpdate();
                        System.out.println("Assignment submitted successfully.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to submit assignment: " + e.getMessage());
        }
    }


    public void updateAssignment() {
        System.out.print("Enter the assignment ID you want to update: ");
        int assignmentId = sc.nextInt();
        sc.nextLine(); // Consume newline

        // Fetch the existing submission before updating it
        String fetchSql = "SELECT submission FROM students_assignments WHERE student_id = ? AND assignment_id = ?";
        String oldSubmission = null;

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement fetchPstmt = conn.prepareStatement(fetchSql)) {

            fetchPstmt.setInt(1, id);
            fetchPstmt.setInt(2, assignmentId);

            try (ResultSet rs = fetchPstmt.executeQuery()) {
                if (rs.next()) {
                    oldSubmission = rs.getString("submission");
                    System.out.println("Current Submission: " + oldSubmission);
                } else {
                    System.out.println("No submission found for the given assignment.");
                    return; // Exit if there's no submission to update
                }
            }

            // Now ask for the updated submission
            System.out.print("Enter your updated submission (text): ");
            String updatedSubmission = sc.nextLine();

            // Update the assignment submission
            String updateSql = "UPDATE students_assignments SET submission = ? WHERE student_id = ? AND assignment_id = ?";

            try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                updatePstmt.setString(1, updatedSubmission);
                updatePstmt.setInt(2, id);
                updatePstmt.setInt(3, assignmentId);

                int rowsUpdated = updatePstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Assignment updated successfully.");
                    System.out.println("Updated Submission: " + updatedSubmission);
                    System.out.println("Changes: ");
                    System.out.println("Old Submission: " + oldSubmission);
                    System.out.println("New Submission: " + updatedSubmission);
                } else {
                    System.out.println("No matching assignment found to update.");
                }
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
