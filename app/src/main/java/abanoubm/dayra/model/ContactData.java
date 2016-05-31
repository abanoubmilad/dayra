package abanoubm.dayra.model;

public class ContactData extends ContactID {
    private double mapLat, mapLng;
    private float mapZoom;
    private String attendDates, lastAttend, priest, comm, birthDay, email,
            mobile1, mobile2, mobile3, landPhone, address, lastVisit,
            classYear, studyWork, street, site;

    public ContactData(String id, String name, String picDir, double mapLat,
                       double mapLng, float mapZoom, String attendDates,
                       String lastAttend, String priest, String comm, String birthDay,
                       String email, String mobile1, String mobile2, String mobile3,
                       String landPhone, String address, String lastVisit,
                       String classYear, String studyWork, String street, String site) {
        super(id, name, picDir);
        this.mapLat = mapLat;
        this.mapLng = mapLng;
        this.mapZoom = mapZoom;
        this.attendDates = attendDates;
        this.lastAttend = lastAttend;
        this.priest = priest;
        this.comm = comm;
        this.birthDay = birthDay;
        this.email = email;
        this.mobile1 = mobile1;
        this.mobile2 = mobile2;
        this.mobile3 = mobile3;
        this.landPhone = landPhone;
        this.address = address;
        this.lastVisit = lastVisit;
        this.classYear = classYear;
        this.studyWork = studyWork;
        this.street = street;
        this.site = site;
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

    public float getMapZoom() {
        return mapZoom;
    }

    public void setMapZoom(float mapZoom) {
        this.mapZoom = mapZoom;
    }

    public String getAttendDates() {
        return attendDates;
    }

    public void setAttendDates(String attendDates) {
        this.attendDates = attendDates;
    }

    public String getLastAttend() {
        return lastAttend;
    }

    public void setLastAttend(String lastAttend) {
        this.lastAttend = lastAttend;
    }

    public String getPriest() {
        return priest;
    }

    public void setPriest(String priest) {
        this.priest = priest;
    }

    public String getComm() {
        return comm;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getMobile3() {
        return mobile3;
    }

    public void setMobile3(String mobile3) {
        this.mobile3 = mobile3;
    }

    public String getLandPhone() {
        return landPhone;
    }

    public void setLandPhone(String landPhone) {
        this.landPhone = landPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(String lastVisit) {
        this.lastVisit = lastVisit;
    }

    public String getClassYear() {
        return classYear;
    }

    public void setClassYear(String classYear) {
        this.classYear = classYear;
    }

    public String getStudyWork() {
        return studyWork;
    }

    public void setStudyWork(String studyWork) {
        this.studyWork = studyWork;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

}
