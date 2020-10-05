package cookmap.cookandroid.hw.newcalendar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import cookmap.cookandroid.hw.newcalendar.db.Memo_Date;
import cookmap.cookandroid.hw.newcalendar.db.Memo_Room;
import cookmap.cookandroid.hw.newcalendar.adpater.Horizontal_Adapter;
import cookmap.cookandroid.hw.newcalendar.databinding.ActivityPainttingBinding;
import cookmap.cookandroid.hw.newcalendar.db.Content_Room;

public class NoteAditActivity extends BaseActivity implements View.OnClickListener, Gallery_Dialog.PassDataInterface, Label_dialog.passColor{

    ActivityPainttingBinding binding;

    private String m_img = "", img = "", label = "";
    private String none = "NONE";
    private SimpleDateFormat sp;
    private Pair<Long, Long> pair;
    int id = 0;

    private Horizontal_Adapter horizontal_adapter;

    private ArrayList<String> imgAddress;
    private InputMethodManager im;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_paintting);
        initView();
        initControl();

        im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        mLayoutManager.setReverseLayout(true);
        binding.setImgRecycler.setLayoutManager(mLayoutManager);

    }

    @Override
    public void initView() {
        super.initView();
        binding.backBtn.setOnClickListener(this);
        binding.checkBtn.setOnClickListener(this);
        binding.selectDate.setOnClickListener(this);
        binding.fabButton.setOnClickListener(this);
        binding.descriptionLinear.setOnClickListener(this);
        binding.setLabelColor.setOnClickListener(this);

    }

    @Override
    public void initControl() {
        super.initControl();
        sp = new SimpleDateFormat("yyyy/MM/dd");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        imgAddress = new ArrayList<>(10);
        id = bundle.getInt("id");

        if (id > 0) {
            try {
                Modify(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String setDay = new Convert_Date().Convert_Date(bundle.getLong("select_Day"));
            long selectedDay = bundle.getLong("select_Day");
            pair = Pair.create(selectedDay, selectedDay);
            binding.startDate.setText(setDay);
            binding.endDate.setText(setDay);
            label = "#" + Integer.toHexString(ContextCompat.getColor(this, R.color.colorGray));
            binding.setLabelColor.setBackgroundColor(Color.parseColor(label));

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
                binding.startDate.setText(spd.format(new Date(selection.first + offsetFromUTC)));
                binding.endDate.setText(spd.format(new Date(selection.second + offsetFromUTC)));

            }
        });

    }

    private void Modify(int id) throws Exception {
        //수정

        Memo_Date SNE = Database_Room.getInstance(this).getDao().getXN(id);
        Content_Room thisItem = Database_Room.getInstance(this).getDao().getOneItem(id);

        binding.startDate.setText(SNE.getStart_day());
        binding.endDate.setText(SNE.getEnd_day());
        binding.titleEdit.setText(thisItem.getTitle());
        binding.descriptionEdit.setText(thisItem.getDescription());

        pair = Pair.create(sp.parse(SNE.getStart_day()).getTime(), sp.parse(SNE.getEnd_day()).getTime());
        label = thisItem.getLabel();
        binding.setLabelColor.setBackgroundColor(Color.parseColor(label));

        int i = 0;
        JSONObject jsonObject = new JSONObject(thisItem.getImg());
        while (i < 10) {
            if (jsonObject.has(String.valueOf(i))) {
                String key = jsonObject.getString(String.valueOf(i));
                imgAddress.add(key);
                i++;
            } else break;
        }
        horizontal_adapter = new Horizontal_Adapter(imgAddress);
        binding.setImgRecycler.setAdapter(horizontal_adapter);
        horizontal_adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v == binding.backBtn){
            endAct();
        }else if (v == binding.checkBtn){
            //check data
            if (binding.titleEdit.getText().toString().length() > 0) {
                try {
                    getImage();
                } catch (JSONException e) {
                    e.printStackTrace();
                    m_img = none;
                    img = none;
                    Toast.makeText(this, "이미지 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
                insertDb((id > 0)? true : false);
            } else {
                Toast.makeText(getApplicationContext(), "제목을 입력하여 주세요", Toast.LENGTH_SHORT).show();
                return;
            }
        }else if (v == binding.selectDate){
            showDatePickerDialog();
        }else if (v == binding.fabButton){
            checkSelfPermission();
            return;

        }else if (v == binding.descriptionLinear){
            binding.descriptionEdit.requestFocus(); // 내용 부분

            im.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        }else if (v == binding.setLabelColor){
            DialogFragment lableDF = new Label_dialog(this);
            lableDF.show(getSupportFragmentManager(), "Label_Dialog");
        }

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
        String des;
        if (binding.descriptionEdit.getText().toString().trim().length() <= 0) des = none;
        else des = binding.descriptionEdit.getText().toString();

        if (po){
            Database_Room.getInstance(this).getDao().content_update(binding.titleEdit.getText().toString(), des, m_img, img, label, id);
        }else {
            id = Math.toIntExact(Database_Room.getInstance(this).getDao().content_insert(new Content_Room(
                    binding.titleEdit.getText().toString(), des, m_img, img, label)));
        }


        try {

            Calendar start_Cal = new GregorianCalendar();
            Calendar end_Cal = new GregorianCalendar();

            Date start_date = sp.parse(binding.startDate.getText().toString());
            Date end_date = sp.parse(binding.endDate.getText().toString());

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


        endAct();
    }

    private void endAct(){
        im.hideSoftInputFromWindow(binding.titleEdit.getWindowToken(), 0);
        im.hideSoftInputFromWindow(binding.descriptionEdit.getWindowToken(), 0);
        finish();
    }

    @Override
    public void onDataReceived(ArrayList<String> imgAddress) {
        if (!imgAddress.isEmpty()) {
            for (int i = 0; i < imgAddress.size(); i++) {
                Log.d("for", String.valueOf(imgAddress.get(i)));
                this.imgAddress.add(String.valueOf(imgAddress.get(i)));
            }

            horizontal_adapter = new Horizontal_Adapter(this.imgAddress);
            binding.setImgRecycler.setAdapter(horizontal_adapter);
            horizontal_adapter.notifyDataSetChanged();


        }
    }

    @Override
    public void getColor(String color) {
        label = color;
        binding.setLabelColor.setBackgroundColor(Color.parseColor(label));
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
}
