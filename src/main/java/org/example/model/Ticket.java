package org.example.model;

public class Ticket {

    private Integer id;
    private Flight flight;
    private Integer seat;
    private User user;
    private Promo promo;

    public Ticket(Integer id, Flight flight, Integer seat, User user, Promo promo) {
        this.id = id;
        this.flight = flight;
        this.seat = seat;
        this.user = user;
        this.promo = promo;
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

    public Promo getPromo() {
        return promo;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
    }

    @Override
    public String toString() {
        return "Ticket {" +
                "id: " + id +
                ", origin: " + flight.getOrigin() +
                ", destination: " + flight.getDestination() +
                ", departure date: " + flight.getDepartureTime() +
                ", seat: " + seat + '\'' +
                ", price: " + calculatePrice() +
                '}';
    }

    private float calculatePrice() {
        Float flightPrice = flight.getPrice();
        Float discount = promo.getPercentDiscount().floatValue();
        return flightPrice - flightPrice*discount/100;
    }
}
