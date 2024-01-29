package org.example.repository;

import org.example.model.Flight;
import org.example.model.Plane;
import org.example.utils.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FlightRepository {

    private final PlaneRepository planeRepository;

    public FlightRepository(PlaneRepository planeRepository) {
        this.planeRepository = planeRepository;
    }

    public List<Flight> getAllFlights() {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement("SELECT * FROM flight");

            ResultSet response = statement.executeQuery();
            if (response == null) {
                return null;
            }

            List<Flight> flights = new ArrayList<>();
            while (response.next()) {
                Integer id = response.getInt("id");
                Integer planeId = response.getInt("plane_id");
                Plane plane = planeRepository.getPlaneById(planeId);
                String destination = response.getString("destination");
                String origin = response.getString("origin");
                LocalDate departureTime = response.getDate("departure_time").toLocalDate();
                Integer delay = response.getInt("delay");
                Float price = response.getFloat("price");

                Flight flight = new Flight(id, plane, destination, origin, departureTime, delay, price);
                flights.add(flight);
            }

            return flights;
        } catch (SQLException e) {
            return null;
        }
    }

    public Flight getFlightById(Integer id) {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "SELECT * FROM flight WHERE id=?"
            );

            statement.setInt(1, id);
            if (statement.executeQuery() == null) {
                return null;
            }

            ResultSet response = statement.executeQuery();
            if (response.next()) {
                Integer planeId = response.getInt("plane_id");
                Plane plane = planeRepository.getPlaneById(planeId);
                String destination = response.getString("destination");
                String origin = response.getString("origin");
                LocalDate departureTime = response.getDate("departure_time").toLocalDate();
                Integer delay = response.getInt("delay");
                Float price = response.getFloat("price");

                return new Flight(id, plane, destination, origin, departureTime, delay, price);
            }
        } catch (SQLException ex) {
            return null;
        }

        return null;
    }

    public Flight createFlight(Flight flight) {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "INSERT INTO flight(plane_id, destination, origin, departure_time, delay, price) " +
                            "VALUES (?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            statement.setInt(1, flight.getPlane().getId());
            statement.setString(2, flight.getDestination());
            statement.setString(3, flight.getOrigin());
            statement.setDate(4, Date.valueOf(flight.getDepartureTime()));
            statement.setInt(5, flight.getDelay());
            statement.setFloat(6, flight.getPrice());

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

    public Flight updateFlight(Flight flight) {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "UPDATE flight SET plane_id=?, destination=?, origin=?, departure_time=?, delay=?, price=?" +
                            "WHERE id=?"
            );
            statement.setInt(1, flight.getPlane().getId());
            statement.setString(2, flight.getDestination());
            statement.setString(3, flight.getOrigin());
            statement.setDate(4, Date.valueOf(flight.getDepartureTime()));
            statement.setInt(5, flight.getDelay());
            statement.setFloat(6, flight.getPrice());
            statement.setInt(7, flight.getId());
            if (statement.executeUpdate() <= 0) {
               return null;
            }

            if (statement.executeUpdate() > 0) {
                return flight;
            }

        } catch (SQLException ex) {
            return null;
        }

        return null;
    }

    public void deleteFlight(Integer id) {

        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement("DELETE FROM flight WHERE id=?");
            statement.setInt(1, id);

            if (statement.executeUpdate() < 0) {
                System.out.printf("Error while deleting flight with id: %d", id);
            }
        } catch (SQLException ex) {
            System.out.printf("Error occurred while deleting flight with id: %d", id);
        }
    }

    public void deleteFlightsByPlaneId(Integer id) {

        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "DELETE FROM flight WHERE plane_id=?"
            );
            statement.setInt(1, id);

            if (statement.executeUpdate() < 0) {
                System.out.printf("Error while deleting flight with plane id: %d", id);
            }
        } catch (SQLException ex) {
            System.out.printf("Error occurred while deleting flight with plane id: %d", id);
        }
    }
}
