package cookmap.cookandroid.hw.newcalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

import cookmap.cookandroid.hw.newcalendar.Database.Database_Room;
import cookmap.cookandroid.hw.newcalendar.widget.CalendarItemView;
import cookmap.cookandroid.hw.newcalendar.widget.CalendarView;

public class FrgCalendar extends Fragment {

    private int position;
    CalendarView calendarView;
    CalendarItemView child;
    private long timeByMillis;
    private OnFragmentListener onFragmentListener;
    private View mRootView;

    public void setOnFragmentListener(OnFragmentListener onFragmentListener) {
        this.onFragmentListener = onFragmentListener;
    }

    public interface OnFragmentListener {
        void onFragmentListener(View view);
    }

    public static FrgCalendar newInstance(int position) {
        FrgCalendar frg = new FrgCalendar();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        frg.setArguments(bundle);
        return frg;
    }

    public FrgCalendar() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("poisition");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_calendar, null);
        initView();
        return mRootView;
    }

    protected void initView() {
        calendarView = (CalendarView) mRootView.findViewById(R.id.calendarview);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeByMillis);
        calendar.set(Calendar.DATE, 1);
        // 1일의 요일
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //이달의 마지막 날
        int maxDateOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendarView.initCalendar(dayOfWeek, maxDateOfMonth);
        for (int i = 0; i < maxDateOfMonth + 7; i++) {
            //CalendarItemView child = new CalendarItemView(getActivity().getApplicationContext());
            child = new CalendarItemView(getActivity().getApplicationContext());
            child.setDate(calendar.getTimeInMillis());
            int count_memo = Database_Room.getInstance(getContext()).getDao().getMemoCount(
                    new Convert_Date().Convert_Date(calendar.getTimeInMillis()));
            if (count_memo > 0){
                child.setEvent();
            }
            if (i < 7) {
                child.setDayOfWeek(i);
            } else {
                calendar.add(Calendar.DATE, 1);
            }
            calendarView.addView(child);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (onFragmentListener != null && mRootView != null) {
            onFragmentListener.onFragmentListener(mRootView);
            child.invalidate();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser && onFragmentListener != null && mRootView != null) {
            onFragmentListener.onFragmentListener(mRootView);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {

            mRootView.post(() -> onFragmentListener.onFragmentListener(mRootView));

        }
    }

    public void setTimeByMillis(long timeByMillis) {
        this.timeByMillis = timeByMillis;
    }
}
