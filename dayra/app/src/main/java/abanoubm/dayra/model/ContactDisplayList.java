package abanoubm.dayra.model;

import android.graphics.Bitmap;

public class ContactDisplayList extends ContactID {
    private String priest, classYear, studyWork, street, site,home;

    public ContactDisplayList(String id, String name, byte [] photo,
                              String priest, String classYear, String studyWork, String street, String site, String home) {
        super(id, name, photo);
        this.priest = priest;
        this.classYear = classYear;
        this.studyWork = studyWork;
        this.street = street;
        this.site = site;
        this.home=home;
    }


    public String getPriest() {
        return priest;
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
