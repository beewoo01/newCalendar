package cookmap.cookandroid.hw.newcalendar.adpater;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cookmap.cookandroid.hw.newcalendar.R;
import cookmap.cookandroid.hw.newcalendar.databinding.LastImgItemBinding;
import cookmap.cookandroid.hw.newcalendar.utill.BaseViewHolder;

public class Horizontal_Adapter extends RecyclerView.Adapter<BaseViewHolder<LastImgItemBinding>> {

    private Context context;
    private ArrayList list;
    public int coverNum;
    protected BaseViewHolder.OnRCVItemListener listener;

    public void setOnItemClickListener(BaseViewHolder.OnRCVItemListener listener){
        this.listener = listener;
    }

    public Horizontal_Adapter(ArrayList list){
        this.list = list;
        coverNum = 0;
    }

    @NonNull
    @Override
    public BaseViewHolder<LastImgItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new BaseViewHolder(inflater.inflate(R.layout.last_img_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<LastImgItemBinding> holder, int position) {
        Glide.with(context).load(list.get(position)).into(holder.binding().lastImg);
        Log.d("posisisi", String.valueOf(position));
        if (coverNum == position) {
            holder.binding().lastImgBg.setBackgroundColor(Color.parseColor("#000000"));
        } else {
            holder.binding().lastImgBg.setBackgroundColor(0x00000000);
        }
        holder.binding().lastImg.setOnClickListener(v -> {
            holder.binding().lastImgBg.setBackgroundColor(Color.parseColor("#000000"));
            int prev = coverNum;
            coverNum = position;
            notifyItemChanged(prev);
            notifyItemChanged(coverNum);
        });
        holder.binding().lastCloseImg.setOnClickListener(v -> removeItem(position));
    }

    private void removeItem(int position) {
        if (coverNum == position && list.size()-1 > 0) coverNum = 0;
        else {if (coverNum > position) coverNum--;}
        list.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
