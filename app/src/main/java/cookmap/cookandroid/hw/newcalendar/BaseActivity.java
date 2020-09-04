package cookmap.cookandroid.hw.newcalendar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.HashMap;

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

    @Override
    public void initIntent(Context context, Bundle bundle, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
