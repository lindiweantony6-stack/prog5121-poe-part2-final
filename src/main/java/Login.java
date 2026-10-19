import java.util.regex.Pattern;

/**
 * Login.java
 *
 * PROG5121 POE - Part 1: Registration and Login feature.
 *
 * Handles registering a single user and verifying login credentials
 * against South African cell phone number, username, and password
 * complexity rules.
 *
 /**
 * Reference for cell phone number regular expression construction:
 * Goyvaerts, J. & Levithan, S., 2012. Regular Expressions Cookbook.
 * 2nd ed. Sebastopol: O'Reilly Media. Section 4.3, Validate International
 * Phone Numbers, based on the ITU-T E.123 and E.164 international
 * numbering standards.
 * Available at: https://www.oreilly.com/library/view/regular-expressions-cookbook/9781449327453/ch04s03.html
 * (Accessed: 18 September 2026).
 */
 
public class Login {

    // Stored details for the currently registered user
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    private boolean userIsRegistered = false;

    // Regex: username must contain an underscore and be no more than 5 characters total
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^(?=.{1,5}$).*_.*$");

    // Regex: at least 1 capital letter, at least 1 digit, at least 1 special character
    private static final Pattern CAPITAL_LETTER_PATTERN = Pattern.compile(".*[A-Z].*");
    private static final Pattern DIGIT_PATTERN = Pattern.compile(".*[0-9].*");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile(".*[^a-zA-Z0-9].*");

    // Regex: '+' followed by a 1-3 digit international/country code, then a 9 digit local number
    // e.g. +27 838968976 -> South African style international number
    private static final Pattern CELL_NUMBER_PATTERN = Pattern.compile("^\\+\\d{1,3}\\d{9}$");

    public Login() {
    }

    /**
     * Checks that the username contains an underscore and is no more than
     * five characters long.
     *
     * @param username the username to validate
     * @return true if valid, false otherwise
     */
    public boolean checkUserName(String username) {
        if (username == null) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }

    /**
     * Checks that the password is at least eight characters long and
     * contains a capital letter, a number, and a special character.
     *
     * @param password the password to validate
     * @return true if valid, false otherwise
     */
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasCapital = CAPITAL_LETTER_PATTERN.matcher(password).matches();
        boolean hasDigit = DIGIT_PATTERN.matcher(password).matches();
        boolean hasSpecial = SPECIAL_CHAR_PATTERN.matcher(password).matches();
        return hasCapital && hasDigit && hasSpecial;
    }

    /**
     * Checks that the cell phone number contains an international country
     * code followed by the number, and is correctly formatted.
     *
     * @param cellPhoneNumber the cell phone number to validate
     * @return true if valid, false otherwise
     */
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) {
            return false;
        }
        return CELL_NUMBER_PATTERN.matcher(cellPhoneNumber).matches();
    }

    /**
     * Validates and registers a new user. Only one user can be registered
     * at a time in this simple system.
     *
     * @param username        chosen username
     * @param password        chosen password
     * @param cellPhoneNumber chosen cell phone number
     * @param firstName       user's first name
     * @param lastName        user's last name
     * @return the appropriate registration outcome message
     */
    public String registerUser(String username, String password, String cellPhoneNumber,
                                String firstName, String lastName) {

        boolean usernameOk = checkUserName(username);
        boolean passwordOk = checkPasswordComplexity(password);
        boolean cellOk = checkCellPhoneNumber(cellPhoneNumber);

        StringBuilder result = new StringBuilder();

        if (!usernameOk) {
            result.append("Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.\n");
        } else {
            result.append("Username successfully captured.\n");
        }

        if (!passwordOk) {
            result.append("Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, and a "
                    + "special character.\n");
        } else {
            result.append("Password successfully captured.\n");
        }

        if (!cellOk) {
            result.append("Cell phone number incorrectly formatted or does not contain international code.\n");
        } else {
            result.append("Cell phone number successfully added.\n");
        }

        if (usernameOk && passwordOk && cellOk) {
            this.username = username;
            this.password = password;
            this.cellPhoneNumber = cellPhoneNumber;
            this.firstName = firstName;
            this.lastName = lastName;
            this.userIsRegistered = true;
        }

        return result.toString().trim();
    }

    /**
     * Verifies that the login details entered match the details stored
     * when the user registered.
     *
     * @param username entered username
     * @param password entered password
     * @return true if the credentials match a registered user, false otherwise
     */
    public boolean loginUser(String username, String password) {
        if (!userIsRegistered || username == null || password == null) {
            return false;
        }
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Returns the appropriate login status message.
     *
     * @param loginSuccess result of a prior loginUser() call
     * @return welcome message on success, or an error message on failure
     */
    public String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isUserRegistered() {
        return userIsRegistered;
    }
}
