package cookmap.cookandroid.hw.newcalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import cookmap.cookandroid.hw.newcalendar.Database.Content_Room;
import cookmap.cookandroid.hw.newcalendar.Database.Database_Room;
import cookmap.cookandroid.hw.newcalendar.adpater.Memo_List_Adapter;

public class Memo_Click_Activity extends AppCompatActivity {
    private List<Content_Room> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_click);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        int position = bundle.getInt("position");
        String date = bundle.getString("date");

        list = Database_Room.getInstance(this).getDao().getClickMemo(date);

        View backBtn = findViewById(R.id.back_in_mfrag);
        RecyclerView recyclerView = findViewById(R.id.memo_recycler_mfrag);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new Memo_List_Adapter(this, list));



        scrollFunction(linearLayoutManager, position);

        backBtn.setOnClickListener(onClickListener);
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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // backBtn = 종료
            finish();
        }
    };
}
