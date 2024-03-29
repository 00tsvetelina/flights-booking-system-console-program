package org.example.model;

import java.util.List;

public class Ticket extends BaseId {

    private Flight flight;
    private Integer seat;
    private User user;
    private List<Promo> promos;

    public Ticket(Integer id, Flight flight, Integer seat, User user, List<Promo> promos) {
        super(id);
        this.flight = flight;
        this.seat = seat;
        this.user = user;
        this.promos = promos;
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

    public void setPromos(List<Promo> promos) {
        this.promos = promos;
    }

    @Override
    public String toString() {
        return "Ticket {id: " + getId() +
                ", origin: " + flight.getOrigin() +
                ", destination: " + flight.getDestination() +
                ", departure date: " + flight.getDepartureTime() +
                ", seat: " + seat + '\'' +
                ", price: " + calculatePrice() +
                '}';
    }

    private float calculatePrice() {
        Float flightPrice = flight.getPrice();
        if (promos != null) {
            for (Promo promo : promos) {
                float discount = promo.getPercentDiscount().floatValue();
                flightPrice = flightPrice - flightPrice * discount / 100;
            }
        }

        return flightPrice;
    }
}
