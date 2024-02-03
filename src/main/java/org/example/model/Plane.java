package org.example.model;

public class Plane extends BaseId {

    private String model;
    private Integer seatsCount;

    public Plane(Integer id, String model, Integer seatsCount) {
        super(id);
        this.model = model;
        this.seatsCount = seatsCount;
    }

    public Plane(Integer id) {
        super(id);
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
        return "Plane {id: " + getId() +
                ", model: " + model + '\'' +
                ", seats: " + seatsCount +
                '}';
    }

}
