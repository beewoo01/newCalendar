package cookmap.cookandroid.hw.newcalendar.adpater;

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
import cookmap.cookandroid.hw.newcalendar.R;

public class Memo_Adapter extends RecyclerView.Adapter<Memo_Adapter.ViewHolder> {
    private List<Content_Room> items;
    private Context context;

    public Memo_Adapter(List<Content_Room> list) {
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
        Content_Room item = items.get(position);
        holder.c_title.setText(item.getTitle());
        holder.c_desc.setText(item.getDescription());
        holder.label.setBackgroundColor(Color.parseColor(item.getLabel()));

        if (item.getMain_Img().equals("NONE")) {
            holder.c_img_main.setVisibility(View.GONE);
        } else {
            Glide.with(context).load(item.getMain_Img()).into(holder.c_img_main);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View label;
        private TextView c_title, c_desc;
        private ImageView c_img_main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.con_label);
            c_title = itemView.findViewById(R.id.con_title);
            c_desc = itemView.findViewById(R.id.con_desc);
            c_img_main = itemView.findViewById(R.id.con_img);

        }
    }
}
