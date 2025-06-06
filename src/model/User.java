package model; //MVC Pattern

public class User {
    private String username;
    private String password;
    private boolean isAdmin;
    private String fullName;
    private String email;
    private boolean isLoggedIn;

    // Constructor
    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.fullName = "";
        this.email = "";
        this.isLoggedIn = false;
    }

    // Overloaded constructor
    public User(String username, String password, boolean isAdmin, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.fullName = fullName;
        this.email = email;
        this.isLoggedIn = false;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    // Setters
    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    // Login validation (for UserService)
    public boolean validateLogin(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }

    // toString method
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", isAdmin=" + isAdmin +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", isLoggedIn=" + isLoggedIn +
                '}';
    }
}
