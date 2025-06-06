package test;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;
    private User adminUser;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "password123", false, "Test User", "test@example.com");
        adminUser = new User("admin", "admin123", true);
    }

    @Test
    void testUserCreationWithFullConstructor() {
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertFalse(user.isAdmin());
        assertEquals("Test User", user.getFullName());
        assertEquals("test@example.com", user.getEmail());
        assertFalse(user.isLoggedIn());
    }

    @Test
    void testUserCreationWithBasicConstructor() {
        assertEquals("admin", adminUser.getUsername());
        assertEquals("admin123", adminUser.getPassword());
        assertTrue(adminUser.isAdmin());
        assertEquals("", adminUser.getFullName());
        assertEquals("", adminUser.getEmail());
        assertFalse(adminUser.isLoggedIn());
    }

    @Test
    void testValidateLoginSuccess() {
        assertTrue(user.validateLogin("testuser", "password123"));
    }

    @Test
    void testValidateLoginWrongUsername() {
        assertFalse(user.validateLogin("wronguser", "password123"));
    }

    @Test
    void testValidateLoginWrongPassword() {
        assertFalse(user.validateLogin("testuser", "wrongpassword"));
    }

    @Test
    void testValidateLoginWrongBoth() {
        assertFalse(user.validateLogin("wronguser", "wrongpassword"));
    }

    @Test
    void testValidateLoginWithNullValues() {
        assertFalse(user.validateLogin(null, "password123"));
        assertFalse(user.validateLogin("testuser", null));
        assertFalse(user.validateLogin(null, null));
    }

    @Test
    void testValidateLoginWithEmptyValues() {
        assertFalse(user.validateLogin("", "password123"));
        assertFalse(user.validateLogin("testuser", ""));
        assertFalse(user.validateLogin("", ""));
    }

    @Test
    void testValidateLoginCaseSensitive() {
        assertFalse(user.validateLogin("TESTUSER", "password123"));
        assertFalse(user.validateLogin("TestUser", "password123"));

        assertFalse(user.validateLogin("testuser", "PASSWORD123"));
        assertFalse(user.validateLogin("testuser", "Password123"));
    }

    @Test
    void testUserCreationWithNullValues() {
        User nullUser = new User(null, null, false, null, null);

        assertNull(nullUser.getUsername());
        assertNull(nullUser.getPassword());
        assertFalse(nullUser.isAdmin());
        assertNull(nullUser.getFullName());
        assertNull(nullUser.getEmail());
    }

    @Test
    void testUserCreationWithEmptyValues() {
        User emptyUser = new User("", "", false, "", "");

        assertEquals("", emptyUser.getUsername());
        assertEquals("", emptyUser.getPassword());
        assertEquals("", emptyUser.getFullName());
        assertEquals("", emptyUser.getEmail());
    }

    @Test
    void testSettersAndGetters() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());

        user.setAdmin(true);
        assertTrue(user.isAdmin());

        user.setFullName("New Name");
        assertEquals("New Name", user.getFullName());

        user.setEmail("new@example.com");
        assertEquals("new@example.com", user.getEmail());

        user.setLoggedIn(true);
        assertTrue(user.isLoggedIn());
    }

    @Test
    void testSettersWithNullValues() {
        user.setPassword(null);
        assertNull(user.getPassword());

        user.setFullName(null);
        assertNull(user.getFullName());

        user.setEmail(null);
        assertNull(user.getEmail());
    }

    @Test
    void testSettersWithEmptyValues() {
        user.setPassword("");
        assertEquals("", user.getPassword());

        user.setFullName("");
        assertEquals("", user.getFullName());

        user.setEmail("");
        assertEquals("", user.getEmail());
    }

    @Test
    void testLoginStatusToggle() {
        assertFalse(user.isLoggedIn());

        user.setLoggedIn(true);
        assertTrue(user.isLoggedIn());

        user.setLoggedIn(false);
        assertFalse(user.isLoggedIn());
    }

    @Test
    void testAdminStatusToggle() {
        assertFalse(user.isAdmin());

        user.setAdmin(true);
        assertTrue(user.isAdmin());

        user.setAdmin(false);
        assertFalse(user.isAdmin());
    }

    @Test
    void testToString() {
        String expected = "User{username='testuser', isAdmin=false, fullName='Test User', email='test@example.com', isLoggedIn=false}";
        assertEquals(expected, user.toString());
    }

    @Test
    void testToStringAfterModification() {
        user.setLoggedIn(true);
        user.setAdmin(true);

        String expected = "User{username='testuser', isAdmin=true, fullName='Test User', email='test@example.com', isLoggedIn=true}";
        assertEquals(expected, user.toString());
    }

    @Test
    void testPasswordSecurity() {
        String originalPassword = user.getPassword();
        user.setPassword("changedPassword");

        assertNotEquals(originalPassword, user.getPassword());
        assertEquals("changedPassword", user.getPassword());
    }

    @Test
    void testEmailValidation() {
        user.setEmail("invalid-email");
        assertEquals("invalid-email", user.getEmail()); // Basit setter test

        user.setEmail("valid@example.com");
        assertEquals("valid@example.com", user.getEmail());
    }
}