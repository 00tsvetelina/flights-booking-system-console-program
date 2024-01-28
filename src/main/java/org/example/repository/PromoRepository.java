package org.example.repository;

import org.example.model.Promo;
import org.example.utils.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PromoRepository {

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
}
