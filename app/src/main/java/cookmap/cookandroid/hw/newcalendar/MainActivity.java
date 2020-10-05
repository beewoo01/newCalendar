package cookmap.cookandroid.hw.newcalendar;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Calendar;
import java.util.List;

import cookmap.cookandroid.hw.newcalendar.Database.Database_Room;
import cookmap.cookandroid.hw.newcalendar.adpater.AdapterFrgCalendar;
import cookmap.cookandroid.hw.newcalendar.adpater.Adapter_memoList;
import cookmap.cookandroid.hw.newcalendar.databinding.ActivityMainBinding;
import cookmap.cookandroid.hw.newcalendar.db.Content_Room;

public class MainActivity extends BaseActivity implements FrgCalendar.OnFragmentListener {
    private static final int COUNT_PAGE = 12;

    private long selectDate;
    private ActivityMainBinding binding;
    private AdapterFrgCalendar adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initialize(Calendar.getInstance().getTimeInMillis());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData(selectDate);
    }

    @Override
    public void initData(long data) {
        super.initData(data);

        if (data > 0) {
            String full = new Convert_Date().Convert_Date(data);
            List<Content_Room> memo_Click_List = Database_Room.getInstance(this).getDao().getClickMemo(full);
            Adapter_memoList baseAdapter = new Adapter_memoList(memo_Click_List);
            binding.memoRecycler.setAdapter(baseAdapter);
            binding.memoRecycler.setLayoutManager(new LinearLayoutManager(this));
            baseAdapter.setOnItemClickListener(this::onItemClick);
            baseAdapter.notifyDataSetChanged();

            if (memo_Click_List.size() <= 0){
                binding.emptyContent.setVisibility(View.VISIBLE);
            }else{
                binding.emptyContent.setVisibility(View.GONE);

            }

        }
        selectDate = data;


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
        if (item.getItemId() == R.id.setDate) {
            showPicker();

        } else if (item.getItemId() == R.id.setToday) {
            initControl();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPicker(){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(selectDate);
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        //LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = LayoutInflater.from(this).inflate(R.layout.custom_datepicker, null);
        NumberPicker year = view.findViewById(R.id.year_picker);
        NumberPicker month = view.findViewById(R.id.month_picker);
        RelativeLayout cancle = view.findViewById(R.id.Cancel_picker);
        RelativeLayout confirm = view.findViewById(R.id.Confirm_picker);

        year.setWrapSelectorWheel(false);
        month.setWrapSelectorWheel(false);
        year.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        month.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        year.setMinValue(1900);
        month.setMinValue(1);
        year.setMaxValue(2100);
        month.setMaxValue(12);

        year.setValue(c.get(Calendar.YEAR));
        month.setValue(c.get(Calendar.MONTH)+1);

        cancle.setOnClickListener(v -> {
            dialog.dismiss();
            dialog.cancel();
        });

        confirm.setOnClickListener(v -> {
            c.set(Calendar.YEAR, year.getValue());
            c.set(Calendar.MONTH, month.getValue() - 1 );
            selectDate = c.getTimeInMillis();
            initAdapter(c);
            dialog.dismiss();
            dialog.cancel();
        });

        dialog.setView(view);
        dialog.show();
    }

    private void initAdapter(Calendar calendar) {


        adapter = new AdapterFrgCalendar(this, calendar);
        binding.pager.setAdapter(adapter);
        adapter.setOnFragmentListener(this);
        adapter.setNumOfMonth(COUNT_PAGE);
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
        Calendar calendar = Calendar.getInstance();
        binding.setActivity(this);
        initAdapter(calendar);
    }

    @SuppressWarnings("deprecation")
    public Spanned fromHtml(String html) {
        if (html == null) {
            return new SpannableString("");
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
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
        anim.addUpdateListener(animation -> {
            int val = (Integer) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParam = binding.pager.getLayoutParams();
            layoutParam.height = val;
            binding.pager.setLayoutParams(layoutParam);
        });
        anim.setDuration(200);
        anim.start();
    }

    public void onItemClick(View view, int position) {
        Log.d("onItemClick", "Yes");
        Bundle bundle = new Bundle();
        bundle.putLong("date", selectDate);
        bundle.putInt("position", position);
        initIntent(MainActivity.this, bundle, Memo_Click_Activity.class);
    }

    public void feb_click(View view) {
        Log.d("feb_click", "옴?");
        Bundle bundle = new Bundle();
        bundle.putInt("id", 0);
        bundle.putLong("select_Day", selectDate);
        initIntent(MainActivity.this, bundle, NoteAditActivity.class);

    }
}
