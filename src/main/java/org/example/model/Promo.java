package org.example.model;

import java.time.LocalDate;

public class Promo {

    private Integer id;
    private String promoCode;
    private Integer percentDiscount;
    private LocalDate durationEnd;
    private Boolean singleUse;
    private Boolean isUsed;


    public Promo(Integer id, String promoCode, Integer percentDiscount, LocalDate durationEnd, Boolean singleUse, Boolean isUsed) {
        this.id = id;
        this.promoCode = promoCode;
        this.percentDiscount = percentDiscount;
        this.durationEnd = durationEnd;
        this.singleUse = singleUse;
        this.isUsed = isUsed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public Integer getPercentDiscount() {
        return percentDiscount;
    }

    public void setPercentDiscount(Integer percentDiscount) {
        this.percentDiscount = percentDiscount;
    }

    public LocalDate getDurationEnd() {
        return durationEnd;
    }

    public void setDurationEnd(LocalDate durationEnd) {
        this.durationEnd = durationEnd;
    }

    public Boolean getSingleUse() {
        return singleUse;
    }

    public void setSingleUse(Boolean singleUse) {
        this.singleUse = singleUse;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    @Override
    public String toString() {
        return "Promo{" +
                "id=" + id +
                ", promoCode='" + promoCode + '\'' +
                ", percentDiscount=" + percentDiscount +
                ", durationEnd=" + durationEnd +
                ", singleUse=" + singleUse +
                ", isUsed=" + isUsed +
                '}';
    }

}
