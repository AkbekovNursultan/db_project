

public class User {
    private String username;
    private String password;
    public int id;


    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;


    }
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void displayMenu() {

    }

    public void doMenuCommand(int option) {

    }
    public String getUsername() {
       return username;
    }
    public void setUsername(String username){
        this.username=username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password=password;
    }
}



