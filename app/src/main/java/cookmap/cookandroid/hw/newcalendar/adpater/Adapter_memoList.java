package cookmap.cookandroid.hw.newcalendar.adpater;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cookmap.cookandroid.hw.newcalendar.R;
import cookmap.cookandroid.hw.newcalendar.databinding.MemoListContentsBinding;
import cookmap.cookandroid.hw.newcalendar.db.Content_Room;
import cookmap.cookandroid.hw.newcalendar.utill.BaseViewHolder;

public class Adapter_memoList extends RecyclerView.Adapter<BaseViewHolder<MemoListContentsBinding>> {
    private List<Content_Room> items;
    protected BaseViewHolder.OnRCVItemListener itemListener = null;

    public void setOnItemClickListener(BaseViewHolder.OnRCVItemListener listener) {
        this.itemListener = listener;
    }

    public Adapter_memoList(List<Content_Room> items) {
        this.items = items;
    }

    @Override
    public BaseViewHolder<MemoListContentsBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new BaseViewHolder(inflater.inflate(R.layout.memo_list_contents, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<MemoListContentsBinding> holder, int position) {
        holder.binding().setActivity(items.get(position));
        holder.setOnItemClickListener(itemListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
