package abanoubm.dayra.model;

public class ContactSort extends ContactID {
    private String priest, classYear, studyWork, street, site;
    private Date lastVisit, lastAttend, birthDay;

    public ContactSort(String id, String name, String picDir, String lastAttend,
                       String priest, String birthDay, String lastVisit,
                       String classYear, String studyWork, String street, String site) {
        super(id, name, picDir);
        this.lastAttend = new Date(lastAttend);
        this.priest = priest;
        this.birthDay = new Date(birthDay);
        this.lastVisit = new Date(lastVisit);
        this.classYear = classYear;
        this.studyWork = studyWork;
        this.street = street;
        this.site = site;
    }

    public String getLastAttend() {
        return lastAttend.getDate();
    }

    public Date getLastAttendObj() {
        return lastAttend;
    }

    public void setLastAttend(String lastAttend) {
        this.lastAttend = new Date(lastAttend);
    }

    public String getPriest() {
        return priest;
    }

    public void setPriest(String priest) {
        this.priest = priest;
    }

    public String getBirthDay() {
        return birthDay.getDate();
    }

    public Date getBirthDayObj() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = new Date(birthDay);
    }

    public String getLastVisit() {
        return lastVisit.getDate();
    }

    public Date getLastVisitObj() {
        return lastVisit;
    }

    public void setLastVisit(String lastVisit) {
        this.lastVisit = new Date(lastVisit);
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
