package org.example.model;

public class User {

    private Integer id;
    private String userName;
    private String email;
    private String password;
    private Boolean isEnabled;
    private String role;

    public User(Integer id, String userName, String email, String password, Boolean isEnabled, String role) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.isEnabled = isEnabled;
        this.role = role;
    }

    public User(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public String getRole() {
        return role;
    }

    public void setRoles(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User {" +
                "id: " + id +
                ", userName: " + userName + '\'' +
                ", enable status: " + isEnabled +
                ", role: " + role +
                '}';
    }

}
