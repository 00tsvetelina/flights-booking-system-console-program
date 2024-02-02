package org.example.dashboard;

import org.example.model.User;
import org.example.service.UserService;

import java.util.Scanner;

public class UserDashboard {

    private final UserService userService;
    private final PlaneDashboard planeDashboard;
    private final FlightDashboard flightDashboard;
    private final PromoDashboard promoDashboard;
    private final TicketDashboard ticketDashboard;

    public UserDashboard(UserService userService, PlaneDashboard planeDashboard, FlightDashboard flightDashboard,
                         PromoDashboard promoDashboard, TicketDashboard ticketDashboard) {
        this.userService = userService;
        this.planeDashboard = planeDashboard;
        this.flightDashboard = flightDashboard;
        this.promoDashboard = promoDashboard;
        this.ticketDashboard = ticketDashboard;
    }

    public void printUserMenu(User user) {

        boolean run = true;
        Scanner scannerInt = new Scanner(System.in);
        Scanner scannerString = new Scanner(System.in);

        while (run) {
            System.out.println("\nPlease Enter Your Choice!\n");
            System.out.println("1. Manage Tickets");
            System.out.println("2. Delete account");
            System.out.println("3. Logout");

            int choice = scannerInt.nextInt();

            switch (choice) {
                case 1:
                    ticketDashboard.printTicketMenu(user.getId());
                    break;

                case 2:
                    System.out.println("Are you certain that you want to delete your account?");
                    System.out.println("Please enter yes/no");
                    String choiceDelete = scannerString.nextLine();

                    if (choiceDelete.equals("yes")) {
                        userService.deleteUser(user.getId());
                        run = false;

                        break;
                    }

                    break;

                case 3:
                    System.out.println("Logging out...");
                    run = false;

                    break;

                default:
                    System.out.println("Please enter a valid choice of number");

                    break;
            }
        }
    }

    public void printAdminMenu(User user) {
        boolean run = true;
        Scanner scannerInt = new Scanner(System.in);
        Scanner scannerBoolean = new Scanner(System.in);

        while (run) {
            System.out.println("\nPlease Enter Your Choice!\n");
            System.out.println("1. Manage Planes");
            System.out.println("2. Manage Flights");
            System.out.println("3. Manage Promos");
            System.out.println("4. Enable or Disable User");
            System.out.println("5. Logout");

            int choice = scannerInt.nextInt();

            switch (choice) {
                case 1:
                    planeDashboard.printPlaneMenu();
                    break;

                case 2:
                    flightDashboard.printFlightMenu();
                    break;

                case 3:
                    promoDashboard.printPromoMenu();
                    break;

                case 4:
                    String listOfUsers = userService.getAllUsers();
                    System.out.println(listOfUsers);
                    System.out.println("");
                    System.out.println("Please enter the id of the user, whose status you would like to change");
                    Integer choiceUserId = scannerInt.nextInt();

                    String choiceUser = userService.getUserById(choiceUserId);
                    System.out.println(choiceUser);
                    System.out.println("Enter *true* if you would like to enable the user");
                    System.out.println("Enter *false* if you would like to disable the user");
                    Boolean choiceStatus = scannerBoolean.nextBoolean();

                    String userNewStatus = userService.setDisableOrEnableUser(new User(choiceUserId), choiceStatus);
                    System.out.println(userNewStatus);

                    break;

                case 5:
                    System.out.println("Logging out...");

                    run = false;

                    break;
            }
        }
    }
}