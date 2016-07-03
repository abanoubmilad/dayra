package abanoubm.dayra.model;

import android.graphics.Bitmap;

public class ContactSort extends ContactID {
    private String priest, classYear, studyWork, street, site,home;

    public ContactSort(String id, String name, Bitmap photo,
                       String priest, String classYear, String studyWork, String street, String site,String home) {
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

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }
}
