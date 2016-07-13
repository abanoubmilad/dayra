package abanoubm.dayra.model;

public class ContactLocationList {
    private double mapLat, mapLng;
    private String name;

    public ContactLocationList(String name, double mapLat, double mapLng) {
        this.mapLat = mapLat;
        this.mapLng = mapLng;
        this.name = name;
    }

    public double getMapLat() {
        return mapLat;
    }


    public double getMapLng() {
        return mapLng;
    }


    public String getName() {
        return name;
    }

}