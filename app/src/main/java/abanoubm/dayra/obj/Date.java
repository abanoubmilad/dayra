package abanoubm.dayra.obj;

public class Date {

    private String day, month, year;

    public Date(String day, String month, String year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Date(String date) {
        String[] temp = date.split("-");
        if (temp.length == 3) {
            this.day = temp[0];
            this.month = temp[1];
            this.year = temp[2];
        } else {
            this.day = "";
            this.month = "";
            this.year = "";
        }
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int compare(Date date) {
        if (this.year.length() != date.year.length())
            return this.year.length() - date.year.length();
        if (!this.year.equals(date.year))
            return this.year.compareTo(date.year);

        if (this.month.length() != date.month.length())
            return this.month.length() - date.month.length();
        if (!this.month.equals(date.month))
            return this.month.compareTo(date.month);

        if (this.day.length() != date.day.length())
            return this.day.length() - date.day.length();
        if (!this.day.equals(date.day))
            return this.day.compareTo(date.day);
        return 0;
    }

    public int compareDayAndMonth(Date date) {
        if (this.month.length() != date.month.length())
            return this.month.length() - date.month.length();
        if (!this.month.equals(date.month))
            return this.month.compareTo(date.month);

        if (this.day.length() != date.day.length())
            return this.day.length() - date.day.length();
        if (!this.day.equals(date.day))
            return this.day.compareTo(date.day);
        return 0;
    }

    public String getDate() {
        if (day.length() > 0)
            return this.day + "-" + this.month + "-" + this.year;
        return "";
    }

}
