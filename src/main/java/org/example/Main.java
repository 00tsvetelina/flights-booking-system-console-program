package org.example;


import org.example.dashboard.FlightDashboard;
import org.example.dashboard.PlaneDashboard;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.repository.*;
import org.example.service.FlightService;
import org.example.service.PlaneService;
import org.example.utils.DBUtil;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        PlaneRepository planeRepository = new PlaneRepository();
        FlightRepository flightRepository = new FlightRepository(planeRepository);
        UserRepository userRepository = new UserRepository();
        PromoRepository promoRepository = new PromoRepository();
        TicketRepository ticketRepository = new TicketRepository(flightRepository, userRepository, promoRepository);

        FlightService flightService = new FlightService(flightRepository);
        PlaneService planeService = new PlaneService(planeRepository, flightRepository);

        PlaneDashboard planeDashboard = new PlaneDashboard(planeService);
        FlightDashboard flightDashboard = new FlightDashboard(flightService);

//        planeDashboard.printPlaneMenu();
//        flightDashboard.printFlightMenu();
//        ticketRepository.getAllTickets();
        ticketRepository.createTicket(new Ticket(null, flightRepository.getFlightById(3),123 ,new User(1), null));

        try {
            DBUtil.getConnection().close();
        } catch (SQLException e) {
            System.out.println("Problem while closing the connection occurred.");
        }
    }

}