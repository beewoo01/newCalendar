package cookmap.cookandroid.hw.newcalendar;

import java.util.Calendar;

public class Convert_Date {

    public String Convert_Date(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        String fullday = cal.get(Calendar.YEAR) + "/";
        if ((cal.get(Calendar.MONTH)+1) < 10){
            fullday += "0" + (cal.get(Calendar.MONTH)+1) + "/";
        }else fullday += cal.get(Calendar.MONTH) + "/";

        if (cal.get(Calendar.DATE) < 10){
            fullday += "0" + cal.get(Calendar.DATE);
        }else fullday += cal.get(Calendar.DATE);

        return fullday;
    }
}
