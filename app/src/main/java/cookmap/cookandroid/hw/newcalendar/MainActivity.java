package cookmap.cookandroid.hw.newcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cookmap.cookandroid.hw.newcalendar.Database.Database_Room;
import cookmap.cookandroid.hw.newcalendar.Database.Content_Room;
import cookmap.cookandroid.hw.newcalendar.adpater.CalendarAdapter;
import cookmap.cookandroid.hw.newcalendar.adpater.Memo_Adapter;
import cookmap.cookandroid.hw.newcalendar.utill.RecyclerItemClickListener;
import cookmap.cookandroid.hw.newcalendar.utill.RecyclerViewDecoration;
import cookmap.cookandroid.hw.newcalendar.view.SimpleViewBinder;


public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {



    private TextView mTvCalendarTitle;
    private RecyclerView mCalendar_Gv;
    private RecyclerView memo_list;

    private ArrayList<DayInfo> mDayList;
    private List<Content_Room> memo_items_List = new ArrayList<>();
    private List<Content_Room> memo_Click_List = new ArrayList<>();;
    private CalendarAdapter mCalendarAdapter;
    private Memo_Adapter memo_Adapter = null;

    private int today_Position = 0;


    private SimpleDateFormat format;

    private GestureDetectorCompat detector;

    private Calendar mThisMonthCalendar;
    private int height = 0;
    private String selectQuery = null;

    private SimpleViewBinder.RecyclerViewBuilder builder;

    public static int SUNDAY = 1;
    private int DISTANCE = 200;
    private int VELOCITY = 350;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvCalendarTitle = findViewById(R.id.Month_Text);
        mCalendar_Gv = findViewById(R.id.Calendar_Grid);
        memo_list = findViewById(R.id.Memo_List);
        memo_list.addItemDecoration(new RecyclerViewDecoration(15));
        format = new SimpleDateFormat("yyyy/MM/dd");
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        selectQuery = format.format(date);

        init();


        mCalendar_Gv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }

        });

        mCalendar_Gv.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, mCalendar_Gv,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void OnItemClick(View view, int position) {
                        day_touch(position);
                        today_Position = position;

                    }

                }));

        memo_list.addOnItemTouchListener(new RecyclerItemClickListener(this, memo_list,
                new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                start_memo_Activity(position, selectQuery);

            }
        }));

        findViewById(R.id.fab_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSelfPermission();

            }
        });
    }
    private void start_memo_Activity(int position, String date){
        Intent intent = new Intent(MainActivity.this, Memo_Click_Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("date", date);
        bundle.putInt("position", position);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void checkSelfPermission() {
        String temp = "";

        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";

        }

        if (!TextUtils.isEmpty(temp)) {
            //권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "), 1);

        } else {
            // 모두 허용 상태
            Intent intent = new Intent(this, writeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("id", 0);
            bundle.putString("select_Day",selectQuery);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            int length = permissions.length;
            Intent intent = new Intent(this, writeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("id", 0);
            bundle.putString("select_Day",selectQuery);
            intent.putExtras(bundle);
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "사진 업로드 기능이 제한 됩니다.", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        }
    }

    private void day_touch(int position) {

        for (int i = 0; i < mCalendar_Gv.getChildCount(); i++) {
            if (position == i) {
                mCalendar_Gv.getChildAt(i).setBackgroundColor(Color.parseColor("#8AC9FC"));

                try {
                    format = new SimpleDateFormat("yyyy/MM/dd");
                    Date date = format.parse(mThisMonthCalendar.get(Calendar.YEAR)
                            + "/" + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "/" + mDayList.get(i).getDay());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    selectQuery = format.format(calendar.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                sql_select();

            } else {
                mCalendar_Gv.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

            }

        }
    }

    private void init() {
        detector = new GestureDetectorCompat(this, this);

        mDayList = new ArrayList<DayInfo>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        memo_list.setLayoutManager(mLayoutManager);

        select();
        mThisMonthCalendar = Calendar.getInstance();
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        getCalendar(mThisMonthCalendar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("RESUME", "여기와?");
        DayperformClick();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mCalendarAdapter.notifyDataSetChanged();
        getCalendar(mThisMonthCalendar);
    }

    private void DayperformClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                day_touch(today_Position);
            }
        }, 100);
    }

    private void getCalendar(Calendar calendar) {
        int lastMonthStartDay;
        int dayOfMonth;
        int thisMonthLastDay;

        mDayList.clear();


        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);

        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, 1);

        if (dayOfMonth == SUNDAY) {
            dayOfMonth += 7;
        }

        lastMonthStartDay -= (dayOfMonth - 1) - 1;

        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        DayInfo day;


        for (int i = 0; i < dayOfMonth - 1; i++) {
            int date = lastMonthStartDay + i;
            day = new DayInfo();
            day.setDay(Integer.toString(date));
            day.setInMonth(false);
            day.setFull_Day(mThisMonthCalendar.get(Calendar.YEAR) + "/" + change((mThisMonthCalendar.get(Calendar.MONTH))) + "/" + change(Integer.parseInt(day.getDay())));
            mDayList.add(day);
        }
        for (int i = 1; i <= thisMonthLastDay; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(true);
            day.setFull_Day(mThisMonthCalendar.get(Calendar.YEAR) + "/" + change((mThisMonthCalendar.get(Calendar.MONTH) + 1)) + "/" + change(Integer.parseInt(day.getDay())));
            mDayList.add(day);
        }
        for (int i = 1; i < 42 - (thisMonthLastDay + dayOfMonth - 1) + 1; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(false);
            day.setFull_Day(mThisMonthCalendar.get(Calendar.YEAR) + "/" + change((mThisMonthCalendar.get(Calendar.MONTH) + 2)) + "/" + change(Integer.parseInt(day.getDay())));
            mDayList.add(day);
        }
        for (int i = 0; i <= mDayList.size() - 1; i++) {
            int memoCount = Database_Room.getInstance(this).getDao().getMemoCount(mDayList.get(i).getFull_Day());
            mDayList.get(i).setIsMemo(memoCount);
            if (mDayList.get(i).getFull_Day().equals(selectQuery)) {
                today_Position = i;
            }

        }
        initCalendarAdapter();


    }

    private void select() {
        memo_items_List.clear();
        memo_items_List = Database_Room.getInstance(MainActivity.this)
                .getDao()
                .getAllContents();

    }

    private String change(int num) {
        String charter;
        if (num < 10) charter = "0" + num;
        else charter = String.valueOf(num);

        return charter;
    }

    private void initMemoAdapter() {

        memo_Adapter = new Memo_Adapter(memo_Click_List);
        memo_Adapter.notifyDataSetChanged();
        memo_list.setAdapter(memo_Adapter);

        /*builder = new SimpleViewBinder.RecyclerViewBuilder(getWindow()).setAdapter(memo_Adapter,
                 getSupportFragmentManager()).setList(memo_Click_List);*/
    }

    private void sql_select() {

        memo_Click_List.clear();
        memo_Click_List = Database_Room.getInstance(this).getDao().getClickMemo(selectQuery);

        if (memo_Click_List.size() <= 0){
            findViewById(R.id.empty_layout).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.empty_layout).setVisibility(View.GONE);
            initMemoAdapter();
        }

    }

    private Calendar getLastMonth(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, -1);
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");

        try {
            Date date = format.parse(mThisMonthCalendar.get(Calendar.YEAR) + "/" + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "/" + "01");
            calendar = Calendar.getInstance();
            calendar.setTime(date);
            selectQuery = format.format(calendar.getTime());

        } catch (ParseException e) {
            e.getErrorOffset();
        }


        return calendar;
    }

    private Calendar getNextMonth(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");

        try {
            Date date = format.parse(mThisMonthCalendar.get(Calendar.YEAR) + "/" + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "/" + "01");
            calendar = Calendar.getInstance();
            calendar.setTime(date);
            selectQuery = format.format(calendar.getTime());

        } catch (ParseException e) {
            e.getErrorOffset();
        }
        return calendar;
    }


    private void initCalendarAdapter() {
            mCalendar_Gv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (height == 0){
                        height = mCalendar_Gv.getHeight() / 6 / 4;
                        mCalendar_Gv.addItemDecoration(new RecyclerViewDecoration(height));
                    }
                    mCalendarAdapter.notifyDataSetChanged();
                    mCalendar_Gv.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                }
            });

        mCalendarAdapter = new CalendarAdapter(this, mDayList);

        GridLayoutManager mgmr = new GridLayoutManager(this, 7);
        mCalendar_Gv.setLayoutManager(mgmr);
        mCalendar_Gv.setAdapter(mCalendarAdapter);

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

        if (e1.getX() - e2.getX() < DISTANCE && Math.abs(velocityX) > VELOCITY) {
            select();
            getCalendar(getLastMonth(mThisMonthCalendar));
            DayperformClick();
        }
        if (e2.getX() - e1.getX() < DISTANCE && Math.abs(velocityX) > VELOCITY) {
            select();
            getCalendar(getNextMonth(mThisMonthCalendar));
            DayperformClick();
        }
        return true;
    }
}
