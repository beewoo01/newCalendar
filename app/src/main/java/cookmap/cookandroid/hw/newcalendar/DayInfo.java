package cookmap.cookandroid.hw.newcalendar;

public class DayInfo {
    private String day;
    private String full_Day;
    private boolean inMonth;
    private int isMemo;

    public int getIsMemo() {
        return isMemo;
    }


    public void setIsMemo(int isMemo) {
        this.isMemo = isMemo;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFull_Day() {
        return full_Day;
    }

    public void setFull_Day(String full_Day) {
        this.full_Day = full_Day;
    }

    public boolean isInMonth() {
        return inMonth;
    }

    public void setInMonth(boolean inMonth) {
        this.inMonth = inMonth;
    }

}
