package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Domain;

public class Node {
    private String id;
    private double lat;
    private double lon;

    public Node() {}
    public Node(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
    public Node(String id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public String getID() {
        return this.id;
    }

    public double getLat() {
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}

