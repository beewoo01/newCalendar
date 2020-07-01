package cookmap.cookandroid.hw.newcalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.databinding.DataBindingUtil;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import cookmap.cookandroid.hw.newcalendar.databinding.CalendarListBinding;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

    public static int SUNDAY        = 1;
    public static int MONDAY        = 2;
    public static int TUESDAY       = 3;
    public static int WEDNSESDAY    = 4;
    public static int THURSDAY      = 5;
    public static int FRIDAY        = 6;
    public static int SATURDAY      = 7;

    private TextView mTvCalendarTitle;
    private GridView mGvCalendar;

    private ArrayList<DayInfo> mDayList;
    private CalendarAdapter mCalendarAdapter;

    private CalendarListBinding binding;

    final static int DISTANCE = 200;
    final static int VELOCITY = 350;

    private GestureDetectorCompat detector;

    Calendar mLastMonthCalendar;
    Calendar mThisMonthCalendar;
    Calendar mNextMonthCalendar;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //mode = ;
        //binding.setModel();
        binding.setLifecycleOwner(this);
        //observe();
        //obsetve 함수 만들어 주셈


        mTvCalendarTitle = (TextView)findViewById(R.id.Month_Text);
        mGvCalendar = (GridView)findViewById(R.id.Calendar_Grid);
        //
        detector = new GestureDetectorCompat(this, this);
        //detector.setOnDoubleTapListener((GestureDetector.OnDoubleTapListener) this);

        //

        mGvCalendar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }

        });




                mDayList = new ArrayList<DayInfo>();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // 이번달 의 캘린더 인스턴스를 생성한다.
        mThisMonthCalendar = Calendar.getInstance();
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(mThisMonthCalendar);
    }

    /**
     * 달력을 셋팅한다.
     *
     * @param calendar 달력에 보여지는 이번달의 Calendar 객체
     */
    private void getCalendar(Calendar calendar)
    {
        int lastMonthStartDay;
        int dayOfMonth;
        int thisMonthLastDay;

        mDayList.clear();

        // 이번달 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다.)
        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);
        Log.e("지난달 마지막일", calendar.get(Calendar.DAY_OF_MONTH)+"");

        // 지난달의 마지막 일자를 구한다.
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, 1);
        Log.e("이번달 시작일", calendar.get(Calendar.DAY_OF_MONTH)+"");

        if(dayOfMonth == SUNDAY)
        {
            dayOfMonth += 7;
        }

        lastMonthStartDay -= (dayOfMonth-1)-1;


        // 캘린더 타이틀(년월 표시)을 세팅한다.
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");

        DayInfo day;
        String fixmonth = null, fixday = null;

        Log.e("DayOfMOnth", dayOfMonth+"");

        for(int i=0; i<dayOfMonth-1; i++)
        {
            int date = lastMonthStartDay+i;
            day = new DayInfo();

            //mThisMonthCalendar.get(Calendar.YEAR);
            //if (mThisMonthCalendar.get(Calendar.MONTH) > 10) fixmonth = "0" + mThisMonthCalendar.get(Calendar.MONTH);
            //if (date > 10) fixday = "0" + date;

            //String ful = Integer.toString(mThisMonthCalendar.get(Calendar.YEAR)).concat(Integer.toString(mThisMonthCalendar.get(Calendar.MONTH)).concat(String.valueOf(date)));
            //Log.d("형식을 보자1", ful);

            day.setDay(Integer.toString(date));
            //day.setDay(ful);

            day.setInMonth(false);

            mDayList.add(day);
        }
        for(int i=1; i <= thisMonthLastDay; i++)
        {
            day = new DayInfo();
            //String ful = Integer.toString(mThisMonthCalendar.get(Calendar.YEAR)).concat(Integer.toString(mThisMonthCalendar.get(Calendar.MONTH)+1).concat(String.valueOf(i)));

            //day.setDay(ful);
            day.setDay(Integer.toString(i));

            //Log.d("형식을 보자2", ful);
            day.setInMonth(true);

            mDayList.add(day);
        }
        for(int i = 1; i < 42 - (thisMonthLastDay+dayOfMonth-1) + 1; i++)
        {
            day = new DayInfo();
            //String ful = Integer.toString(mThisMonthCalendar.get(Calendar.YEAR)).concat(Integer.toString(mThisMonthCalendar.get(Calendar.MONTH)+2).concat(String.valueOf(i)));
            //Log.d("형식을 보자3", ful);

            //day.setDay(ful);
            day.setDay(Integer.toString(i));

            day.setInMonth(false);
            mDayList.add(day);
        }

        initCalendarAdapter();
    }

    /**
     * 지난달의 Calendar 객체를 반환합니다.
     *
     * @param calendar
     * @return LastMonthCalendar
     */
    private Calendar getLastMonth(Calendar calendar)
    {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, -1);
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }

    /**
     * 다음달의 Calendar 객체를 반환합니다.
     *
     * @param calendar
     * @return NextMonthCalendar
     */
    private Calendar getNextMonth(Calendar calendar)
    {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }



    private void initCalendarAdapter()
    {
        mCalendarAdapter = new CalendarAdapter(this, R.layout.day, mDayList);
        mGvCalendar.setAdapter(mCalendarAdapter);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("onFling", "여긴옴");
        if (e1.getX() - e2.getX() < DISTANCE && Math.abs(velocityX) > VELOCITY){
            Toast.makeText(MainActivity.this, "좌 -> 우", Toast.LENGTH_SHORT).show();
            getCalendar(getLastMonth(mThisMonthCalendar));
        }
        if (e2.getX() - e1.getX() < DISTANCE && Math.abs(velocityX) > VELOCITY){
            Toast.makeText(MainActivity.this, "우 -> 좌", Toast.LENGTH_SHORT).show();
            getCalendar(getNextMonth(mThisMonthCalendar));
        }
        return true;
    }
}
