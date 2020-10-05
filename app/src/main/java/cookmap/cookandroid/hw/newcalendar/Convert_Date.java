package cookmap.cookandroid.hw.newcalendar;

import android.icu.util.LocaleData;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Convert_Date {

    public String Convert_Date(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        String fullday = cal.get(Calendar.YEAR) + "/";
        if ((cal.get(Calendar.MONTH) + 1) < 10) {
            fullday += "0" + (cal.get(Calendar.MONTH) + 1) + "/";
        } else fullday += cal.get(Calendar.MONTH) + "/";

        if (cal.get(Calendar.DATE) < 10) {
            fullday += "0" + cal.get(Calendar.DATE);
        } else fullday += cal.get(Calendar.DATE);

        return fullday;
    }

    public String Convert_date_short(long day, int type) {
        // type 0 = month 까지, 1 = day 까지, 2 = day, 요일
        Calendar calendar = Calendar.getInstance();
        int yyyy = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(day);
        if (type == 1){
            return calendar.getDisplayName(Calendar.DAY_OF_MONTH, Calendar.SHORT, Locale.getDefault());
        }else if (type == 2){
            return calendar.get(Calendar.DAY_OF_MONTH)+ "일 " + Convert_Day_Of_Week(calendar.get(Calendar.DAY_OF_WEEK));
        }else{
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


    public long Convert_StringToLong(String date)  {
        Date to = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            to = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milli = to.getTime();
        return milli;
    }

    private String Convert_Day_Of_Week(int day){
        String dayOfWeek = "";
        switch (day){
            case 1: dayOfWeek = "Son";    break ;
            case 2: dayOfWeek = "Mon";    break ;
            case 3: dayOfWeek = "Tue";    break ;
            case 4: dayOfWeek = "Wed";    break ;
            case 5: dayOfWeek = "Thu";    break ;
            case 6: dayOfWeek = "Fri";    break ;
            case 7: dayOfWeek = "Sat";    break ;
        }
        return dayOfWeek;
    }
}
