package org.example.repository;

import org.example.model.Plane;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaneRepository implements Repository<Plane> {

    public PlaneRepository() {}

    @Override
    public List<Plane> getAll() {
        String query = "SELECT * FROM plane";
        return Repository.super.executeQuery(query, this::mapToPlaneList);
    }

    @Override
    public Plane getById(Integer id) {
        String query = String.format("SELECT * FROM plane WHERE id=%d", id);
        return Repository.super.executeQuery(query, this::mapToPlane);
    }

    @Override
    public Plane create(Plane plane) {
        String query = "INSERT INTO plane(model, seats_count) VALUES(?,?)";
        int result = Repository.super.executeUpdate(query, this::mapToStatementFields, plane);
        if (result > 0) {
            plane.setId(result);
            return plane;
        }

        return null;
    }


    @Override
    public Plane update(Plane plane) {
        String query = String.format("UPDATE plane SET model=?, seats_count=? WHERE id=%d", plane.getId());
        int result = Repository.super.executeUpdate(query, this::mapToStatementFields, plane);
        if (result > 0) {
            return plane;
        }

        return null;
    }

    @Override
    public void deleteById(Integer id) {
        String query = String.format("DELETE FROM plane WHERE id=%d", id);
        int result = Repository.super.executeDelete(query);

        if (result < 0) {
            System.out.print("Error occurred while performing delete");
        }
    }

    private Plane mapToPlane(ResultSet resultSet) {
        try {
            if (!resultSet.next()) {
                return null;
            }

            Integer id = resultSet.getInt("id");
            String model = resultSet.getString("model");
            Integer seatsCount = resultSet.getInt("seats_count");

            return new Plane(id, model, seatsCount);
        } catch (SQLException ex) {
            return null;
        }
    }

    private void mapToStatementFields(PreparedStatement statement, Plane plane) {
        try {
            statement.setString(1,plane.getModel());
            statement.setInt(2, plane.getSeatsCount());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private List<Plane> mapToPlaneList(ResultSet resultSet) {
            List<Plane> planes = new ArrayList<>();
            while (true) {
                Plane plane = mapToPlane(resultSet);
                if (plane == null) {
                    break;
                }
                planes.add(plane);
            }
            return planes;
    }
}
