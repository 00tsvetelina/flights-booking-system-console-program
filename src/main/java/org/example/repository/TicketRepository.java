package org.example.repository;

import org.example.model.Flight;
import org.example.model.Promo;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TicketRepository {

    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final PromoRepository promoRepository;

    public TicketRepository(FlightRepository flightRepository, UserRepository userRepository, PromoRepository promoRepository) {
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
        this.promoRepository = promoRepository;
    }

    public List<Ticket> getAllTickets() {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement("SELECT * FROM ticket");

            ResultSet response = statement.executeQuery();
            if (response == null) {
                return null;
            }

            List<Ticket> tickets = new ArrayList<>();
            while (response.next()){
                Integer id = response.getInt("id");
                Integer flightId = response.getInt("flight_id");
                Flight flight = flightRepository.getFlightById(flightId);
                Integer seat = response.getInt("seat");
                Integer userId = response.getInt("user_id");
                User user = userRepository.getUserById(userId);
                Integer promoId = response.getInt("promo_id");
                Promo promo = promoRepository.getPromoById(promoId);

                Ticket ticket = new Ticket(id, flight, seat, user, promo);
                tickets.add(ticket);
            }
            return tickets;
        } catch (SQLException ex) {
            return null;
        }
    }

    public Ticket createTicket(Ticket ticket) {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "INSERT INTO ticket(flight_id, user_id, promo_id, seat) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            statement.setInt(1, ticket.getFlight().getId());
            statement.setInt(2, ticket.getUser().getId());

            Promo promo = ticket.getPromo();
            if (promo == null) {
                statement.setNull(3, Types.INTEGER);
            } else {
                statement.setInt(3, ticket.getPromo().getId());
            }
            statement.setInt(4, ticket.getSeat());

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

     public int getAvailableSeats(Integer flightId) {
         Integer seatsCount = flightRepository.getFlightById(flightId).getPlane().getSeatsCount();

         List<Ticket> tickets = getAllTickets();
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