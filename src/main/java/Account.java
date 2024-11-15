import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
 * CWE-1113: Inappropriate Comment Style.
 * This class demonstrates proper commenting style and formatting for Java code.
 * All class, method, and variable names adhere to Java naming conventions.
 * Multi-line comments are formatted as block comments for enhanced readability 
 * and clarity, ensuring that the code is easy to maintain and understand.
 */
public class Account {
    private String hashedPassword;
    private BCryptPasswordEncoder encoder;
    private String username;
    private String authToken;
    private ArrayList<Integer> checkedOutBooks;

    Account(String username, String password) {
        this.encoder = new BCryptPasswordEncoder();

        /*
         * CWE-261: Weak Encoding for Password. This is the standard for password
         * encoding. BCrypt is a password hashing algorithm that will also salt the
         * password for more security. The password in a larger application will be
         * saved
         * in hash form in a db.
         */
        if (!validatePasswordWithTypeCheck(password)) {
            throw new IllegalArgumentException("Password validation failed - account cannot be created");
        }

        this.hashedPassword = encoder.encode(password);
        this.username = username;
        this.checkedOutBooks = new ArrayList<Integer>();
    }

    private int loginAttempts = 0;// Counter for login attempts
    private static final int MAX_ATTEMPTS = 3;// Maximum allowed attempts

    /*
     * This function will check the username and the hashed password. If they match,
     * it will create an auth token to be used for the rest of the user's session.
     */
    public Boolean logIn(String username, String password) {

        /*
         * CWE-178: Improper Handling of Case Sensitivity.
         * This function declaration demonstrates performing a case-sensitive comparison
         * on both username and password. This ensures only an exact match allows
         * access,
         * preventing unauthorized access due to case mismatches.
         * Example if this rule was broken: "admin" == "aDmIn".
         * 
         * CWE-835: Loop with unreachable exit condition (Infinite Loop).
         * This method limits login attempts to prevent excessive or infinite login
         * attempts, adding a counter that restricts the user to a maximum number of
         * consecutive failed login attempts. Once the limit is reached, further
         * login attempts are temporarily blocked until the counter is reset, ensuring
         * that the method cannot loop infinitely due to repeated failed attempts.
         */
        if (loginAttempts >= MAX_ATTEMPTS) {
            System.out.println("Account temporarily locked due to too many login failures.");
            return false;
        }
        if (this.username.equals(username) && encoder.matches(password, this.hashedPassword)) {
            generateAuthToken();
            System.out.println("Log in successful. Auth token: " + authToken);
            loginAttempts = 0;// reset attempt counter on successful login
            return true;
        } else {
            loginAttempts++;// Increment counter on failed login
            System.out.println("Log in failed: incorrect username or password");
            return false;
        }
    }

    private void generateAuthToken() {
        /*
         * CWE-335: Incorrect Usage of Seeds in Pseudo-Random Number Generator (PRNG).
         * Using SecureRandom, which is seeded securely by default.
         * High Entropy: The seed should be difficult to guess, with many possible
         * values. Entropy can come from unpredictable sources like system or
         * hardware-generated randomness.
         */
        SecureRandom securePrng = new SecureRandom();
        byte[] tokenBytes = new byte[24]; // 24 bytes -> 192-bit token
        securePrng.nextBytes(tokenBytes);
        this.authToken = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    /*
     * This is an example of how the auth token would be used. Only the argument
     * currentAuthToken would be sent back via HTTP header.
     */
    public Boolean changePassword(String currentAuthToken, String newPassword) {
        // Verifying the auth token.
        if (this.authToken == null || !this.authToken.equals(currentAuthToken)) {
            System.err.println("Error: Invalid or missing authentication token.");
            return false;
        }

        // Validate the new password using type checking and requirements
        if (!validatePasswordWithTypeCheck(newPassword)) {
            // validatePasswordWithTypeCheck already prints specific error messages
            return false;
        }

        // Check if new password is different from the current password.
        if (encoder.matches(newPassword, this.hashedPassword)) {
            System.err.println("Error: New password must be different from the current password.");
            return false;
        }

        // Update password.
        this.hashedPassword = encoder.encode(newPassword);
        System.out.println("Password changed successfully.");
        return true;
    }

    /*
     * CWE-252: Unchecked Return Value
     * The `secureChangePassword()` function demonstrates how to properly handle the
     * return
     * value of the `changePassword()` method, which performs a critical operation
     * (changing
     * the user's password). By checking the return value and implementing
     * appropriate error
     * handling, this function addresses the CWE-252 issue, which can lead to
     * security
     * vulnerabilities if left unaddressed.
     */
    public boolean secureChangePassword(String currentAuthToken, String newPassword) {
        boolean passwordChanged = changePassword(currentAuthToken, newPassword);
        if (!passwordChanged) {
            // Handle the failure case
            System.err.println("Error changing password. Please try again.");
            return false;
        } else {
            // Handle the success case
            System.out.println("Password changed successfully.");
            return true;
        }
    }

    /*
     * CWE-351: Insufficient Type Distinction
     * This function demonstrates proper type distinction for password validation.
     * Without proper type checking, the system might accept invalid input types
     * (like arrays or objects) as passwords, leading to security vulnerabilities
     * or unexpected behavior.
     * 
     * Example of vulnerability:
     * - Passing Integer/Object might cause unexpected toString() conversion
     * - Passing null might cause NullPointerException
     */
    public boolean validatePasswordWithTypeCheck(Object passwordInput) {
        // First ensure the input is actually a String
        if (!(passwordInput instanceof String)) {
            System.err.println("Error: Password must be a string. Received type: "
                    + (passwordInput != null ? passwordInput.getClass().getName() : "null"));
            return false;
        }

        String password = (String) passwordInput;

        // Check for empty or whitespace-only passwords
        if (password.trim().isEmpty()) {
            System.err.println("Error: Password cannot be empty or whitespace only");
            return false;
        }

        // Check if the password meets minimum requirements
        if (password.length() < 8) {
            System.err.println("Error: Password must be at least 8 characters long");
            return false;
        }

        // Check if the password contains at least one number and one letter
        boolean hasNumber = false;
        boolean hasLetter = false;

        for (char c : password.toCharArray()) {
            if (Character.isDigit(c))
                hasNumber = true;
            if (Character.isLetter(c))
                hasLetter = true;
        }

        if (!hasNumber || !hasLetter) {
            System.err.println("Error: Password must contain at least one number and one letter");
            return false;
        }

        System.out.println("Password validation successful");
        return true;
    }

    public void printUsername() {
        System.out.println(getUsername());
    }

    // 'Account' getters.
    public String getAuthToken() {
        return this.authToken;
    }

    public String getUsername() {
        return this.username;
    }

    /*
     * CWE-374: Passing Mutable Objects to an Untrusted Method
     * Here we are returning a clone of the checkedOutBooks arraylist. The
     * checkedOutBooks arrayList is a mutable object, so we do not want to return
     * the original list. If we returned the original list, this would allow
     * untrusted or external methods to be able to modify the contents in the list.
     */
    public ArrayList<Integer> getCheckoutBooks() {
        ArrayList<Integer> temp_list = new ArrayList<Integer>();
        temp_list = (ArrayList) checkedOutBooks.clone();
        return temp_list;
    }

    public void addBookToCheckedOut(int bookId) {
        this.checkedOutBooks.add(bookId);
    }

    public void removeBookFromCheckedOut(int bookId) {
        int bookIdIndex = this.checkedOutBooks.indexOf(bookId);
        this.checkedOutBooks.remove(bookIdIndex);
    }
}
