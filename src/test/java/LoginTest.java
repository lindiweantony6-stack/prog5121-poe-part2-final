import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LoginTest {

    private final Login login = new Login();

    @Test
    void usernameCorrect() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    void usernameIncorrect() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    void passwordComplexity() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    void cellPhone() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    @Test
    void loginSuccessAndFailure() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
        assertFalse(login.loginUser("kyl_1", "wrong"));
        assertEquals("Welcome Kyle, Smith it is great to see you again.", login.returnLoginStatus(true));
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus(false));
    }
}
