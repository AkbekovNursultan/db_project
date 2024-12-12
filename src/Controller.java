import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener{

    private Viewer viewer;
    private Model model;

    public Controller(Viewer viewer) {
        this.viewer = viewer;
        this.model = new Model(viewer);
    }
    public Model getModel() {
        return model;
    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if(command.equals("signin")) {
            model.handleSignIn();
        } else if(command.equals("logout")) {
            model.logout();
        } else if (command.equals("addAssignment")) {
            viewer.showAddAssignmentMenu();
        } else if (command.equals("gradeAssignment")) {
            viewer.showGradeAssignmentMenu();
        } else if (command.equals("showTasks")) {
            model.showMyTasks();
        } else if (command.equals("showTasksNoSubmissions")) {
            model.showMyTasksWithNoSubmissions();
        } else if (command.equals("showSubmissions")) {
            model.showSubmissions();
        } else if (command.equals("backToMenu")) {
            model.handleSignIn();
        } else if (command.equals("showStudentProfile")) {
            model.showMyProfile();
        } else if (command.equals("showStudentGrades")) {
            model.showGrades();
        } else if (command.equals("showAllAssignments")) {
            model.showAllAssignments();
        } else if (command.equals("showAssignmentsStudent")) {
            model.showAllAvailableAssignments();
        } else if (command.equals("showSubmissionsStudent")) {
            model.showMySubmissions();
        }

    }

}
