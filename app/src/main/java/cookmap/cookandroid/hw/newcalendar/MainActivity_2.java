package cookmap.cookandroid.hw.newcalendar;

import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cookmap.cookandroid.hw.newcalendar.Database.Content_Room;
import cookmap.cookandroid.hw.newcalendar.Database.Database_Room;
import cookmap.cookandroid.hw.newcalendar.adpater.AdapterFrgCalendar;
import cookmap.cookandroid.hw.newcalendar.adpater.AdapterRCVBase;
import cookmap.cookandroid.hw.newcalendar.adpater.adapter_memoList;
import cookmap.cookandroid.hw.newcalendar.databinding.ActivityMultiCalendarBinding;
import cookmap.cookandroid.hw.newcalendar.view.SimpleViewBinder;

public class MainActivity_2 extends BaseActivity implements FrgCalendar.OnFragmentListener, AdapterRCVBase.OnRCVItemListener {
    private static final int COUNT_PAGE = 12;
    private RecyclerView memo_list;

    long selectDate;

    //private TextView mTvCalendarTitle;
    private AdapterFrgCalendar adapter;
    private adapter_memoList adapter_memoLine;
    //private RecyclerView rcv;
    private SimpleViewBinder.RecyclerViewBuilder builder;

    private List<Content_Room> memo_Click_List = new ArrayList<>();;

    ActivityMultiCalendarBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_multi_calendar);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_multi_calendar);
        initialize(Calendar.getInstance().getTimeInMillis());
    }

    @Override
    public void initData(long data) {
        super.initData(data);
        //데이터를 넣어주는곳
        // 다른 곳에서는 recyclerview(메모)데이터를 여기서 넣어 줬음
        //adapter_memoLine = new adapter_memoList();
        builder = new SimpleViewBinder.RecyclerViewBuilder(
                getWindow()).setAdapter(adapter_memoLine, getSupportFragmentManager()).setList(memo_Click_List);

        AdapterRCVBase baseAdapter = new adapter_memoList();
        baseAdapter.setList(memo_Click_List);
        binding.memoRecycler.setAdapter(baseAdapter);
        binding.memoRecycler.setLayoutManager(new LinearLayoutManager(this));
        baseAdapter.notifyDataSetChanged();
        Log.d("main2 data? ", String.valueOf(data));
        selectDate = data;
        //CalendarView - setCurrentSelectedView 에서 memo_Click_List에 item을 넣어줘야함

    }

    @Override
    public void initView() {
        super.initView();
    }

    public void initControl() {
        adapter = new AdapterFrgCalendar(this);
        binding.pager.setAdapter(adapter);

        binding.pager.setAdapter(adapter);
        adapter.setOnFragmentListener(this);
        adapter.setNumOfMonth(COUNT_PAGE);
        binding.pager.setCurrentItem(COUNT_PAGE);
        String title = adapter.getMonthDisplayed(COUNT_PAGE);
        //mTvCalendarTitle.setText(title);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        getSupportActionBar().setTitle(Html.fromHtml(("<font color=\"black\">" + title+
                "</font>")));


        Log.d("getSupportActionBar", String.valueOf(getSupportActionBar().getTitle()));


        binding.pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                String title = adapter.getMonthDisplayed(position);
                //mTvCalendarTitle.setText(title);
                getSupportActionBar().setTitle(Html.fromHtml(("<font color=\"black\">" + title+
                        "</font>")));

                if (position == 0) {
                    adapter.addPrev();
                    binding.pager.setCurrentItem(COUNT_PAGE, false);
                } else if (position == adapter.getItemCount() - 1) {
                    adapter.addNext();
                    binding.pager.setCurrentItem(adapter.getItemCount() - (COUNT_PAGE + 1), false);
                }
            }
        });


    }



    @Override
    public void onFragmentListener(View view) {
        resizeHeight(view);
    }

    public void resizeHeight(View mRootView) {

        if (mRootView.getHeight() < 1) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = binding.pager.getLayoutParams();
        if (layoutParams.height <= 0) {
            layoutParams.height = mRootView.getHeight();
            binding.pager.setLayoutParams(layoutParams);
            return;
        }
        ValueAnimator anim = ValueAnimator.ofInt(binding.pager.getLayoutParams().height, mRootView.getHeight());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = binding.pager.getLayoutParams();
                layoutParams.height = val;
                binding.pager.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(200);
        anim.start();
    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putLong("date", selectDate);
        bundle.putInt("position", position);
        initIntent(MainActivity_2.this, bundle, Memo_Click_Activity.class);
    }

    public void feb_click(){
        Bundle bundle = new Bundle();
        bundle.putInt("id", 0);
        bundle.putLong("select_Day", selectDate);
        initIntent(MainActivity_2.this, bundle, writeActivity.class);

    }
}
