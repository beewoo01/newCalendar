package cookmap.cookandroid.hw.newcalendar.adpater;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

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


}
