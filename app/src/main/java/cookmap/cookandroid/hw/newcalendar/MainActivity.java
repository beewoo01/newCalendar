package cookmap.cookandroid.hw.newcalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    public static int SUNDAY = 1;
    public static int MONDAY = 2;
    public static int TUESDAY = 3;
    public static int WEDNSESDAY = 4;
    public static int THURSDAY = 5;
    public static int FRIDAY = 6;
    public static int SATURDAY = 7;

    private TextView mTvCalendarTitle;
    private GridView mGvCalendar;
    private RecyclerView mCalendar_Gv;
    private RecyclerView memo_list;
    private FloatingActionButton fab;
    private LinearLayout empt_lay;

    private String TAG = "메인클래스";

    private ArrayList<DayInfo> mDayList;
    private ArrayList<memo_item> memo_items_List = new ArrayList<memo_item>();
    //private CalendarAdapter mCalendarAdapter;
    private testRecyclerGrid mTestRecyclerAdapter;
    private memo_Recycler_Adapter memo_Adapter = null;

    private SQLiteDatabase db;
    private SQLiteOpenHelper helper;
    private SimpleDateFormat format;
    int dbVersion = 1;

    //private CalendarListBinding binding;

    final static int DISTANCE = 200;
    final static int VELOCITY = 350;

    private GestureDetectorCompat detector;

    Calendar mLastMonthCalendar;
    Calendar mThisMonthCalendar;
    Calendar mNextMonthCalendar;
    int height = 0;
    String cu_time = "01";
    String selectQuery = null;
    int testint = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvCalendarTitle = (TextView) findViewById(R.id.Month_Text);
        //mGvCalendar = (GridView) findViewById(R.id.Calendar_Grid);
        mCalendar_Gv = findViewById(R.id.Calendar_Grid);
        memo_list = findViewById(R.id.Memo_List);
        fab = findViewById(R.id.fab_button);
        empt_lay = findViewById(R.id.empty_layout);
        //helper = new SQLiteOpenHelper(this);
        format = new SimpleDateFormat("yyyy/MM/dd");
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        selectQuery = format.format(date);
        helper = new SQLiteOpenHelper(this, "contents", null, dbVersion);
        /*if (checkTable()) {
            sql_select();
        }*/
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
                        day_touch(view, position);

                    }

                }));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, writeActivity.class));
            }
        });
    }

    private void day_touch(View view, int position) {
        for (int i = 0; i < mCalendar_Gv.getChildCount(); i++) {
            if (position == i) {
                mCalendar_Gv.getChildAt(i).setBackgroundColor(Color.parseColor("#FFE0AB"));
                cu_time = mDayList.get(i).getDay();

                try {
                    format = new SimpleDateFormat("yyyy/MM/dd");
                    Date date = format.parse(mThisMonthCalendar.get(Calendar.YEAR) + "/" + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "/" + mDayList.get(i).getDay());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    selectQuery = format.format(calendar.getTime());
                    if (checkTable()) {
                        sql_select();
                    }
                    //여기로2
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                // 날짜 변경으로 리스트뷰 초기화 함수로 ㄱㄱ 해야함
            } else {
                mCalendar_Gv.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

            }

        }
    }

    private void init() {
        //그리드뷰 swipe시 필요
        detector = new GestureDetectorCompat(this, this);

        mDayList = new ArrayList<DayInfo>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        memo_list.setLayoutManager(mLayoutManager);
        memo_list.addItemDecoration(new RecyclerViewDecoration(15));
        if (checkTable()) {
            select();
        }
        // 이번달 의 캘린더 인스턴스를 생성한다.
        mThisMonthCalendar = Calendar.getInstance();
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        getCalendar(mThisMonthCalendar);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 달력을 셋팅한다.
     *
     * @param calendar 달력에 보여지는 이번달의 Calendar 객체
     */
    private void getCalendar(Calendar calendar) {
        int lastMonthStartDay;
        int dayOfMonth;
        int thisMonthLastDay;
        db = openOrCreateDatabase("con_file.db", Context.MODE_PRIVATE, null);

        mDayList.clear();


        // 이번달 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다.)
        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);
        Log.e("지난달 마지막일", calendar.get(Calendar.DAY_OF_MONTH) + "");

        // 지난달의 마지막 일자를 구한다.
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, 1);
        Log.e("이번달 시작일", calendar.get(Calendar.DAY_OF_MONTH) + "");

        if (dayOfMonth == SUNDAY) {
            dayOfMonth += 7;
        }

        lastMonthStartDay -= (dayOfMonth - 1) - 1;



        // 캘린더 타이틀(년월 표시)을 세팅한다.
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        DayInfo day;


        for (int i = 0; i < dayOfMonth - 1; i++) {
            int date = lastMonthStartDay + i;
            day = new DayInfo();
            day.setDay(Integer.toString(date));
            day.setInMonth(false);
            day.setFull_Day(mThisMonthCalendar.get(Calendar.YEAR) + "/" + change((mThisMonthCalendar.get(Calendar.MONTH) )) + "/" +change(Integer.parseInt(day.getDay())));

            mDayList.add(day);
        }
        for (int i = 1; i <= thisMonthLastDay; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(true);
            day.setFull_Day(mThisMonthCalendar.get(Calendar.YEAR) + "/" + change((mThisMonthCalendar.get(Calendar.MONTH)+1 )) + "/" +change(Integer.parseInt(day.getDay())));

            mDayList.add(day);
        }
        for (int i = 1; i < 42 - (thisMonthLastDay + dayOfMonth - 1) + 1; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(false);
            day.setFull_Day(mThisMonthCalendar.get(Calendar.YEAR) + "/" + change((mThisMonthCalendar.get(Calendar.MONTH)+2 )) + "/" + change(Integer.parseInt(day.getDay())));
            mDayList.add(day);
        }



        for (int i = 0; i <= mDayList.size()-1; i++){
            int z = 0;
            for (int j = 0; j <= memo_items_List.size()-1; j++){
                if (mDayList.get(i).getFull_Day().equals(memo_items_List.get(j).getS_date())){
                    z++;
                }
            }
            mDayList.get(i).setIsMemo(z);

        }
        initCalendarAdapter();
        if (checkTable()) {
            sql_select();
        }



    }

    private void select(){
        memo_items_List.clear();
        String few = "contents";
        String sql = "select * from " + few + ";";
        Cursor results = db.rawQuery(sql,null);
        int i = 0;
        while (results.moveToNext()){
            memo_item list = new memo_item();
            list.setS_date(results.getString(5));
            memo_items_List.add(list);
            i++;
        }
        results.close();



    }

    private String change(int num) {
        String charter;
        if (num <= 10) charter = "0" + num;
        else charter = String.valueOf(num);

        return charter;
    }

    private void initMemoAdapter() {

        memo_Adapter = new memo_Recycler_Adapter(memo_items_List);
        memo_Adapter.notifyDataSetChanged();
        memo_list.setAdapter(memo_Adapter);
        for (int i = 0; i < memo_items_List.size(); i++) {
            memo_items_List.get(i).getTitle();
        }


    }

    private boolean checkTable() {

        db = openOrCreateDatabase("con_file.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='contents'", null);
        cursor.moveToNext();
        if (cursor.getCount() > 0) {
            return true;
        } else {
            Log.d(TAG, "테이블이 없음");
            helper = new SQLiteOpenHelper(this, "contents", null, dbVersion);
            return false;
        }
    }

    private void sql_select() {
        testint++;
        String sql = "SELECT * FROM contents WHERE s_date = ?";
        Cursor results = db.rawQuery(sql, new String[]{selectQuery});

        //Cursor results1 = db.rawQuery(testsql, null);

        memo_items_List.clear();

        while (results.moveToNext()) {
            memo_item list = new memo_item();


            list.setId(String.valueOf(results.getInt(0)));
            list.setTitle(results.getString(1));
            list.setDesc(results.getString(2));
            list.setImg_main(results.getString(3));
            list.setImgs(results.getString(4));
            list.setS_date(results.getString(5));
            list.setE_date(results.getString(6));
            String label = results.getString(7);
            if (label == null) { label = "#000000"; }
            list.setLabel(label);

            memo_items_List.add(list);
        }
        results.close();
        if (memo_items_List.size() <= 0) {
            empt_lay.setVisibility(View.VISIBLE);
        } else {
            empt_lay.setVisibility(View.GONE);
            initMemoAdapter();
        }

    }

    /**
     * 지난달의 Calendar 객체를 반환합니다.
     *
     * @param calendar
     * @return LastMonthCalendar
     */
    private Calendar getLastMonth(Calendar calendar) {
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
    private Calendar getNextMonth(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }


    private void initCalendarAdapter() {
        if (height == 0) {
            mCalendar_Gv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    height = mCalendar_Gv.getHeight() / 6 / 4;
                    mCalendar_Gv.addItemDecoration(new RecyclerViewDecoration(height));
                    //mTestRecyclerAdapter.setLength(height);
                    mTestRecyclerAdapter.notifyDataSetChanged();
                    mCalendar_Gv.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                }
            });
        }
        mTestRecyclerAdapter = new testRecyclerGrid(this, R.layout.day, mDayList);

        GridLayoutManager mgmr = new GridLayoutManager(this, 7);
        mCalendar_Gv.setLayoutManager(mgmr);
        mCalendar_Gv.setAdapter(mTestRecyclerAdapter);

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
            Log.d("계산", String.valueOf(e1.getX() - e2.getX()));
            if (checkTable())
            select();
            getCalendar(getLastMonth(mThisMonthCalendar));

        }
        if (e2.getX() - e1.getX() < DISTANCE && Math.abs(velocityX) > VELOCITY) {
            Log.d("계산", String.valueOf(e1.getX() - e2.getX()));
            if (checkTable())
            select();
            getCalendar(getNextMonth(mThisMonthCalendar));
        }
        return true;
    }
}
