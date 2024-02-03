package org.example.dashboard;

import org.example.model.Plane;
import org.example.service.PlaneService;

import java.util.Scanner;

public class PlaneDashboard implements Dashboard {

    private final PlaneService planeService;
    private final Scanner scannerInt;
    private final Scanner scannerString;

    public PlaneDashboard(PlaneService planeService) {
        this.planeService = planeService;
        this.scannerInt = new Scanner(System.in);
        this.scannerString = new Scanner(System.in);
    }

    @Override
    public void printMenu() {
        boolean run = true;

        while (run) {
            printOptions();

            int choice = scannerInt.nextInt();
            String listOfPlanes = planeService.getAll();

            switch (choice) {

                case 1:
                    addPlane();
                    break;

                case 2:
                    editPlane(listOfPlanes);
                    break;

                case 3:
                    deletePlane(listOfPlanes);
                    break;

                case 4:
                    System.out.println(listOfPlanes);
                    break;

                case 5:
                    getPlaneById();
                    break;

                case 6:
                    System.out.println("Exiting Plane Dashboard...");
                    run = false;
                    break;

                default:
                    System.out.println("Please enter a valid choice of number");
                    break;
            }
        }
    }

    private void printOptions() {
        System.out.println("\nWelcome to Plane Dashboard!\n");
        System.out.println("Please Enter Your Choice!\n");
        System.out.println("1. Add Plane");
        System.out.println("2. Edit Plane");
        System.out.println("3. Delete Plane");
        System.out.println("4. Get List of All Planes");
        System.out.println("5. Get a Single Plane by Id");
        System.out.println("6. Exit Plane Dashboard");
    }

    private void addPlane() {
        System.out.println("Create a Plane:");
        System.out.println("Please Enter Plane Model");
        String choiceModel = scannerString.nextLine();

        System.out.println("Please Enter Plane Seats Count");
        int choiceSeatsCount = scannerInt.nextInt();

        Plane createdPlane = new Plane(null, choiceModel, choiceSeatsCount);
        String responseCreated = planeService.create(createdPlane);
        System.out.println(responseCreated);
    }

    private void editPlane(String listOfPlanes) {
        System.out.println(listOfPlanes);
        System.out.println();

        System.out.println("Edit a Plane:");
        System.out.println("Please Enter Existing Plane Id");
        int choiceIdEdit = scannerInt.nextInt();

        System.out.println("Please Enter New Plane Model");
        String choiceModelEdit = scannerString.nextLine();

        System.out.println("Please Enter New Plane Seats Count");
        int choiceSeatsCountEdit = scannerString.nextInt();

        Plane editedPlane = new Plane(choiceIdEdit, choiceModelEdit, choiceSeatsCountEdit);
        String responseEdit = planeService.update(editedPlane);
        System.out.println(responseEdit);
    }

    private void deletePlane(String listOfPlanes) {
        System.out.println(listOfPlanes);
        System.out.println();

        System.out.println("Delete a Plane:");
        System.out.println("Please Enter Plane Id");
        int choiceIdDelete = scannerInt.nextInt();

        String responseDelete = planeService.deleteById(choiceIdDelete);
        System.out.println(responseDelete);
    }

    private void getPlaneById() {
        System.out.println("View a Plane by Id:");
        System.out.println("Please Enter Plane Id");
        int choiceIdFind = scannerInt.nextInt();

        String responseFindById = planeService.getById(choiceIdFind);
        System.out.println(responseFindById);
    }
}