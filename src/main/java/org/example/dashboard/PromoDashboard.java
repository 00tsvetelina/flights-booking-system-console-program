package org.example.dashboard;

import org.example.model.Promo;
import org.example.service.PromoService;

import java.time.LocalDate;
import java.util.Scanner;

public class PromoDashboard {

    private final PromoService promoService;

    public PromoDashboard(PromoService promoService) {
        this.promoService = promoService;
    }

    public void printPromoMenu() {
        boolean run = true;
        Scanner scannerInt = new Scanner(System.in);
        Scanner scannerString = new Scanner(System.in);
        Scanner scannerBoolean = new Scanner(System.in);

        while (run) {
            System.out.println("\nWelcome to Promo Dashboard!\n");
            System.out.println("Please Enter Your Choice!\n");
            System.out.println("1. Add Promo");
            System.out.println("2. Edit Promo");
            System.out.println("3. Delete Promo");
            System.out.println("4. Get List of All Promos");
            System.out.println("5. Get a Single Promo by Id");
            System.out.println("6. Exit Promo Dashboard");

            int choice = scannerInt.nextInt();
            String listOfPromos = promoService.getAllPromos();

            switch (choice) {

                case 1:
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
                    String responseCreated = promoService.createPromo(createdPromo);
                    System.out.println(responseCreated);

                    break;

                case 2:
                    System.out.println(listOfPromos);
                    System.out.println("");

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
                    String responseEdited = promoService.updatePromo(editefPromo);
                    System.out.println(responseEdited);

                    break;

                case 3:
                    System.out.println(listOfPromos);
                    System.out.println("");

                    System.out.println("Delete a Promo:");
                    System.out.println("Please Enter Promo Id");
                    int choiceIdDelete = scannerInt.nextInt();

                    String responseDelete = promoService.deletePromo(choiceIdDelete);
                    System.out.println(responseDelete);

                    break;

                case 4:
                    System.out.println("List of Promos: \n");
                    System.out.println(listOfPromos);

                    break;

                case 5:
                    System.out.println("View a Promo by Id:");
                    System.out.println("Please Enter Promo Id");
                    int choiceIdFind = scannerInt.nextInt();

                    String responseFindById = promoService.getPromoById(choiceIdFind);
                    System.out.println(responseFindById);

                    break;

                case 6:
                    System.out.println("Exiting Promo Dashboard...");

                    scannerInt.close();
                    scannerString.close();
                    run = false;

                    break;
            }
        }
    }
}
