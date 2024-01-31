package org.example.repository;

import org.example.model.Promo;
import org.example.utils.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PromoRepository {

    public List<Promo> getAllPromos() {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement("SELECT * FROM promo");

            ResultSet response = statement.executeQuery();
            if (response == null) {
                return null;
            }

            List<Promo> promos = new ArrayList<>();
            while (response.next()) {
                Integer id = response.getInt("id");
                String promoCode = response.getString("promo_code");
                Integer percentDiscount = response.getInt("percent_discount");
                LocalDate durationEnd = response.getDate("duration_end").toLocalDate();
                Boolean singleUse = response.getBoolean("single_use");
                Boolean isUsed = response.getBoolean("is_used");

                Promo promo = new Promo(id, promoCode, percentDiscount, durationEnd, singleUse, isUsed);
                promos.add(promo);
            }
            return promos;
        } catch (SQLException ex) {
            return null;
        }
    }

    public Promo getPromoById(Integer id){
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement("SELECT * FROM promo WHERE id=?");

            statement.setInt(1, id);
            if (statement.executeQuery() == null) {
                return null;
            }

            ResultSet response = statement.executeQuery();
            if (response.next()) {
                String promoCode = response.getString("promo_code");
                Integer percentDiscount = response.getInt("percent_discount");
                LocalDate durationEnd = response.getDate("duration_end").toLocalDate();
                Boolean singleUse = response.getBoolean("single_use");
                Boolean isUsed = response.getBoolean("is_used");

                return new Promo(id, promoCode, percentDiscount, durationEnd, singleUse, isUsed);
            }
        } catch (SQLException ex) {
            return null;
        }

        return null;
    }

    public List<Promo> getPromosByTicketId(Integer ticketId) {
        try (PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                "SELECT * FROM ticket" +
                        " JOIN promo_ticket ON ticket.id=promo_ticket.ticket_id " +
                        "JOIN promo ON promo_ticket.promo_id=promo.id " +
                        "WHERE ticket.id=?"
        )){
            statement.setInt(1, ticketId);
            if (statement.executeQuery() == null) {
                return null;
            }

            ResultSet response = statement.executeQuery();
            List<Promo> promos = new ArrayList<>();
            while(response.next()) {
                Integer id = response.getInt("id");
                String promoCode = response.getString("promo_code");
                Integer percentDiscount = response.getInt("percent_discount");
                LocalDate durationEnd = response.getDate("duration_end").toLocalDate();
                Boolean singleUse = response.getBoolean("single_use");
                Boolean isUsed = response.getBoolean("is_used");

                Promo promo = new Promo(id, promoCode, percentDiscount, durationEnd, singleUse, isUsed);
                promos.add(promo);
            }
            return promos;

        } catch (SQLException ex) {
            return null;
        }
    }

    public Promo getPromoByPromoCode(String promoCode) {
        try(PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                "SELECT * FROM promo WHERE promo_code=?"
        )) {

            statement.setString(1, promoCode);
            if (statement.executeQuery() == null) {
                return null;
            }

            ResultSet response = statement.executeQuery();
            if (response.next()) {
                Integer id = response.getInt("id");
                Integer percentDiscount = response.getInt("percent_discount");
                LocalDate durationEnd = response.getDate("duration_end").toLocalDate();
                Boolean singleUse = response.getBoolean("single_use");
                Boolean isUsed = response.getBoolean("is_used");

                return new Promo(id, promoCode, percentDiscount, durationEnd, singleUse, isUsed);
            }

        } catch (SQLException ex) {
            return null;
        }

        return null;
    }

    public Promo createPromo(Promo promo) {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "INSERT INTO promo(promo_code, percent_discount, duration_end, single_use, is_used)" +
                            " VALUES (?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            statement.setString(1, promo.getPromoCode());
            statement.setInt(2, promo.getPercentDiscount());
            statement.setDate(3, Date.valueOf(promo.getDurationEnd()));
            statement.setBoolean(4, promo.getSingleUse());
            statement.setBoolean(5, promo.getUsed());

            if (statement.executeUpdate() <= 0) {
                return null;
            }

            ResultSet response = statement.getGeneratedKeys();
            if (response.next()) {
                Integer id = response.getInt(1);
                promo.setId(id);

                return promo;
            }
        } catch (SQLException ex) {
            return null;
        }

        return null;
    }

    public Promo updatePromo(Promo promo) {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "UPDATE promo SET promo_code=?, percent_discount=?," +
                            " duration_end=?, single_use=?, is_used=? WHERE id=?"
            );
            statement.setString(1, promo.getPromoCode());
            statement.setInt(2, promo.getPercentDiscount());
            statement.setDate(3, Date.valueOf(promo.getDurationEnd()));
            statement.setBoolean(4, promo.getSingleUse());
            statement.setBoolean(5, promo.getUsed());
            statement.setInt(6, promo.getId());
            if (statement.executeUpdate() <= 0) {
                return null;
            }

            if (statement.executeUpdate() > 0) {
                System.out.println("in " + promo);

                return promo;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        System.out.println("out");

        return null;
    }
}
