package cookmap.cookandroid.hw.newcalendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cookmap.cookandroid.hw.newcalendar.Database.Content_Room;

public class Memo_List_Adapter extends RecyclerView.Adapter<Memo_List_Adapter.ViewHolder> {
    private Context context;
    private List<Content_Room> list;

    public Memo_List_Adapter(Context context, List<Content_Room> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.memo_frag_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.label_mfrag.setBackgroundColor(Color.parseColor(list.get(position).getLabel()));
        holder.title_mfrag.setText(list.get(position).getTitle());

        if (!list.get(position).getDescription().equals("none")){
            holder.sub_txt_mfrag.setText(list.get(position).getDescription());
        }
        if (!list.get(position).getMain_Img().equals("none")){
            //glide
            Glide.with(context).load(list.get(position).getMain_Img()).into(holder.coverImg_mfrg);
        }else{
            holder.coverImg_mfrg.setBackgroundColor(Color.parseColor(list.get(position).getLabel()));
        }
        if (!list.get(position).getImg().equals("none")){
            //임시임 Json 형태일껀데 풀어줘야함
            Glide.with(context).load(list.get(position).getMain_Img()).into(holder.coverImg_mfrg);
            /*for (int i = 0; i < imgAddress.size(); i++){
                String key = (String) jsonObject.getString(String.valueOf(i));
                Log.d("Json??", key);
            }*/
            //json and glide
        }else holder.imgs_mfrag.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View label_mfrag;
        private ImageView coverImg_mfrg,imgs_mfrag, moreBtn_mfrag ;
        private TextView title_mfrag, sub_txt_mfrag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            label_mfrag = itemView.findViewById(R.id.label_mfrag);
            coverImg_mfrg = itemView.findViewById(R.id.coverImg_mfrg);
            imgs_mfrag = itemView.findViewById(R.id.imgs_mfrag);
            moreBtn_mfrag = itemView.findViewById(R.id.moreBtn_mfrag);
            title_mfrag = itemView.findViewById(R.id.title_mfrag);
            sub_txt_mfrag = itemView.findViewById(R.id.sub_txt_mfrag);

            moreBtn_mfrag.setOnClickListener(onClickListener);

        }
        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //수정 삭제 버튼 화면 중앙에
            }
        };
    }
}
