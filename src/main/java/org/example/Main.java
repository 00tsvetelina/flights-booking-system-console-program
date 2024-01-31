package org.example;


import org.example.dashboard.FlightDashboard;
import org.example.dashboard.PlaneDashboard;
import org.example.dashboard.PromoDashboard;
import org.example.dashboard.TicketDashboard;
import org.example.repository.*;
import org.example.service.FlightService;
import org.example.service.PlaneService;
import org.example.service.PromoService;
import org.example.service.TicketService;
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

        PlaneDashboard planeDashboard = new PlaneDashboard(planeService);
        FlightDashboard flightDashboard = new FlightDashboard(flightService, planeService);
        TicketDashboard ticketDashboard = new TicketDashboard(ticketService, flightService, promoService);
        PromoDashboard promoDashboard = new PromoDashboard(promoService);


//        planeDashboard.printPlaneMenu();
//        flightDashboard.printFlightMenu();
        ticketDashboard.printTicketMenu(1);
//        promoDashboard.printPromoMenu();


        try {
            DBUtil.getConnection().close();
        } catch (SQLException e) {
            System.out.println("Problem while closing the connection occurred.");
        }
    }

}