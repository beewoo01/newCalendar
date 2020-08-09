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
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.TimeZone;

public class writeActivity extends AppCompatActivity implements View.OnClickListener, test_Gallery_Dialog.PassDataInterface{

    private ImageView backbtn;
    private EditText titleEdit, desEdit;
    private TextView s_date, e_date, s_txt, e_txt, checkbtn;
    private RelativeLayout select_date, fap;
    private LinearLayout descr_Linear;
    private RecyclerView img_recycle;
    //private int dbVersion = 2;
    //private SQLiteOpenHelper helper;
    //private SQLiteDatabase db;
    private String startDate, endDate;
    private String tag = "SQLite";
    private String tableName = "contents";
    private String des = "", m_img = "" ,img= "", label= "";
    private String none = "NONE";

    private horizontal_Adapter horizontal_adapter;

    private ArrayList<String> imgAddress;

    String TAG = "writeAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wite);

        init();
        imgAddress = new ArrayList<>(10);
        LinearLayoutManager mLayoutManager= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        //mLayoutManager.setReverseLayout(true);
        //mLayoutManager.setStackFromEnd(true);
        img_recycle.setLayoutManager(mLayoutManager);

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
        descr_Linear = findViewById(R.id.description_Linear);
        img_recycle = findViewById(R.id.setImg_recycler);

        descr_Linear.setOnClickListener(this);
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
            if (titleEdit.getText().toString().length() > 0 ) {
                try { getImage(); }
                catch (JSONException e) { e.printStackTrace();
                Toast.makeText(this, "이미지 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show(); }
                insertDb();
            }
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
            return;
        }
        // 사진 선택창 띄워줘야함
        else if (v.getId() == R.id.description_Linear) {
            desEdit.requestFocus(); // 내용 부분
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
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
            //권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);

        }else {
            // 모두 허용 상태
            //Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();

            DialogFragment dialogFragment = new test_Gallery_Dialog(this);
            dialogFragment.show(getSupportFragmentManager(), "Gallery_Dialog");
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

    private void getImage() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if (imgAddress.size() > 0){
            int jsonkey = 1;
            for (int i = 0; i < imgAddress.size(); i++){

                if (horizontal_adapter.coverNum == i){
                    m_img = imgAddress.get(i);
                    jsonObject.put(String.valueOf(0), m_img);
                }else {
                    jsonObject.put(String.valueOf(jsonkey), imgAddress.get(i));
                    jsonkey++;
                }

            }
            /*for (int i = 0; i < imgAddress.size(); i++){
                String key = (String) jsonObject.getString(String.valueOf(i));
                Log.d("Json??", key);
            }*/ // SELECT시 이런식으로 가져오면 됨!!
            img = jsonObject.toString();
            Log.d("Jsonstring??", img);

        }else {
            m_img = none;
            img = none;
        }

    }
    private void insertDb(){
        //helper = new SQLiteOpenHelper(this);




        /*try {
            db = openOrCreateDatabase("con_file.db", Context.MODE_PRIVATE, null);
            //db = helper.getWritableDatabase();
        }catch (SQLiteException e){
            e.printStackTrace();
            Log.d(tag, "데이터 베이스를 열수 없음");
            Toast.makeText(this,"저장에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }*/

        if (desEdit.getText().toString().trim().length() <= 0) des = none;
        else des = desEdit.getText().toString();


        /*ContentValues values = new ContentValues();
        values.put("title", titleEdit.getText().toString());
        values.put("description", des);
        values.put("main_Img", m_img);

        values.put("img", img);
        values.put("s_date", s_date.getText().toString());
        values.put("e_date", e_date.getText().toString());
        values.put("label", "#9500ff" );*/
        ContentDatabase_Room.getInstance(this).getContentDao().insert(new Content_Room(
                titleEdit.getText().toString(),
                des,
                m_img,
                img,
                s_date.getText().toString(),
                e_date.getText().toString(),
                "#9500ff"
        ));

        /*long result = db.insert(tableName, null, values);
        Log.d(tag, result + "번째 row insert 성공");
        db.close();
        helper.close();*/
        finish();
    }

    @Override
    public void onDataReceived(ArrayList imgAddress) {
        Log.d("onDataReceived", "왓네");
        Log.d("onDataReceived", String.valueOf(imgAddress.size()));

        if (!imgAddress.isEmpty()){
            Log.d("onDataReceived", "if");
            for (int i = 0; i < imgAddress.size(); i++){
                Log.d("for", String.valueOf(imgAddress.get(i)));
                this.imgAddress.add(String.valueOf(imgAddress.get(i)));
            }
            Collections.reverse(this.imgAddress);

            horizontal_adapter = new horizontal_Adapter(this, this.imgAddress);
            img_recycle.setAdapter(horizontal_adapter);
            horizontal_adapter.notifyDataSetChanged();


        }else {

            Log.d("onDataReceived", "else");
        }

    }

    class horizontal_Adapter extends RecyclerView.Adapter<horizontal_Adapter.CustomViewHolder>{
        private Context context;
        private ArrayList list;
        private int coverNum;

        public horizontal_Adapter(Context context, ArrayList list){
            this.context = context;
            this.list = list;
            coverNum = list.size()-1;
        }

        @NonNull
        @Override
        public horizontal_Adapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.last_img_item, parent, false);
            CustomViewHolder viewHolder = new CustomViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            Glide.with(context).load(list.get(position)).into(holder.picture);
            if (coverNum == position){
                holder.background.setBackgroundColor(Color.parseColor("#000000"));
            }else {
                holder.background.setBackgroundColor(0x00000000);
            }

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            private ImageView picture, x_img;
            private RelativeLayout background ,closeImg;

            private CustomViewHolder(View itemView){
                super(itemView);
                this.picture = itemView.findViewById(R.id.last_img);
                this.background = itemView.findViewById(R.id.last_img_bg);
                this.closeImg = itemView.findViewById(R.id.last_close_img);
                this.x_img = itemView.findViewById(R.id.last_x_img);

                x_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeItem(getAdapterPosition());
                    }
                });

                closeImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 삭제
                        removeItem(getAdapterPosition());
                    }
                });

                picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        background.setBackgroundColor(Color.parseColor("#000000"));
                        Log.d("picpostion: ", String.valueOf(getAdapterPosition()));
                        //notifyDataSetChanged();
                        coverNum = getAdapterPosition();
                        notifyDataSetChanged();
                    }
                });

            }

            private void removeItem(int position){
                list.remove(position);
                if (coverNum == position && list.size() > 0){
                    coverNum = list.size()-1;
                    notifyItemRemoved(position);
                    notifyItemChanged(coverNum);
                }else if (list.size() <= 0){
                    coverNum = -1;
                    notifyItemRemoved(position);
                }else {
                    notifyItemRemoved(position);
                }
            }
        }
    }
}
