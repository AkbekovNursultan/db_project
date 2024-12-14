import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Viewer {

    private JFrame frame;
    private JPanel mainPanel;
    private JTextField userField;
    private JPasswordField passwordField;
    private JButton signInButton;
    private JButton goBack;
    private JScrollPane scrollPane;
    private JTextArea displayArea;  // Area for displaying output to the user
    private Controller controller;
    private Model model;
    private JButton additionalButton;

    public Viewer() {
        controller = new Controller(this);
        model = controller.getModel();
        frame = new JFrame("Sign In");
        frame.setSize(500, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        goBack = new JButton("Go back");
        goBack.setBounds(300, 600, 100, 50);
        goBack.addActionListener(controller);
        goBack.setActionCommand("backToMenu");
        displayArea = new JTextArea();
        displayArea.setBounds(50, 50, frame.getWidth() - 50, frame.getHeight() - 50);
        displayArea.setEditable(false);  // Make it non-editable
        displayArea.setFont(new Font("Arial", Font.PLAIN, 14));
        scrollPane = new JScrollPane(displayArea);
        scrollPane.setBounds(50, 50, 400, 500);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);

        showSignIn();
    }

    public void showSignIn(){
        mainPanel.removeAll();
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(150, 150, 100, 30);
        userField = new JTextField();
        userField.setBounds(150, 190, 200, 30);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(150, 230, 100, 30);
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 270, 200, 30);

        signInButton = new JButton("Sign In");
        signInButton.setBounds(150, 310, 200, 30);
        signInButton.addActionListener(controller);
        signInButton.setActionCommand("signin");

        mainPanel.add(userLabel);
        mainPanel.add(userField);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);
        mainPanel.add(signInButton);

        frame.add(mainPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
        showFrame();
    }

    public void showFrame() {
        frame.setVisible(true);
    }

    public void showAdminMenu() {
        mainPanel.removeAll();

        JLabel welcomeLabel = new JLabel("Welcome Sir!");
        welcomeLabel.setBounds(150, 50, 350, 30);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Buttons for Teacher's menu options
        JButton showUsersButton = new JButton("Show users");
        JButton showStudentsButton = new JButton("Show students");
        JButton showTeachersButton = new JButton("Show teachers");
        JButton addUserButton = new JButton("Add user");
        JButton deleteUserButton = new JButton("Delete user");
        JButton logoutButton = new JButton("Log Out");

        showUsersButton.setBounds(150, 100, 200, 30);
        showStudentsButton.setBounds(150, 140, 200, 30);
        showTeachersButton.setBounds(150, 180, 200, 30);
        addUserButton.setBounds(150, 220, 200, 30);
        deleteUserButton.setBounds(150, 260, 200, 30);
        logoutButton.setBounds(150, 300, 200, 30);

        // Adding buttons to the panel
        mainPanel.add(welcomeLabel);
        mainPanel.add(showUsersButton);
        mainPanel.add(showStudentsButton);
        mainPanel.add(showTeachersButton);
        mainPanel.add(addUserButton);
        mainPanel.add(deleteUserButton);
        mainPanel.add(logoutButton);

        // Button listeners
        showUsersButton.addActionListener(controller);
        showStudentsButton.addActionListener(controller);
        showTeachersButton.addActionListener(controller);
        addUserButton.addActionListener(controller);
        deleteUserButton.addActionListener(controller);
        logoutButton.addActionListener(controller);

        showUsersButton.setActionCommand("showUsers");
        showStudentsButton.setActionCommand("showStudents");
        showTeachersButton.setActionCommand("showTeachers");
        addUserButton.setActionCommand("addUser");
        deleteUserButton.setActionCommand("deleteUser");
        logoutButton.setActionCommand("logout");

        mainPanel.revalidate();
        mainPanel.repaint();
        showFrame();
    }
    public void addUser(){
        mainPanel.removeAll();

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(150, 50, 100, 30);
        JTextField usernameField = new JTextField();
        usernameField.setBounds(150, 80, 200, 30);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(150, 120, 100, 30);
        JTextField nameField = new JTextField();
        nameField.setBounds(150, 150, 200, 30);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(150, 190, 100, 30);
        JTextField passwordField = new JTextField();
        passwordField.setBounds(150, 220, 200, 30);

        JLabel typeLabel = new JLabel("Account type:");
        typeLabel.setBounds(150, 250, 100, 30);
        JTextField typeField = new JTextField();
        typeField.setBounds(150, 280, 200, 30);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(150, 350, 200, 30);
        submitButton.addActionListener(e -> {
            // Retrieve the values from the input fields
            String name = nameField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String accountType = typeField.getText();

            // Call the Model's gradeStudent method
            model.addUser(name, username, password, accountType);
        });

        mainPanel.add(nameLabel);
        mainPanel.add(nameField);
        mainPanel.add(usernameLabel);
        mainPanel.add(usernameField);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);
        mainPanel.add(typeLabel);
        mainPanel.add(typeField);
        mainPanel.add(submitButton);
        mainPanel.add(goBack);

        mainPanel.revalidate();
        mainPanel.repaint();
        showFrame();
    }
    public void deleteUser(){
        mainPanel.removeAll();

        JLabel idLabel = new JLabel("Delete user\nID:");
        idLabel.setBounds(150, 50, 100, 30);
        JTextField idField = new JTextField();
        idField.setBounds(150, 80, 200, 30);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(150, 150, 200, 30);
        submitButton.addActionListener(e -> {
            // Retrieve the values from the input fields
            int id = Integer.parseInt(idField.getText());

            // Call the Model's gradeStudent method
            model.deleteUser(id);
        });

        mainPanel.add(idLabel);
        mainPanel.add(idField);
        mainPanel.add(submitButton);
        mainPanel.add(goBack);

        mainPanel.revalidate();
        mainPanel.repaint();
        showFrame();
    }


    public void showTeacherMenu() {
        mainPanel.removeAll();

        JLabel welcomeLabel = new JLabel("Welcome Teacher!");
        welcomeLabel.setBounds(150, 50, 350, 30);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Buttons for Teacher's menu options
        JButton addAssignmentButton = new JButton("Add Assignment");
        JButton gradeAssignmentButton = new JButton("Grade Assignment");
        JButton showTasksButton = new JButton("Show My Tasks");
        JButton showTasksNoSubButton = new JButton("Show Tasks with No Submissions");
        JButton showSubmissionsButton = new JButton("Show Submissions");
        JButton logoutButton = new JButton("Log Out");

        addAssignmentButton.setBounds(150, 100, 200, 30);
        gradeAssignmentButton.setBounds(150, 140, 200, 30);
        showTasksButton.setBounds(150, 180, 200, 30);
        showTasksNoSubButton.setBounds(150, 220, 200, 30);
        showSubmissionsButton.setBounds(150, 260, 200, 30);
        logoutButton.setBounds(150, 300, 200, 30);

        // Adding buttons to the panel
        mainPanel.add(welcomeLabel);
        mainPanel.add(addAssignmentButton);
        mainPanel.add(gradeAssignmentButton);
        mainPanel.add(showTasksButton);
        mainPanel.add(showTasksNoSubButton);
        mainPanel.add(showSubmissionsButton);
        mainPanel.add(logoutButton);

        // Button listeners
        addAssignmentButton.addActionListener(controller);
        gradeAssignmentButton.addActionListener(controller);
        showTasksButton.addActionListener(controller);
        showTasksNoSubButton.addActionListener(controller);
        showSubmissionsButton.addActionListener(controller);
        logoutButton.addActionListener(controller);

        addAssignmentButton.setActionCommand("addAssignment");
        gradeAssignmentButton.setActionCommand("gradeAssignment");
        showTasksButton.setActionCommand("showTasks");
        showTasksNoSubButton.setActionCommand("showTasksNoSubmissions");
        showSubmissionsButton.setActionCommand("showSubmissions");
        logoutButton.setActionCommand("logout");

        mainPanel.revalidate();
        mainPanel.repaint();
        showFrame();
    }
    public void showAddAssignmentMenu() {
        mainPanel.removeAll();

        // Labels and text fields for task name and description
        JLabel taskNameLabel = new JLabel("Task Name:");
        taskNameLabel.setBounds(150, 50, 100, 30);
        JTextField taskNameField = new JTextField();
        taskNameField.setBounds(150, 80, 200, 30);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(150, 120, 100, 30);
        JTextField descriptionField = new JTextField();
        descriptionField.setBounds(150, 150, 200, 30);

        // Button to submit the new assignment
        JButton addAssignmentButton = new JButton("Add Assignment");
        addAssignmentButton.setBounds(150, 190, 200, 30);
        addAssignmentButton.addActionListener(e -> {
            // Get the task name and description from input fields
            String taskName = taskNameField.getText();
            String description = descriptionField.getText();
            System.out.println(taskName + "  " + description);
            // Validate inputs before calling the model method
            if (taskName.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Both Task Name and Description must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Call the Model's addAssignment method with the entered values
                model.addAssignment(taskName, description);
            }
        });

        // Add components to the panel
        mainPanel.add(taskNameLabel);
        mainPanel.add(taskNameField);
        mainPanel.add(descriptionLabel);
        mainPanel.add(descriptionField);
        mainPanel.add(addAssignmentButton);
        mainPanel.add(goBack);

        // Update the frame
        mainPanel.revalidate();
        mainPanel.repaint();
        showFrame();
    }
    public void showGradeAssignmentMenu() {
        mainPanel.removeAll();

        JLabel assignmentLabel = new JLabel("Assignment ID:");
        assignmentLabel.setBounds(150, 50, 100, 30);
        JTextField assignmentField = new JTextField();
        assignmentField.setBounds(150, 80, 200, 30);

        JLabel studentLabel = new JLabel("Student ID:");
        studentLabel.setBounds(150, 120, 100, 30);
        JTextField studentField = new JTextField();
        studentField.setBounds(150, 150, 200, 30);

        JLabel gradeLabel = new JLabel("Grade:");
        gradeLabel.setBounds(150, 190, 100, 30);
        JSpinner gradeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));  // Grade from 0 to 100
        gradeSpinner.setBounds(150, 220, 200, 30);

        JButton gradeButton = new JButton("Grade Student");
        gradeButton.setBounds(150, 260, 200, 30);
        gradeButton.addActionListener(e -> {
            // Retrieve the values from the input fields
            int assignmentId = Integer.parseInt(assignmentField.getText());
            int studentId = Integer.parseInt(studentField.getText());
            int grade = (int) gradeSpinner.getValue();

            // Call the Model's gradeStudent method
            model.gradeStudent(assignmentId, studentId, grade);
        });

        mainPanel.add(assignmentLabel);
        mainPanel.add(assignmentField);
        mainPanel.add(studentLabel);
        mainPanel.add(studentField);
        mainPanel.add(gradeLabel);
        mainPanel.add(gradeSpinner);
        mainPanel.add(gradeButton);
        mainPanel.add(goBack);

        mainPanel.revalidate();
        mainPanel.repaint();
        showFrame();
    }

    public void showStudentMenu(){
        mainPanel.removeAll();

        JLabel welcomeLabel = new JLabel("Welcome Student!");
        welcomeLabel.setBounds(150, 50, 350, 30);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JButton showAllAssignmentsButton = new JButton("All assignments");
        JButton showAvailableAssignmentsButton = new JButton("Available assignments");
        JButton showGradesButton = new JButton("Grades list");
        JButton showMyProfileButton = new JButton("My profile");
        JButton showMySubmissionsButton = new JButton("My submissions");
        JButton logoutButton = new JButton("Log Out");

        showAllAssignmentsButton.setBounds(150, 100, 200, 30);
        showMyProfileButton.setBounds(150, 140, 200, 30);
        showGradesButton.setBounds(150, 180, 200, 30);
        showAvailableAssignmentsButton.setBounds(150, 220, 200, 30);
        showMySubmissionsButton.setBounds(150, 260, 200, 30);
        logoutButton.setBounds(150, 340, 200, 30);

        // Adding buttons to the panel
        mainPanel.add(welcomeLabel);
        mainPanel.add(showAllAssignmentsButton);
        mainPanel.add(showMyProfileButton);
        mainPanel.add(showGradesButton);
        mainPanel.add(showAvailableAssignmentsButton);
        mainPanel.add(showMySubmissionsButton);
        mainPanel.add(logoutButton);

        // Button listeners
        showAllAssignmentsButton.addActionListener(controller);
        showGradesButton.addActionListener(controller);
        showMyProfileButton.addActionListener(controller);
        showAvailableAssignmentsButton.addActionListener(controller);
        showMySubmissionsButton.addActionListener(controller);
        logoutButton.addActionListener(controller);

        showAllAssignmentsButton.setActionCommand("showAllAssignments");
        showMyProfileButton.setActionCommand("showStudentProfile");
        showGradesButton.setActionCommand("showStudentGrades");
        showAvailableAssignmentsButton.setActionCommand("showAssignmentsStudent");
        showMySubmissionsButton.setActionCommand("showSubmissionsStudent");
        logoutButton.setActionCommand("logout");

        mainPanel.revalidate();
        mainPanel.repaint();
        showFrame();
    }
    public void showSubmitAssignmentMenu(){
        mainPanel.removeAll();

        JLabel assignmentLabel = new JLabel("Assignment ID:");
        assignmentLabel.setBounds(150, 50, 100, 30);
        JTextField assignmentField = new JTextField();
        assignmentField.setBounds(150, 80, 200, 30);

        JLabel submissionLabel = new JLabel("Submission text:");
        submissionLabel.setBounds(150, 120, 100, 30);
        JTextField submissionField = new JTextField();
        submissionField.setBounds(150, 150, 200, 30);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(150, 260, 200, 30);
        submitButton.addActionListener(e -> {
            // Retrieve the values from the input fields
            int assignmentId = Integer.parseInt(assignmentField.getText());
            String submission = submissionField.getText();

            // Call the Model's gradeStudent method
            model.submitAssignment(assignmentId, submission);
        });

        mainPanel.add(assignmentLabel);
        mainPanel.add(assignmentField);
        mainPanel.add(submissionLabel);
        mainPanel.add(submissionField);
        mainPanel.add(submissionLabel);
        mainPanel.add(submitButton);
        mainPanel.add(goBack);

        mainPanel.revalidate();
        mainPanel.repaint();
        showFrame();
    }
    public void updateAssignment(){}

    public void showLoginError() {
        JOptionPane.showMessageDialog(null, "Username or Password is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void displayOutput(String output) {
        // Update the JTextArea with the given output
        mainPanel.removeAll();
        displayArea.setText(output);
        mainPanel.add(scrollPane);
        goBack.setActionCommand("backToMenu");
        mainPanel.add(goBack);
        mainPanel.revalidate();
        mainPanel.repaint();

    }

    public void addSubmitAssignmentButton(){
        additionalButton = new JButton("Add submission");
        additionalButton.setBounds(100, 600, 150, 50);
        additionalButton.addActionListener(controller);
        additionalButton.setActionCommand("addSubmission");
        mainPanel.add(additionalButton);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public String getUsername() {
        return userField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }
}
