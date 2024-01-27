package org.example;


import org.example.dashboard.PlaneDashboard;
import org.example.repository.FlightRepository;
import org.example.repository.PlaneRepository;
import org.example.service.PlaneService;
import org.example.utils.DBUtil;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        PlaneRepository planeRepository = new PlaneRepository();
        FlightRepository flightRepository = new FlightRepository(planeRepository);

        PlaneService planeService = new PlaneService(planeRepository, flightRepository);
        PlaneDashboard planeDashboard = new PlaneDashboard(planeService);

        planeDashboard.printPlaneMenu();

        try {
            DBUtil.getConnection().close();
        } catch (SQLException e) {
            System.out.println("Problem while closing the connection occurred.");
        }
    }

}