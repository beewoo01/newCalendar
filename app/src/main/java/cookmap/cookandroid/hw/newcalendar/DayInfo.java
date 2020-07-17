package cookmap.cookandroid.hw.newcalendar;

public class DayInfo {
    private String day;
    private boolean inMonth;
    private boolean isMemo;

    public boolean isMemo() {
        return isMemo;
    }

    public void setMemo(boolean memo) {
        isMemo = memo;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isInMonth() {
        return inMonth;
    }

    public void setInMonth(boolean inMonth) {
        this.inMonth = inMonth;
    }

    public String fixNumber(String orNum){
        if (Integer.parseInt(orNum) < 10) orNum = "0" + orNum;
        return null;
    }

}
