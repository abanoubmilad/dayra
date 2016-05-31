package abanoubm.dayra.model;

public class ContactDay extends ContactID {
    private String day;

    public ContactDay(String id, String name, String day, String picDir) {
        super(id, name, picDir);
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

}
