package abanoubm.dayra.obj;

public class ContactDay extends ContactID {
    private String day;

    public ContactDay(int id, String name, String day, String picDir) {
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
