package org.example.dashboard;

import org.example.model.Plane;
import org.example.service.PlaneService;

import java.util.Scanner;

public class PlaneDashboard {

    PlaneService planeService;

    public PlaneDashboard(PlaneService planeService) {
        this.planeService = planeService;
    }

    public void printPlaneMenu() {
        boolean run = true;
        Scanner scannerInt = new Scanner(System.in);
        Scanner scannerString = new Scanner(System.in);

        while (run) {
            System.out.println("\nWelcome to Plane Dashboard!\n");
            System.out.println("Please Enter Your Choice!\n");
            System.out.println("1. Add Plane");
            System.out.println("2. Edit Plane");
            System.out.println("3. Delete Plane");
            System.out.println("4. Get List of All Planes");
            System.out.println("5. Get a Single Plane by Id");
            System.out.println("6. Exit Plane Dashboard");

            int choice = scannerInt.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("Create a Plane:");
                    System.out.println("Please Enter Plane Model");
                    String choiceModel = scannerString.nextLine();

                    System.out.println("Please Enter Plane Seats Count");
                    int choiceSeatsCount = scannerInt.nextInt();

                    Plane planeCreate = new Plane(null, choiceModel, choiceSeatsCount);
                    String responseCreate = planeService.createPlane(planeCreate);
                    System.out.println(responseCreate);

                    break;

                case 2:
                    System.out.println("Edit a Plane:");
                    System.out.println("Please Enter Plane Id");
                    int choiceIdEdit = scannerInt.nextInt();

                    System.out.println("Please Enter Plane Model");
                    String choiceModelEdit = scannerString.nextLine();

                    System.out.println("Please Enter Plane Seats Count");
                    int choiceSeatsCountEdit = scannerString.nextInt();

                    Plane planeEdit = new Plane(choiceIdEdit, choiceModelEdit, choiceSeatsCountEdit);
                    String responseEdit = planeService.updatePlane(planeEdit);
                    System.out.println(responseEdit);

                    break;

                case 3:
                    System.out.println("Delete a Plane:");
                    System.out.println("Please Enter Plane Id");
                    int choiceIdDelete = scannerInt.nextInt();

                    String responseDelete = planeService.deletePlane(choiceIdDelete);
                    System.out.println(responseDelete);

                    break;

                case 4:
                    System.out.println("List of Planes: \n");
                    String responseGetAllPlanes = planeService.getAllPlanes();
                    System.out.println(responseGetAllPlanes);

                    break;

                case 5:
                    System.out.println("View a Plane by Id: \n");
                    System.out.println("Please Enter Plane Id");
                    int choiceIdFind = scannerInt.nextInt();

                    String responseFindById = planeService.getPlaneById(choiceIdFind);
                    System.out.println(responseFindById);

                    break;

                case 6:
                    System.out.println("Exiting Plane Dashboard...");
                    scannerInt.close();
                    scannerString.close();
                    run = false;
                    break;
            }
        }
    }
}