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


    public double getMapLng() {
        return mapLng;
    }


    public float getMapZoom() {
        return mapZoom;
    }


    public String getAttendDates() {
        return attendDates;
    }


    public String getLastAttend() {
        return lastAttend;
    }


    public String getPriest() {
        return priest;
    }


    public String getComm() {
        return comm;
    }


    public String getBirthDay() {
        return birthDay;
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


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLastVisit() {
        return lastVisit;
    }


    public String getClassYear() {
        return classYear;
    }


    public String getStudyWork() {
        return studyWork;
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
