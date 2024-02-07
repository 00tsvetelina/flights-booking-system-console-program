package org.example.service;

import org.example.model.Promo;
import org.example.repository.PromoRepository;
import org.example.utils.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PromoService implements Service<Promo>{

    private final PromoRepository promoRepository;

    public PromoService(PromoRepository promoRepository) {
        this.promoRepository = promoRepository;
    }

    @Override
    public String getAll() {
        List<Promo> promos = promoRepository.getAll();
        if (promos.isEmpty()) {
            return "No promos found, sorry!";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here is a list of all promos: \n");
        for (Promo promo : promos) {
            sb.append(promo.toString()).append("\n");
        }

        return sb.toString();
    }

    @Override
    public String getById(Integer id) {
        if (id <= 0) {
            return "Invalid id, please enter a positive number";
        }

        Promo promo = promoRepository.getById(id);
        if (promo == null) {
            return String.format("Could not find promo with id: %d", id);
        }

        return String.format("Promo with id: %d found - %s", id, promo);
    }

    @Override
    public String create(Promo promo) {
        String validationError = validatePromo(promo);
        if (validationError != null) {
            return validationError;
        }

        promoRepository.create(promo);
        return  "Promo created successfully";
    }

    @Override
    public String update(Promo promo) {
        if (promo.getId() == null) {
           return "Promo id cannot be null";
        }

        String validationError = validatePromo(promo);
        if (validationError != null) {
            return validationError;
        }

        promoRepository.update(promo);
        return "Promo updated successfully";
    }

    @Override
    public String deleteById(Integer id) {
        try {
            if (id <= 0) {
                return "Invalid id, please enter a positive number";
            }

            if (promoRepository.getPromosByTicketId(id) == null) {
                return String.format("Cannot find promo with id: %d", id);
            }

            Connection con = DBUtil.getConnection();
            con.setAutoCommit(false);

            promoRepository.deletePromoTicketRelations(id);
            promoRepository.deleteById(id);
            con.commit();

            return String.format("Promo with id: %d successfully deleted!", id);
        } catch (SQLException ex) {
            return String.format("Error while trying to delete promo with id: %d", id);
        }
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

    public Promo getPromoByPromoCode(String promoCode) {
        return promoRepository.getPromoByPromoCode(promoCode);
    }

}
