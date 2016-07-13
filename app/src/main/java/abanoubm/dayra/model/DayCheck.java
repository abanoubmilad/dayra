package abanoubm.dayra.model;

public class DayCheck {
    private String day;
    private boolean checked = false;

    public DayCheck(String day, boolean checked) {
        this.day = day;
        this.checked = checked;
    }

    public DayCheck(String dayNumber) {
        this.day = dayNumber;
    }

    public String getDay() {
        return day;
    }

    public boolean isChecked() {
        return checked;
    }

}
