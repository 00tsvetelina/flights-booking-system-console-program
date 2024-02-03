package org.example.repository;

import org.example.model.BaseId;
import org.example.utils.DBUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface Repository<T extends BaseId> {

    List<T> getAll();

    T getById(Integer id);

    T create(T obj);

    T update (T obj);

    default void deleteById(Integer id, String object) {
        String query = String.format("DELETE FROM %s WHERE id=?", object);
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() < 0) {
                System.out.print("Error occurred while performing delete");
            }
        } catch (SQLException ex) {
            System.out.print("Error occurred while performing delete");
        }
    }

}