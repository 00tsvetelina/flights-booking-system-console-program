package org.example.dashboard;

import org.example.model.User;
import org.example.service.UserService;

import java.util.Scanner;

public class UserDashboard implements Dashboard {

    private final UserService userService;
    private final PlaneDashboard planeDashboard;
    private final FlightDashboard flightDashboard;
    private final PromoDashboard promoDashboard;
    private final TicketDashboard ticketDashboard;
    private final Scanner scannerInt;
    private final Scanner scannerString;
    private final Scanner scannerBoolean;

    public UserDashboard(UserService userService, PlaneDashboard planeDashboard, FlightDashboard flightDashboard,
                         PromoDashboard promoDashboard, TicketDashboard ticketDashboard) {
        this.userService = userService;
        this.planeDashboard = planeDashboard;
        this.flightDashboard = flightDashboard;
        this.promoDashboard = promoDashboard;
        this.ticketDashboard = ticketDashboard;
        this.scannerInt = new Scanner(System.in);
        this.scannerString = new Scanner(System.in);
        this.scannerBoolean = new Scanner(System.in);
    }

    @Override
    public void printMenu() {
        boolean run = true;

        while (run) {
            printOptions();

            int choice = scannerInt.nextInt();

            switch (choice) {
                case 1:
                    login();
                    break;

                case 2:
                    register();
                    break;

                case 3:
                    System.out.println("Exiting program...");
                    run = false;
                    break;

                default:
                    System.out.println("Please enter a valid choice of number");
                    break;
            }
        }
    }

    private void printOptions() {
        System.out.println("\nWelcome to Flight Management System!\n");
        System.out.println("Please Enter Your Choice!\n");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
    }

    private void login() {
        System.out.println("Please enter your username");
        String choiceUsername = scannerString.nextLine();

        System.out.println("Please enter your password");
        String choicePassword = scannerString.nextLine();

        User user = userService.loginUser(choiceUsername, choicePassword);
        if (user == null) {
            return;
        }

        if (user.getRole().equals("admin")) {
            printAdminMenu();
        } else {
            printUserMenu(user);
        }
    }

    private void register() {
        System.out.println("Please enter a username");
        String choiceNewUsername = scannerString.nextLine();

        System.out.println("Please enter an email");
        String choiceNewEmail = scannerString.nextLine();

        System.out.println("Please enter a password");
        String choiceNewPassword = scannerString.nextLine();

        String registration = userService.create(new User(null, choiceNewUsername, choiceNewEmail,
                choiceNewPassword, true, "user"));
        System.out.println(registration);
    }

    public void printUserMenu(User user) {

        boolean run = true;

        while (run) {
            printUserOptions();

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
                        userService.deleteById(user.getId());
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

    public void printAdminMenu() {
        boolean run = true;

        while (run) {
            printAdminOptions();

            int choice = scannerInt.nextInt();

            switch (choice) {
                case 1:
                    planeDashboard.printMenu();
                    break;

                case 2:
                    flightDashboard.printMenu();
                    break;

                case 3:
                    promoDashboard.printMenu();
                    break;

                case 4:
                    enableOrDisableUser();
                    break;

                case 5:
                    System.out.println("Logging out...");
                    run = false;
                    break;
            }
        }
    }

    private void printUserOptions() {
        System.out.println("\nPlease Enter Your Choice!\n");
        System.out.println("1. Manage Tickets");
        System.out.println("2. Delete account");
        System.out.println("3. Logout");
    }

    private void printAdminOptions() {
        System.out.println("\nPlease Enter Your Choice!\n");
        System.out.println("1. Manage Planes");
        System.out.println("2. Manage Flights");
        System.out.println("3. Manage Promos");
        System.out.println("4. Enable or Disable User");
        System.out.println("5. Logout");
    }

    private void enableOrDisableUser() {
        String listOfUsers = userService.getAll();
        System.out.println(listOfUsers);
        System.out.println();
        System.out.println("Please enter the id of the user, whose status you would like to change");
        Integer choiceUserId = scannerInt.nextInt();

        String choiceUser = userService.getById(choiceUserId);
        System.out.println(choiceUser);
        System.out.println("Enter *true* if you would like to enable the user");
        System.out.println("Enter *false* if you would like to disable the user");
        Boolean choiceStatus = scannerBoolean.nextBoolean();

        String userNewStatus = userService.setDisableOrEnableUser(choiceUserId, choiceStatus);
        System.out.println(userNewStatus);
    }

}