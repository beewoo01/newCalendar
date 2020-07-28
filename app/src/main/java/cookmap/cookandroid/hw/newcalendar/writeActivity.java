package cookmap.cookandroid.hw.newcalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class writeActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView backbtn;
    EditText titleEdit, desEdit;
    TextView s_date, e_date, s_txt, e_txt, checkbtn;
    RelativeLayout select_date, fap;
    String dbName = "con_file.db";
    int dbVersion = 1;
    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;
    String startDate, endDate;
    String tag = "SQLite";
    String tableName = "contents";
    String des = "", m_img = "" ,img= "", label= "";

    String TAG = "writeAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wite);

        init();
        // findViewById 처리 함수




    }

    public void init(){
        backbtn = findViewById(R.id.back_btn);
        checkbtn = findViewById(R.id.check_btn);
        titleEdit = findViewById(R.id.title_Edit);
        desEdit = findViewById(R.id.description_Edit);
        select_date = findViewById(R.id.select_Date);
        s_date = findViewById(R.id.start_date);
        e_date = findViewById(R.id.end_date);
        fap = findViewById(R.id.fab_button);

        backbtn.setOnClickListener(this);
        checkbtn.setOnClickListener(this);
        fap.setOnClickListener(this);
        select_date.setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //현재날짜 가져와 s_txt,e_txt에 띄워 줄거임
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sp = new SimpleDateFormat("yyyy/MM/dd");
        startDate = sp.format(date);
        endDate = startDate;
        s_date.setText(startDate);
        e_date.setText(endDate);


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
        else if (v.getId() == R.id.select_Date ) showDatePickerDialog();
        // 달력으로
        else if (v.getId() == R.id.fab_button); return;
        // 사진 선택창 띄워줘야함
    }

    public void showDatePickerDialog(){
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("날짜를 선택하세요");
        final MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.show(getSupportFragmentManager(), "태그");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                TimeZone timeZone = TimeZone.getDefault();
                int offsetFromUTC = timeZone.getOffset(new Date().getTime()) * -1;
                SimpleDateFormat spd = new SimpleDateFormat("yyyy/MM/dd");
                startDate = spd.format(new Date(selection.first + offsetFromUTC));
                endDate = spd.format(new Date(selection.second + offsetFromUTC));
                Log.d(TAG, "start: "+ startDate);
                Log.d(TAG, "end: " + endDate);
            }
        });

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
