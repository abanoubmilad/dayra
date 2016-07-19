package abanoubm.dayra.model;

public class ContactLocationList {
    private String id;
    private double mapLat, mapLng;
    private String name;

    public ContactLocationList(String id, String name, double mapLat, double mapLng) {
        this.mapLat = mapLat;
        this.mapLng = mapLng;
        this.name = name;
        this.id = id;
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

    public String getId() {
        return id;
    }
}