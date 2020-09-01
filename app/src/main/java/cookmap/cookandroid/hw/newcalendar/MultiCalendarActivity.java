package cookmap.cookandroid.hw.newcalendar;

import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Calendar;

import cookmap.cookandroid.hw.newcalendar.adapter.AdapterFrgCalendar;
import cookmap.cookandroid.hw.newcalendar.adapter.AdapterRcvSimple;
import cookmap.cookandroid.hw.newcalendar.Database.Database_Room;
import cookmap.cookandroid.hw.newcalendar.adpater.AdapterFrgCalendar;
import cookmap.cookandroid.hw.newcalendar.view.SimpleViewBinder;

public class MultiCalendarActivity extends BaseActivity implements FrgCalendar.OnFragmentListener {
    private static final int COUNT_PAGE = 12;
    ViewPager2 pager;
    private AdapterFrgCalendar adapter;
    private AdapterRcvSimple adapterHourLine;
    private RecyclerView rcv;
    private ArrayList mList;
    private SimpleViewBinder.RecyclerViewBuilder builder;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_calendar);
        initialize(Calendar.getInstance().getTimeInMillis());
    }


    @Override
    public void initData(long data) {
        super.initData(data);

        mList = new ArrayList();
        /*if (String.valueOf(data) != ""){
            mList.add(String.valueOf(data));
        }*/
        mList.add(String.valueOf(data));
        mList.add("06:00 wakeup");
        mList.add("07:00 breakfast");
        mList.add("08:00 go to office");
        mList.add("09:00 work ");
        mList.add("12:00 lunch");
        mList.add("13:00 work");
        mList.add("14:00 sleep");
        mList.add("18:00 get off work");
        mList.add("16:30 dinner");
        mList.add("20:00 sleep");
        changeMemo();

    }

    @Override
    public void initView() {
        super.initView();
        pager = (ViewPager2) findViewById(R.id.pager);
    }

    public void initControl() {
        adapter = new AdapterFrgCalendar(this);

        pager.setAdapter(adapter);
        adapter.setOnFragmentListener(this);
        adapter.setNumOfMonth(COUNT_PAGE);
        pager.setCurrentItem(COUNT_PAGE);
        String title = adapter.getMonthDisplayed(COUNT_PAGE);
        getSupportActionBar().setTitle(title);
        Log.d("getSupportActionBar", String.valueOf(getSupportActionBar().getTitle()));

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                String title = adapter.getMonthDisplayed(position);
                getSupportActionBar().setTitle(Html.fromHtml(("<font color=\"red\">" + title+
                        "</font>")));

                if (position == 0) {
                    adapter.addPrev();
                    pager.setCurrentItem(COUNT_PAGE, false);
                } else if (position == adapter.getItemCount() - 1) {
                    adapter.addNext();
                    pager.setCurrentItem(adapter.getItemCount() - (COUNT_PAGE + 1), false);
                }
            }
        });


    }

    public void changeMemo(){
        adapterHourLine = new AdapterRcvSimple(R.layout.item_rcv_simple);
        builder =  new SimpleViewBinder.RecyclerViewBuilder(getWindow()).setAdapter(adapterHourLine, getSupportFragmentManager()).setList(mList);
        rcv = builder.build();
        builder.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onFragmentListener(View view) {
        resizeHeight(view);
    }

    public void resizeHeight(View mRootView) {

        if (mRootView.getHeight() < 1) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = pager.getLayoutParams();
        if (layoutParams.height <= 0) {
            layoutParams.height = mRootView.getHeight();
            pager.setLayoutParams(layoutParams);
            return;
        }
        ValueAnimator anim = ValueAnimator.ofInt(pager.getLayoutParams().height, mRootView.getHeight());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = pager.getLayoutParams();
                layoutParams.height = val;
                pager.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(200);
        anim.start();
    }
}
