package org.example.repository;

import org.example.model.Plane;
import org.example.utils.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlaneRepository {

    public PlaneRepository() {}

    public List<Plane> getAllPlanes() {
        String query = "SELECT * FROM plane";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            ResultSet response = statement.executeQuery();
            if (response == null) {
                return null;
            }

            List<Plane> planes = new ArrayList<>();
            while (response.next()) {
                Integer id = response.getInt("id");

                Plane plane = responseGetFields(response, id);
                planes.add(plane);
            }

            return planes;
        } catch (SQLException e) {
            return null;
        }
    }

    public Plane getPlaneById(Integer id) {
        String query = "SELECT * FROM plane WHERE id=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setInt(1, id);
            if (statement.executeQuery() == null) {
                return null;
            }

            ResultSet response = statement.executeQuery();
            if (response.next()) {
                return responseGetFields(response, id);
            }
        } catch (SQLException e) {
            return null;
        }

        return null;
    }

    public Plane createPlane(Plane plane) {
        String query = "INSERT INTO plane(model, seats_count) VALUES(?,?)";
        try (PreparedStatement statement = DBUtil.getStatement(query,Statement.RETURN_GENERATED_KEYS)) {
            statementSetFields(statement, plane);
            if (statement.executeUpdate() <= 0) {
                return null;
            }

            ResultSet response = statement.getGeneratedKeys();
            if (response.next()) {
                Integer id = response.getInt(1);
                plane.setId(id);

                return plane;
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    public Plane updatePlane(Plane plane) {
        String query = "UPDATE plane SET model=?, seats_count=? WHERE id=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statementSetFields(statement, plane);
            statement.setInt(3, plane.getId());
            if (statement.executeUpdate() <= 0) {
                return null;
            }

            if(statement.executeUpdate() > 0) {
                return plane;
            }
        } catch (SQLException e) {
            return null;
        }

        return null;
    }

    public void deletePlane(Integer id) {
        String query = "DELETE FROM plane WHERE id=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setInt(1, id);
            if(statement.executeUpdate() < 0) {
                System.out.printf("Error while deleting plane with id: %d", id);
            }
        } catch (SQLException e) {
            System.out.printf("Error occurred while deleting plane with id: %d", id);
        }
    }


    private Plane responseGetFields(ResultSet response, Integer id) {
        try {
            String model = response.getString("model");
            Integer seatsCount = response.getInt("seats_count");

            return new Plane(id, model, seatsCount);
        } catch (SQLException ex) {
            return null;
        }
    }

    private void statementSetFields(PreparedStatement statement, Plane plane) {
        try {
            statement.setString(1,plane.getModel());
            statement.setInt(2, plane.getSeatsCount());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
