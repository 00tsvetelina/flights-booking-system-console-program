package org.example.dashboard;

import org.example.model.Flight;
import org.example.model.Plane;
import org.example.service.FlightService;
import org.example.service.PlaneService;

import java.time.LocalDate;
import java.util.Scanner;

public class FlightDashboard implements Dashboard{

    private final FlightService flightService;
    private final PlaneService planeService;
    private final Scanner scannerInt;
    private final Scanner scannerString;
    private final Scanner scannerFloat;

    public FlightDashboard(FlightService flightService, PlaneService planeService) {
        this.flightService = flightService;
        this.planeService = planeService;
        this.scannerInt = new Scanner(System.in);
        this.scannerString = new Scanner(System.in);
        this.scannerFloat = new Scanner(System.in);
    }

    @Override
    public void printMenu() {
        boolean run = true;

        while (run) {

            printOptions();

            int choice = scannerInt.nextInt();
            String listOfPlanes = planeService.getAll();
            String listOfFlights = flightService.getAll();

            switch (choice) {

                case 1:
                    addFlight(listOfPlanes);
                    break;

                case 2:
                    editFlight(listOfFlights, listOfPlanes);
                    break;

                case 3:
                    deleteFlight(listOfFlights);
                    break;

                case 4:
                    System.out.println(listOfFlights);
                    break;

                case 5:
                    getFlightById();
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

    private void printOptions() {
        System.out.println("\nWelcome to Flight Dashboard!\n");
        System.out.println("Please Enter Your Choice!\n");
        System.out.println("1. Add Flight");
        System.out.println("2. Edit Flight");
        System.out.println("3. Delete Flight");
        System.out.println("4. Get List of All Flights");
        System.out.println("5. Get a Single Flight by Id");
        System.out.println("6. Exit Flight Dashboard");
    }

    private void addFlight(String listOfPlanes) {
        System.out.println(listOfPlanes);
        System.out.println();

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
        String responseCreated = flightService.create(createdFlight);
        System.out.println(responseCreated);
    }

    private void editFlight(String listOfFlights, String listOfPlanes) {
        System.out.println(listOfFlights);
        System.out.println();

        System.out.println("Edit a Flight:");
        System.out.println("Please Enter Existing Flight Id");
        int choiceIdEdit = scannerInt.nextInt();

        System.out.println(listOfPlanes);
        System.out.println();
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
        String responseEdited = flightService.update(editedFlight);
        System.out.println(responseEdited);
    }

    private void deleteFlight(String listOfFlights) {
        System.out.println(listOfFlights);
        System.out.println();

        System.out.println("Delete a Flight:");
        System.out.println("Please Flight Id");
        int choiceIdDelete = scannerInt.nextInt();

        String responseDelete = flightService.deleteById(choiceIdDelete);
        System.out.println(responseDelete);
    }

    private void getFlightById() {
        System.out.println("View a Flight by Id:");
        System.out.println("Please Enter Flight Id");
        int choiceIdFind = scannerInt.nextInt();

        String responseFindById = flightService.getById(choiceIdFind);
        System.out.println(responseFindById);
    }
}
