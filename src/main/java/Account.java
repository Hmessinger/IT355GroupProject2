import java.security.SecureRandom;
import java.util.Base64;

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

    Account(String username, String password) {
        this.encoder = new BCryptPasswordEncoder();

        /*
         * CWE-261: Weak Encoding for Password. This is the standard for password
         * encoding. BCrypt is a password hashing algorithm that will also salt the
         * password for more security. The password in a larger application will be
         * saved
         * in hash form in a db.
         */
        this.hashedPassword = encoder.encode(password);
        this.username = username;
    }

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
         */
        if (this.username.equals(username) && encoder.matches(password, this.hashedPassword)) {
            generateAuthToken();
            System.out.println("Log in successful. Auth token: " + authToken);
            return true;
        } else {
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
            System.out.println("Error: Invalid or missing authentication token.");
            return false;
        }

        // Check if new password is different from the current password.
        if (encoder.matches(newPassword, this.hashedPassword)) {
            System.out.println("Error: New password must be different from the current password.");
            return false;
        }

        // Update password.
        this.hashedPassword = encoder.encode(newPassword);
        System.out.println("Password changed successfully.");

        return true;
    }

    public void printUsername() {
        System.out.println(getUsername());
    }

    private void printHashedPassword() {
        System.out.println(this.hashedPassword);
    }

    // 'Account' getters.
    public String getAuthToken() {
        return this.authToken;
    }

    public String getUsername() {
        return this.username;
    }

    public static void main(String[] args) {
        Account myAccount = new Account("Daulton", "password");
        myAccount.printUsername();
        myAccount.printHashedPassword();
        myAccount.logIn("Daulton", "password");
        myAccount.changePassword(myAccount.getAuthToken(), "pword");
        myAccount.printHashedPassword();
        myAccount.logIn("Daulton", "password");
        myAccount.logIn("Daulton", "pword");
        myAccount.changePassword(myAccount.getAuthToken(), "gword");
    }
}
