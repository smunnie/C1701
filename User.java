public class User {
    public enum Role{
        user,
        admin,
    }
    private final int id;
    private final String username;
    private final String password;
    private final Role role; // "USER" or "ADMIN"
    public int idCounter = 1;

    public User(String username, String password, Role role) {
        this.id = idCounter++;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}