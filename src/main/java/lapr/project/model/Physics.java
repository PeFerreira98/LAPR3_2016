/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import lapr.project.model.network.Segment;
import lapr.project.model.register.AircraftModelRegister;
import java.lang.Math;
import java.util.List;
import lapr.project.model.PhysicsConverters;
import lapr.project.model.network.AirNetwork;

/**
 *
 * @author João
 */
public class Physics {

    public static void calculateAirDensityTemperatureDueAltitude(double altitude, double p, double t) {
        //Valores em unidades SI

        if (altitude > 0 && altitude <= 1000) {
            p = 12.25;
            t = 15;

        } else if (altitude > 1000 && altitude <= 2000) {
            p = 11.12;
            t = 8.5;
        } else if (altitude > 2000 && altitude <= 3000) {
            p = 10.07;
            t = 2.00;
        } else if (altitude > 3000 && altitude <= 4000) {
            p = 9.093;
            t = -4.49;
        } else if (altitude > 4000 && altitude <= 5000) {
            p = 8.194;
            t = -10.98;
        } else if (altitude > 5000 && altitude <= 6000) {
            p = 7.634;
            t = -17.47;
        } else if (altitude > 6000 && altitude <= 7000) {
            p = 6.601;
            t = -23.96;
        } else if (altitude > 7000 && altitude <= 8000) {
            p = 5.9;
            t = -30.45;
        } else if (altitude > 8000 && altitude <= 9000) {
            p = 5.258;
            t = -36.94;
        } else if (altitude > 9000 && altitude <= 10000) {
            p = 4.671;
            t = -43.42;
        } else if (altitude > 10000 && altitude <= 11000) {
            p = 4.135;
            t = -49.90;
        } else if (altitude > 11000 && altitude <= 12000) {
            p = 4.135;
            t = -49.90;
        } else if (altitude > 12000 && altitude <= 13000) {
            p = 4.135;
            t = -49.90;
        } else if (altitude > 13000 && altitude <= 14000) {
            p = 4.135;
            t = -49.90;
        }
        //Valores iguais entre 10km e 15km
    }

    public static double calculateSpeedDueAltitudeClimbing(Aircraft aircraft, double altitude, double speed) {

        if (altitude >= 0 && altitude <= 1000) {
            speed = 210;
        }
        if (altitude > 1000 && altitude <= 2000) {
            speed = 210;
        }
        if (altitude > 2000 && altitude <= 3000) {
            speed = 220;
        }
        if (altitude > 3000 && altitude <= 4000) {
            speed = 230;
        }
        if (altitude > 4000 && altitude <= 5000) {
            speed = 250;
        }
        if (altitude > 5000 && altitude <= 6000) {
            speed = 260;
        }
        if (altitude > 6000 && altitude <= 7000) {
            speed = 290;
        }
        if (altitude > 7000 && altitude <= 8000) {
            speed = 290;
        }
        if (altitude > 8000 && altitude <= 9000) {
            speed = 290;
        }
        if (altitude > 9000 && altitude <= 10000) {
            speed = 290;
        }
        if (altitude > 10000 && altitude <= 11000) {
            speed = 290;
        }
        if (altitude > 11000 && altitude <= 12000) {
            speed = 300;
        }
        if (altitude > 12000 && altitude <= 13000) {
            speed = 300;
        }
        if (altitude > 13000 && altitude <= 14000) {
            speed = 300;
        }
        if (altitude > 14000) {
            speed = 300;
        }
        return speed;
    }

    public static double calculateSpeedDueAltitudeDescending(Aircraft aircraft, double altitude, double speed) {

        if (altitude >= 0 && altitude <= 1000) {
            speed = 180;
        }
        if (altitude > 1000 && altitude <= 2000) {
            speed = 200;
        }
        if (altitude > 2000 && altitude <= 3000) {
            speed = 250;
        }
        if (altitude > 3000 && altitude <= 4000) {
            speed = 250;
        }
        if (altitude > 4000 && altitude <= 5000) {
            speed = 270;
        }
        if (altitude > 5000 && altitude <= 6000) {
            speed = 300;
        }
        if (altitude > 6000 && altitude <= 7000) {
            speed = 300;
        }
        if (altitude > 7000 && altitude <= 8000) {
            speed = 300;
        }
        if (altitude > 8000 && altitude <= 9000) {
            speed = 300;
        }
        if (altitude > 9000 && altitude <= 10000) {
            speed = 300;
        }
        if (altitude > 10000 && altitude <= 11000) {
            speed = 300;
        }
        if (altitude > 11000 && altitude <= 12000) {
            speed = 300;
        }
        if (altitude > 12000 && altitude <= 13000) {
            speed = 300;
        }
        if (altitude > 13000 && altitude <= 14000) {
            speed = 300;
        }
        if (altitude > 14000) {
            speed = 300;
        }
        return speed;
    }

    //__________________________________________Variations with altitude formulas___________________________
    public static double calculateTemperatureDueAltitude(double altitude) {
        double temperatureLapseRate = -0.0065;
        if (altitude < 14000) {
            return altitude * temperatureLapseRate + 288.2;
        }
        return 216.7;
    }

    public static double calculateDensityDueAltitude(double altitude, double pressure, double temperature) {
        double universalGasConstant = 287;
        return pressure / (universalGasConstant * temperature);
    }

    public static double calculateSpeedOfSoundDueAltitude(double altitude) {

        double temperature = calculateTemperatureDueAltitude(altitude);

        double gamma = 1.4;
        double universalGasConstant = 287;
        return Math.sqrt(gamma * universalGasConstant * temperature); //valor em metros por segundo
    }

    public static double calculatePressureDueAltitude(double altitude) {

        return 101325 * Math.pow(1 - 0.0065 * altitude / 288.2, 5.2561); //288.2-Temp kelvin sea level
        //return a temperatura em kelvins
        //primeiro valor é a pressao ao nivel do mar
    }

    //___________________________________Forces and aircraft related formulas_____________________________________
    public static double calculateLiftForceInASegment(Aircraft aircraft, double altitude) {
        double AirDensity = 0;
        if (altitude == -1) {
            altitude = aircraft.getModel().getCruiseAltitude();
        }

        double temperature = calculateTemperatureDueAltitude(altitude);
        double pressure = calculatePressureDueAltitude(altitude);
        AirDensity = calculateDensityDueAltitude(altitude, pressure, temperature);
        //calculateAirDensityTemperatureDueAltitude(altitude, AirDensity, 0);

        return calculateLiftCoeficient(aircraft, altitude) * (AirDensity * Math.pow(aircraft.getModel().getCruiseSpeed(), 2) / 2) * aircraft.getModel().getWingArea();
    }

    public static double calculateDragForceInASegment(Aircraft aircraft, double altitude) {
        double AirDensity = 0;
        if (altitude == -1) {
            altitude = aircraft.getModel().getCruiseAltitude();
        }

        double temperature = calculateTemperatureDueAltitude(altitude);
        double pressure = calculatePressureDueAltitude(altitude);
        double speedVIAS = 0;
        AirDensity = calculateDensityDueAltitude(altitude, pressure, temperature);
        speedVIAS = calculateSpeedDueAltitudeClimbing(aircraft, altitude, speedVIAS);
        double machNumber = calculateTrueMachNumber(aircraft, altitude, speedVIAS);

        double speedOfSound = calculateSpeedOfSoundDueAltitude(altitude);

        double TAS = calculateTrueAirSpeed(machNumber, speedOfSound);
        double dragCoef = calculateDragCoeficient(aircraft, altitude);

        //calculateAirDensityTemperatureDueAltitude(altitude, AirDensity, 0);
        return 0.5 * AirDensity * Math.pow(TAS, 2)
                * aircraft.getModel().getWingArea() * dragCoef;
    }

    public static double calculateLiftCoeficient(Aircraft aircraft, double altitude) {
        double AirDensity = 0;
        if (altitude == -1) {
            altitude = aircraft.getModel().getCruiseAltitude();
        }
        double temperature = calculateTemperatureDueAltitude(altitude);
        double pressure = calculatePressureDueAltitude(altitude);
        AirDensity = calculateDensityDueAltitude(altitude, pressure, temperature);
        double speedVIAS = 0;
        speedVIAS = calculateSpeedDueAltitudeClimbing(aircraft, altitude, speedVIAS);
        double machNumber = calculateTrueMachNumber(aircraft, altitude, speedVIAS);

        double speedOfSound = calculateSpeedOfSoundDueAltitude(altitude);

        double TAS = calculateTrueAirSpeed(machNumber, speedOfSound);
        //calculateAirDensityTemperatureDueAltitude(altitude, AirDensity, 0);
        double finalWeight = calculateAircraftFinalWeight(aircraft);
        return (2 * finalWeight * 9.81) / (AirDensity * aircraft.getModel().getWingArea() * Math.pow(TAS, 2));
    }

    public static double calculateDragCoeficient(Aircraft aircraft, Double altitude) {
        if (altitude == -1) {
            altitude = aircraft.getModel().getCruiseAltitude();
        }

        double liftcoeficient = calculateLiftCoeficient(aircraft, altitude);
        //calculateAirDensityTemperatureDueAltitude(altitude, AirDensity, 0);
        //return aircraft.getModel().getCdragRegister().getCDrag(0).getcDrag0() + (Math.pow(calculateLiftCoeficient(aircraft, segment, altitude), 2)) / (Math.PI * (Math.pow(aircraft.getModel().getWingSpan(), 2) / aircraft.getModel().getWingArea()) * aircraft.getModel().getE());
        return aircraft.getModel().getCdragRegister().getCDrag(0).getcDrag0() + (Math.pow(liftcoeficient, 2) / (Math.PI * aircraft.getModel().getAspectRatio() * aircraft.getModel().getE()));
        //Cdrag 0 varia consoante a velocidade, falta depois compor, no excel o drag é sempre o mesmo
    }

    public static double calculateRangeEachSegment(Aircraft aircraft, Segment segment, double altitude) {
        if (altitude == -1) {
            altitude = aircraft.getModel().getCruiseAltitude();
        }

        double distance = calculateSegmentDistance(aircraft, segment);

        double time = calculateTravelTimeInASegment(aircraft, segment);

        double fuelComsuption = time * aircraft.getModel().getTSFC(); //(Consumo no segment)

        //Falta calcular o consumo nesta distancia à velocidade cruseiro
        return (aircraft.getModel().getCruiseSpeed() / aircraft.getModel().getTSFC())
                * (calculateLiftCoeficient(aircraft, altitude) / calculateDragCoeficient(aircraft, altitude)
                * Math.log(aircraft.getModel().getEmptyWeight() / calculateAircraftFinalWeight(aircraft)));
    }

    public static double calculateTravelTimeInASegment(Aircraft aircraft, Segment segment) {
        double distance = calculateSegmentDistance(aircraft, segment);
        //calculateSegmentDistanceInMiles(distance) / speedAndMMOConverterMachToKmsHour(aircraft.getModel().getRegimeRegister().getRegime("Cruise").getSpeed());
        return distance / PhysicsConverters.speedAndMMOConverterMachToKmsHour(aircraft.getModel().getCruiseSpeed()); //tempo(s)=distance(m)/speed(miles/sec?)
    }

    public static double calculateSegmentDistance(Aircraft aircraft, Segment segment) {

        double latitude1 = segment.getBeginningNode().getLatitude();
        double longitude1 = segment.getBeginningNode().getLongitude();

        double latitude2 = segment.getEndNode().getLatitude();
        double longitude2 = segment.getEndNode().getLongitude();
//        latitude1=Math.toRadians(latitude1);
//        latitude2=Math.toRadians(latitude2);
//        longitude1=Math.toRadians(longitude1);
//        longitude2=Math.toRadians(longitude2);

        double a = Math.toRadians(latitude2 - latitude1);
//        if(longitude2==longitude1){
//            longitude1=longitude1-0.0001;
//        }
        double b = Math.toRadians(longitude2 - longitude1);

        double a1 = Math.sin(a / 2) * Math.sin(a / 2)
                + Math.cos(latitude1) * Math.cos(latitude2)
                * Math.sin(b / 2) * Math.sin(b / 2);

        double c = 2 * Math.atan2(Math.sqrt(a1), Math.sqrt(1 - a1));
        double r = 6371000;

        return r * c;
    }

    public static double calculateFuelComsuptionEachSegment(Aircraft aircraft, Segment segment) {

        return 0;
    }

    public static void setsToAircraftValues(Aircraft aircraft) {

    }

    //__________________________________________Trip related formulas_______________________________________________
    public static double calculateTrueMachNumber(Aircraft aircraft, double altitude, double speedVIAS) {

        double temperature = calculateTemperatureDueAltitude(altitude);
        double pressure = calculatePressureDueAltitude(altitude);
        double airDensity = calculateDensityDueAltitude(altitude, pressure, temperature);

        double a = Math.pow((1 + 0.2 * Math.pow((speedVIAS / 661.5), 2)), 3.5) - 1;
        return Math.sqrt(5 * (Math.pow((1.225 / airDensity) * (a) + 1, 0.286) - 1));
    }

    public static double calculateThrust(Aircraft aircraft, double altitude, double trueMachNumber) {

        double temperature = calculateTemperatureDueAltitude(altitude);
        double pressure = calculatePressureDueAltitude(altitude);
        double AirDensity = calculateDensityDueAltitude(altitude, pressure, temperature);
        double thrustSeaLevelMach0 = aircraft.getModel().getThrust_0(); // em newtons
        double thrustChangeRate = (thrustSeaLevelMach0 - aircraft.getModel().getThrustMaxSpeed()) / aircraft.getModel().getMaxSpeed(); //duvida no Lapse Rate

        return thrustSeaLevelMach0 - thrustChangeRate * trueMachNumber;

    }

    public static double calculateThrustAltitude(Aircraft aircraft, double altitude, double trueMachNumber) {

        double temperature = calculateTemperatureDueAltitude(altitude);
        double pressure = calculatePressureDueAltitude(altitude);
        double AirDensity = calculateDensityDueAltitude(altitude, pressure, temperature);
        double thrust = calculateThrust(aircraft, altitude, trueMachNumber);
        return thrust * Math.pow((AirDensity / 1.225), 0.96); //0.96, valor dado pelo prof

    }

    public static double calculateTotalThrust(Aircraft aircraft, double thrust) {
        return aircraft.getModel().getNumberMotors() * thrust;
    }

    public static double calculateTrueAirSpeed(double trueMachNumber, double speedOfSound) {
        return trueMachNumber * speedOfSound; //valor em metros por segundo
    }

    public static double calculateAircraftClimbRate(Aircraft aircraft, double thrustTotal, double dragForce, double maxWeight, double trueAirSpeed) {

        return ((thrustTotal - dragForce) * trueAirSpeed / maxWeight / 9.81); // dividir por gravidade ou multiplicar?

    }

    public static double calculateFuelBurned(Aircraft aircraft, double dragForce) {

        return -aircraft.getModel().getTSFC() * dragForce;
    }

    public static double calculateAltitudeVariation(double trueAirSpeed, double totalThrust, double dragForce, double maxWeight) {

        return (totalThrust - dragForce) * trueAirSpeed / maxWeight / 9.81;
    }

    public static double calculateClimbingAngle(double trueAirSpeed, double climbRate) {

        return Math.asin(climbRate / trueAirSpeed);
    }

    public static double calculatedWdT(Aircraft aircraft, double time, double totalThrust) {

        return totalThrust * time * aircraft.getModel().getTSFC() / 9.81;
    }

    public static double calculateDistanceTraveledWhileClimbing(double trueAirspeed, double climbAngle, double time) {

        return trueAirspeed * Math.cos(climbAngle) * time;
    }

    public static double calculateDistanceEach60SecAtCruiseAltitude(Aircraft aircraft, double speed) {

        return speed * 120;
    }

    public static double calculateAircraftFinalWeight(Aircraft aircraft) {
        // return (aircraft.getNumberElementsCrew() + aircraft.getNumberFirstClass() + aircraft.getNumberNormalClass()) * 195 + aircraft.getModel().getFuelCapacity() + aircraft.getModel().getEmptyWeight();
        double litersInKg = PhysicsConverters.litersToKgConverter(aircraft.getModel().getFuelCapacity());

        return aircraft.getModel().getEmptyWeight() + aircraft.getModel().getMaxPayload() + litersInKg;
    }

    public static double[] aircraftClimb(Aircraft aircraft, double[] valuesVec, Airport airport, Segment[] segments) {

        double liftForce = 0;
        double dragForce = 0;
        double thrustAltitude = 0;
        double maxWeight = 0;
        double climbRate = 0;
        double altitude = airport.getLocation().getAltitude();
        double time = 1;
        double fuelBurned = 0;
        double totalFuelBurned = 0;
        double distanceTraveled = 0;
        double distance = 0;
        double altitudeVariation = 0;
        double speed = 0;
        double trueMachNumber = 0;
        double trueAirSpeed = 0;
        double speedOfSound = 0;
        double climbAngle = 0;
        double distanceTraveledInSegment = 0;
        double percAux;
        double i = valuesVec[5];
        int a = ((int) i);
        Segment segment = segments[a];
        double segmentDistance = calculateSegmentDistance(aircraft, segment);

        do {

            speed = calculateSpeedDueAltitudeClimbing(aircraft, altitude, speed); //Speed Due Altitude
            trueMachNumber = calculateTrueMachNumber(aircraft, altitude, speed); //MachNumber
            speedOfSound = calculateSpeedOfSoundDueAltitude(altitude);
            trueAirSpeed = calculateTrueAirSpeed(trueMachNumber, speedOfSound);
            liftForce = calculateLiftForceInASegment(aircraft, altitude);
            dragForce = calculateDragForceInASegment(aircraft, altitude);
            thrustAltitude = calculateThrustAltitude(aircraft, altitude, trueMachNumber);
            thrustAltitude = aircraft.getModel().getNumberMotors() * thrustAltitude;
            maxWeight = calculateAircraftFinalWeight(aircraft);
            climbRate = calculateAircraftClimbRate(aircraft, thrustAltitude, dragForce, maxWeight, trueAirSpeed); //climbRate
            climbAngle = calculateClimbingAngle(trueAirSpeed, climbRate);
            distance = calculateDistanceTraveledWhileClimbing(trueAirSpeed, climbAngle, 60);
            fuelBurned = calculatedWdT(aircraft, 60, thrustAltitude);
            altitudeVariation = calculateAltitudeVariation(trueAirSpeed, thrustAltitude, dragForce, maxWeight);
            distanceTraveled = distanceTraveled + distance;
            distanceTraveledInSegment = distanceTraveledInSegment + distance;

            if (distanceTraveledInSegment > segmentDistance) {
                percAux = (distanceTraveled - segmentDistance) / distance;
                maxWeight = maxWeight - fuelBurned * percAux;
                totalFuelBurned = totalFuelBurned + fuelBurned * percAux;
                altitude = altitude + altitudeVariation * 60 * percAux;
                time = time + 60 * percAux;
                if (segments[(int) i + 1] != null && i + 1 <= segments.length) {
                    segment = segments[(int) i + 1];
                    i = i + 1;
                    distanceTraveledInSegment = 0;
                }
            }
            maxWeight = maxWeight - fuelBurned;
            totalFuelBurned = totalFuelBurned + fuelBurned;
            altitude = altitude + altitudeVariation * 60;
            time = time + 60;

        } while (altitudeVariation * 60 >= 50);

        valuesVec[0] = altitude;
        valuesVec[1] = maxWeight;
        valuesVec[2] = distanceTraveled;
        valuesVec[3] = totalFuelBurned;
        valuesVec[4] = time;
        valuesVec[5] = i;
        valuesVec[6] = distanceTraveledInSegment;

        return valuesVec;
    }

    public static double[] aircraftDescent(Aircraft aircraft, double[] valuesVec, Airport airport, Segment[] segments) {

        double altitudeAirport = airport.getLocation().getAltitude();

        double liftForce = 0;
        double dragForce = 0;
        double thrustAltitude = 0;
        double maxWeight = valuesVec[1];
        double descentRate = 0;
        double altitude = valuesVec[0];
        double time = valuesVec[4];
        double fuelBurned = 0;
        double totalFuelBurned = valuesVec[3];
        double distanceTraveled = valuesVec[2];
        double distance = 0;
        double distanceDescending = 0;
        double altitudeVariation = 0;
        double altitudeAux = 0;
        double speed = 0;
        double trueMachNumber = 0;
        double trueAirSpeed = 0;
        double speedOfSound = 0;
        double descentAngle = 0;
        double percAux = 0;
        double distanceTraveledInSegment = valuesVec[6];
        double i = valuesVec[5];
        Segment segment = segments[(int) i];
        double segmentDistance = calculateSegmentDistance(aircraft, segment);

        do {

            speed = calculateSpeedDueAltitudeClimbing(aircraft, altitude, speed); //Speed Due Altitude
            trueMachNumber = calculateTrueMachNumber(aircraft, altitude, speed); //MachNumber
            speedOfSound = calculateSpeedOfSoundDueAltitude(altitude);
            trueAirSpeed = calculateTrueAirSpeed(trueMachNumber, speedOfSound);
            liftForce = calculateLiftForceInASegment(aircraft, altitude);
            dragForce = calculateDragForceInASegment(aircraft, altitude);
            thrustAltitude = calculateThrustAltitude(aircraft, altitude, trueMachNumber);
            thrustAltitude = 0.1 * aircraft.getModel().getNumberMotors() * thrustAltitude; //thrust quando está a descer
            descentRate = calculateAircraftClimbRate(aircraft, thrustAltitude, dragForce, maxWeight, trueAirSpeed); //climbRate
            descentAngle = calculateClimbingAngle(trueAirSpeed, descentRate);
            distance = calculateDistanceTraveledWhileClimbing(trueAirSpeed, descentAngle, 60);

            if (distanceTraveledInSegment > segmentDistance) {
                percAux = (distanceTraveled - segmentDistance) / distance;
                maxWeight = maxWeight - fuelBurned * percAux;
                totalFuelBurned = totalFuelBurned + fuelBurned * percAux;
                altitude = altitude + altitudeVariation * 60 * percAux;
                time = time + 60 * percAux;
                if (segments[(int) i + 1] != null && i + 1 <= segments.length) {
                    segment = segments[(int) i + 1];
                    i = i + 1;
                    distanceTraveledInSegment = 0;
                }
            }

            fuelBurned = calculatedWdT(aircraft, 60, thrustAltitude);
            altitudeVariation = calculateAltitudeVariation(trueAirSpeed, thrustAltitude, dragForce, maxWeight);
            altitudeAux = altitude;
            altitude = altitude + altitudeVariation * 60;

            if (altitude < altitudeAirport) {
                percAux = -((altitudeAux - altitudeAirport) / (altitudeVariation * 60));
                altitude = altitudeAux - (-(altitudeVariation * 60)) * percAux;
                maxWeight = maxWeight - fuelBurned * percAux;
                totalFuelBurned = totalFuelBurned + fuelBurned * percAux;
                distanceTraveled = distanceTraveled + distance * percAux;
                time = time + 60 * percAux;
                break;
            }
            maxWeight = maxWeight - fuelBurned;
            totalFuelBurned = totalFuelBurned + fuelBurned;
            distanceDescending = distanceDescending + distance;
            distanceTraveled = distanceTraveled + distance;
            time = time + 60;

        } while (altitude > altitudeAirport);

        valuesVec[0] = altitude;
        valuesVec[1] = maxWeight;
        valuesVec[2] = distanceTraveled;
        valuesVec[3] = totalFuelBurned;
        valuesVec[4] = time;
        valuesVec[5] = i;
        valuesVec[6] = distanceTraveledInSegment + distanceDescending;
        valuesVec[7] = distanceDescending;

        return valuesVec;

    }


    public static double[] aircraftDistanceToDescent(Aircraft aircraft, double[] valuesVec, double totalDist, double[] valuesVecAux, Segment[] segments) {

        double liftForce = 0;
        double dragForce = 0;
        double thrustAltitude = 0;
        double maxWeight = valuesVec[1];
        double altitude = valuesVec[0];
        double time = valuesVec[4];
        double fuelBurned = 0;
        double totalFuelBurned = valuesVec[3];
        double distanceTraveled = valuesVec[2];
        double distanceTraveledInSegment = valuesVec[6];
        double speed = 0;
        double trueMachNumber = 0;
        double trueAirSpeed = 0;
        double speedOfSound = 0;
        double i = valuesVec[5];
        Segment segment = segments[(int) i];
        double segmentDistance = calculateSegmentDistance(aircraft, segment);
        double percAux;

        maxWeight = calculateAircraftFinalWeight(aircraft);
        speed = calculateSpeedDueAltitudeClimbing(aircraft, altitude, speed); //Speed Due Altitude
        trueMachNumber = calculateTrueMachNumber(aircraft, altitude, speed); //MachNumber
        speedOfSound = calculateSpeedOfSoundDueAltitude(altitude);
        trueAirSpeed = calculateTrueAirSpeed(trueMachNumber, speedOfSound);
        liftForce = calculateLiftForceInASegment(aircraft, altitude);
        dragForce = calculateDragForceInASegment(aircraft, altitude);
        thrustAltitude = calculateThrustAltitude(aircraft, altitude, trueMachNumber);
        thrustAltitude = aircraft.getModel().getNumberMotors() * thrustAltitude;

        double distanceTraveledEach60secs = calculateDistanceEach60SecAtCruiseAltitude(aircraft, trueAirSpeed);

        while (distanceTraveled < totalDist - 120000) {//altitude <= aircraft.getModel().getCruiseAltitude()) {

            fuelBurned = calculatedWdT(aircraft, 60, thrustAltitude);
            distanceTraveledInSegment = distanceTraveledInSegment + distanceTraveledEach60secs;
            if (distanceTraveledInSegment > segmentDistance) {
                percAux = (distanceTraveledInSegment - segmentDistance) / distanceTraveledEach60secs;
                maxWeight = maxWeight - fuelBurned * percAux;
                totalFuelBurned = totalFuelBurned + fuelBurned * percAux;
                distanceTraveled = distanceTraveled + distanceTraveledEach60secs * percAux;
                distanceTraveledInSegment = distanceTraveledInSegment + distanceTraveledEach60secs * percAux;
                if (segments[(int) i + 1] != null && i + 1 <= segments.length) {
                    segment = segments[(int) i + 1];
                    i = i + 1;
                }
                distanceTraveledInSegment = 0;
                segmentDistance = calculateSegmentDistance(aircraft, segment);
            }
            maxWeight = maxWeight - fuelBurned;
            totalFuelBurned = totalFuelBurned + fuelBurned;
            distanceTraveled = distanceTraveled + distanceTraveledEach60secs;
            time = time + 60;
        }

        valuesVecAux[0] = altitude;
        valuesVecAux[1] = maxWeight;
        valuesVecAux[2] = distanceTraveled;
        valuesVecAux[3] = totalFuelBurned;
        valuesVecAux[4] = time;
        valuesVecAux[5] = i;
        valuesVecAux[6] = distanceTraveledInSegment;

        return valuesVecAux;

    }

    public static double[] aircraftCruiseAltitudeCalculations(Aircraft aircraft, double[] valuesVec, double totalDist, double distanceToDescend, Segment[] segments) {

        double liftForce = 0;
        double dragForce = 0;
        double thrustAltitude = 0;
        double maxWeight = valuesVec[1];
        double altitude = valuesVec[0];
        double time = valuesVec[4];
        double fuelBurned = 0;
        double totalFuelBurned = valuesVec[3];
        double distanceTraveled = valuesVec[2];
        double distanceTraveledInSegment = valuesVec[6];
        double totalDistance = totalDist;
        double speed = 0;
        double trueMachNumber = 0;
        double trueAirSpeed = 0;
        double speedOfSound = 0;
        double percAux;
        double i = valuesVec[5];
        Segment segment = segments[(int) i];
        double segmentDistance = calculateSegmentDistance(aircraft, segment);

        maxWeight = calculateAircraftFinalWeight(aircraft);
        speed = calculateSpeedDueAltitudeClimbing(aircraft, altitude, speed); //Speed Due Altitude
        trueMachNumber = calculateTrueMachNumber(aircraft, altitude, speed); //MachNumber
        speedOfSound = calculateSpeedOfSoundDueAltitude(altitude);
        trueAirSpeed = calculateTrueAirSpeed(trueMachNumber, speedOfSound);
        liftForce = calculateLiftForceInASegment(aircraft, altitude);
        dragForce = calculateDragForceInASegment(aircraft, altitude);
        thrustAltitude = calculateThrustAltitude(aircraft, altitude, trueMachNumber);
        thrustAltitude = aircraft.getModel().getNumberMotors() * thrustAltitude;

        double distanceTraveledEach60secs = calculateDistanceEach60SecAtCruiseAltitude(aircraft, trueAirSpeed);

        while (distanceTraveled < (totalDistance - distanceToDescend)) {//altitude <= aircraft.getModel().getCruiseAltitude()) {

            fuelBurned = calculatedWdT(aircraft, 60, thrustAltitude);
            distanceTraveledInSegment = distanceTraveledInSegment + distanceTraveledEach60secs;
            if (distanceTraveledInSegment > segmentDistance) {
                percAux = (distanceTraveledInSegment - segmentDistance) / distanceTraveledEach60secs;
                maxWeight = maxWeight - fuelBurned * percAux;
                totalFuelBurned = totalFuelBurned + fuelBurned * percAux;
                distanceTraveled = distanceTraveled + distanceTraveledEach60secs * percAux;
                distanceTraveledInSegment = distanceTraveledInSegment + distanceTraveledEach60secs * percAux;
                if (segments[(int) i + 1] != null && i + 1 <= segments.length) {
                    segment = segments[(int) i + 1];
                    i = i + 1;
                }
                distanceTraveledInSegment = 0;
                segmentDistance = calculateSegmentDistance(aircraft, segment);
            }
            maxWeight = maxWeight - fuelBurned;
            totalFuelBurned = totalFuelBurned + fuelBurned;
            distanceTraveled = distanceTraveled + distanceTraveledEach60secs;
            time = time + 60;
        }

        valuesVec[0] = altitude;
        valuesVec[1] = maxWeight;
        valuesVec[2] = distanceTraveled;
        valuesVec[3] = totalFuelBurned;
        valuesVec[4] = time;
        valuesVec[5] = i;
        valuesVec[6] = distanceTraveledInSegment;

        return valuesVec;

    }

    public static double[] allFlightCalculations(Aircraft aircraft, Airport initialAirport, Airport endAirport, double dist, Segment[] segments) {

        double[] valuesVec = new double[8];
        double[] valuesVecAux = new double[8];
        valuesVec[5] = 0;
        double totalDistance = 0;

        for (int i = 0; i <= segments.length; i++) {
            totalDistance = totalDistance + calculateSegmentDistance(aircraft, segments[0]);
        }
        //distance=dist;
        valuesVec = aircraftClimb(aircraft, valuesVec, initialAirport, segments);
        valuesVecAux = aircraftDistanceToDescent(aircraft, valuesVec, totalDistance, valuesVecAux, segments); //este método é para saber os valores do vec até 120km antes do aeropor(valor dos slides para quando aviao costuma começar a descer)
        valuesVecAux = aircraftDescent(aircraft, valuesVecAux, endAirport, segments);
        valuesVec = aircraftCruiseAltitudeCalculations(aircraft, valuesVec, totalDistance, valuesVecAux[7], segments);
        valuesVec = aircraftDescent(aircraft, valuesVec, endAirport, segments);

        // falta mudar a altitude da velocidade (tem que se ter em consideração a altitude do aeroporto onde começa, 0 a partir dessa altura, que é diferente da altitude para os outros calculos)
        return valuesVec;
    }

}
