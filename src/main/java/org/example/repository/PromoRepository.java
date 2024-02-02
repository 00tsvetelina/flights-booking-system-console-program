package org.example.repository;

import org.example.model.Promo;
import org.example.utils.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PromoRepository implements Repository<Promo> {

    @Override
    public List<Promo> getAll() {
        String query = "SELECT * FROM promo";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            ResultSet response = statement.executeQuery();
            if (response == null) {
                return null;
            }

            List<Promo> promos = new ArrayList<>();
            while (response.next()) {
                Integer id = response.getInt("id");
                String promoCode = response.getString("promo_code");

                Promo promo = responseGetFields(response, id, promoCode);
                promos.add(promo);
            }
            return promos;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public Promo getById(Integer id){
        String query = "SELECT * FROM promo WHERE id=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setInt(1, id);
            if (statement.executeQuery() == null) {
                return null;
            }

            ResultSet response = statement.executeQuery();
            if (response.next()) {
                String promoCode = response.getString("promo_code");
          
                return responseGetFields(response, id, promoCode);
            }
        } catch (SQLException ex) {
            return null;
        }

        return null;
    }

    public List<Promo> getPromosByTicketId(Integer ticketId) {
        String query = "SELECT * FROM ticket" +
                " JOIN promo_ticket ON ticket.id=promo_ticket.ticket_id " +
                "JOIN promo ON promo_ticket.promo_id=promo.id " +
                "WHERE ticket.id=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setInt(1, ticketId);
            if (statement.executeQuery() == null) {
                return null;
            }
            ResultSet response = statement.executeQuery();
            List<Promo> promos = new ArrayList<>();
            while(response.next()) {
                Integer id = response.getInt("id");
                String promoCode = response.getString("promo_code");

                Promo promo = responseGetFields(response, id, promoCode);
                promos.add(promo);
            }
            return promos;

        } catch (SQLException ex) {
            return null;
        }
    }

    public Promo getPromoByPromoCode(String promoCode) {
        String query = "SELECT * FROM promo WHERE promo_code=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setString(1, promoCode);
            if (statement.executeQuery() == null) {
                return null;
            }

            ResultSet response = statement.executeQuery();
            if (response.next()) {
                Integer id = response.getInt("id");
                
                return responseGetFields(response, id, promoCode);
            }

        } catch (SQLException ex) {
            return null;
        }

        return null;
    }

    @Override
    public Promo create(Promo promo) {
        String query = "INSERT INTO promo(promo_code, percent_discount, duration_end, single_use, is_used)" +
                " VALUES (?,?,?,?,?)";
        try (PreparedStatement statement = DBUtil.getStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statementSetFields(statement, promo);
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

    @Override
    public Promo update(Promo promo) {
        String query = "UPDATE promo SET promo_code=?, percent_discount=?," +
                " duration_end=?, single_use=?, is_used=? WHERE id=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statementSetFields(statement, promo);
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

    @Override
    public void delete(Integer id) {
        String query = "DELETE FROM promo WHERE id=?";
        try (PreparedStatement statement = DBUtil.getStatement(query, 0)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() < 0) {
                System.out.printf("Error while deleting promo with id: %d", id);
            }
        } catch (SQLException ex) {
            System.out.printf("Error occurred while deleting promo with id: %d", id);
        }
    }

    public void deletePromoTicketRelations(Integer promoId) {
        try (PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                "DELETE FROM promo_ticket WHERE promo_id=?"
        )) {
            statement.setInt(1, promoId);
            if (statement.executeUpdate() < 0) {
                System.out.printf("Error occurred while deleting relation with promo id: %d", promoId);
            }
        } catch (SQLException ex) {
            System.out.printf("Error occurred while deleting relation with promo id: %d", promoId);
        }
    }

    private Promo responseGetFields(ResultSet response, Integer id, String promoCode) {
        try {
            
            Integer percentDiscount = response.getInt("percent_discount");
            LocalDate durationEnd = response.getDate("duration_end").toLocalDate();
            Boolean singleUse = response.getBoolean("single_use");
            Boolean isUsed = response.getBoolean("is_used");

            return new Promo(id, promoCode, percentDiscount, durationEnd, singleUse, isUsed);
        } catch (SQLException ex) {
            return null;
        }
    }

    private void statementSetFields(PreparedStatement statement, Promo promo) {
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
 }
