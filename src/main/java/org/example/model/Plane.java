package org.example.model;

public class Plane extends CommonIdClass {

    private String model;
    private Integer seatsCount;

    public Plane(Integer id, String model, Integer seatsCount) {
        this.id = id;
        this.model = model;
        this.seatsCount = seatsCount;
    }

    public Plane(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getSeatsCount() {
        return seatsCount;
    }

    public void setSeatsCount(Integer seatsCount) {
        this.seatsCount = seatsCount;
    }

    @Override
    public String toString() {
        return "Plane {id: " + id +
                ", model: " + model + '\'' +
                '}';
    }

}
