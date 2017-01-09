/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.util.ArrayList;
import java.util.Date;
import lapr.project.model.network.Segment;

/**
 *
 * @author zero_
 */
public class FlightPlan {

    private int id;
    private String name;
    private AircraftModel.Type aircraftType;
    private Airport origin;
    private Airport dest;

    private double nNormalClass;
    private double nFirstClass;
    private double nCrew;


    public FlightPlan(String name, AircraftModel.Type aircraftType, Airport origin, Airport dest, double nNormalClass, double nFirstClass, double nCrew) {
        this.name = name;
        this.aircraftType = aircraftType;
        this.origin = origin;
        this.dest = dest;
        this.nNormalClass = nNormalClass;
        this.nFirstClass = nFirstClass;
        this.nCrew = nCrew;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public AircraftModel.Type getAircraftType() {
        return aircraftType;
    }

    public Airport getOrigin() {
        return origin;
    }

    public Airport getDest() {
        return dest;
    }

    public double getnNormalClass() {
        return nNormalClass;
    }

    public double getnFirstClass() {
        return nFirstClass;
    }

    public double getnCrew() {
        return nCrew;
    }

    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }
        if (otherObj == null || this.getClass() != otherObj.getClass()) {
            return false;
        }
        FlightPlan otherFlight = (FlightPlan) otherObj;
        return this.id == otherFlight.id;
    }

    @Override
    public String toString() {
        return "Flight{" + "id=" + id + ", name=" + name + '}';
    }

}
