package org.example.dashboard;

import org.example.model.Promo;
import org.example.service.PromoService;

import java.time.LocalDate;
import java.util.Scanner;

public class PromoDashboard implements Dashboard {

    private final PromoService promoService;

    private final Scanner scannerInt;
    private final Scanner scannerString;
    private final Scanner scannerBoolean;

    public PromoDashboard(PromoService promoService) {
        this.promoService = promoService;
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
            String listOfPromos = promoService.getAll();

            switch (choice) {

                case 1:
                    createPromo();
                    break;

                case 2:
                    editPromo(listOfPromos);
                    break;

                case 3:
                    deletePromo(listOfPromos);
                    break;

                case 4:
                    System.out.println(listOfPromos);
                    break;

                case 5:
                    getPromoById();
                    break;

                case 6:
                    System.out.println("Exiting Promo Dashboard...");
                    run = false;
                    break;

                default:
                    System.out.println("Please enter a valid choice of number");
                    break;
            }
        }
    }

    private void printOptions() {
        System.out.println("\nWelcome to Promo Dashboard!\n");
        System.out.println("Please Enter Your Choice!\n");
        System.out.println("1. Add Promo");
        System.out.println("2. Edit Promo");
        System.out.println("3. Delete Promo");
        System.out.println("4. Get List of All Promos");
        System.out.println("5. Get a Single Promo by Id");
        System.out.println("6. Exit Promo Dashboard");
    }

    private void createPromo() {
        System.out.println("Create a Promo:");
        System.out.println("Please Enter Promo Code");
        String choicePromoCode = scannerString.nextLine();

        System.out.println("Please Enter Percent Discount");
        int choicePercentDiscount = scannerInt.nextInt();

        System.out.println("Please Enter Duration End Date in Format yyyy-mm-dd");
        String choiceDurationEnd = scannerString.nextLine();

        System.out.println("Please Confirm If Promo Is Single Use (true/false)");
        Boolean choiceSingleUse = scannerBoolean.nextBoolean();

        LocalDate durationEnd = LocalDate.parse(choiceDurationEnd);
        Promo createdPromo = new Promo(null, choicePromoCode, choicePercentDiscount, durationEnd,
                choiceSingleUse, false);
        String responseCreated = promoService.create(createdPromo);
        System.out.println(responseCreated);
    }

    private void editPromo(String listOfPromos) {
        System.out.println(listOfPromos);
        System.out.println();

        System.out.println("Edit a Promo:");
        System.out.println("Please Enter Existing Promo Id");
        int choiceIdEdit = scannerInt.nextInt();

        System.out.println("Please Enter New Promo Code");
        String choicePromoCodeEdit = scannerString.nextLine();

        System.out.println("Please New Enter Percent Discount");
        int choicePercentDiscountEdit = scannerInt.nextInt();

        System.out.println("Please Enter New Duration End Date in Format yyyy-mm-dd");
        String choiceDurationEndEdit = scannerString.nextLine();

        System.out.println("Please Confirm If Promo Is Single Use (true/false)");
        Boolean choiceSingleUseEdit = scannerBoolean.nextBoolean();

        LocalDate durationEndEdit = LocalDate.parse(choiceDurationEndEdit);
        Promo editefPromo = new Promo(choiceIdEdit, choicePromoCodeEdit, choicePercentDiscountEdit,
                durationEndEdit, choiceSingleUseEdit, false);
        String responseEdited = promoService.update(editefPromo);
        System.out.println(responseEdited);
    }

    private void deletePromo(String listOfPromos) {
        System.out.println(listOfPromos);
        System.out.println();

        System.out.println("Delete a Promo:");
        System.out.println("Please Enter Promo Id");
        int choiceIdDelete = scannerInt.nextInt();

        String responseDelete = promoService.deleteById(choiceIdDelete);
        System.out.println(responseDelete);
    }

    private void getPromoById() {
        System.out.println("View a Promo by Id:");
        System.out.println("Please Enter Promo Id");
        int choiceIdFind = scannerInt.nextInt();

        String responseFindById = promoService.getById(choiceIdFind);
        System.out.println(responseFindById);
    }
}
