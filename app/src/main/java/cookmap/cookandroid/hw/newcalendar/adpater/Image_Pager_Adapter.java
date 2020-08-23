package cookmap.cookandroid.hw.newcalendar.adpater;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cookmap.cookandroid.hw.newcalendar.R;

public class Image_Pager_Adapter extends RecyclerView.Adapter<Image_Pager_Adapter.ViewHolder> {
    private ArrayList<String> list;
    private Context context;

    Image_Pager_Adapter(ArrayList list, Context context){
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item_view,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("Viewpager_Img", list.get(position));
        Glide.with(context).load(list.get(position)).into(holder.carouselImageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView carouselImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carouselImageView = itemView.findViewById(R.id.carouselImageView);
        }
    }

}
