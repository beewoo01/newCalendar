package cookmap.cookandroid.hw.newcalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public String Convert_date_short(long day){
            Calendar calendar = Calendar.getInstance();
            int yyyy = calendar.get(Calendar.YEAR);
            calendar.setTimeInMillis(day);
            if (yyyy != calendar.get(Calendar.YEAR)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM");
                Date date = new Date();
                date.setTime(day);

                return sdf.format(date);
            } else {
                return calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
            }
    }
}
