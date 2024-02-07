package org.example.repository;

import org.example.model.User;
import org.example.utils.DBUtil;

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
        return Repository.super.executeQuery(query, this::mapToUserList).get(0);

    }

    public User getUserByUserName(String userName) {
        String query = "SELECT * FROM user WHERE user_name=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setString(1, userName);
            ResultSet response = statement.executeQuery();
            if (response == null) {
                return null;
            }

            if (response.next()) {
                return mapToResultSet(response);
            }
        } catch (SQLException ex) {
            return null;
        }

        return null;
    }

    public User getUserByEmail(String email) {
        String query = "SELECT * FROM user WHERE email=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setString(1, email);
            ResultSet response = statement.executeQuery();
            if (response == null) {
                return null;
            }

            if (response.next()) {
                return mapToResultSet(response);
            }
        } catch (SQLException ex) {
            return null;
        }

        return null;
    }

    private String encryptPassword(String password) {
        String query = "SELECT SHA2(?, 224)";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setString(1, password);
            ResultSet response = statement.executeQuery();
            if (response == null) {
                return null;
            }

            if (response.next()) {
                return response.getString(1);
            }
        } catch (SQLException ex) {
            System.out.print(ex.getMessage());
        }
        return null;
    }

    @Override
    public User create(User user) {
        String query = "INSERT INTO user(user_name, email, password, is_enabled, role) VALUES (?,?,?,?,?)";
        int generatedId = Repository.super.executeUpdate(query, this::mapToStatementFields, user);
        if (generatedId > 0) {
            user.setId(generatedId);
            return user;
        }

        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    public User loginUser(String userName, String password) {
        String query = "SELECT * FROM user WHERE user_name=? AND password=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setString(1, userName);
            statement.setString(2, encryptPassword(password));
            ResultSet response = statement.executeQuery();
            if (response == null) {
                return null;
            }

            if (response.next()) {
                return mapToResultSet(response);
            }
        } catch (SQLException ex) {
            return null;
        }
        return null;
    }

    public void setDisableOrEnableUser(User user, Boolean choice) {
        String query = "UPDATE user SET is_enabled=? WHERE id=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setBoolean(1, choice);
            statement.setInt(2, user.getId());

            if (statement.executeUpdate() < 0) {
                System.out.printf("Error while disabling user with id: %d", user.getId());
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        String query = "DELETE FROM user WHERE id=?";
        int generatedId = Repository.super.executeDelete(query, id);

        if (generatedId < 0) {
            System.out.print("Error occurred while performing delete");
        }
    }

    public void deleteTicketsByUserId(Integer id){
        String query = "DELETE FROM ticket WHERE user_id=?";
        int generatedId = Repository.super.executeDelete(query, id);

        if (generatedId < 0) {
            System.out.printf("Error occurred while deleting ticket with user id: %d", id);
        }
    }

    public User mapToResultSet(ResultSet resultSet) {
        try {
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
        try {
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = mapToResultSet(resultSet);
                users.add(user);
            }
            return users;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

}
