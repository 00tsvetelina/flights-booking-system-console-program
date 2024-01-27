package org.example.service;

import org.example.model.Plane;
import org.example.repository.FlightRepository;
import org.example.repository.PlaneRepository;
import org.example.utils.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PlaneService {

    private final PlaneRepository planeRepository;
    private final FlightRepository flightRepository;

    public PlaneService(PlaneRepository planeRepository, FlightRepository flightRepository) {
        this.planeRepository = planeRepository;
        this.flightRepository = flightRepository;
    }

    public String getAllPlanes() {
        List<Plane> planes = planeRepository.getAllPlanes();
        if (planes.isEmpty()) {
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
        if (id <= 0) {
            return "Invalid id, please enter a positive number";
        }

        Plane plane = planeRepository.getPlaneById(id);
        if (plane == null) {
            return String.format("Could not find plane with id: %d", id);
        }

        return String.format("Plane with id: %d found - %s", id, plane);
    }

    public String createPlane(Plane plane) {
        String validationError = validatePlane(plane);
        if (validationError != null) {
            return validationError;
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

        String validationError = validatePlane(plane);
        if (validationError != null) {
            return validationError;
        }

        Plane updatedPlane = planeRepository.updatePlane(plane);
        if (updatedPlane == null) {
            return "Could not update plane";
        }

        return "Plane updated successfully";
    }

    public String deletePlane(Integer id) {
        try {
            if (id <= 0) {
                return "Invalid id, please enter a positive number";
            }

            if (planeRepository.getPlaneById(id) == null) {
                return String.format("Cannot find plane with id: %d", id);
            }

            Connection con = DBUtil.getConnection();
            con.setAutoCommit(false);

            flightRepository.deleteFlightsByPlaneId(id);
            planeRepository.deletePlane(id);

            con.commit();

            return String.format("Successfully deleted plane with id: %d", id);
        } catch (SQLException e) {
            return String.format("Error while trying to delete plane with id: %d", id);
        }

    }

    private String validatePlane(Plane plane) {
        if (plane.getModel().isBlank() || plane.getModel().isEmpty()) {
            return "Plane model cannot be empty or null";
        }

        if (plane.getModel().length() > 10) {
            return "Plane model cannot contain more than 10 characters";
        }

        if  (plane.getSeatsCount() < 50) {
            return "Minimum seats count could be 50";
        }

        return null;
    }

}
