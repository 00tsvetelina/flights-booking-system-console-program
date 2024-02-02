package org.example.dashboard;

import org.example.model.User;
import org.example.service.UserService;

import java.util.Scanner;

public class MainDashboard {
    private final UserService userService;
    private final UserDashboard userDashboard;

    public MainDashboard(UserService userService, UserDashboard userDashboard) {
        this.userService = userService;
        this.userDashboard = userDashboard;
    }

    public void printMainDashboard() {
        boolean run = true;
        Scanner scannerInt = new Scanner(System.in);
        Scanner scannerString = new Scanner(System.in);

        while (run) {
            System.out.println("\nWelcome to Flight Management System!\n");
            System.out.println("Please Enter Your Choice!\n");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");


            int choice = scannerInt.nextInt();
            User user;

            switch (choice) {
                case 1:
                    System.out.println("Please enter your username");
                    String choiceUsername = scannerString.nextLine();

                    System.out.println("Please enter your password");
                    String choicePassword = scannerString.nextLine();

                    user = userService.loginUser(choiceUsername, choicePassword);

                    if (user.getRole().equals("admin")) {
                        userDashboard.printAdminMenu(user);
                    } else {
                        userDashboard.printUserMenu(user);
                    }

                    break;

                case 2:
                    System.out.println("Please enter a username");
                    String choiceNewUsername = scannerString.nextLine();

                    System.out.println("Please enter an email");
                    String choiceNewEmail = scannerString.nextLine();

                    System.out.println("Please enter a password");
                    String choiceNewPassword = scannerString.nextLine();

                    String registration = userService.registerUser(new User(null, choiceNewUsername, choiceNewEmail,
                    choiceNewPassword, true, "user"));
                    System.out.println(registration);

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
}
