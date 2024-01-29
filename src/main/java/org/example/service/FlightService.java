package org.example.service;

import org.example.model.Flight;
import org.example.repository.FlightRepository;
import org.example.repository.TicketRepository;
import org.example.utils.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class FlightService {

    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;


    public FlightService(FlightRepository flightRepository, TicketRepository ticketRepository) {
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
    }

    public String getAllFlights() {
        List<Flight> flights = flightRepository.getAllFlights();
        if (flights.isEmpty()) {
            return "No flights found, sorry!";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here are all available flights: \n");
        for (Flight flight: flights) {
            sb.append(flight.toString()).append("\n");
        }

        return sb.toString();
    }

    public String getFlightById(Integer id) {
        if (id <= 0) {
            return "Invalid id, please enter a positive number";
        }

        Flight flight = flightRepository.getFlightById(id);
        if (flight == null) {
            return String.format("Could not find flight with id: %d", id);
        }

        return String.format("Flight with id: %d found - %s", id, flight);
    }

    public String createFlight(Flight flight) {
        String validationError = validateFlight(flight);
        if (validationError != null) {
            return validationError;
        }

       flightRepository.createFlight(flight);


        return "Flight created successfully";
    }

    public String updateFlight(Flight flight) {
        if (flight.getId() == null) {
            return "Flight id cannot be null";
        }

        String validationError = validateFlight(flight);
        if (validationError != null) {
            return validationError;
        }

        flightRepository.updateFlight(flight);

        return "Flight updated successfully";
    }

    public String deleteFlight(Integer id) {
        try {
            if (id <= 0) {
                return "Invalid id, please enter a positive number";
            }

            if (flightRepository.getFlightById(id) == null) {
                return String.format("Cannot find flight with id: %d", id);
            }

            Connection con = DBUtil.getConnection();
            con.setAutoCommit(false);

            ticketRepository.deleteTicketByFlightId(id);
            flightRepository.deleteFlight(id);

            con.commit();

            return String.format("Flight with id: %d successfully deleted!", id);
        } catch (SQLException ex) {
            return String.format("Error while trying to delete flight with id: %d", id);
        }

    }

    private String validateFlight(Flight flight) {
        if (flight.getPlane().getId() <= 0 || flight.getPlane() == null) {
            return "Invalid plane id, please enter a valid one";
        }

        if (flight.getDestination().isBlank() || flight.getDestination().isEmpty()
                || flight.getDestination().length() > 20) {
            return "Destination should not be blank or exceed 20 characters";
        }

        if (flight.getOrigin().isBlank() || flight.getOrigin().isEmpty() || flight.getOrigin().length() > 20) {
            return "Origin should not be blank or exceed 20 characters";
        }

        if (flight.getDepartureTime().isBefore(LocalDate.now()) || flight.getDepartureTime() == null) {
            return "Invalid departure date, please enter a valid future date";
        }

        if (flight.getPrice() == 0) {
            return "Price should be a valid positive number";
        }

        return null;
    }

}

