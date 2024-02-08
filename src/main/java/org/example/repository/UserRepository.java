package org.example.repository;

import org.example.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements Repository<User> {

    @Override
    public List<User> getAll() {
        String query =  "SELECT * FROM user";
        return Repository.super.executeQuery(query, this::mapToUserList);
    }

    @Override
    public User getById(Integer id) {
        String query = String.format("SELECT * FROM user WHERE id=%d", id);
        return Repository.super.executeQuery(query, this::mapToUser);

    }

    public User getUserByUserName(String userName) {
        String query = String.format("SELECT * FROM user WHERE user_name='%s'", userName);
        PreparedStatement statement = Repository.super.getStatement(query, 0);

        ResultSet resultSet = Repository.super.getResultSet(statement);
        return mapToUser(resultSet);
    }

    public User getUserByEmail(String email) {
        String query = String.format("SELECT * FROM user WHERE email=%s", email);
        PreparedStatement statement = Repository.super.getStatement(query, 0);

        ResultSet resultSet = Repository.super.getResultSet(statement);
        return mapToUser(resultSet);
    }

    private String encryptPassword(String password) {
        String query = String.format("SELECT SHA2('%s', 224)", password);
        PreparedStatement statement = Repository.super.getStatement(query, 0);
        try {
            ResultSet resultSet = Repository.super.getResultSet(statement);
            return resultSet.getString(1);
        } catch (SQLException ex) {
            System.out.print(ex.getMessage());
            return null;
        }
    }

    @Override
    public User create(User user) {
        String query = "INSERT INTO user(user_name, email, password, is_enabled, role) VALUES (?,?,?,?,?)";
        int result = Repository.super.executeUpdate(query, this::mapToStatementFields, user);
        if (result > 0) {
            user.setId(result);
            return user;
        }
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    public User loginUser(String userName, String password) {
        String query = String.format("SELECT * FROM user WHERE user_name='%s' AND password='%s'",
                userName, encryptPassword(password)
        );
        return Repository.super.executeQuery(query, this::mapToUser);
    }

    public void setDisableOrEnableUser(User user, Boolean choice) {
        String query = String.format("UPDATE user SET is_enabled=%s WHERE id=%d", choice,user.getId());
        int result = Repository.super.executeUpdate(query, this::mapToStatementFields, user);

        if (result < 0) {
            System.out.printf("Error while disabling user with id: %d", user.getId());
        }
    }

    @Override
    public void deleteById(Integer id) {
        String query = String.format("DELETE FROM user WHERE id=%d", id);
        int result = Repository.super.executeDelete(query);

        if (result < 0) {
            System.out.print("Error occurred while performing delete");
        }
    }

    public void deleteTicketsByUserId(Integer id){
        String query = String.format("DELETE FROM ticket WHERE user_id=%d", id);
        int result = Repository.super.executeDelete(query);

        if (result < 0) {
            System.out.printf("Error occurred while deleting ticket with user id: %d", id);
        }
    }

    public User mapToUser(ResultSet resultSet) {
        try {
            if (!resultSet.next()) {
                return null;
            }

            Integer id = resultSet.getInt("id");
            String userName = resultSet.getString("user_name");
            String email = resultSet.getString("email");
            String encryptedPassword = resultSet.getString("password");
            Boolean isEnabled = resultSet.getBoolean("is_enabled");
            String role = resultSet.getString("role");

            return new User(id, userName, email, encryptedPassword, isEnabled, role);
        } catch (SQLException ex) {
            return null;
        }
    }

    private void mapToStatementFields(PreparedStatement statement, User user) {
        try {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, encryptPassword(user.getPassword()));
            statement.setBoolean(4, user.getEnabled());
            statement.setString(5, user.getRole());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private List<User> mapToUserList(ResultSet resultSet) {
        List<User> users = new ArrayList<>();
        while (true) {
            User user = mapToUser(resultSet);
            if (user == null) {
                break;
            }
            users.add(user);
        }
        return users;
    }

}
