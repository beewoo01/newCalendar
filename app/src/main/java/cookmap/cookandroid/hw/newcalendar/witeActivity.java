package cookmap.cookandroid.hw.newcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class witeActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView backbtn, checkbtn;
    EditText titleEdit, disEdit;
    TextView s_date, e_date, s_txt, e_txt;
    FloatingActionButton fap;
    String stime, etime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wite);

        findview();
        // findViewById 처리 함수
        backbtn.setOnClickListener(this);
        checkbtn.setOnClickListener(this);
        s_date.setOnClickListener(this);
        e_date.setOnClickListener(this);
        s_txt.setOnClickListener(this);
        e_txt.setOnClickListener(this);
        fap.setOnClickListener(this);

        //현재날짜 가져와 s_txt,e_txt에 띄워 줄거임
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sp = new SimpleDateFormat("yyyy/MM/dd");
        stime = sp.format(date);
        s_date.setText(stime);
        e_date.setText(stime);


    }

    public void findview(){
        backbtn = findViewById(R.id.back_btn);
        checkbtn = findViewById(R.id.check_btn);
        titleEdit = findViewById(R.id.title_Edit);
        disEdit = findViewById(R.id.discription_Edit);
        s_date = findViewById(R.id.start_date);
        e_date = findViewById(R.id.end_date);
        s_txt = findViewById(R.id.start_day_txt);
        e_txt= findViewById(R.id.end_day_txt);
        fap = findViewById(R.id.fab_button);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.back_btn) finish();
        else if (v.getId() == R.id.check_btn) {
            if (titleEdit.getText().toString().length() > 0 ) finish();
            else Toast.makeText(witeActivity.this, "제목을 입력하여 주세요", Toast.LENGTH_SHORT).show();
        }
        //제목, 입력되어있는지 확인 (제목만 작성 되어 있어도 괜찮음)
        else if (v.getId() == R.id.start_date || v.getId() == R.id.start_day_txt) return;
        else if (v.getId() == R.id.end_date || v.getId() == R.id.end_day_txt) return;
        // 날짜를 띄워 줘야함 달력형식으로
        else if (v.getId() == R.id.fab_button); return;
        // 사진 선택창 띄워줘야함
    }
}
