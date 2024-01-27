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

    // get all planes
    public List<Plane> getAllPlanes() {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement("SELECT * FROM plane");

            ResultSet response = statement.executeQuery();
            if (response == null) {
                return null;
            }

            List<Plane> planes = new ArrayList<>();
            while (response.next()) {
                Integer id = response.getInt("id");
                String model = response.getString("model");
                Integer seatsCount = response.getInt("seats_count");

                Plane plane = new Plane(id, model, seatsCount);
                planes.add(plane);
            }

            return planes;
        } catch (SQLException e) {
            return null;
        }

    }

    // get plane by id
    public Plane getPlaneById(Integer id) {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement("SELECT * FROM plane WHERE id=?");

            statement.setInt(1, id);
            if (statement.executeQuery() == null) {
                return null;
            }

            ResultSet response = statement.executeQuery();
            if (response.next()) {
                String model = response.getString("model");
                Integer seatsCount = response.getInt("seats_count");

                return new Plane(id, model, seatsCount);
            }
        } catch (SQLException e) {
            return null;
        }

        return null;
    }


    // save plane
    public Plane createPlane(Plane plane) {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "INSERT INTO plane(model, seats_count) VALUES(?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            statement.setString(1, plane.getModel());
            statement.setInt(2, plane.getSeatsCount());

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


    // edit plane
    public Plane updatePlane(Plane plane) {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "UPDATE plane SET model=?, seats_count=? WHERE id=?"
            );
            statement.setString(1,plane.getModel());
            statement.setInt(2, plane.getSeatsCount());
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


    // delete plane
    public void deletePlane(Integer id) {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement("DELETE FROM plane WHERE id=?");
            statement.setInt(1, id);

            if(statement.executeUpdate() < 0) {
                System.out.printf("Error while deleting plane with id: %d", id);
            }
        } catch (SQLException e) {
            System.out.printf("Error occurred while deleting plane with id: %d", id);
        }
    }
}
