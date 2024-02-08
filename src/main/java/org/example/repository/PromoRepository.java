package org.example.repository;

import org.example.model.Promo;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PromoRepository implements Repository<Promo> {

    @Override
    public List<Promo> getAll() {
        String query = "SELECT * FROM promo";
        return Repository.super.executeQuery(query, this::mapToPromoList);
    }

    @Override
    public Promo getById(Integer id){
        String query = String.format("SELECT * FROM promo WHERE id=%d", id);
        return Repository.super.executeQuery(query, this::mapToPromo);
    }

    public List<Promo> getPromosByTicketId(Integer ticketId) {
        String query = String.format("SELECT * FROM ticket" +
                " JOIN promo_ticket ON ticket.id=promo_ticket.ticket_id " +
                "JOIN promo ON promo_ticket.promo_id=promo.id " +
                "WHERE ticket.id=%d", ticketId);
        return Repository.super.executeQuery(query, this::mapToPromoList);
    }

    public Promo getPromoByPromoCode(String promoCode) {
        String query = String.format("SELECT * FROM promo WHERE promo_code='%s'", promoCode);
        return Repository.super.executeQuery(query, this::mapToPromo);
    }

    @Override
    public Promo create(Promo promo) {
        String query = "INSERT INTO promo(promo_code, percent_discount, duration_end, single_use, is_used)" +
                " VALUES (?,?,?,?,?)";
        int result = Repository.super.executeUpdate(query, this::mapToStatementFields, promo);
        if (result > 0) {
            promo.setId(result);
            return promo;
        }

        return null;
    }

    @Override
    public Promo update(Promo promo) {
        String query = String.format("UPDATE promo SET promo_code=?, percent_discount=?," +
                " duration_end=?, single_use=?, is_used=? WHERE id=%d", promo.getId());
        int result = Repository.super.executeUpdate(query, this::mapToStatementFields, promo);
        if (result > 0) {
            return promo;
        }

        return null;
    }

    @Override
    public void deleteById(Integer id) {
        String query = String.format("DELETE FROM promo WHERE id=%d", id);
        int result = Repository.super.executeDelete(query);

        if (result < 0) {
            System.out.print("Error occurred while performing delete");
        }
    }

    public void deletePromoTicketRelations(Integer promoId) {
        String query = String.format("DELETE FROM promo_ticket WHERE promo_id=%d", promoId);
        int result = Repository.super.executeDelete(query);

        if (result < 0) {
            System.out.printf("Error occurred while deleting ticket with promo id: %d", promoId);
        }
    }



    private Promo mapToPromo(ResultSet resultSet) {
        try {
            if (!resultSet.next()) {
                return null;
            }

            Integer id = resultSet.getInt("id");
            String promoCode = resultSet.getString("promo_code");
            Integer percentDiscount = resultSet.getInt("percent_discount");
            LocalDate durationEnd = resultSet.getDate("duration_end").toLocalDate();
            Boolean singleUse = resultSet.getBoolean("single_use");
            Boolean isUsed = resultSet.getBoolean("is_used");

            return new Promo(id, promoCode, percentDiscount, durationEnd, singleUse, isUsed);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }


    private void mapToStatementFields(PreparedStatement statement, Promo promo) {
        try {
            statement.setString(1, promo.getPromoCode());
            statement.setInt(2, promo.getPercentDiscount());
            statement.setDate(3, Date.valueOf(promo.getDurationEnd()));
            statement.setBoolean(4, promo.getSingleUse());
            statement.setBoolean(5, promo.getUsed());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private List<Promo> mapToPromoList(ResultSet resultSet) {
        List<Promo> promos = new ArrayList<>();
        while (true) {
            Promo promo = mapToPromo(resultSet);
            if (promo == null) {
                break;
            }
            promos.add(promo);
        }
        return promos;
    }
 }
