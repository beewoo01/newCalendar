package cookmap.cookandroid.hw.newcalendar;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import cookmap.cookandroid.hw.newcalendar.Database.Database_Room;
import cookmap.cookandroid.hw.newcalendar.adpater.Memo_ListView_Adapter;
import cookmap.cookandroid.hw.newcalendar.databinding.ActivityMemoClickBinding;
import cookmap.cookandroid.hw.newcalendar.db.Content_Room;
import cookmap.cookandroid.hw.newcalendar.widget.CalendarView;

public class Memo_Click_Activity extends BaseActivity {
    private List<Content_Room> list;
    private int position;
    private ActivityMemoClickBinding binding;

    private long date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_memo_click);
        binding.setActivity(this);
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position");
        date = bundle.getLong("date");
        //initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();

    }

    @Override
    public void initView() {
        super.initView();


        String full_day = new Convert_Date().Convert_Date(date);
        String short_day = new Convert_Date().Convert_date_short(date, 0);
        binding.selectedDay.setText(short_day);
        list = Database_Room.getInstance(this).getDao().getClickMemo(full_day);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        binding.memoRecyclerMfrag.setLayoutManager(linearLayoutManager);
        binding.memoRecyclerMfrag.setAdapter(new Memo_ListView_Adapter(list));

        scrollFunction(linearLayoutManager, position);
    }

    private void scrollFunction(LinearLayoutManager linearLayoutManager, int position){
        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(this){
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        smoothScroller.setTargetPosition(position);
        linearLayoutManager.startSmoothScroll(smoothScroller);
    }

    public void onclick(View view){
        finish();
    }
}
