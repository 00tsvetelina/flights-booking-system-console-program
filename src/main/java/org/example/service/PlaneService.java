package org.example.service;

import org.example.model.Plane;
import org.example.repository.PlaneRepository;

import java.util.List;

public class PlaneService {

    PlaneRepository planeRepository;

    public PlaneService(PlaneRepository planeRepository) {
        this.planeRepository = planeRepository;
    }

    public String getAllPlanes() {
        List<Plane> planes = planeRepository.getAllPlanes();
        if(planes.isEmpty()) {
            return "No planes found, sorry!";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here are all available planes: \n" );
        for (Plane plane: planes) {
           sb.append(plane.toString()).append("\n");
        }

        return sb.toString();
    }

    public String getPlaneById(Integer id) {
        if(id <= 0) {
            return "Invalid id, please enter a positive number";
        }

        Plane plane = planeRepository.getPlaneById(id);
        if (plane == null) {
            return String.format("Could not find plane with id: %d", id);
        }

        return String.format("Plane with id: %d found - %s", id, plane);
    }

    public String createPlane(Plane plane) {

        if (plane.getModel().isBlank() || plane.getModel().isEmpty()) {
            return "Plane model cannot be empty or null";
        }

        if(plane.getSeatsCount() < 50) {
            return "Minimum seats count could be 50";
        }

        Plane createdPlane = planeRepository.createPlane(plane);
        if (createdPlane == null) {
            return "Could not create new plane";
        }

        return "Plane created successfully";
    }

    public String updatePlane(Plane plane) {
        if (plane.getId() == null) {
            return "Plane id cannot be null";
        }

        if (plane.getModel().isBlank() || plane.getModel().isEmpty()) {
            return "Plane model cannot be empty or null";
        }

        if(plane.getSeatsCount() < 50) {
            return "Minimum seats count could be 50";
        }

        Plane updatedPlane = planeRepository.updatePlane(plane);
        if (updatedPlane == null) {
            return "Could not update plane";
        }

        return "Plane updated successfully";
    }

    public String deletePlane(Integer id) {
        if(id <= 0) {
            return "Invalid id, please enter a positive number";
        }

        if(planeRepository.getPlaneById(id) == null) {
            return String.format("Cannot find plane with id: %d", id);
        }

        planeRepository.deletePlane(id);
        return String.format("Plane with id: %d successfully deleted!", id);
    }

}
