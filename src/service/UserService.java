package service;

import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    // Singleton Pattern - Lazy Initialization
    private static UserService instance;
    private List<User> users;

    // Private constructor to prevent instantiation
    private UserService() {
        users = new ArrayList<>();

        // Predefined users for testing
        users.add(new User("admin", "admin123", true, "Admin User", "admin@example.com"));
        users.add(new User("john", "john123", false, "John Doe", "john@example.com"));
    }

    // Singleton getInstance method
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public boolean registerUser(String username, String password, String fullName, String email) {
        if (getUserByUsername(username) != null) {
            return false; // username already exists
        }
        users.add(new User(username, password, false, fullName, email));
        return true;
    }

    public User loginUser(String username, String password) {
        for (User user : users) {
            if (user.validateLogin(username, password)) {
                user.setLoggedIn(true);
                return user;
            }
        }
        return null; // login failed
    }

    private User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return users;
    }
}