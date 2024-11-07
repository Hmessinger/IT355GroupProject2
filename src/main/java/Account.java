import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Account {
    private String hashedPassword;
    private BCryptPasswordEncoder encoder;
    private String username;

    Account(String username, String password) {
        this.encoder = new BCryptPasswordEncoder();

        // Daulton Widlacki
        // CWE-261: Weak Encoding for Password. This is the standard for password
        // encoding.
        // Bycrpt is a password hashing algorithm that will also salt the password for
        // more
        // security. The password in a larger application will be saved in hash form in
        // a db.
        this.hashedPassword = encoder.encode(password);
        this.username = username;
    }

    public String get_username() {
        return this.username;
    }

    public void print_user_name() {
        System.out.println(get_username());
    }

    private void print_hashed_password() {
        System.out.println(this.hashedPassword);
    }

    public static void main(String[] args) {
        Account myAccount = new Account("Daulton", "password");
        myAccount.print_user_name();
        myAccount.print_hashed_password();
    }
}
