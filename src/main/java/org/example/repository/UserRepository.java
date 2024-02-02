package org.example.repository;

import org.example.model.User;
import org.example.utils.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements Repository<User> {

    @Override
    public List<User> getAll() {
        String query =  "SELECT * FROM user";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)){
            ResultSet response = statement.executeQuery();
            if (response == null) {
                return null;
            }

            List<User> users = new ArrayList<>();
            while (response.next()) {
                Integer id = response.getInt("id");
                User user = responseGetFields(response, id);
                users.add(user);
            }

            return users;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public User getById(Integer id) {
        String query = "SELECT * FROM user WHERE id=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setInt(1, id);
            if (statement.executeQuery() == null) {
                return null;
            }

            ResultSet response = statement.executeQuery();
            if (response.next()) {
                return responseGetFields(response, id);
            }

        } catch (SQLException ex) {
            return null;
        }

        return null;
    }

    public User getUserByUserName(String userName) {
        String query = "SELECT * FROM user WHERE user_name=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setString(1, userName);
            if (statement.executeQuery() == null) {
                return null;
            }

            ResultSet response = statement.executeQuery();
            if (response.next()) {
                Integer id = response.getInt("id");
                return responseGetFields(response, id);
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
            if (statement.executeQuery() == null) {
                return null;
            }

            ResultSet response = statement.executeQuery();
            if (response.next()) {
                Integer id = response.getInt("id");
                return responseGetFields(response, id);
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
        try (PreparedStatement statement = DBUtil.getStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statementSetFields(statement, user);
            if (statement.executeUpdate() <= 0){
                return null;
            }

            ResultSet response = statement.getGeneratedKeys();
            if (response.next()) {
                Integer id =  response.getInt(1);
                user.setId(id);

                return user;
            }
        } catch (SQLException ex) {
            return null;
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
            if (statement.executeQuery() == null) {
                return null;
            }

            ResultSet response = statement.executeQuery();
            if (response.next()) {
                Integer id = response.getInt("id");
                return responseGetFields(response, id);
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

    public void delete (Integer id) {
        String query = "DELETE FROM user WHERE id=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() < 0) {
                System.out.printf("Error while deleting user with id: %d", id);
            }
        } catch (SQLException ex) {
            System.out.printf("Error occurred while deleting user with id: %d", id);
        }
    }

    public void deleteTicketsByUserId(Integer id){
        String query = "DELETE FROM ticket WHERE user_id=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() < 0) {
                System.out.printf("Error while deleting ticket with user id: %d", id);
            }
        } catch (SQLException ex) {
            System.out.printf("Error occurred while deleting ticket with user id: %d", id);
        }
    }

    private User responseGetFields(ResultSet response, Integer id) {
        try {
            String userName = response.getString("user_name");
            String email = response.getString("email");
            String encryptedPassword = response.getString("password");
            Boolean isEnabled = response.getBoolean("is_enabled");
            String role = response.getString("role");

            return new User(id, userName, email, encryptedPassword, isEnabled, role);
        } catch (SQLException ex) {
            return null;
        }
    }

    private void statementSetFields(PreparedStatement statement, User user) {
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

}
