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

            rs = stmt.executeQuery();

            if (rs.next()) {
                String accountType = rs.getString("accountType");
                userId = rs.getInt("id");
                System.out.println("Login successfully");
                switch (accountType) {
                    case "Admin":
                        System.out.println("Welcome Admin");
                        //Admin admin = new Admin(id, username, password);
                        //admin.displayMenu();

                        break;
                    case "Teacher":
                        System.out.println("Welcome Teacher");
                        //Teacher teacher = new Teacher(id, username, password);
                        viewer.showTeacherMenu();
                        break;
                    case "Student":
                        System.out.println("Welcome Student");
                        //Student student = new Student(id, username, password);
                        //student.displayMenu();
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
        if(username.isEmpty()){
            username = viewer.getUsername();
            password = viewer.getPassword();
        }
        if (username.isEmpty() || password.isEmpty()) {
            viewer.showLoginError();
        }
        authenticate(username, password);

    }

    public void logout() {
        viewer.showSignIn();
    }

    // Add an assignment (only available to teachers)
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
            viewer.displayOutput("Assignment added successfully!");
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
            SELECT a.id, a.description
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
                    output.append("ID: ").append(rs.getInt("id"))
                            .append(", Description: ").append(rs.getString("description"))
                            .append("\n");
                }
                viewer.displayOutput(output.toString());
                viewer.showTeacherMenu();
            }
        } catch (SQLException e) {
            viewer.displayOutput("Failed to retrieve tasks with no submissions: " + e.getMessage());
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

}
