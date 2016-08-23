package abanoubm.dayra.model;

import android.graphics.Bitmap;

public class ContactData extends ContactID {
    private double mapLat, mapLng;
    private float mapZoom;
    private String priest, comm, birthDay, email,
            mobile1, mobile2, mobile3, landPhone, address,
            classYear, studyWork, street, site,home;

    public ContactData(String id, String name, byte [] photo, double mapLat,
                       double mapLng, float mapZoom, String priest,
                       String comm, String birthDay,
                       String email, String mobile1,
                       String mobile2, String mobile3,
                       String landPhone, String address,
                       String classYear, String studyWork,
                       String street, String site,String home) {
        super(id, name, photo);
        this.mapLat = mapLat;
        this.mapLng = mapLng;
        this.mapZoom = mapZoom;
        this.priest = priest;
        this.comm = comm;
        this.birthDay = birthDay;
        this.email = email;
        this.mobile1 = mobile1;
        this.mobile2 = mobile2;
        this.mobile3 = mobile3;
        this.landPhone = landPhone;
        this.address = address;
        this.classYear = classYear;
        this.studyWork = studyWork;
        this.street = street;
        this.site = site;
        this.home = home;
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

    public String getMobile1() {
        return mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public String getMobile3() {
        return mobile3;
    }

    public String getLandPhone() {
        return landPhone;
    }


    public String getAddress() {
        return address;
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

    public String getSite() {
        return site;
    }


    public String getHome() {
        return home;
    }

}
