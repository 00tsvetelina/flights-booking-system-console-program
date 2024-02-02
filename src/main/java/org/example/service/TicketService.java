package org.example.service;

import org.example.model.*;
import org.example.repository.*;
import org.example.utils.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final PromoRepository promoRepository;
    private final FlightRepository flightRepository;
    private final PlaneRepository planeRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository,
                         PromoRepository promoRepository, FlightRepository flightRepository,
                         PlaneRepository planeRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.promoRepository = promoRepository;
        this.flightRepository = flightRepository;
        this.planeRepository = planeRepository;
    }

    public String getAllTickets() {
        List<Ticket> tickets = ticketRepository.getAllTickets();
        if (tickets.isEmpty()) {
            return "No tickets found, sorry!";
        }

        for (Ticket ticket : tickets) {
            Flight flight = flightRepository.getFlightById(ticket.getFlight().getId());
            User user = userRepository.getUserById(ticket.getUser().getId());
            List<Promo> promos = promoRepository.getPromosByTicketId(ticket.getId());
            ticket.setFlight(flight);
            ticket.setUser(user);
            ticket.setPromos(promos);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here is a list of all booked tickets: \n");
        for (Ticket ticket : tickets) {
            sb.append(ticket.toString()).append("\n");
        }

        return sb.toString();
    }

    public String getTicketsByUserId(Integer userId) {
        if (userId <= 0) {
            return "Invalid id, please enter a positive number";
        }

        User user = userRepository.getUserById(userId);
        if (user == null) {
            return String.format("No user found with id: %d", userId);
        }

        List<Ticket> tickets = ticketRepository.getTicketsByUserId(userId);
        for (Ticket ticket : tickets) {
            Flight flight = flightRepository.getFlightById(ticket.getFlight().getId());
            List<Promo> promos = promoRepository.getPromosByTicketId(ticket.getId());

            ticket.setFlight(flight);
            ticket.setPromos(promos);
        }
        if (tickets.isEmpty()) {
            return String.format("No tickets found for user with id %d, sorry!", userId);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Here is a list of all booked tickets for %s: \n", user.getUserName()));
        for (Ticket ticket : tickets) {
            sb.append(ticket.toString()).append("\n");
        }

        return sb.toString();
    }

    public String getTicketById(Integer id) {
        if (id <= 0) {
            return "Invalid id, please enter a positive number";
        }

        Ticket ticket = ticketRepository.getTicketById(id);
        if (ticket == null) {
            return String.format("Could not find ticket with id: %d", id);
        }

        return String.format("Ticket with id: %d found - %s", id, ticket);
    }

    public String createTicket(Ticket ticket) {
        try {
            String validationError = validateTicket(ticket);
            if (validationError != null) {
                return validationError;
            }

            Connection con = DBUtil.getConnection();
            con.setAutoCommit(false);

            ticket = ticketRepository.createTicket(ticket);
            ticketRepository.createTicketPromoRelations(ticket);

            for (Promo promo: ticket.getPromos()) {
                promo.setUsed(true);
                promoRepository.updatePromo(promo);
            }

            con.commit();

            return "Ticket created successfully";
        } catch (SQLException ex) {
            return "Error while trying to create ticket";
        }

    }

    public String deleteTicket(Integer id) {
        try {
            if (id <= 0) {
                return "Invalid id, please enter a positive number";
            }

            if (ticketRepository.getTicketById(id) == null) {
                return String.format("Cannot find ticket with id: %d", id);
            }

            Connection con = DBUtil.getConnection();
            con.setAutoCommit(false);

            ticketRepository.deleteTicketPromoRelations(id);
            ticketRepository.deleteTicket(id);
            con.commit();

            return String.format("Ticket with id: %d successfully deleted!", id);
        } catch (SQLException ex) {
            return String.format("Error while trying to delete ticket with id: %d", id);
        }
    }

    private String validateTicket(Ticket ticket) {
        if (ticket.getFlight().getId() <= 0 || ticket.getFlight() == null) {
            return "Invalid flight id, please enter a valid one";
        }

        if (ticket.getUser().getId() <= 0 || ticket.getUser() == null) {
            return "Invalid user id, please enter a valid one";
        }

        if (ticket.getSeat() == 0) {
            return "No available tickets for the selected flight";
        }

        for (Promo promo : ticket.getPromos()) {
            if (promo.getUsed() && promo.getSingleUse()) {
                return String.format("Promo code: %s is already used", promo.getPromoCode());
            }
        }

        return null;
    }

    public Integer getAvailableSeatNumber(Integer flightId) {
        Flight flight = flightRepository.getFlightById(flightId);
        Plane plane = planeRepository.getPlaneById(flight.getPlane().getId());
        Integer seatsCount = plane.getSeatsCount();

         List<Ticket> tickets = ticketRepository.getAllTickets();
         Set<Integer> occupiedSeats = new HashSet<>();
         tickets.forEach(ticket -> {
            Integer seat = ticket.getSeat();
            occupiedSeats.add(seat);
         });

         int availableSeat = 0;
         for (int i = 1; i <= seatsCount; i++) {
             if (!occupiedSeats.contains(i)) {
                 availableSeat = i;
                 break;
             }
         }

         return availableSeat;
    }
}
