package org.example.dashboard;

import org.example.model.Flight;
import org.example.model.Promo;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.service.FlightService;
import org.example.service.PromoService;
import org.example.service.TicketService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TicketDashboard {

    private final TicketService ticketService;
    private final FlightService flightService;
    private final PromoService promoService;

    public TicketDashboard(TicketService ticketService, FlightService flightService, PromoService promoService) {
        this.ticketService = ticketService;
        this.flightService = flightService;
        this.promoService = promoService;
    }

    public void printTicketMenu(Integer userId) {
        boolean run = true;
        Scanner scannerInt = new Scanner(System.in);
        Scanner scannerString = new Scanner(System.in);

        while (run) {
            System.out.println("\nWelcome to Ticket Dashboard!\n");
            System.out.println("Please Enter Your Choice!\n");
            System.out.println("1. Book a Ticket");
            System.out.println("2. View All Booked Tickets");
            System.out.println("3. Delete a Ticket");
            System.out.println("4. Exit Ticket Dashboard");

            int choice = scannerInt.nextInt();
            String listOfTickets = ticketService.getAllTickets();

            switch (choice) {
                case 1:
                    String listOfFlights = flightService.getAllFlights();
                    System.out.println(listOfFlights);

                    System.out.println("Please, enter the flight id for the flight, " +
                            "you would like to book a ticket for");
                    int flightId = scannerInt.nextInt();

                    System.out.println("Please enter your promo codes, separated by commas");
                    System.out.println("If none, please enter 0");
                    String promoCodes = scannerString.nextLine();

                    List<Promo> promos = new ArrayList<>();
                    if (!promoCodes.equals("0")) {
                        List<String> codes = Arrays.stream(promoCodes.split(", ")).toList();
                        for (String code: codes) {
                            Promo promo = promoService.getPromoByPromoCode(code);
                            promos.add(promo);
                        }
                    }

                    Integer seatNumber = ticketService.getAvailableSeatNumber(flightId);
                    Ticket ticket = new Ticket(null, new Flight(flightId), seatNumber, new User(userId), promos);
                    String responseCreate = ticketService.createTicket(ticket);

                    System.out.println(responseCreate);

                    break;

                case 2:
                    System.out.println(listOfTickets);

                    break;

                case 3:
                    String listOfTicketsByUser = ticketService.getTicketsByUserId(userId);
                    System.out.println(listOfTicketsByUser);
                    System.out.println("Please, enter the ticket id for the ticket, you would like to delete");
                    int ticketId = scannerInt.nextInt();

                    String responseDelete = ticketService.deleteTicket(ticketId);
                    System.out.println(responseDelete);

                    break;

                case 4:
                    System.out.println("Exiting Ticket Dashboard...");
                    run = false;

                    break;

                default:
                    System.out.println("Please enter a valid choice of number");

                    break;
            }
        }

    }
}
