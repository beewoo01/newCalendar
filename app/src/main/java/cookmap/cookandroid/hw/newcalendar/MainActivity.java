package cookmap.cookandroid.hw.newcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvCalendarTitle = (TextView) findViewById(R.id.Month_Text);
        //mGvCalendar = (GridView) findViewById(R.id.Calendar_Grid);
        mCalendar_Gv = findViewById(R.id.Calendar_Grid);
        memo_list = findViewById(R.id.Memo_List);
        fab = findViewById(R.id.fab_button);
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
        /*mCalendar_Gv.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), mCalendar_Gv,
                new RecyclerItemClickListener()));*/

        mCalendar_Gv.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, mCalendar_Gv,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void OnItemClick(View view, int position) {
                        for (int i = 0; i < mCalendar_Gv.getChildCount(); i++) {
                            if (position == i) {
                                mCalendar_Gv.getChildAt(i).setBackgroundColor(Color.parseColor("#FFE0AB"));
                                cu_time = mDayList.get(i).getDay();
                                /*String f =
                                        mThisMonthCalendar.get(Calendar.YEAR) + "/" + mThisMonthCalendar.get(Calendar.MONTH)+*/

                                try {
                                    format = new SimpleDateFormat("yyyy/MM/dd");
                                    Date date = format.parse(mThisMonthCalendar.get(Calendar.YEAR) + "/" + (mThisMonthCalendar.get(Calendar.MONTH)+1) + "/" + mDayList.get(i).getDay());
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    selectQuery = format.format(calendar.getTime());
                                    Log.d("gv됨?", selectQuery);
                                    if (checkTable()){ sql_select(); }
                                    //여기로2
                                    } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Calendar calendar = Calendar.getInstance();

                                Log.d("gv됨?32", mThisMonthCalendar.get(Calendar.YEAR) + "/" + (mThisMonthCalendar.get(Calendar.MONTH)+1) + "/" + mDayList.get(i).getDay());


                                // 날짜 변경으로 리스트뷰 초기화 함수로 ㄱㄱ 해야함
                            } else {
                                mCalendar_Gv.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

                            }

                        }
                    }

                }));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, writeActivity.class));
            }
        });
    }
    private String change_num(int number){
        return "";
    }

    private void init(){
        //그리드뷰 swipe시 필요
        detector = new GestureDetectorCompat(this, this);


        mDayList = new ArrayList<DayInfo>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        memo_list.setLayoutManager(mLayoutManager);
        memo_list.addItemDecoration(new RecyclerViewDecoration(15));
        // 이번달 의 캘린더 인스턴스를 생성한다.
        mThisMonthCalendar = Calendar.getInstance();
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(mThisMonthCalendar);
    }


    @Override
    protected void onStart() {
        super.onStart();

        // 이번달 의 캘린더 인스턴스를 생성한다.
        /*mThisMonthCalendar = Calendar.getInstance();
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(mThisMonthCalendar);*/
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

        Log.e("DayOfMOnth", dayOfMonth + "");

        for (int i = 0; i < dayOfMonth - 1; i++) {
            int date = lastMonthStartDay + i;
            day = new DayInfo();
            day.setDay(Integer.toString(date));
            day.setInMonth(false);
            mDayList.add(day);
        }
        for (int i = 1; i <= thisMonthLastDay; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(true);
            mDayList.add(day);
        }
        for (int i = 1; i < 42 - (thisMonthLastDay + dayOfMonth - 1) + 1; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(false);
            mDayList.add(day);
        }

        initCalendarAdapter();
        if (checkTable()){ sql_select(); }


    }
    private void initMemoAdapter(){

        memo_Adapter = new memo_Recycler_Adapter(memo_items_List);
        memo_Adapter.notifyDataSetChanged();
        memo_list.setAdapter(memo_Adapter);
        for (int i = 0; i < memo_items_List.size(); i++){
            Log.d("메모 리스트",memo_items_List.get(i).getTitle() + ",i =" + i);
            memo_items_List.get(i).getTitle();
        }


    }
    boolean checkTable(){

        db = openOrCreateDatabase("con_file.db", Context.MODE_PRIVATE,null);
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='contents'" , null);
        cursor.moveToNext();
        if (cursor.getCount() > 0){
            return true;
        }else {
            Log.d(TAG, "테이블이 없음");
            helper = new SQLiteOpenHelper(this, "contents", null, dbVersion);
            return false;
        }
    }
    void sql_select(){
        String sql = "SELECT * FROM contents WHERE s_date = ?";


        Cursor results = db.rawQuery(sql, new String[]{selectQuery});

        memo_items_List.clear();
        while (results.moveToNext()){
            Log.d("while문","오나요?");
            int i = 1;
            memo_item list = new memo_item();
            int _id = results.getInt(0);
            String title = results.getString(1);
            String description = results.getString(2);
            String main_Img = results.getString(3);
            String img = results.getString(4);
            String s_date = results.getString(5);
            String e_date = results.getString(6);
            String label = results.getString(7);

            list.setId(String.valueOf(_id));
            list.setTitle(title);
            list.setDesc(description);
            list.setImg_main(main_Img);
            list.setImgs(img);
            list.setS_date(s_date);
            list.setE_date(e_date);
            if (label == null) {
                label = "#000000";
            }
            list.setLabel(label);

            Log.d("와일문 ", String.valueOf(i));
            Log.d("셀렉트 ID", String.valueOf(_id));
            Log.d("셀렉트 제목", title);
            Log.d("셀렉트 묘사", description);
            Log.d("셀렉트 커버", main_Img);
            Log.d("셀렉트 이미지", img);
            Log.d("셀렉트 시작날", s_date);
            Log.d("셀렉트 끝날", e_date);
            Log.d("셀렉트 라벨", label);

            memo_items_List.add(list);
        }

        results.close();

        initMemoAdapter();
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
        if (height == 0){
            mCalendar_Gv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    height = mCalendar_Gv.getHeight() /6/3;
                    mCalendar_Gv.addItemDecoration(new RecyclerViewDecoration(height));
                    //mTestRecyclerAdapter.setLength(height);
                    mTestRecyclerAdapter.notifyDataSetChanged();
                    mCalendar_Gv.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                }
            });
        }

        //mCalendarAdapter = new CalendarAdapter(this, R.layout.day, mDayList);
        mTestRecyclerAdapter = new testRecyclerGrid(this, R.layout.day, mDayList);

        GridLayoutManager mgmr = new GridLayoutManager(this, 7);
        mCalendar_Gv.setLayoutManager(mgmr);
        mCalendar_Gv.setAdapter(mTestRecyclerAdapter);
        /*int height = mCalendar_Gv.getHeight() /6/3;
        mCalendar_Gv.addItemDecoration(new RecyclerViewDecoration(height));
        mTestRecyclerAdapter.notifyDataSetChanged();*/

        //mGvCalendar.setAdapter(mCalendarAdapter);

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
            Log.d("계산", String.valueOf(e1.getX()-e2.getX()));
            getCalendar(getLastMonth(mThisMonthCalendar));

        }
        if (e2.getX() - e1.getX() < DISTANCE && Math.abs(velocityX) > VELOCITY) {
            Log.d("계산", String.valueOf(e1.getX()-e2.getX()));
            getCalendar(getNextMonth(mThisMonthCalendar));
        }
        return true;
    }
}
