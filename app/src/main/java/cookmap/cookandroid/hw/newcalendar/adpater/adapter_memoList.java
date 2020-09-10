package cookmap.cookandroid.hw.newcalendar.adpater;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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

public class adapter_memoList extends AdapterRCVBase {

    private List<Content_Room> items;
    private Context context;

    public adapter_memoList(int idLayout) {
        super();
        this.idLayout = idLayout;
    }

    public adapter_memoList() {
    }

    public adapter_memoList(List<Content_Room> list) {
        items = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View child = View.inflate(parent.getContext(), R.layout.memo_list_contents, null);
        holder = new adapter_memoList.ViewHolder(child);
        return super.onCreateViewHolder(parent, viewType);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((adapter_memoList.ViewHolder) holder).c_title.setText(mList.get(position).getTitle());
        ((adapter_memoList.ViewHolder) holder).c_desc.setText(mList.get(position).getDescription());
        ((adapter_memoList.ViewHolder) holder).label.setBackgroundColor(Color.parseColor(mList.get(position).getLabel()));
        if (mList.get(position).getMain_Img().equals("NONE")) {
            ((adapter_memoList.ViewHolder) holder).c_img_main.setVisibility(View.GONE);
        } else {
            Glide.with(super.context).load(mList.get(position).getMain_Img()).into(((adapter_memoList.ViewHolder) holder).c_img_main);
        }

        super.onBindViewHolder(holder, position);
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
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    if (onRCVItemListener != null){
                        onRCVItemListener.onItemClick(v, position);
                        Log.d("ViewHolder ", "onRCVItemListener != null");
                    }else {
                        Log.d("ViewHolder ", "onRCVItemListener ===== null");
                    }

                }
            });



        }
    }
}
