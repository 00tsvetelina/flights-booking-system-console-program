package org.example.repository;

import org.example.model.Flight;
import org.example.model.Plane;
import org.example.utils.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FlightRepository implements Repository<Flight>{

    public FlightRepository() {}

    @Override
    public List<Flight> getAll() {
        String query = "SELECT * FROM flight";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            ResultSet response = statement.executeQuery();
            if (response == null) {
                return null;
            }

            List<Flight> flights = new ArrayList<>();
            while (response.next()) {
                Flight flight = responseGetFields(response);
                flights.add(flight);
            }

            return flights;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Flight getById(Integer id) {
        String query = "SELECT * FROM flight WHERE id=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setInt(1, id);
            ResultSet response = statement.executeQuery();
            if (response == null) {
                return null;
            }

            if (response.next()) {
                return responseGetFields(response);
            }
        } catch (SQLException ex) {
            return null;
        }

        return null;
    }

    @Override
    public Flight create(Flight flight) {
        String query = "INSERT INTO flight(plane_id, destination, origin, departure_time, delay, price) " +
                "VALUES (?,?,?,?,?,?)";
        try (PreparedStatement statement = DBUtil.getStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statementSetFields(statement, flight);
            if (statement.executeUpdate() <= 0) {
               return null;
            }

            ResultSet response = statement.getGeneratedKeys();
            if (response.next()) {
                Integer id = response.getInt(1);
                flight.setId(id);

                return flight;
            }

        } catch (SQLException ex) {
            return null;
        }

        return null;
    }

    @Override
    public Flight update(Flight flight) {
        String query = "UPDATE flight SET plane_id=?, destination=?, origin=?, departure_time=?," +
                " delay=?, price=? WHERE id=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statementSetFields(statement, flight);
            statement.setInt(7, flight.getId());
            if (statement.executeUpdate() <= 0) {
               return null;
            }

            return flight;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public void deleteById(Integer id, String object) {
        Repository.super.deleteById(id, object);
    }

    public void deleteFlightsByPlaneId(Integer id) {
        String query = "DELETE FROM flight WHERE plane_id=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() < 0) {
                System.out.printf("Error while deleting flight with plane id: %d", id);
            }
        } catch (SQLException ex) {
            System.out.printf("Error occurred while deleting flight with plane id: %d", id);
        }
    }


    private Flight responseGetFields(ResultSet response) {
        try {
            Integer id = response.getInt("id");
            Integer planeId = response.getInt("plane_id");
            String destination = response.getString("destination");
            String origin = response.getString("origin");
            LocalDate departureTime = response.getDate("departure_time").toLocalDate();
            Integer delay = response.getInt("delay");
            Float price = response.getFloat("price");

            return new Flight(id, new Plane(planeId), destination, origin, departureTime, delay, price);
        } catch (SQLException ex) {
            return null;
        }
    }

    private void statementSetFields(PreparedStatement statement, Flight flight) {
        try {
            statement.setInt(1, flight.getPlane().getId());
            statement.setString(2, flight.getDestination());
            statement.setString(3, flight.getOrigin());
            statement.setDate(4, Date.valueOf(flight.getDepartureTime()));
            statement.setInt(5, flight.getDelay());
            statement.setFloat(6, flight.getPrice());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
