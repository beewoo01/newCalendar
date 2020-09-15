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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cookmap.cookandroid.hw.newcalendar.Custom_Dialog;
import cookmap.cookandroid.hw.newcalendar.Database.Database_Room;
import cookmap.cookandroid.hw.newcalendar.NoteAditActivity;
import cookmap.cookandroid.hw.newcalendar.databinding.MemoFragItemBinding;
import cookmap.cookandroid.hw.newcalendar.db.Content_Room;
import cookmap.cookandroid.hw.newcalendar.R;

public class Memo_ListView_Adapter extends RecyclerView.Adapter<Memo_ListView_Adapter.Viewholder> {
    private List<Content_Room> list;
    private Context context;
    private Custom_Dialog builder;

    public Memo_ListView_Adapter(List<Content_Room> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        MemoFragItemBinding binding = MemoFragItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.binding.setItem(this);
        holder.binding.setActivity(list.get(position));
        if (!list.get(position).getImg().equals("NONE")) {
            Log.d("NONE?", list.get(position).getImg());
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
                    holder.binding.viewpager.setAdapter(new Image_Pager_Adapter(arrayList, context));
                    new TabLayoutMediator(holder.binding.tabLayout, holder.binding.viewpager, (tab, position1) ->  { }).attach();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else holder.binding.viewpagerBg.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class Viewholder extends RecyclerView.ViewHolder {
        MemoFragItemBinding binding;
        public Viewholder(@NonNull MemoFragItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.moreBtnMfrag.setOnClickListener(v -> showDialog(1));
        }

        public void showDialog(int param){
            Log.d("showDialog", String.valueOf(param));
            ArrayList arrayList = new ArrayList();
            if (param == 1) {
                arrayList.add("수정");
                arrayList.add("삭제");
            }else {
                arrayList.add("확인");
                arrayList.add("취소");
            }

            builder = new Custom_Dialog(context, value ->  {
                if (value.equals("수정")){
                    Intent intent = new Intent(context, NoteAditActivity.class);
                    intent.putExtra("id", list.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }else if (value.equals("삭제")){
                    showDialog(2);
                }else if (value.equals("확인")){
                    Database_Room.getInstance(context).getDao().content_delete(list.get(getAdapterPosition()).getId());
                    Database_Room.getInstance(context).getDao().memo_delete(list.get(getAdapterPosition()).getId());
                    ((Activity)context).finish();
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

}
