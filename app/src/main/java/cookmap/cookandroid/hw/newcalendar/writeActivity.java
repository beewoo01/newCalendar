package cookmap.cookandroid.hw.newcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import cookmap.cookandroid.hw.newcalendar.Database.Database_Room;
import cookmap.cookandroid.hw.newcalendar.Database.Content_Room;
import cookmap.cookandroid.hw.newcalendar.Database.Memo_Room;
import cookmap.cookandroid.hw.newcalendar.Database.Memo_Date;
import cookmap.cookandroid.hw.newcalendar.adpater.horizontal_Adapter;

public class writeActivity extends AppCompatActivity implements View.OnClickListener, Gallery_Dialog.PassDataInterface, label_dialog.passColor {

    private EditText titleEdit, desEdit;
    private TextView s_date, e_date;
    private RecyclerView img_recycle;
    private String startDate, endDate;
    private String des = "", m_img = "", img = "", label = "";
    private String none = "NONE";
    private SimpleDateFormat sp;
    private Date s_day, e_day;
    private Pair<Long, Long> pair;
    private long now;
    int id = 0;

    private horizontal_Adapter horizontal_adapter;

    private ArrayList<String> imgAddress;

    String TAG = "writeAct";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wite);

        init();
        imgAddress = new ArrayList<>(10);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        mLayoutManager.setReverseLayout(true);
        img_recycle.setLayoutManager(mLayoutManager);
    }

    private void Modify(int id) throws Exception {

        Memo_Date SNE = Database_Room.getInstance(this).getDao().getXN(id);
        Content_Room thisItem = Database_Room.getInstance(this).getDao().getOneItem(id);

        s_date.setText(SNE.getStart_day());
        e_date.setText(SNE.getEnd_day());
        startDate = SNE.getStart_day();
        endDate = SNE.getEnd_day();
        titleEdit.setText(thisItem.getTitle());
        desEdit.setText(thisItem.getDescription());

        s_day = sp.parse(SNE.getStart_day());
        e_day = sp.parse(SNE.getEnd_day());
        long S_time = s_day.getTime();
        long E_time = e_day.getTime();
        pair = Pair.create(S_time, E_time);
        label = thisItem.getLabel();
        findViewById(R.id.setLabelColor).setBackgroundColor(Color.parseColor(thisItem.getLabel()));

        int i = 0;
        JSONObject jsonObject = new JSONObject(thisItem.getImg());
        while (i < 10) {
            if (jsonObject.has(String.valueOf(i))) {
                String key = jsonObject.getString(String.valueOf(i));
                imgAddress.add(key);
                i++;
            } else break;
        }
        horizontal_adapter = new horizontal_Adapter(this, imgAddress);
        img_recycle.setAdapter(horizontal_adapter);
        horizontal_adapter.notifyDataSetChanged();
    }

    private void init() {

        titleEdit = findViewById(R.id.title_Edit);
        desEdit = findViewById(R.id.description_Edit);
        s_date = findViewById(R.id.start_date);
        e_date = findViewById(R.id.end_date);
        img_recycle = findViewById(R.id.setImg_recycler);

        findViewById(R.id.description_Linear).setOnClickListener(this);
        findViewById(R.id.check_btn).setOnClickListener(this);
        findViewById(R.id.back_btn).setOnClickListener(this);
        findViewById(R.id.fab_button).setOnClickListener(this);
        findViewById(R.id.select_Date).setOnClickListener(this);
        findViewById(R.id.setLabelColor).setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //현재날짜 가져와 s_txt,e_txt에 띄워 줄거임
        now = System.currentTimeMillis();
        Date date = new Date(now);
        sp = new SimpleDateFormat("yyyy/MM/dd");
        id = getIntent().getIntExtra("id", 0);

        if (id > 0) {
            try {
                Modify(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            startDate = sp.format(date);
            endDate = startDate;
            s_date.setText(startDate);
            e_date.setText(endDate);
            pair = Pair.create(now, now);
            label = "#BCC7C7C7";
            findViewById(R.id.setLabelColor).setBackgroundColor(Color.parseColor(label));
        }


    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.back_btn) finish();
        else if (v.getId() == R.id.check_btn) {
            if (titleEdit.getText().toString().length() > 0) {
                try {
                    getImage();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "이미지 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
                insertDb((id > 0)? true : false);
            } else {
                Toast.makeText(writeActivity.this, "제목을 입력하여 주세요", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //제목, 입력되어있는지 확인 (제목만 작성 되어 있어도 괜찮음)
        else if (v.getId() == R.id.select_Date) showDatePickerDialog();
            // 달력으로
        else if (v.getId() == R.id.fab_button) {
            checkSelfPermission();
            return;
        }
        // 사진 선택창 띄워줘야함
        else if (v.getId() == R.id.description_Linear) {
            desEdit.requestFocus(); // 내용 부분
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
        else if (v.getId() == R.id.setLabelColor) {
            DialogFragment lableDF = new label_dialog(this);
            lableDF.show(getSupportFragmentManager(), "Label_Dialog");
        }

    }

    public void checkSelfPermission() {
        String temp = "";

        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";

        }

        if (TextUtils.isEmpty(temp) == false) {
            ActivityCompat.requestPermissions(this, temp.trim().split(" "), 1);

        } else {

            DialogFragment dialogFragment = new Gallery_Dialog(this);
            dialogFragment.show(getSupportFragmentManager(), "Gallery_Dialog");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    DialogFragment dialogFragment = new Gallery_Dialog(this);
                    dialogFragment.show(getSupportFragmentManager(), "Gallery_Dialog");
                } else {
                    Toast.makeText(this, "권한이 없어 접근할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void showDatePickerDialog() {

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("날짜를 선택하세요");
        builder.setSelection(pair);
        MaterialDatePicker materialDatePicker = builder.build();

        materialDatePicker.show(getSupportFragmentManager(), "태그");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                TimeZone timeZone = TimeZone.getDefault();
                int offsetFromUTC = timeZone.getOffset(new Date().getTime()) * -1;
                SimpleDateFormat spd = new SimpleDateFormat("yyyy/MM/dd");
                startDate = spd.format(new Date(selection.first + offsetFromUTC));
                endDate = spd.format(new Date(selection.second + offsetFromUTC));
                Log.d(TAG, "start: " + startDate);
                Log.d(TAG, "end: " + endDate);
                s_date.setText(startDate);
                e_date.setText(endDate);

            }
        });

    }

    private void getImage() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if (imgAddress.size() > 0) {
            int jsonkey = 1;
            for (int i = 0; i < imgAddress.size(); i++) {

                if (horizontal_adapter.coverNum == i) {
                    m_img = imgAddress.get(i);
                    jsonObject.put(String.valueOf(0), m_img);
                } else {
                    jsonObject.put(String.valueOf(jsonkey), imgAddress.get(i));
                    jsonkey++;
                }

            }
            img = jsonObject.toString();

        } else {
            m_img = none;
            img = none;
        }

    }


    private void insertDb(boolean po) {

        if (desEdit.getText().toString().trim().length() <= 0) des = none;
        else des = desEdit.getText().toString();

        if (po){
            Database_Room.getInstance(this).getDao().content_update(titleEdit.getText().toString(), des, m_img, img, label, id);
        }else {
            id = Math.toIntExact(Database_Room.getInstance(this).getDao().content_insert(new Content_Room(
                    titleEdit.getText().toString(), des, m_img, img, label)));
        }


        try {

            Calendar start_Cal = new GregorianCalendar();
            Calendar end_Cal = new GregorianCalendar();

            Date start_date = sp.parse(startDate);
            Date end_date = sp.parse(endDate);

            start_Cal.setTime(start_date);
            end_Cal.setTime(end_date);

            long diffSec = (end_Cal.getTimeInMillis() - start_Cal.getTimeInMillis()) / 1000;
            long diffDays = diffSec / (24 * 60 * 60);
            Calendar calendar = Calendar.getInstance();
            if (po) Database_Room.getInstance(this).getDao().memo_delete(id);
            for (int i = 0; i <= diffDays; i++) {
                calendar.setTime(start_date);
                calendar.add(Calendar.DATE, i);
                Database_Room.getInstance(this).getDao().memo_Insert(
                        new Memo_Room(Integer.parseInt(String.valueOf(id)), sp.format(calendar.getTime())));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        finish();
    }

    @Override
    public void onDataReceived(ArrayList imgAddress) {

        if (!imgAddress.isEmpty()) {
            for (int i = 0; i < imgAddress.size(); i++) {
                Log.d("for", String.valueOf(imgAddress.get(i)));
                this.imgAddress.add(String.valueOf(imgAddress.get(i)));
            }
            //Collections.reverse(this.imgAddress);

            horizontal_adapter = new horizontal_Adapter(this, this.imgAddress);
            img_recycle.setAdapter(horizontal_adapter);
            horizontal_adapter.notifyDataSetChanged();


        }

    }

    @Override
    public void getColor(String color) {
        label = color;
        findViewById(R.id.setLabelColor).setBackgroundColor(Color.parseColor(label));
    }
}
