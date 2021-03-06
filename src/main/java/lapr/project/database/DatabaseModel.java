/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lapr.project.model.*;
import lapr.project.model.network.*;
import lapr.project.model.register.CDragRegister;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author zero_
 */
public class DatabaseModel {

    public static final String DBURL = "jdbc:oracle:thin://@gandalf.dei.isep.ipp.pt:1521/pdborcl";
    public static final String DBUSER = "LAPR3_33";
    public static final String DBPASS = "20ftw";
    Connection con;
    CallableStatement cs;
    Statement st;
    Project project;

    public DatabaseModel() {
        openDB();
    }

    public DatabaseModel(Project p) {
        this.project = p;
        openDB();
    }

    /**
     * Método utilizado para ligar a base de dados.
     *
     */
    public void openDB() {
        // LER O DRIVER Oracle JDBC E CONECTAR À BASE DE DADOS ORACLE
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            this.con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
            this.st = con.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeDB() {
        try {
            this.con.close();
            this.cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc=" LISTS ">
    /**
     * Método utilizado para receber todos os projectos da base de dados.(DAL)
     *
     * @return
     */
    public List<Project> getProjects() {
        List<Project> list_projects = new ArrayList<>();

        try {

            this.cs = this.con.prepareCall("{ ? = call GETALLPROJECTS() }");
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet cs1 = (ResultSet) cs.getObject(1);

            while (cs1.next()) {
                String id = cs1.getString("NAME");
                String des = cs1.getString("DESCRIPTION");

                Project p = new Project(id, des);
                list_projects.add(p);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        closeDB();
        return list_projects;
    }

    /**
     * returns the list of segments (DAL)
     *
     * @return
     */
    public List<Segment> getSegments() {
        List<Segment> lst_seg = new ArrayList<>();

        try {
            this.cs = this.con.prepareCall("{ ? = call getProjectSegments(?) }");
            cs.setInt(2, getProjectId());
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet cs1 = (ResultSet) cs.getObject(1);

            while (cs1.next()) {
                Node n1 = getNode(cs1.getInt("Node_Start"));
                Node n2 = getNode(cs1.getInt("Node_End"));
                Segment s = new Segment(cs1.getString("name"),
                        n1,
                        n2,
                        null,
                        cs1.getString("direction"),
                        cs1.getDouble("wind_direction"),
                        cs1.getDouble("wind_intensity"));
                lst_seg.add(s);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        closeDB();
        return lst_seg;
    }

    /**
     * return the list of aircraft models (DAL)
     *
     * @return
     */
    public List<AircraftModel> getListAircraftModels() {
        List<AircraftModel> lst_a = new ArrayList<>();

        try {
            cs = con.prepareCall("{ ? = call getProjectAircraftModels(?) }");
            cs.setInt(2, getProjectId());
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet cs1 = (ResultSet) cs.getObject(1);

            while (cs1.next()) {
                AircraftModel am = new AircraftModel(cs1.getString("name"),
                        cs1.getString("description"), cs1.getString("maker"),
                        AircraftModel.Type.valueOf(cs1.getString("type")),
                        cs1.getDouble("number_motors"), cs1.getString("motor"),
                        AircraftModel.MotorType.valueOf(cs1.getString("motor_type")),
                        cs1.getDouble("cruise_altitude"),
                        cs1.getDouble("cruise_speed"), cs1.getDouble("TSFC"),
                        cs1.getDouble("lapse_Rate_Factor"),
                        cs1.getDouble("thrust_0"),
                        cs1.getDouble("thrust_Max_Speed"),
                        cs1.getDouble("max_Speed"), cs1.getDouble("eWeight"),
                        cs1.getDouble("MTOW"), cs1.getDouble("max_payload"),
                        cs1.getDouble("fuel_capacity"), cs1.getDouble("VMO"),
                        cs1.getDouble("MMO"), cs1.getDouble("wing_area"),
                        cs1.getDouble("wing_span"), cs1.getDouble("aspect_ratio"),
                        cs1.getDouble("e"),
                        getListCDrag(cs1.getString("name")));
                lst_a.add(am);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        closeDB();
        return lst_a;
    }

    /**
     * return the list of airports by project (DAL)
     *
     * @param p
     * @return
     */
    public List<Airport> getListAirports() {
        List<Airport> lst_airports = new ArrayList<>();

        try {

            this.cs = this.con.prepareCall("{ ? = call GETPROJECTAIRPORTS(?) }");
            cs.setInt(2, getProjectId());
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet cs1 = (ResultSet) cs.getObject(1);

            while (cs1.next()) {
                Location l = new Location(cs1.getDouble("latitude"), cs1.getDouble("longitude"), cs1.getDouble("altitude"));
                Airport a = new Airport(cs1.getString("name"),
                        cs1.getString("town"),
                        cs1.getString("country"),
                        cs1.getString("cod_IATA"), l);
                lst_airports.add(a);

            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        closeDB();
        return lst_airports;
    }

    /**
     * return the list of nodes by project(DAL)
     *
     * @param p
     * @return
     */
    public List<Node> getListNodes() {
        List<Node> lst_nodes = new ArrayList<>();

        try {
            this.cs = this.con.prepareCall("{ ? = call GETPROJECTNODES(?) }");

            this.cs.setInt(2, getProjectId());
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet cs1 = (ResultSet) cs.getObject(1);

            while (cs1.next()) {
                Node n = new Node(cs1.getString("name"),
                        cs1.getDouble("latitude"),
                        cs1.getDouble("longitude"));
                lst_nodes.add(n);
            }

        } catch (Exception ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        closeDB();
        return lst_nodes;
    }

    /**
     * return the list of nodes by project(DAL)
     *
     * @param p
     * @return
     */
    public List<Aircraft> getListAircrafts() {
        List<Aircraft> lst_aircraft = new ArrayList<>();

        try {
            this.cs = this.con.prepareCall("{ ? = call getProjectAircrafts(?) }");

            this.cs.setInt(2, getProjectId());
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet cs1 = (ResultSet) cs.getObject(1);

//            while (cs1.next()) {
//                Aircraft a = new Aircraft(cs1.getString("name"),
//                        cs1.getString("description"),
//                        cs1.getDouble("firstclass"),
//                        cs1.getDouble("normalclass"), 
//                        cs1.getDouble("crewelements"), 
//                        getAircraftModel(cs1.getInt("aircraftmodel_id")));
//                lst_aircraft.add(a);
//            }
        } catch (Exception ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        closeDB();
        return lst_aircraft;
    }

    public List<FlightPlan> getListFlightPlans() {
        List<FlightPlan> lst_fp = new ArrayList<>();

        try {
            this.cs = this.con.prepareCall("{ ? = call getProjectFlightPlans(?) }");

            this.cs.setInt(2, getProjectId());
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet cs1 = (ResultSet) cs.getObject(1);

            while (cs1.next()) {
                FlightPlan fp = new FlightPlan(cs1.getString("name"), AircraftModel.Type.valueOf(cs1.getString("type")),
                        getAirport(cs1.getInt("Airport_start")), getAirport(cs1.getInt("Airport_end")),
                        cs1.getDouble("normalclass"), cs1.getDouble("firstclass"), cs1.getDouble("crewelement"));
                lst_fp.add(fp);
            }

        } catch (Exception ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        closeDB();
        return lst_fp;
    }

    public List<Flight> getListFlights() {
        List<Flight> lst_flights = new ArrayList<>();

        try {
            this.cs = this.con.prepareCall("{ ? = call getProjectFlights(?) }");
            this.cs.setInt(2, getProjectId());
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet cs1 = (ResultSet) cs.getObject(1);

//            while (cs1.next()) {
//                Flight f = new Flight(getFlightPlan(cs1.getInt("FLIGHTPLAN_ID")),
//                        getAircraft(cs1.getInt("AIRCRAFT_ID")), pathTaken,
//                        cs1.getDouble("TRAVALINGTIME"),
//                        cs1.getDouble("ENERGYCONSUNPTION"));
//                lst_flights.add(f);
//            }
        } catch (Exception ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        closeDB();
        return lst_flights;
    }

    public CDragRegister getListCDrag(String aircraftModelName) {
        CDragRegister lst_cdrag = new CDragRegister();

        try {
            this.cs = this.con.prepareCall("{ ? = call getProjectCDrags(?) }");

            this.cs.setInt(2, getAircraftModelId(aircraftModelName));
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet cs1 = (ResultSet) cs.getObject(1);

            while (cs1.next()) {
                String p=cs1.getString("cdrag_0");
                p = p.replaceAll(",",".");
                
                Double d=Double.valueOf(p); 
                CDrag cd = new CDrag(cs1.getDouble("speed"),
                       d);
                lst_cdrag.addCDrag(cd);
            }

        } catch (Exception ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }

//        closeDB();
        return lst_cdrag;
    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" INSERTS">
    /**
     * add the project and the lists to Database.(DAL)
     *
     * @param project
     */
    public void addProject(Project project) {

        int flag = 0;
        try {
            cs = con.prepareCall("{call insertProject(?,?)}");
            cs.setString(1, project.getName());
            cs.setString(2, project.getDescription());
            cs.executeUpdate();
            this.project = project;

            if (!project.getAirNetwork().getMapNodes().isEmpty()) {
                project.getAirNetwork().getMapNodes().values().stream().forEach((n) -> {
                    addNode(n);
                });
                flag = 1;
            }

            if (!project.getAirNetwork().getMapSegment().isEmpty()) {
                project.getAirNetwork().getMapSegment().values().stream().forEach((s) -> {
                    addSegment(s);
                });
                flag = 1;
            }

            if (!project.getAirportRegister().getAirportRegister().isEmpty()) {
                project.getAirportRegister().getAirportRegister().values().stream().forEach((airport) -> {
                    addAirport(airport);
                });
                flag = 1;
            }

            if (!project.getAircraftModelRegister().getAircraftModelMap().isEmpty()) {
                project.getAircraftModelRegister().getAircraftModelMap().values().stream().forEach((airModel) -> {
                    addAircraftModel(airModel);
                    airModel.getCdragRegister().getCDragList().stream().forEach((cdrag) -> {
                        addCDrag(cdrag, airModel);
                    });
                });
                flag = 1;
            }
            if (!project.getFlightPlanRegister().getFlightPlansList().isEmpty()) {
                project.getFlightRegister().getFlightsList().values().stream().forEach((f) -> {
                    addFlight(f);
                });
                flag = 1;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (flag == 1) {
            closeDB();
        }
    }

    /**
     * add segment to DB
     *
     * @param segment
     */
    public void addSegment(Segment segment) {
        try {

            cs = con.prepareCall("{call insertSegment(?,?,?,?,?,?,?)");
            cs.setString(1, segment.getId());
            cs.setString(2, segment.getDirection());
            cs.setDouble(3, segment.getWind_speed());
            cs.setDouble(4, segment.getWind_direction());
            cs.setInt(5, getNodeId(segment.getBeginningNode().getName()));
            cs.setInt(6, getNodeId(segment.getEndNode().getName()));
            cs.setInt(7, getProjectId());

            cs.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método utilizado para guardar dados de um modelo de um avião na base de
     * dados(DAL)
     *
     * @param air
     */
    public void addAircraftModel(AircraftModel air) {
        try {
            cs = con.prepareCall("{call insertAircraftModel(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            cs.setString(1, air.getId());
            cs.setString(2, air.getDescription());
            cs.setString(3, air.getMaker());
            cs.setString(4, air.getType().toString());
            cs.setDouble(5, air.getNumberMotors());
            cs.setString(6, air.getMotor());
            cs.setString(7, air.getMotorType().toString());
            cs.setDouble(8, air.getCruiseAltitude());
            cs.setDouble(9, air.getCruiseSpeed());
            cs.setDouble(10, air.getTSFC());
            cs.setDouble(11, air.getLapseRateFactor());
            cs.setDouble(12, air.getThrust_0());
            cs.setDouble(13, air.getThrustMaxSpeed());
            cs.setDouble(14, air.getMaxSpeed());
            cs.setDouble(15, air.getEmptyWeight());
            cs.setDouble(16, air.getMTOW());
            cs.setDouble(17, air.getMaxPayload());
            cs.setDouble(18, air.getFuelCapacity());
            cs.setDouble(19, air.getVMO());
            cs.setDouble(20, air.getMMO());
            cs.setDouble(21, air.getWingArea());
            cs.setDouble(22, air.getWingSpan());
            cs.setDouble(23, air.getAspectRatio());
            cs.setDouble(24, air.getE());
            cs.setInt(25, getProjectId());
            cs.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método utilizado para guardar dados de um avião na base de dados.
     *
     * @param air
     */
    public void addAircraft(Aircraft air) {
        try {
            cs = con.prepareCall("{call insertAircraft(?,?,?,?,?,?,?)}");
            cs.setString(1, air.getId());
            cs.setString(2, air.getDescription());
            cs.setDouble(3, air.getNumberFirstClass());
            cs.setDouble(4, air.getNumberNormalClass());
            cs.setDouble(5, air.getNumberElementsCrew());
            cs.setDouble(6, air.getCargo());
            cs.setDouble(7, air.getFuel());
            cs.setInt(8, getAircraftModelId(air.getId()));
            cs.setInt(9, getProjectId());
            cs.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        //closeDB();
    }

    /**
     * Método utilizado para guardar dados de um aeroporto na base de dados.
     *
     * @param air
     */
    public void addAirport(Airport air) {
        try {
            this.cs = con.prepareCall("{call insertAirport(?,?,?,?,?,?,?,?)}");
            this.cs.setString(1, air.getIATAcode());
            this.cs.setString(2, air.getName());
            this.cs.setString(3, air.getTown());
            this.cs.setString(4, air.getCountry());
            this.cs.setDouble(5, air.getLocation().getLatitude());
            this.cs.setDouble(6, air.getLocation().getLongitude());
            this.cs.setDouble(7, air.getLocation().getAltitude());
            this.cs.setInt(8, getProjectId());
            this.cs.executeUpdate();

        } catch (Exception ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * metodo que adiciona flight plans à base de dados.(DAL)
     *
     * @param fp
     */
    public void addFlightPlan(FlightPlan fp) {
        try {
            cs = con.prepareCall("{ call insertFlightPlan(?,?,?,?,?,?,?,?) }");
            cs.setString(1, fp.getName());
            cs.setString(2, fp.getAircraftType().toString());
            cs.setDouble(3, fp.getnNormalClass());
            cs.setDouble(4, fp.getnFirstClass());
            cs.setDouble(5, fp.getnCrew());
            cs.setInt(6, getAirportId(fp.getOrigin().getIATAcode()));
            cs.setInt(7, getAirportId(fp.getDest().getIATAcode()));
            cs.setInt(8, getProjectId());

            cs.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * add Node to Database (DAL)
     *
     * @param n
     */
    public void addNode(Node n) {
        try {
            CallableStatement cs = con.prepareCall("{call insertNode(?,?,?,?)}");
            cs.setString(1, n.getName());
            cs.setDouble(2, n.getLatitude());
            cs.setDouble(3, n.getLongitude());
            cs.setInt(4, getProjectId());
            cs.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo utilizado para guardar dados de um flight na base de dados.
     *
     * @param f
     */
    public void addFlight(Flight f) {
        if (f != null) {
            try {
                cs = con.prepareCall("{call insertFlight(?,?,?,?,?,?) }");
                cs.setString(1, f.getId());
                cs.setDouble(2, f.getTravelingTime());
                cs.setDouble(3, f.getEnergyConsumption());
                cs.setInt(4, getFlightPlanId(f.getFlightPlan().getName())); //flightplan ID
                cs.setInt(5, getAircraftId(f.getAircraft().getId())); //aircraft id
                cs.setInt(6, getProjectId());
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void addFlightSegment(Flight f,Segment s, int index) {
        if (f != null) {
            try {
                cs = con.prepareCall("{call insertFlight_Segment(?,?,?) }");
                cs.setInt(1, getFlightId(f.getId()));
                cs.setInt(2, getSegmentId(s.getId()));
                cs.setInt(3, index);
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addCDrag(CDrag cd, AircraftModel air) {
        if (cd != null) {
            try {
                CallableStatement cs1;
                cs1 = con.prepareCall("{call insertCDrag(?,?,?) }");
                cs1.setDouble(1, cd.getcDrag0());
                cs1.setDouble(2, cd.getSpeed());
                cs1.setInt(3, getAircraftModelId(air.getId()));
                cs1.execute();
                
                cs1.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

// </editor-fold>
// <editor-fold defaultstate="collapsed" desc=" EDIT ">
//DONE
    public void editProject(String name, String description) {
        try {
            openDB();
            cs = con.prepareCall("{ call updateProject(?,?,?) }");
            cs.setInt(1, getProjectId());
            cs.setString(2, name);
            cs.setString(3, description);

            cs.execute();
            closeDB();

        } catch (Exception ex) {
            Logger.getLogger(DatabaseModel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" GETID ">
    /**
     * search the id that only DB knows
     *
     * @return
     */
    public int getProjectId() {
        int id = 0;
        try {
            CallableStatement cs1;
            cs1 = con.prepareCall("{ ? = call getProjectIndex(?) }");
            cs1.setString(2, this.project.getName());
            cs1.registerOutParameter(1, java.sql.Types.INTEGER);
            cs1.execute();

            id = cs1.getInt(1);
            cs1.close();

        } catch (Exception ex) {
            Logger.getLogger(DatabaseModel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return id;
    }

    /**
     * search the id that only DB knows
     *
     * @param name
     * @return id
     */
    public int getNodeId(String name) {
        int id = 0;
        try {
            CallableStatement cs1;
            cs1 = con.prepareCall("{ ? = call getNodeIndex(?,?) }");
            cs1.setString(2, name);
            cs1.setInt(3, getProjectId());
            cs1.registerOutParameter(1, java.sql.Types.INTEGER);
            cs1.execute();

            id = cs1.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /**
     * search the id that only DB knows
     *
     * @param name
     * @return id
     */
    private int getAirportId(String iataCode) {
        int id = 0;
        try {
            CallableStatement cs1;
            cs1 = con.prepareCall("{ ? = call getAirportIndex(?,?) }");
            cs1.setString(2, iataCode);
            cs1.setInt(3, getProjectId());
            cs1.registerOutParameter(1, java.sql.Types.INTEGER);
            cs1.execute();

            id = cs1.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /**
     * search the id that only DB knows
     *
     * @param name
     * @return id
     */
    private int getSegmentId(String name) {
        int id = 0;
        try {
            CallableStatement cs1;
            cs1 = con.prepareCall("{ ? = call getSegmentIndex(?,?) }");
            cs1.setString(2, name);
            cs1.setInt(3, getProjectId());
            cs1.registerOutParameter(1, java.sql.Types.INTEGER);
            cs1.execute();

            id = cs1.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /**
     * search the id that only DB knows
     *
     * @param name
     * @return id
     */
    private int getAircraftModelId(String name) {
        int id = 0;
        try {
            CallableStatement cs1;
            cs1 = con.prepareCall("{ ? = call getAircraftModelIndex(?,?) }");
            cs1.setString(2, name);
            cs1.setInt(3, getProjectId());
            cs1.registerOutParameter(1, java.sql.Types.INTEGER);
            cs1.execute();

            id = cs1.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /**
     * search the id that only DB knows
     *
     * @param name
     * @return id
     */
    private int getAircraftId(String name) {
        int id = 0;
        try {
            CallableStatement cs1;
            cs1 = con.prepareCall("{ ? = call getAircraftIndex(?,?) }");
            cs1.setString(2, name);
            cs1.setInt(3, getProjectId());
            cs1.registerOutParameter(1, java.sql.Types.INTEGER);
            cs1.execute();

            id = cs1.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /**
     * search the id that only DB knows
     *
     * @param name
     * @return id
     */
    private int getFlightId(String name) {
        int id = 0;
        try {
            CallableStatement cs1;
            cs1 = con.prepareCall("{ ? = call getFlightIndex(?,?) }");
            cs1.setString(2, name);
            cs1.setInt(3, getProjectId());
            cs1.registerOutParameter(1, java.sql.Types.INTEGER);
            cs1.execute();

            id = cs1.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /**
     * search the id that only DB knows
     *
     * @param name
     * @return id
     */
    private int getFlightPlanId(String name) {
        int id = 0;
        try {
            CallableStatement cs1;
            cs1 = con.prepareCall("{ ? = call getFlightPlanIndex(?,?) }");
            cs1.setString(2, name);
            cs1.setInt(3, getProjectId());
            cs1.registerOutParameter(1, java.sql.Types.INTEGER);
            cs1.execute();

            id = cs1.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" GETINFO ">
    /**
     * return Node by id
     *
     * @param id_node
     * @return
     */
    private Node getNode(int id_node) {
        Node n = new Node();

        try {
            cs = con.prepareCall("{ ? = call getNodeById(?) }");
            cs.setInt(2, id_node);
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet cs1 = (ResultSet) cs.getObject(1);

            while (cs1.next()) {
                n = this.project.getAirNetwork().getNode(cs1.getString("name"));

            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    private Airport getAirport(int id_airport) {
        Airport n = null;

        try {
            cs = con.prepareCall("{ ? = call getAirportById(?) }");
            cs.setInt(2, id_airport);
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet cs1 = (ResultSet) cs.getObject(1);

            while (cs1.next()) {
                n = this.project.getAirportRegister().getAirportByIATACode("cod_iata");

            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    private Segment getSegment(int id_segment) {
        Segment s = null;

        try {
            cs = con.prepareCall("{ ? = call getSegmentById(?) }");
            cs.setInt(2, id_segment);
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet cs1 = (ResultSet) cs.getObject(1);

            while (cs1.next()) {
                s = this.project.getAirNetwork().getSegment("name");

            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    private Aircraft getAircraft(int id_aircraft) {
        Aircraft a = null;

        try {
            cs = con.prepareCall("{ ? = call getAircraftById(?) }");
            cs.setInt(2, id_aircraft);
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet cs1 = (ResultSet) cs.getObject(1);

            while (cs1.next()) {
                a = this.project.getAircraftRegister().getAircraftByID(
                        cs1.getString("name"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return a;
    }

    private AircraftModel getAircraftModel(int id_aircraftmodel) {
        AircraftModel am = null;

        try {
            cs = con.prepareCall("{ ? = call getAircraftModelById(?) }");
            cs.setInt(2, id_aircraftmodel);
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet cs1 = (ResultSet) cs.getObject(1);

            while (cs1.next()) {
                am = this.project.getAircraftModelRegister().getAircraftModel(
                        cs1.getString("name"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return am;
    }

    private Flight getFlight(int id_flight) {
        Flight f = null;

        try {
            cs = con.prepareCall("{ ? = call getFlightById(?) }");
            cs.setInt(2, id_flight);
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet cs1 = (ResultSet) cs.getObject(1);

            while (cs1.next()) {
                f = this.project.getFlightRegister().getFlightByID(
                        cs1.getString("name"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return f;
    }

    private FlightPlan getFlightPlan(int id_flightplan) {
        FlightPlan fp = null;

        try {
            cs = con.prepareCall("{ ? = call getFlightPlanById(?) }");
            cs.setInt(2, id_flightplan);
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet cs1 = (ResultSet) cs.getObject(1);

            while (cs1.next()) {
                fp = this.project.getFlightPlanRegister().getFlightPlansList().get(
                        cs1.getString("name"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return fp;
    }

    // </editor-fold>
    public boolean validateName(String name) {
        for (Project p : getProjects()) {
            if (p.getName().equals(name)) {
                System.out.println("Nome de projecto já existe na base de dados.");
                return false;
            }
        }
        return true;
    }
}
