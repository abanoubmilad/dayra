package abanoubm.dayra.model;

public class ContactSort extends ContactID {
    private String priest, classYear, studyWork, street, site;

    public ContactSort(String id, String name, String picDir,
                       String priest, String classYear, String studyWork, String street, String site) {
        super(id, name, picDir);
        this.priest = priest;
        this.classYear = classYear;
        this.studyWork = studyWork;
        this.street = street;
        this.site = site;
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

}
