package org.example.repository;

import org.example.model.Flight;
import org.example.model.Promo;
import org.example.model.Ticket;
import org.example.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketRepository implements Repository<Ticket> {

    public TicketRepository() {}

    @Override
    public List<Ticket> getAll() {
        String query = "SELECT * FROM ticket";
        return Repository.super.executeQuery(query, this::mapToTicketList);
    }

    public List<Ticket> getTicketsByUserId(Integer userId) {
        String query = String.format("SELECT * FROM ticket WHERE user_id=%d", userId);
        return Repository.super.executeQuery(query, this::mapToTicketList);
    }

    @Override
    public Ticket getById(Integer id) {
        String query = String.format("SELECT * FROM ticket WHERE id=%d", id);
        return Repository.super.executeQuery(query, this::mapToTicket);
    }

    @Override
    public Ticket create(Ticket ticket) {
        String query = "INSERT INTO ticket(flight_id, user_id, seat) VALUES (?,?,?)";
        int result = Repository.super.executeUpdate(query, this::mapToStatementFields, ticket);
        if (result > 0) {
            ticket.setId(result);
            return ticket;
        }

        return null;
    }

    @Override
    public Ticket update(Ticket ticket) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {
        String query = String.format("DELETE FROM ticket WHERE id=%d", id);
        int result = Repository.super.executeDelete(query);

        if (result < 0) {
            System.out.print("Error occurred while performing delete");
        }
    }

    public void deleteTicketByFlightId(Integer id) {
        String query = String.format("DELETE FROM ticket WHERE flight_id=%d", id);
        int result = Repository.super.executeDelete(query);

        if (result < 0) {
            System.out.printf("Error occurred while deleting ticket with flight id: %d", id);
        }
     }

    public void createTicketPromoRelations(Ticket ticket) {
        for (Promo promo : ticket.getPromos()) {
            String query = String.format(
                    "INSERT INTO promo_ticket(ticket_id, promo_id) VALUES (%d, %d)", ticket.getId(), promo.getId()
            );

            Repository.super.executeUpdate(query, this::mapToTicketPromoRelationsStatement, ticket);
            System.out.println("Promo successfully applied on ticket");
        }
    }

    private void mapToTicketPromoRelationsStatement(PreparedStatement statement, Ticket ticket) {}

    public void deleteTicketPromoRelations(Integer ticketId) {
         String query = String.format("DELETE FROM promo_ticket WHERE ticket_id=%d", ticketId);
         int result = Repository.super.executeDelete(query);

         if (result < 0) {
             System.out.printf("Error occurred while deleting relation with ticket id: %d", ticketId);
         }
    }

    private Ticket mapToTicket(ResultSet resultSet) {
        try {
            if (!resultSet.next()) {
                return null;
            }
            Integer id = resultSet.getInt("id");
            Integer flightId = resultSet.getInt("flight_id");
            Integer userId = resultSet.getInt("user_id");
            Integer seat = resultSet.getInt("seat");

            return new Ticket(id, new Flight(flightId), seat, new User(userId), null);
        } catch (SQLException ex) {
            return null;
        }
    }

    private void mapToStatementFields(PreparedStatement statement, Ticket ticket) {
        try {
            statement.setInt(1, ticket.getFlight().getId());
            statement.setInt(2, ticket.getUser().getId());
            statement.setInt(3, ticket.getSeat());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private List<Ticket> mapToTicketList(ResultSet resultSet) {
        List<Ticket> tickets = new ArrayList<>();
        while (true) {
            Ticket ticket = mapToTicket(resultSet);
            if (ticket == null) {
                break;
            }
            tickets.add(ticket);
        }
        return tickets;
    }
}
