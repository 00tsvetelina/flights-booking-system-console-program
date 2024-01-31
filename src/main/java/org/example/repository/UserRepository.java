package org.example.repository;

import org.example.model.User;
import org.example.utils.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public User getUserById(Integer id) {
        try (PreparedStatement statement = DBUtil.getStatement(
                "SELECT * FROM user WHERE id=?", 0
        )) {
            statement.setInt(1, id);
            if (statement.executeQuery() == null) {
                return null;
            }

            ResultSet response = statement.executeQuery();
            if (response.next()) {
                String userName = response.getString("user_name");
                String email = response.getString("email");
                String password = response.getString("password");
                Boolean isEnabled = response.getBoolean("is_enabled");
                String role = response.getString("role");

                return new User(id, userName, email, password, isEnabled, role);
            }

        } catch (SQLException ex) {
            return null;
        }

        return null;
    }

}
