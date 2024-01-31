package org.example;


import org.example.dashboard.FlightDashboard;
import org.example.dashboard.PlaneDashboard;
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
        FlightRepository flightRepository = new FlightRepository(planeRepository);
        UserRepository userRepository = new UserRepository();
        PromoRepository promoRepository = new PromoRepository();
        TicketRepository ticketRepository = new TicketRepository(flightRepository, userRepository, promoRepository);

        FlightService flightService = new FlightService(flightRepository, ticketRepository);
        PlaneService planeService = new PlaneService(planeRepository, flightRepository);
        TicketService ticketService = new TicketService(ticketRepository, userRepository, promoRepository);
        PromoService promoService = new PromoService(promoRepository);

        PlaneDashboard planeDashboard = new PlaneDashboard(planeService);
        FlightDashboard flightDashboard = new FlightDashboard(flightService);
        TicketDashboard ticketDashboard = new TicketDashboard(ticketService, flightService, promoService);

//        planeDashboard.printPlaneMenu();
//        flightDashboard.printFlightMenu();
        ticketDashboard.printTicketMenu(1);

//        promoRepository.createPromo(new Promo(null, "20", 10,
//                LocalDate.of(2024, 3, 1), false, false));
////        System.out.println(promoRepository.getPromoByPromoCode("PROMO"));

        try {
            DBUtil.getConnection().close();
        } catch (SQLException e) {
            System.out.println("Problem while closing the connection occurred.");
        }
    }

}