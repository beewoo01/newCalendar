package cookmap.cookandroid.hw.newcalendar.adpater;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cookmap.cookandroid.hw.newcalendar.Database.CNM;
import cookmap.cookandroid.hw.newcalendar.R;

public class Memo_Adapter extends RecyclerView.Adapter<Memo_Adapter.ViewHolder> {
    private List<CNM> items;
    private Context context;

    public Memo_Adapter(List<CNM> list) {
        items = list;
    }

    @NonNull
    @Override
    public Memo_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.memo_list_contents, parent, false);
        Memo_Adapter.ViewHolder vh = new Memo_Adapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Memo_Adapter.ViewHolder holder, int position) {
        CNM item = items.get(position);
        holder.c_title.setText(item.getContent_room().getTitle());
        holder.c_desc.setText(item.getContent_room().getDescription());
        holder.label.setBackgroundColor(Color.parseColor(item.getContent_room().getLabel()));

        if (item.getContent_room().getMain_Img().equals("NONE")) {
            //holder.c_img_main.setVisibility(View.INVISIBLE);
            holder.c_img_main.setBackgroundColor(Color.parseColor("#489201"));
        } else {
            Log.d("Recy_Adapter", "glide로 구현하세요");
            Glide.with(context).load(item.getContent_room().getMain_Img()).into(holder.c_img_main);
            // glide 라이브러리 써서 main_img 구현 하세요 holder
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private View label;
        private TextView c_title, c_desc;
        private ImageView c_img_main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.con_label);
            c_title = itemView.findViewById(R.id.con_title);
            c_desc = itemView.findViewById(R.id.con_desc);
            c_img_main = itemView.findViewById(R.id.con_img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
