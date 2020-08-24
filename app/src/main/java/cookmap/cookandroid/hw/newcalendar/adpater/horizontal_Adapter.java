package cookmap.cookandroid.hw.newcalendar.adpater;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cookmap.cookandroid.hw.newcalendar.R;

public class horizontal_Adapter extends RecyclerView.Adapter<horizontal_Adapter.Viewholder> {
    private Context context;
    private ArrayList list;
    public int coverNum;

    public horizontal_Adapter(Context context, ArrayList list){
        this.context = context;
        this.list = list;
        coverNum = 0;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.last_img_item, parent, false);
        Viewholder viewHolder = new Viewholder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        Glide.with(context).load(list.get(position)).into(holder.picture);
        if (coverNum == position) {
            holder.background.setBackgroundColor(Color.parseColor("#000000"));
        } else {
            holder.background.setBackgroundColor(0x00000000);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        private ImageView picture, x_img;
        private RelativeLayout background, closeImg;

        public Viewholder(@NonNull View itemView) {
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

        private void removeItem(int position) {

            if (coverNum == position && list.size()-1 > 0) {
                coverNum = 0;
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemChanged(coverNum);

            } else {

                list.remove(position);
                if (coverNum > position){
                    coverNum--;
                    Log.d("coverNum!!!?", String.valueOf(coverNum));
                }
                notifyItemRemoved(position);
            }
        }
    }
}
