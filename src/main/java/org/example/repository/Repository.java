package org.example.repository;

import org.example.model.BaseId;
import org.example.utils.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface Repository<T extends BaseId> {

    List<T> getAll() throws SQLException;

    T getById(Integer id);

    T create(T obj);

    T update (T obj);

    void deleteById(Integer id);

    default <T> T executeQuery(String query, Function<ResultSet, T> mapper) {
        try (PreparedStatement statement = DBUtil.getConnection().prepareStatement(query)) {
          ResultSet resultSet = statement.executeQuery();
          return mapper.apply(resultSet);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    default int executeUpdate(String query, BiConsumer<PreparedStatement, T> setter, T object) {
        try (PreparedStatement statement = DBUtil.getConnection()
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setter.accept(statement, object);

            int result = statement.executeUpdate();
            if (result < 0) {
                return -1;
            }

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

            return result;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return -1;
        }
    }

    default int executeDelete(String query) {
        try (PreparedStatement statement = getStatement(query, 0)) {
           return statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return -1;
        }
    }

    default PreparedStatement getStatement(String query, int keys) {
        try {
            return DBUtil.getConnection().prepareStatement(query, keys);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    default ResultSet getResultSet(PreparedStatement statement) {
        try {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet;
            }
            return null;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

}