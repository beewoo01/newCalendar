package cookmap.cookandroid.hw.newcalendar;

import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cookmap.cookandroid.hw.newcalendar.Database.Database_Room;
import cookmap.cookandroid.hw.newcalendar.adpater.AdapterFrgCalendar;
import cookmap.cookandroid.hw.newcalendar.adpater.Adapter_memoList;
import cookmap.cookandroid.hw.newcalendar.databinding.ActivityMultiCalendarBinding;
import cookmap.cookandroid.hw.newcalendar.db.Content_Room;
import cookmap.cookandroid.hw.newcalendar.widget.CalendarItemView;

public class MainActivity extends BaseActivity implements FrgCalendar.OnFragmentListener {
    private static final int COUNT_PAGE = 12;

    private long selectDate;
    private List<Content_Room> memo_Click_List = new ArrayList<>();;
    private ActivityMultiCalendarBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_multi_calendar);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_multi_calendar);
        initialize(Calendar.getInstance().getTimeInMillis());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("selectedData", new Convert_Date().Convert_Date(selectDate));
        //binding.memoRecycler.getAdapter().notifyDataSetChanged();
        initData(selectDate);
    }

    @Override
    public void initData(long data) {
        super.initData(data);
        //데이터를 넣어주는곳
        // 다른 곳에서는 recyclerview(메모)데이터를 여기서 넣어 줬음
        //adapter_memoLine = new adapter_memoList();
        if (data > 0){
            String full = new Convert_Date().Convert_Date(data);
            Log.d("whats fullday : ", full);
            memo_Click_List.clear();
            memo_Click_List = Database_Room.getInstance(this).getDao().getClickMemo(full);
            Adapter_memoList baseAdapter = new Adapter_memoList(memo_Click_List);
            binding.memoRecycler.setAdapter(baseAdapter);
            binding.memoRecycler.setLayoutManager(new LinearLayoutManager(this));
            baseAdapter.setOnItemClickListener((view, position) -> onItemClick(view, position));
            baseAdapter.notifyDataSetChanged();
        }
        selectDate = data;
        //CalendarView - setCurrentSelectedView 에서 memo_Click_List에 item을 넣어줘야함


    }


    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setDate){
            Toast.makeText(getApplicationContext(), "선택!",
                    Toast.LENGTH_SHORT).show();
            initselectedDay();
        }else if (item.getItemId() == R.id.setToday){
            Toast.makeText(getApplicationContext(), "Today!!",
                    Toast.LENGTH_SHORT).show();
            initControl();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initselectedDay(){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1993);
        calendar.set(Calendar.MONTH, Calendar.NOVEMBER);

        AdapterFrgCalendar adapter = new AdapterFrgCalendar(this, calendar);
        binding.pager.setAdapter(adapter);
        adapter.setOnFragmentListener(this);
        adapter.setNumOfMonth2(COUNT_PAGE);
        binding.pager.setCurrentItem(COUNT_PAGE);
        String title = adapter.getMonthDisplayed(COUNT_PAGE);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        getSupportActionBar().setTitle(fromHtml(("<font color=\"black\">" + title + "</font>")));

        binding.pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                String title = adapter.getMonthDisplayed(position);
                //mTvCalendarTitle.setText(title);
                getSupportActionBar().setTitle(fromHtml(("<font color=\"black\">" + title + "</font>")));

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

    public void initControl() {
        AdapterFrgCalendar adapter = new AdapterFrgCalendar(this);
        binding.pager.setAdapter(adapter);
        binding.fabButton.setOnClickListener(v -> feb_click(v));
        adapter.setOnFragmentListener(this);
        adapter.setNumOfMonth(COUNT_PAGE);
        binding.pager.setCurrentItem(COUNT_PAGE);
        String title = adapter.getMonthDisplayed(COUNT_PAGE);
        //mTvCalendarTitle.setText(title);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        getSupportActionBar().setTitle(fromHtml(("<font color=\"black\">" + title + "</font>")));

        binding.pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                String title = adapter.getMonthDisplayed(position);
                //mTvCalendarTitle.setText(title);
                getSupportActionBar().setTitle(fromHtml(("<font color=\"black\">" + title + "</font>")));

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

    private void setCalendarselected(){

    }

    @SuppressWarnings("deprecation")
    public Spanned fromHtml(String html){
        if(html == null){
            return new SpannableString("");
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
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
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()  {
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

    public void onItemClick(View view, int position) {
        Log.d("onItemClick" ,"Yes");
        Bundle bundle = new Bundle();
        bundle.putLong("date", selectDate);
        bundle.putInt("position", position);
        initIntent(MainActivity.this, bundle, Memo_Click_Activity.class);
    }

    public void feb_click(View view){
        Log.d("feb_click", "옴?");
        Bundle bundle = new Bundle();
        bundle.putInt("id", 0);
        bundle.putLong("select_Day", selectDate);
        //initIntent(MainActivity_2.this, bundle, writeActivity.class);
        initIntent(MainActivity.this, bundle, NoteAditActivity.class);

    }
}
