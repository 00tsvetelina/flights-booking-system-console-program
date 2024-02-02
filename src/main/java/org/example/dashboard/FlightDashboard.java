package org.example.dashboard;

import org.example.model.Flight;
import org.example.model.Plane;
import org.example.service.FlightService;
import org.example.service.PlaneService;

import java.time.LocalDate;
import java.util.Scanner;

public class FlightDashboard {

    private final FlightService flightService;
    private final PlaneService planeService;

    public FlightDashboard(FlightService flightService, PlaneService planeService) {
        this.flightService = flightService;
        this.planeService = planeService;
    }

    public void printFlightMenu() {
        boolean run = true;
        Scanner scannerInt = new Scanner(System.in);
        Scanner scannerString = new Scanner(System.in);
        Scanner scannerFloat = new Scanner(System.in);

        while (run) {
            System.out.println("\nWelcome to Flight Dashboard!\n");
            System.out.println("Please Enter Your Choice!\n");
            System.out.println("1. Add Flight");
            System.out.println("2. Edit Flight");
            System.out.println("3. Delete Flight");
            System.out.println("4. Get List of All Flights");
            System.out.println("5. Get a Single Flight by Id");
            System.out.println("6. Exit Flight Dashboard");

            int choice = scannerInt.nextInt();
            String listOfPlanes = planeService.getAllPlanes();
            String listOfFlights = flightService.getAllFlights();

            switch (choice) {

                case 1:
                    System.out.println(listOfPlanes);
                    System.out.println("");

                    System.out.println("Create a Flight:");
                    System.out.println("Please Enter Plane Id");
                    Integer choicePlaneId = scannerInt.nextInt();

                    System.out.println("Please Enter Flight Destination");
                    String choiceDestination = scannerString.nextLine();

                    System.out.println("Please Enter Flight Origin");
                    String choiceOrigin = scannerString.nextLine();

                    System.out.println("Please Enter Flight Departure Date in Format yyyy-mm-dd");
                    String choiceDepartureTime = scannerString.nextLine();

                    System.out.println("Please Enter Flight Delay");
                    int choiceDelay = scannerInt.nextInt();

                    System.out.println("Please Enter Flight Price");
                    Float choicePrice = scannerFloat.nextFloat();

                    LocalDate departureTime = LocalDate.parse(choiceDepartureTime);
                    Flight createdFlight = new Flight(null, new Plane(choicePlaneId), choiceDestination,
                            choiceOrigin, departureTime, choiceDelay, choicePrice);
                    String responseCreated = flightService.createFlight(createdFlight);
                    System.out.println(responseCreated);

                    break;

                case 2:
                    System.out.println(listOfFlights);
                    System.out.println("");

                    System.out.println("Edit a Flight:");
                    System.out.println("Please Enter Existing Flight Id");
                    int choiceIdEdit = scannerInt.nextInt();

                    System.out.println(listOfPlanes);
                    System.out.println("");
                    System.out.println("Please Enter New Plane Id");
                    int choicePlaneIdEdit = scannerInt.nextInt();

                    System.out.println("Please Enter New Flight Destination");
                    String choiceDestinationEdit = scannerString.nextLine();

                    System.out.println("Please Enter New Flight Origin");
                    String choiceOriginEdit = scannerString.nextLine();

                    System.out.println("Please Enter New Flight Departure Date in Format yyyy-mm-dd");
                    String choiceDepartureTimeEdit = scannerString.nextLine();

                    System.out.println("Please Enter New Flight Delay");
                    int choiceDelayEdit = scannerInt.nextInt();

                    System.out.println("Please Enter New Flight Price");
                    Float choicePriceEdit = scannerFloat.nextFloat();

                    LocalDate departureTimeEdit = LocalDate.parse(choiceDepartureTimeEdit);
                    Flight editedFlight = new Flight(choiceIdEdit, new Plane(choicePlaneIdEdit), choiceDestinationEdit,
                            choiceOriginEdit, departureTimeEdit, choiceDelayEdit, choicePriceEdit);
                    String responseEdited = flightService.updateFlight(editedFlight);
                    System.out.println(responseEdited);

                    break;

                case 3:
                    System.out.println(listOfFlights);
                    System.out.println("");

                    System.out.println("Delete a Flight:");
                    System.out.println("Please Flight Id");
                    int choiceIdDelete = scannerInt.nextInt();

                    String responseDelete = flightService.deleteFlight(choiceIdDelete);
                    System.out.println(responseDelete);

                    break;

                case 4:
                    System.out.println("List of Flights: \n");
                    System.out.println(listOfFlights);

                    break;

                case 5:
                    System.out.println("View a Flight by Id:");
                    System.out.println("Please Enter Flight Id");
                    int choiceIdFind = scannerInt.nextInt();

                    String responseFindById = flightService.getFlightById(choiceIdFind);
                    System.out.println(responseFindById);

                    break;

                case 6:
                    System.out.println("Exiting Flight Dashboard...");

                    run = false;
                    break;

                default:
                    System.out.println("Please enter a valid choice of number");

                    break;
            }
        }
    }
}
