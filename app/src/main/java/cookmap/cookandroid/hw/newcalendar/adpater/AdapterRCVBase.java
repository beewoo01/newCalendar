package cookmap.cookandroid.hw.newcalendar.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cookmap.cookandroid.hw.newcalendar.db.Content_Room;

public class AdapterRCVBase extends RecyclerView.Adapter {
    protected List<Content_Room> mList = new ArrayList();
    protected int idLayout = -1;
    protected RecyclerView.ViewHolder holder = null;
    //protected TT_Custom_ViewHolder holder = null;
    protected Context context;

    public Object getItem(int position) {
        if (mList == null || mList.size() < 1) {
            return null;
        }
        return mList.get(position);
    }

    public interface OnRCVItemListener {
        void onItemClick(View view, int position);
    }

    protected OnRCVItemListener onRCVItemListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return mList.size();

    }

    public void setList(List list) {
        if (list == null) {
            return;
        }
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnRCVItemListener onRCVItemListener) {
        this.onRCVItemListener = onRCVItemListener;
    }
}
