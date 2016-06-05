package abanoubm.dayra.model;

public class ContactLocation {
    private double mapLat, mapLng;
    private float zoom;

    public ContactLocation(double mapLat, double mapLng, float zoom) {
        this.mapLat = mapLat;
        this.zoom = zoom;
        this.mapLng = mapLng;
    }

    public double getMapLat() {
        return mapLat;
    }

    public void setMapLat(double mapLat) {
        this.mapLat = mapLat;
    }

    public double getMapLng() {
        return mapLng;
    }

    public void setMapLng(double mapLng) {
        this.mapLng = mapLng;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }
}