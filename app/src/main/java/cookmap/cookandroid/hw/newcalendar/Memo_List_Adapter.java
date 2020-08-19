package cookmap.cookandroid.hw.newcalendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cookmap.cookandroid.hw.newcalendar.Database.CNM;
import cookmap.cookandroid.hw.newcalendar.Database.Content_Room;

public class Memo_List_Adapter extends RecyclerView.Adapter<Memo_List_Adapter.ViewHolder> {
    private Context context;
    private List<CNM> list;

    public Memo_List_Adapter(Context context, List<CNM> list) {
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
        holder.label_mfrag.setBackgroundColor(Color.parseColor(list.get(position).getContent_room().getLabel()));
        holder.title_mfrag.setText(list.get(position).getContent_room().getTitle());

        if (!list.get(position).getContent_room().getDescription().equals("none")) {
            holder.sub_txt_mfrag.setText(list.get(position).getContent_room().getDescription());
        }
        if (!list.get(position).getContent_room().getMain_Img().equals("none")) {
            //glide
            Glide.with(context).load(list.get(position).getContent_room().getMain_Img()).apply(new RequestOptions().circleCrop()).into(holder.coverImg_mfrg);
        } else {
            holder.coverImg_mfrg.setBackgroundColor(Color.parseColor(list.get(position).getContent_room().getLabel()));
        }
        if (!list.get(position).getContent_room().getImg().equals("none")) {

            Log.d("img는? json", list.get(position).getContent_room().getImg());
            try {
                int i = 0;
                JSONObject jsonObject = new JSONObject(list.get(position).getContent_room().getImg());
                ArrayList<String> arrayList = new ArrayList<>();
                while (i < 10) {
                    if (jsonObject.has(String.valueOf(i))) {
                        String key = jsonObject.getString(String.valueOf(i));
                        arrayList.add(key);
                        i++;
                    } else break;
                }
                if (i > 0) {
                    holder.viewPager.setAdapter(new Image_Pager_Adapter(arrayList, context));
                    new TabLayoutMediator(holder.tabLayout, holder.viewPager,
                            new TabLayoutMediator.TabConfigurationStrategy() {
                                @Override
                                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                                }
                            }).attach();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else holder.viewPager.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View label_mfrag;
        private ImageView coverImg_mfrg, moreBtn_mfrag;
        private TextView title_mfrag, sub_txt_mfrag;
        private ViewPager2 viewPager;
        private TabLayout tabLayout;
        private Custom_Dialog builder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            label_mfrag = itemView.findViewById(R.id.label_mfrag);
            coverImg_mfrg = itemView.findViewById(R.id.coverImg_mfrg);
            moreBtn_mfrag = itemView.findViewById(R.id.moreBtn_mfrag);
            title_mfrag = itemView.findViewById(R.id.title_mfrag);
            sub_txt_mfrag = itemView.findViewById(R.id.sub_txt_mfrag);
            viewPager = itemView.findViewById(R.id.viewpager);
            tabLayout = itemView.findViewById(R.id.tab_layout);

            moreBtn_mfrag.setOnClickListener(onClickListener);

        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
                //수정 삭제 버튼 화면 중앙에
            }
        };

        private void showDialog(int param){
            Log.d("showDialog", String.valueOf(param));
            ArrayList arrayList = new ArrayList();
            final String[] prm = new String[1];
            if (param == 1) {
                arrayList.add("수정");
                arrayList.add("삭제");
            }else {
                arrayList.add("확인");
                arrayList.add("취소");
            }
            builder = new Custom_Dialog(context, new Custom_Dialog.Custom_Dialog_EventListener() {
                @Override
                public void customDialogEvent(String value) {
                    if (value.equals("수정")){
                        // 수정 페이지 ㄱ
                        prm[0] = value;
                        Intent intent = new Intent(context, writeActivity.class);
                        intent.putExtra("id", list.get(getAdapterPosition()).getContent_room().getId());
                        context.startActivity(intent);
                        //context.startActivity(new Intent(context, writeActivity.class));
                        //list.get(getAdapterPosition()).getId();
                        Log.d("prm",value);
                    }else if (value.equals("삭제")){
                        showDialog(2);
                        Log.d("prm",value);
                        prm[0] = value;
                    }else if (value.equals("확인")){
                        Log.d("prm",value);
                        prm[0] = value;
                    }
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