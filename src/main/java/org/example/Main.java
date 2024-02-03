package org.example;


import org.example.dashboard.*;
import org.example.repository.*;
import org.example.service.*;
import org.example.utils.DBUtil;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        PlaneRepository planeRepository = new PlaneRepository();
        FlightRepository flightRepository = new FlightRepository();
        UserRepository userRepository = new UserRepository();
        PromoRepository promoRepository = new PromoRepository();
        TicketRepository ticketRepository = new TicketRepository();

        FlightService flightService = new FlightService(flightRepository, ticketRepository, planeRepository);
        PlaneService planeService = new PlaneService(planeRepository, flightRepository);
        TicketService ticketService = new TicketService(ticketRepository, userRepository,
                promoRepository, flightRepository, planeRepository);
        PromoService promoService = new PromoService(promoRepository);
        UserService userService = new UserService(userRepository, ticketRepository);

        PlaneDashboard planeDashboard = new PlaneDashboard(planeService);
        FlightDashboard flightDashboard = new FlightDashboard(flightService, planeService);
        TicketDashboard ticketDashboard = new TicketDashboard(ticketService, flightService, promoService);
        PromoDashboard promoDashboard = new PromoDashboard(promoService);
        UserDashboard userDashboard = new UserDashboard(userService, planeDashboard, flightDashboard,
                promoDashboard, ticketDashboard);

        userDashboard.printMenu();

        try {
            DBUtil.getConnection().close();
        } catch (SQLException e) {
            System.out.println("Problem while closing the connection occurred.");
        }
    }

}