package org.example;


import org.example.models.Plane;
import org.example.repositories.PlaneRepository;
import org.example.service.PlaneService;
import org.example.utils.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {


    public static void main(String[] args) {

        PlaneRepository planeRepository = new PlaneRepository();
        PlaneService planeService = new PlaneService(planeRepository);

//        String updatePlaneResponse = planeService.updatePlane(new Plane(3, "SDN-20", 150));
//        System.out.println(updatePlaneResponse);
//
//        String createPlaneResponse = planeService.createPlane(new Plane(null, "VFS-12", 200));
//        System.out.println(createPlaneResponse);

//        String findPlaneByIdResponse = planeService.getPlaneById(5);
//        System.out.println(findPlaneByIdResponse);

//        String findAllPlanesResponse = planeService.getAllPlanes();
//        System.out.println(findAllPlanesResponse);

//        String deletePlaneResponse = planeService.deletePlane(3);
//        System.out.println(deletePlaneResponse);

        DBUtil.getConnection();


    }

}