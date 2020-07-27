package cookmap.cookandroid.hw.newcalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class writeActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView backbtn, checkbtn;
    EditText titleEdit, desEdit;
    TextView s_date, e_date, s_txt, e_txt;
    LinearLayout start_pick, end_pick;
    ImageButton fap;
    String stime, etime;
    String dbName = "con_file.db";
    int dbVersion = 1;
    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;
    String tag = "SQLite";
    String tableName = "contents";
    String des = "", m_img = "" ,img= "", label= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wite);

        findview();
        // findViewById 처리 함수

        backbtn.setOnClickListener(this);
        checkbtn.setOnClickListener(this);
        s_txt.setOnClickListener(this);
        e_txt.setOnClickListener(this);
        fap.setOnClickListener(this);
        start_pick.setOnClickListener(this);
        end_pick.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
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
        desEdit = findViewById(R.id.description_Edit);
        start_pick = findViewById(R.id.start_pick);
        end_pick = findViewById(R.id.end_pick);
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
            if (titleEdit.getText().toString().length() > 0 ) insertDb();
            else {
                Toast.makeText(writeActivity.this, "제목을 입력하여 주세요", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //제목, 입력되어있는지 확인 (제목만 작성 되어 있어도 괜찮음)
        else if (v.getId() == R.id.start_date || v.getId() == R.id.start_day_txt) return;
        else if (v.getId() == R.id.start_pick || v.getId() == R.id.end_pick){
            showDatePickerDialog(v);
        }
        // 날짜를 띄워 줘야함 달력형식으로
        else if (v.getId() == R.id.fab_button); return;
        // 사진 선택창 띄워줘야함
    }

    public void showDatePickerDialog(View view){
        //MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("날짜를 선택하세요");
        MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.show(getSupportFragmentManager(), "태그");


        /*DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");*/

    }

    private void insertDb(){
        helper = new SQLiteOpenHelper(this);
        String none = "NONE";


        try {
            db = helper.getWritableDatabase();
        }catch (SQLiteException e){
            e.printStackTrace();
            Log.d(tag, "데이터 베이스를 열수 없음");
            finish();
        }

        if (desEdit.getText().toString().trim().length() <= 0) des = none;
        else des = desEdit.getText().toString();
        if(img.trim().length() <= 0) img = none;
        if (m_img.trim().length() <= 0) m_img = none;
        if (m_img.trim().length() <= 0 || img.trim().length() > 0) m_img = img;

        ContentValues values = new ContentValues();
        values.put("title", titleEdit.getText().toString());
        values.put("description", des);
        values.put("main_Img", m_img);
        values.put("img", img);
        values.put("s_date", s_date.getText().toString());
        values.put("e_date", e_date.getText().toString());
        values.put("label", "#9500ff" );
        long result = db.insert(tableName, null, values);
        Log.d(tag, result + "번째 row insert 성공");

        db.close();
        helper.close();
        finish();
    }
}
