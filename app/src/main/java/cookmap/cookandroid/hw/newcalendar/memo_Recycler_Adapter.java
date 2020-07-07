package cookmap.cookandroid.hw.newcalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class memo_Recycler_Adapter extends RecyclerView.Adapter<memo_Recycler_Adapter.ViewHolder> {
    private ArrayList<memo_item> items = null;

    memo_Recycler_Adapter(ArrayList<memo_item> list) {
        items = list;
    }

    @NonNull
    @Override
    public memo_Recycler_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.memo_list_contents, parent, false);
        memo_Recycler_Adapter.ViewHolder vh = new memo_Recycler_Adapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull memo_Recycler_Adapter.ViewHolder holder, int position) {
        memo_item item = items.get(position);
        holder.c_title.setText(item.getTitle());
        holder.c_desc.setText(item.getDesc());
        if (item.getLabel().length() <= 0)
            holder.label.setBackgroundColor(Integer.parseInt("#000000" ));
        else
            holder.label.setBackgroundColor(Integer.parseInt(item.getLabel()));
        if (item.img_main.equals("none")) {
            holder.c_img_main.setVisibility(View.INVISIBLE);
        } else {
            // glide 라이브러리 써서 main_img 구현 하세요 holder
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
