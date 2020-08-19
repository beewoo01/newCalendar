package cookmap.cookandroid.hw.newcalendar.Database;

public class Memo_Date {
    private String start_day;
    private String end_day;

    public Memo_Date(String start_day, String end_day) {
        this.start_day = start_day;
        this.end_day = end_day;
    }

    public String getStart_day() {
        return start_day;
    }

    public String getEnd_day() {
        return end_day;
    }

}
