package org.example.repository;

import org.example.model.Flight;
import org.example.model.Promo;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.utils.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TicketRepository implements Repository<Ticket> {

    public TicketRepository() {}

    @Override
    public List<Ticket> getAll() {
        String query = "SELECT * FROM ticket";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            ResultSet response = statement.executeQuery();
            if (response == null) {
                return null;
            }

            List<Ticket> tickets = new ArrayList<>();
            while (response.next()){
                Integer id = response.getInt("id");
                Ticket ticket = responseGetFields(response, id);
                tickets.add(ticket);
            }
            return tickets;
        } catch (SQLException ex) {
            return null;
        }

    }

    public List<Ticket> getTicketsByUserId(Integer userId) {
        String query = "SELECT * FROM ticket WHERE user_id=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setInt(1, userId);
            if (statement.executeQuery() == null) {
                return null;
            }

            ResultSet response = statement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (response.next()){
                Integer ticketId = response.getInt("id");
                
                Ticket ticket = responseGetFields(response, ticketId);
                tickets.add(ticket);
            }

            return tickets;
        } catch (SQLException ex) {
            return null;
        }

    }

    @Override
    public Ticket getById(Integer id) {
        String query = "SELECT * FROM ticket WHERE id=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setInt(1,id);
            if (statement.executeQuery() == null) {
                return null;
            }

            ResultSet response = statement.executeQuery();
            if (response.next()) {
                return responseGetFields(response, id);
            }
        } catch (SQLException ex) {
            return null;
        }

        return null;
    }

    @Override
    public Ticket create(Ticket ticket) {
        String query = "INSERT INTO ticket(flight_id, user_id, seat) VALUES (?,?,?)";
        try (PreparedStatement statement = DBUtil.getStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, ticket.getFlight().getId());
            statement.setInt(2, ticket.getUser().getId());
            statement.setInt(3, ticket.getSeat());

            if (statement.executeUpdate() <= 0) {
                return null;
            }

            ResultSet response = statement.getGeneratedKeys();
            if (response.next()) {
                Integer id = response.getInt(1);
                ticket.setId(id);

                return ticket;
            }
        } catch (SQLException ex) {
            return null;
        }

        return null;
    }

    @Override
    public Ticket update(Ticket ticket) {
        return null;
    }

    @Override
    public void delete(Integer id) {
         String query = "DELETE FROM ticket WHERE id=?";
         try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() < 0) {
                System.out.printf("Error while deleting ticket with id: %d", id);
            }
        } catch (SQLException ex) {
            System.out.printf("Error occurred while deleting ticket with id: %d", id);
        }
     }

     public void deleteTicketByFlightId(Integer id) {
         String query = "DELETE FROM ticket WHERE flight_id=?";
         try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() < 0) {
                System.out.printf("Error while deleting ticket with flight id: %d", id);
            }
        } catch (SQLException ex) {
            System.out.printf("Error occurred while deleting ticket with flight id: %d", id);
        }
     }

    public void createTicketPromoRelations(Ticket ticket) {
        for (Promo promo : ticket.getPromos()) {
            String query = "INSERT INTO promo_ticket(ticket_id, promo_id) VALUES (?,?)";
            try (PreparedStatement statement = DBUtil.getStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, ticket.getId());
                statement.setInt(2, promo.getId());

                if (statement.executeUpdate() <= 0) {
                    System.out.println("Error occurred while executing the statement");
                }

                ResultSet response = statement.getGeneratedKeys();
                if (response.next()) {
                    Integer id = response.getInt(1);
                    ticket.setId(id);

                    System.out.println("Promo successfully applied on ticket");
                }

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

     public void deleteTicketPromoRelations(Integer ticketId) {
         String query = "DELETE FROM promo_ticket WHERE ticket_id=?";
         try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setInt(1, ticketId);
            if (statement.executeUpdate() < 0) {
                System.out.printf("Error occurred while deleting relation with ticket id: %d", ticketId);
            }
        } catch (SQLException ex) {
            System.out.printf("Error occurred while deleting relation with ticket id: %d", ticketId);
        }
     }

    private Ticket responseGetFields(ResultSet response, Integer id) {
        try {
            Integer flightId = response.getInt("flight_id");
            Integer userId = response.getInt("user_id");
            Integer seat = response.getInt("seat");

            return new Ticket(id, new Flight(flightId), seat, new User(userId), null);
        } catch (SQLException ex) {
            return null;
        }
    }
}
