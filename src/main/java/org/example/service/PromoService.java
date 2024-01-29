package org.example.service;

import org.example.model.Promo;
import org.example.repository.PromoRepository;

import java.time.LocalDate;

public class PromoService {

    private final PromoRepository promoRepository;

    public PromoService(PromoRepository promoRepository) {
        this.promoRepository = promoRepository;
    }

    public String updatePromo(Promo promo) {
        if (promo.getId() == null) {
           return "Promo id cannot be null";
        }

        String validationError = validatePromo(promo);
        if (validationError != null) {
            return validationError;
        }

        promoRepository.updatePromo(promo);
        return null;
    }

    private String validatePromo(Promo promo) {
        if (promo.getPromoCode().isEmpty() || promo.getPromoCode().isBlank() || promo.getPromoCode().length() > 10) {
            return "Promo code should not be blank or exceed 10 characters";
        }

        if (promo.getPercentDiscount() < 0 || promo.getPercentDiscount() == null) {
            return "Discount should not be blank or negative number";
        }

        if (promo.getDurationEnd().isBefore(LocalDate.now()) || promo.getDurationEnd() == null) {
            return "Invalid duration end date, please enter a valid future date";
        }

        if (promo.getUsed() == null || promo.getSingleUse() == null) {
            return "Value of *is used* and *single use* cannot be blank, please enter true or false";
        }

        return null;
    }


    public void setUsedToTrue(Promo promo){
        promoRepository.updatePromo(new Promo(promo.getId(), promo.getPromoCode(), promo.getPercentDiscount(),
                promo.getDurationEnd(), promo.getSingleUse(), true));

    }


    public Promo getPromoByPromoCode(String promoCode) {
        return promoRepository.getPromoByPromoCode(promoCode);
    }

}
