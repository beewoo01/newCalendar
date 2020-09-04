package cookmap.cookandroid.hw.newcalendar;

import android.content.Context;
import android.os.Bundle;

import java.util.HashMap;

public interface lInitializer {
    void initData(long date);
    void initView();
    void initControl();
    void initIntent(Context context, Bundle prm, Class<?> cls);
}
