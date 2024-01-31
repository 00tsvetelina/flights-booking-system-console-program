package org.example.model;

import java.util.List;

public class Ticket {

    private Integer id;
    private Flight flight;
    private Integer seat;
    private User user;
    private List<Promo> promos;

    public Ticket(Integer id, Flight flight, Integer seat, User user, List<Promo> promos) {
        this.id = id;
        this.flight = flight;
        this.seat = seat;
        this.user = user;
        this.promos = promos;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Promo> getPromos() {
        return promos;
    }

    public void setPromo(List<Promo> promo) {
        this.promos = promos;
    }

    @Override
    public String toString() {
        return "Ticket {" +
                "id: " + id == null ? "" : id +
                ", origin: " + flight.getOrigin() +
                ", destination: " + flight.getDestination() +
                ", departure date: " + flight.getDepartureTime() +
                ", seat: " + seat + '\'' +
                ", price: " + calculatePrice() +
                '}';
    }

    private float calculatePrice() {
        Float flightPrice = flight.getPrice();
        float newPrice;
        float discount = 0f;

        for (Promo promo: promos) {
            discount = promo.getPercentDiscount().floatValue();
        }
        newPrice =  flightPrice - flightPrice*discount/100;
        return newPrice;
    }
}
