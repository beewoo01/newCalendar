package cookmap.cookandroid.hw.newcalendar;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity implements lInitializer{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initialize(long date) {
        initView();
        initControl();
        initData(date);

    }

    @Override
    public void initData(long data) {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initControl() {

    }
}
