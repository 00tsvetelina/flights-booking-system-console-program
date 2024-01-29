package org.example.service;

import org.example.model.Ticket;
import org.example.model.User;
import org.example.repository.TicketRepository;
import org.example.repository.UserRepository;

import java.util.List;

public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public String getAllTickets() {
        List<Ticket> tickets = ticketRepository.getAllTickets();
        if (tickets.isEmpty()) {
            return "No tickets found, sorry!";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here is a list of all booked tickets: \n");
        for (Ticket ticket: tickets) {
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

        List<Ticket> tickets = ticketRepository.getTicketByUserId(userId);
        if (tickets.isEmpty()) {
            return String.format("No tickets found for user with id %d, sorry!", userId);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Here is a list of all booked tickets for %s: \n", user.getUserName()));
        for (Ticket ticket: tickets) {
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
        String validationError = validateTicket(ticket);
        if (validationError != null) {
            return validationError;
        }

        ticketRepository.createTicket(ticket);
        return  "Ticket created successfully";
    }

    public String deleteTicket(Integer id) {
        if (id <= 0) {
            return "Invalid id, please enter a positive number";
        }

        if (ticketRepository.getTicketById(id) == null) {
            return String.format("Cannot find ticket with id: %d", id);
        }

        ticketRepository.deleteTicket(id);
        return String.format("Ticket with id: %d successfully deleted!", id);
    }

    public String validateTicket(Ticket ticket) {
        if (ticket.getFlight().getId() <= 0 || ticket.getFlight() == null) {
            return "Invalid flight id, please enter a valid one";
        }

        if (ticket.getUser().getId() <= 0 || ticket.getUser() == null) {
            return "Invalid user id, please enter a valid one";
        }

        if (ticket.getSeat() == 0) {
            return "No available tickets for the selected flight";
        }

        return null;
    }

    public Integer getAvailableSeatNumber(Integer flightId) {
        return ticketRepository.getAvailableSeatNumber(flightId);
    }
}