package cookmap.cookandroid.hw.newcalendar.adpater;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cookmap.cookandroid.hw.newcalendar.FrgCalendar;

public class AdapterFrgCalendar extends FragmentStateAdapter {
    private HashMap<Integer, FrgCalendar> frgMap;
    private ArrayList<Long> listMonthByMillis = new ArrayList<>();
    private int numOfMonth;
    private FrgCalendar.OnFragmentListener onFragmentListener;
    private Calendar calendar;

    public AdapterFrgCalendar(FragmentActivity fm, Calendar calendar) {
        super(fm);
        clearPrevFragments(fm);
        this.calendar = calendar;
        frgMap = new HashMap<>();
    }

    private void clearPrevFragments(FragmentActivity fa) {
        List<Fragment> listFragment = fa.getSupportFragmentManager().getFragments();


        if (listFragment != null) {
            FragmentTransaction ft = fa.getSupportFragmentManager().beginTransaction();

            for (Fragment f : listFragment) {
                if (f instanceof FrgCalendar) {
                    ft.remove(f);
                }
            }
            ft.commitAllowingStateLoss();
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        FrgCalendar frg = null;
        if (frgMap.size() > 0) {
            frg = frgMap.get(position);
        }
        if (frg == null) {
            frg = FrgCalendar.newInstance(position);
            frg.setOnFragmentListener(onFragmentListener);
            frgMap.put(position, frg);
        }
        frg.setTimeByMillis(listMonthByMillis.get(position));

        return frg;
    }

    @Override
    public int getItemCount() {
        return listMonthByMillis.size();
    }

    public void setNumOfMonth(int numOfMonth) {
        this.numOfMonth = numOfMonth;


        calendar.add(Calendar.MONTH, -numOfMonth);
        calendar.set(Calendar.DATE, 1);

        for (int i = 0; i < numOfMonth * 2 + 1; i++) {
            listMonthByMillis.add(calendar.getTimeInMillis());
            calendar.add(Calendar.MONTH, 1);
        }

        notifyDataSetChanged();
    }

    public void addNext() {
        long lastMonthMillis = listMonthByMillis.get(listMonthByMillis.size() - 1);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastMonthMillis);
        for (int i = 0; i < numOfMonth; i++) {
            calendar.add(Calendar.MONTH, 1);
            listMonthByMillis.add(calendar.getTimeInMillis());
        }
        notifyDataSetChanged();
    }

    public void addPrev() {
        long lastMonthMillis = listMonthByMillis.get(0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastMonthMillis);
        calendar.set(Calendar.DATE, 1);
        for (int i = numOfMonth; i > 0; i--) {
            calendar.add(Calendar.MONTH, -1);

            listMonthByMillis.add(0, calendar.getTimeInMillis());
        }
        notifyDataSetChanged();
    }


    public String getMonthDisplayed(int position) {
        Calendar calendar = Calendar.getInstance();
        int yyyy = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(listMonthByMillis.get(position));
        if (yyyy != calendar.get(Calendar.YEAR)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM");
            Date date = new Date();
            date.setTime(listMonthByMillis.get(position));
            Log.d("getMonth", calendar.get(Calendar.YEAR) + ", " + calendar.get(Calendar.MONTH));

            return sdf.format(date);
        } else {
            Log.d("getMonth", calendar.get(Calendar.YEAR) + ", " + calendar.get(Calendar.MONTH));
            return calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        }
    }

    public void setOnFragmentListener(FrgCalendar.OnFragmentListener onFragmentListener) {
        this.onFragmentListener = onFragmentListener;
    }


}