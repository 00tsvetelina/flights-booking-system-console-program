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
        return Repository.super.executeQuery(query, this::mapToPlaneList).get(0);
    }

    @Override
    public Plane create(Plane plane) {
        String query = "INSERT INTO plane(model, seats_count) VALUES(?,?)";
        int generatedId = Repository.super.executeUpdate(query, this::mapToStatementFields, plane);
        if (generatedId > 0) {
            plane.setId(generatedId);
            return plane;
        }

        return null;
    }


    @Override
    public Plane update(Plane plane) {
        String query = String.format("UPDATE plane SET model=?, seats_count=? WHERE id=%d", plane.getId());
        int generatedId = Repository.super.executeUpdate(query, this::mapToStatementFields, plane);
        if (generatedId > 0) {
            plane.setId(generatedId);
            return plane;
        }

        return null;
    }

    @Override
    public void deleteById(Integer id) {
        String query = "DELETE FROM plane WHERE id=?";
        int generatedId = Repository.super.executeDelete(query, id);

        if (generatedId < 0) {
            System.out.print("Error occurred while performing delete");
        }
    }

    private Plane mapToResultSet(ResultSet resultSet) {
        try {
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
        try {
            List<Plane> planes = new ArrayList<>();
            while (resultSet.next()) {
                Plane plane = mapToResultSet(resultSet);
                planes.add(plane);
            }
            return planes;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
