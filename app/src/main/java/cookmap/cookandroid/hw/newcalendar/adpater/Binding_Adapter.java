package cookmap.cookandroid.hw.newcalendar.adpater;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cookmap.cookandroid.hw.newcalendar.Convert_Date;
import cookmap.cookandroid.hw.newcalendar.R;

public class Binding_Adapter {

    @BindingAdapter({"imgUrl","cicle"})
    public static void setImg(ImageView view, String url, boolean isCicle) {
        if (!url.equals("NONE")) {
            if (isCicle) {
                Glide.with(view.getContext()).load(url).apply(new RequestOptions().circleCrop()).into(view);
            } else {
                Glide.with(view.getContext()).load(url).into(view);
            }
        }
    }

    @BindingAdapter("bgColor")
    public static void setBgcolor(View view, String color) {
        view.setBackgroundColor(Color.parseColor(color));
    }

    @BindingAdapter("lightColor")
    public static void setTbgColor(View view, String color){
        int co = R.color.light_Gray;
        switch (color){

            case "#FFF44336": co = R.color.lightRed;    break;
            case "#FF8BC34A": co = R.color.lightGreen;  break;
            case "#FFFFEB3B": co = R.color.lightYellow; break;
            case "#FF9C27B0": co = R.color.lightPurple; break;
            case "#FF03A9F4": co = R.color.lightblue;   break;
            case "#FF3F51B5": co = R.color.lightNavy;   break;
            case "#FF000000": co = R.color.lightFluorescent;    break;
            case "#FF4CAF50": co = R.color.light_DGreen;    break;
            case "#FFFF9800": co = R.color.light_Orange;    break;
        }

        view.setBackgroundColor(view.getContext().getColor(co));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @BindingAdapter({"day_text", "type"})
    public static void setTextforday(TextView view, String day, int type){
        long date = new Convert_Date().Convert_StringToLong(day);
        day = new Convert_Date().Convert_date_short(date, type);
        view.setText(day);
    }

}
