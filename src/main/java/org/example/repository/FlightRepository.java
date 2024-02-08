package org.example.repository;

import org.example.model.Flight;
import org.example.model.Plane;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FlightRepository implements Repository<Flight>{

    public FlightRepository() {}

    @Override
    public List<Flight> getAll() {
        String query = "SELECT * FROM flight";
        return Repository.super.executeQuery(query, this::mapToFlightList);
    }

    @Override
    public Flight getById(Integer id) {
        String query = String.format("SELECT * FROM flight WHERE id=%d", id);
        return Repository.super.executeQuery(query, this::mapToFlight);
    }

    @Override
    public Flight create(Flight flight) {
        String query = "INSERT INTO flight(plane_id, destination, origin, departure_time, delay, price) VALUES (?,?,?,?,?,?)";

        int result = Repository.super.executeUpdate(query, this::mapToStatementFields, flight);
        if (result > 0) {
            flight.setId(result);
            return flight;
        }

        return null;
    }

    @Override
    public Flight update(Flight flight) {
        String query = String.format("UPDATE flight SET plane_id=?, destination=?, origin=?, departure_time=?," +
                " delay=?, price=? WHERE id=%d", flight.getId());
        int result = Repository.super.executeUpdate(query, this::mapToStatementFields, flight);
        if (result > 0) {
            flight.setId(result);
            return flight;
        }

        return null;
    }

    @Override
    public void deleteById(Integer id) {
        String query = String.format("DELETE FROM flight WHERE id=%d", id);
        int result = Repository.super.executeDelete(query);

        if (result < 0) {
            System.out.print("Error occurred while performing delete");
        }
    }

    public void deleteFlightsByPlaneId(Integer id) {
        String query = String.format("DELETE FROM flight WHERE plane_id=%d", id);
        int result = Repository.super.executeDelete(query);

        if (result < 0) {
            System.out.printf("Error occurred while deleting flight with plane id: %d", id);
        }
    }

    private Flight mapToFlight(ResultSet resultSet) {
        try {
            if (!resultSet.next()) {
                return null;
            }

            Integer id = resultSet.getInt("id");
            Integer planeId = resultSet.getInt("plane_id");
            String destination = resultSet.getString("destination");
            String origin = resultSet.getString("origin");
            LocalDate departureTime = resultSet.getDate("departure_time").toLocalDate();
            Integer delay = resultSet.getInt("delay");
            Float price = resultSet.getFloat("price");

            return new Flight(id, new Plane(planeId), destination, origin, departureTime, delay, price);
        } catch (SQLException ex) {
            return null;
        }
    }

    private void mapToStatementFields(PreparedStatement statement, Flight flight) {
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

    private List<Flight> mapToFlightList(ResultSet resultSet) {
        List<Flight> flights = new ArrayList<>();
        while (true) {
            Flight flight = mapToFlight(resultSet);
            if (flight == null) {
                break;
            }
            flights.add(flight);
        }
        return flights;
    }
}
