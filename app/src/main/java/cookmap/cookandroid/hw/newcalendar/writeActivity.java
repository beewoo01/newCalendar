package cookmap.cookandroid.hw.newcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
        else if (v.getId() == R.id.fab_button){
            checkSelfPermission();
            Log.d(TAG,"Fab_Button");
            /*Fragment fragment = new GalleryDialog();
            Bundle bundle = new Bundle();
            getSupportFragmentManager().findFragmentById(R.id.gallery_frag);*/

            //galleryDialog.callFunction();
            /*Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);*/
            return;
        }
        // 사진 선택창 띄워줘야함
    }

    public void checkSelfPermission() {
        String temp = "";

        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";

        }

        if (TextUtils.isEmpty(temp) == false) {
            //권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);

        }else {
            // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
            GalleryDialog galleryDialog = new GalleryDialog();
            galleryDialog.show(getSupportFragmentManager(), "tag");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"PermissionsResult");
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    GalleryDialog galleryDialog = new GalleryDialog();
                    galleryDialog.show(getSupportFragmentManager(), "tag");
                } else {
                    Toast.makeText(this, "권한이 없어 접근할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
                s_date.setText(startDate);
                e_date.setText(endDate);

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
