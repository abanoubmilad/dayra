package abanoubm.dayra.model;

public class ContactLoc {
    private double mapLat, mapLng;
    private String name;

    public ContactLoc(String name, double mapLat, double mapLng) {
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

    public void setName(String name) {
        this.name = name;
    }

}