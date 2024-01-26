package org.example.model;

public class Ticket {

    private Integer id;
    private Flight flight;
    private String seat;
    private User user;
    private Promo promo;

    public Ticket(Integer id, Flight flight, String seat, User user, Promo promo) {
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

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
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
        return "Ticket{" +
                "id=" + id +
                ", flight=" + flight +
                ", seat='" + seat + '\'' +
                ", user=" + user +
                ", promo=" + promo +
                '}';
    }

}
