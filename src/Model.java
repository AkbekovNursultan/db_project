import javax.swing.*;
import java.sql.*;

public class Model {
    private Viewer viewer;
    private int userId;
    private String username;
    private String password;

    public Model(Viewer viewer) {
        this.viewer = viewer;
        username = "";
    }

    // Updated authenticate method to interact with the database
    public void authenticate(String username, String password) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = MyJDBC.getConnection();  // Assuming MyJDBC is your DB connection utility

            String query = "SELECT id, username, password, accountType FROM users WHERE username = ? AND password = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, new String(password));
            System.out.println(username + " " + new String(password));

            rs = stmt.executeQuery();

            if (rs.next()) {
                String accountType = rs.getString("accountType");
                userId = rs.getInt("id");
                System.out.println("Login successfully: " + accountType + "\nID: " + userId);
                switch (accountType) {
                    case "Admin":
                        System.out.println("Welcome Admin");
                        //Admin admin = new Admin(id, username, password);
                        viewer.showAdminMenu();

                        break;
                    case "Teacher":
                        System.out.println("Welcome Teacher");
                        //Teacher teacher = new Teacher(id, username, password);
                        viewer.showTeacherMenu();
                        break;
                    case "Student":
                        System.out.println("Welcome Student");
                        //Student student = new Student(id, username, password);
                        viewer.showStudentMenu();
                        break;
                    default:
                        System.out.println("Invalid account type");
                        break;
                }
            } else {
                System.out.println("Invalid username or password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleSignIn() {
        username = viewer.getUsername();
        password = viewer.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            viewer.showLoginError();
        }
        authenticate(username, password);

    }

    public void logout() {
        username = "";
        password = "";
        viewer.showSignIn();
    }

    // Add an assignment (only available to teachers)
    public void addAssignment(String taskName, String description) {
        String sql = "INSERT INTO assignments (name, description, teacher_id) VALUES (?, ?, ?)";
        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (taskName.isEmpty() || description.isEmpty()) {
                viewer.displayOutput("Task name and description cannot be empty.");
                return;
            }

            pstmt.setString(1, taskName);
            pstmt.setString(2, description);
            pstmt.setInt(3, userId);  // Using userId for teacher

            pstmt.executeUpdate();
            System.out.println("Assignment added successfully!");
            viewer.showTeacherMenu();
        } catch (SQLException e) {
            System.out.println("nah");
            viewer.displayOutput("Failed to add the assignment: " + e.getMessage());
            viewer.showTeacherMenu();
        }
    }

    // Show all tasks assigned to the current user (for teachers)
    public void showMyTasks() {
        String sql = "SELECT * FROM assignments WHERE teacher_id = ?";

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);  // Show tasks for the teacher based on userId
            ResultSet rs = pstmt.executeQuery();

            StringBuilder output = new StringBuilder("My Assignments:\n");
            while (rs.next()) {
                output.append("ID: ").append(rs.getInt("id"))
                        .append("\nName: ").append(rs.getString("name"))
                        .append("\nDescription: ").append(rs.getString("description"))
                        .append("\n---------------\n");
            }
            System.out.println(output.toString());
            viewer.displayOutput(output.toString());

        } catch (SQLException e) {
            viewer.displayOutput("Failed to retrieve tasks: " + e.getMessage());
            viewer.showTeacherMenu();
        }
    }

    // Grade a student (only available to teachers)
    public void gradeStudent(int assignmentId, int studentId, int grade) {
        String sql = "UPDATE students_assignments SET grade = ? WHERE assignment_id = ? AND student_id = ?";

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, grade);
            pstmt.setInt(2, assignmentId);
            pstmt.setInt(3, studentId);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                viewer.displayOutput("Grade updated successfully.");
            } else {
                viewer.displayOutput("No matching submission found to update.");
            }
            viewer.showTeacherMenu();
        } catch (SQLException e) {
            viewer.displayOutput("Failed to update grade: " + e.getMessage());
            viewer.showTeacherMenu();
        }
    }

    // Show tasks with no submissions (for teachers)
    public void showMyTasksWithNoSubmissions() {
        String sql = """
            SELECT a.id, a.name, a.description
            FROM assignments a
            LEFT JOIN students_assignments sa ON a.id = sa.assignment_id
            WHERE sa.assignment_id IS NULL AND a.teacher_id = ?
            """;

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);  // Filter by teacher's userId
            try (ResultSet rs = pstmt.executeQuery()) {
                StringBuilder output = new StringBuilder("Assignments with No Submissions:\n");
                while (rs.next()) {
                    output.append("\nID: ").append(rs.getInt("id"))
                            .append("\nTask name: ").append("name")
                            .append("\nDescription: ").append(rs.getString("description"))
                            .append("\n--------------");
                }
                viewer.displayOutput(output.toString());
            }
        } catch (SQLException e) {
            System.out.println(e);
            viewer.showTeacherMenu();
        }
    }

    // Show submissions for a specific assignment (for teachers)
    public void showSubmissions() {
        String sql = """
            SELECT sa.student_id, sa.submission, sa.grade
            FROM students_assignments sa
            JOIN assignments a ON sa.assignment_id = a.id
            WHERE a.teacher_id = ? AND sa.submission IS NOT NULL           
            """;

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                StringBuilder output = new StringBuilder("Submissions:\n");
                while (rs.next()) {
                    output.append("Student ID: ").append(rs.getInt("student_id"))
                            .append("\nSubmission: ").append(rs.getString("submission"))
                            .append("\nGrade: ").append(rs.getInt("grade"))
                            .append("\n-------------\n");
                }
                System.out.println(output.toString());
                viewer.displayOutput(output.toString());
            }
        } catch (SQLException e) {
            viewer.displayOutput("Failed to retrieve submissions: " + e.getMessage());
            viewer.showTeacherMenu();
        }
    }

    public void showMyProfile() {
        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            StringBuilder output = new StringBuilder();
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    output.append("---------------")
                            .append("\nID: ").append(rs.getInt("id"))
                            .append("\nName: ").append(rs.getString("name"))
                            .append("\nUsername: ").append(rs.getString("username"))
                            .append("\n---------------\n");

                }
            }
            viewer.displayOutput(output.toString());
        } catch (SQLException e) {
            System.out.println("Error retrieving profile: " + e.getMessage());
            viewer.showStudentMenu();
        }
    }
    public void showAllAssignments() {
        String sql = "SELECT * FROM assignments";
        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            StringBuilder output = new StringBuilder("My Assignments:\n");
            while (rs.next()) {
                output.append("\nID: ").append(rs.getInt("id"))
                        .append("\nName: ").append(rs.getString("name"))
                        .append("\nDescription: ").append(rs.getString("description"))
                        .append("\n---------------\n");
            }
            System.out.println(output.toString());
            viewer.displayOutput(output.toString());

        } catch (SQLException e) {
            viewer.displayOutput("Failed to retrieve tasks: " + e.getMessage());
            viewer.showStudentMenu();
        }
    }
    public void showAllAvailableAssignments(){
        String sql = "SELECT a.id, a.name, a.description FROM assignments a " +
                "LEFT JOIN students_assignments sa ON a.id = sa.assignment_id AND sa.student_id = ? " +
                "WHERE sa.assignment_id IS NULL";  // Only show assignments that have not been submitted

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);  // Show tasks for the teacher based on userId
            ResultSet rs = pstmt.executeQuery();

            StringBuilder output = new StringBuilder("My Assignments:\n");
            while (rs.next()) {
                output.append("\nID: ").append(rs.getInt("id"))
                        .append("\nName: ").append(rs.getString("name"))
                        .append("\nDescription: ").append(rs.getString("description"))
                        .append("\n---------------\n");
            }
            System.out.println(output.toString());
            viewer.displayOutput(output.toString());
            viewer.addSubmitAssignmentButton();

        } catch (SQLException e) {
            viewer.displayOutput("Failed to retrieve tasks: " + e.getMessage());
            viewer.showTeacherMenu();
        }
    }
    public void showGrades(){
        String sql =
                "SELECT a.id, a.name, a.description, " +
                "s.submission, s.grade FROM assignments a " +
                "JOIN students_assignments s ON a.id = s.assignment_id " +
                "WHERE s.student_id = ?";
        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            StringBuilder output = new StringBuilder();
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    output.append("---------------")
                            .append("\nID: ").append(rs.getInt("id"))
                            .append("\nAssignment name: ").append(rs.getString("name"))
                            .append("\nGrade: ").append(rs.getString("grade"))
                            .append("\n---------------\n");

                }
            }
            viewer.displayOutput(output.toString());
        } catch (SQLException e) {
            System.out.println("Error retrieving grades: " + e.getMessage());
            viewer.showStudentMenu();
        }
    }
    public void showMySubmissions(){
        String sql = "SELECT sa.assignment_id, a.name, sa.submission, sa.grade " +
                "FROM students_assignments sa JOIN assignments a ON sa.assignment_id = a.id  " +
                "WHERE sa.student_id = ?";
        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            StringBuilder output = new StringBuilder();
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    output.append("---------------")
                            .append("\nID: ").append(rs.getInt("assignment_id"))
                            .append("\nName: ").append(rs.getString("name"))
                            .append("\nSubmission: ").append(rs.getString("submission"))
                            .append("\nGrade: ").append(rs.getString("grade"))
                            .append("\n---------------\n");

                }
            }
            viewer.displayOutput(output.toString());
        } catch (SQLException e) {
            System.out.println("Error retrieving grades: " + e.getMessage());
            viewer.showStudentMenu();
        }
    }
    public void submitAssignment(int taskId, String submission) {
        // First, try to update the existing submission.
        String updateSql = "UPDATE students_assignments SET submission = ?, grade = 0 WHERE student_id = ? AND assignment_id = ?";
        String insertSql = "INSERT INTO students_assignments (student_id, assignment_id, submission) VALUES (?, ?, ?)";

        try (Connection conn = MyJDBC.getConnection()) {
            // Check if a submission already exists
            String checkSql = "SELECT COUNT(*) FROM students_assignments WHERE student_id = ? AND assignment_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, userId);
                checkStmt.setInt(2, taskId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // If the student already has a submission, update it
                        try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                            pstmt.setString(1, submission);
                            pstmt.setInt(2, userId);
                            pstmt.setInt(3, taskId);
                            int rowsUpdated = pstmt.executeUpdate();
                            if (rowsUpdated > 0) {
                                System.out.println("Submission updated.");
                            } else {
                                System.out.println("No matching task was found to update.");
                            }
                        }
                    } else {
                        // If no submission exists, insert a new one
                        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                            pstmt.setInt(1, userId);
                            pstmt.setInt(2, taskId);
                            pstmt.setString(3, submission);
                            int rowsInserted = pstmt.executeUpdate();
                            if (rowsInserted > 0) {
                                System.out.println("Submission added.");
                            } else {
                                System.out.println("Failed to add submission.");
                            }
                        }
                    }
                }
            }

            viewer.showStudentMenu();

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
