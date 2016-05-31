package abanoubm.dayra.model;

public class ContactStatistics extends ContactID {
    private String minDay,maxDay;
    private int daysCount;

    public ContactStatistics(String id, String name, String picDir, String minDay, String maxDay, int daysCount) {
        super(id, name, picDir);
        this.minDay = minDay;
        this.maxDay = maxDay;
        this.daysCount = daysCount;
    }

    public String getMinDay() {
        return minDay;
    }

    public void setMinDay(String minDay) {
        this.minDay = minDay;
    }

    public String getMaxDay() {
        return maxDay;
    }

    public void setMaxDay(String maxDay) {
        this.maxDay = maxDay;
    }

    public int getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(int daysCount) {
        this.daysCount = daysCount;
    }
}
