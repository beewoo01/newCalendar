package cookmap.cookandroid.hw.newcalendar.adpater;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cookmap.cookandroid.hw.newcalendar.Database.Content_Room;

public class AdapterRCVBase extends RecyclerView.Adapter {
    protected List<Content_Room> mList = new ArrayList();
    protected HashMap mMap = new HashMap();
    protected int idLayout = -1;
    protected RecyclerView.ViewHolder holder = null;

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
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

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
    public void setMap(HashMap map) {
        if (map == null) {
            return;
        }
        mMap.clear();
        mMap.putAll(map);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnRCVItemListener onRCVItemListener) {
        this.onRCVItemListener = onRCVItemListener;
    }
}
