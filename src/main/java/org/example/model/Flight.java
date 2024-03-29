package org.example.model;

import java.time.LocalDate;

public class Flight extends BaseId {

    private Plane plane;
    private String destination;
    private String origin;
    private LocalDate departureTime;
    private Integer delay;
    private Float price;

    public Flight(Integer id, Plane plane, String destination, String origin,
                  LocalDate departureTime, Integer delay, Float price) {
        super(id);
        this.plane = plane;
        this.destination = destination;
        this.origin = origin;
        this.departureTime = departureTime;
        this.delay = delay;
        this.price = price;
    }

    public Flight(Integer id) {
        super(id);
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public LocalDate getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDate departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Flight {" +
                "id: " + getId() +
                ", plane: " + plane +
                ", destination: " + destination + '\'' +
                ", origin: " + origin + '\'' +
                ", departure date: " + departureTime +
                ", delay: " + delay + " mins " +
                ", price: $" + price +
                '}';
    }

}
