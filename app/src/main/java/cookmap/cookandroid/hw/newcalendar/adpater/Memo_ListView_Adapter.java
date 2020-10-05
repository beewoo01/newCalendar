package cookmap.cookandroid.hw.newcalendar.adpater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cookmap.cookandroid.hw.newcalendar.BR;
import cookmap.cookandroid.hw.newcalendar.Custom_Dialog;
import cookmap.cookandroid.hw.newcalendar.Database.Database_Room;
import cookmap.cookandroid.hw.newcalendar.NoteAditActivity;
import cookmap.cookandroid.hw.newcalendar.databinding.MemoFragItemBinding;
import cookmap.cookandroid.hw.newcalendar.databinding.MemoListNoimgBinding;
import cookmap.cookandroid.hw.newcalendar.db.Content_Room;
import cookmap.cookandroid.hw.newcalendar.R;
import cookmap.cookandroid.hw.newcalendar.db.Memo_Date;

public class Memo_ListView_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_NOIMG = 0;
    private List<Content_Room> list;
    private Context context;
    private Custom_Dialog builder;

    public Memo_ListView_Adapter(List<Content_Room> list) {
        this.list = list;
    }

    private Animation setAnima(){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.recyclerview_anim);
        animation.setStartOffset(-200);
        return animation;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == VIEW_ITEM) {
            MemoFragItemBinding binding = MemoFragItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);

            return new itemViewholder(binding);
        } else {
            MemoListNoimgBinding binding = MemoListNoimgBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);

            return new noImge(binding);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getMain_Img().equals("NONE") ? VIEW_NOIMG : VIEW_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setAnimation(setAnima());
        if (holder instanceof itemViewholder) {
            ((itemViewholder) holder).binding.setItem(this);
            ((itemViewholder) holder).binding.setActivity(list.get(position));
            try {
                int i = 0;
                JSONObject jsonObject = new JSONObject(list.get(position).getImg());
                ArrayList<String> arrayList = new ArrayList<>();
                while (i < 10) {
                    if (jsonObject.has(String.valueOf(i))) {
                        String key = jsonObject.getString(String.valueOf(i));
                        arrayList.add(key);
                        i++;
                    } else break;
                }
                if (i > 0) {
                    ((itemViewholder) holder).binding.viewpager.setAdapter(new Image_Pager_Adapter(arrayList, context));
                    new TabLayoutMediator(((itemViewholder) holder).binding.tabLayout,
                            ((itemViewholder) holder).binding.viewpager, (tab, position1) -> {
                    }).attach();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Memo_Date SNE = Database_Room.getInstance(context).getDao().getXN(list.get(position).getId());
            ((noImge) holder).binding.setItemContent(list.get(position));
            ((noImge) holder).binding.setVariable(BR.item_term, SNE);
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class itemViewholder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        MemoFragItemBinding binding;

        public itemViewholder(@NonNull MemoFragItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.moreBtnMfrag.setOnClickListener(v -> showDialog(1, getAdapterPosition()));
        }


        @Override
        public boolean onLongClick(View v) {
            showDialog(1, getAdapterPosition());
            return false;
        }
    }

    class noImge extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        MemoListNoimgBinding binding;

        public noImge(@NonNull MemoListNoimgBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            showDialog(1, getAdapterPosition());
            return false;
        }
    }


    public void showDialog(int param, int position) {
        Log.d("showDialog", String.valueOf(param));
        ArrayList arrayList = new ArrayList();
        if (param == 1) {
            arrayList.add("수정");
            arrayList.add("삭제");
        } else {
            arrayList.add("확인");
            arrayList.add("취소");
        }

        builder = new Custom_Dialog(context, value -> {
            if (value.equals("수정")) {
                Intent intent = new Intent(context, NoteAditActivity.class);
                intent.putExtra("id", list.get(position).getId());
                context.startActivity(intent);
            } else if (value.equals("삭제")) {
                showDialog(2, position);
            } else if (value.equals("확인")) {
                Database_Room.getInstance(context).getDao().content_delete(list.get(position).getId());
                Database_Room.getInstance(context).getDao().memo_delete(list.get(position).getId());
                ((Activity) context).finish();
            }
        }, arrayList, param);

        DisplayMetrics display = context.getResources().getDisplayMetrics();
        Window window = builder.getWindow();
        window.setLayout(Math.round(display.widthPixels * 0.8f),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        builder.getWindow().setWindowAnimations(R.style.DialogTheme);
        builder.show();
    }

}
