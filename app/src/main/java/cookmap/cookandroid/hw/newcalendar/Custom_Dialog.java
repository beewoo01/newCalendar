package cookmap.cookandroid.hw.newcalendar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import cookmap.cookandroid.hw.newcalendar.utill.RecyclerViewDecoration;

public class Custom_Dialog extends Dialog {

    private Context context;
    private ArrayList arrayList;
    private String[] param;
    private int position;
    private RecyclerView dialog_recyclerView;

    public interface Custom_Dialog_EventListener{
        void customDialogEvent(String value);
    }

    private Custom_Dialog_EventListener onDialogEvent;

    public Custom_Dialog(@NonNull Context context, Custom_Dialog_EventListener onDialogEvent, ArrayList arrayList, int position) {
        super(context);
        this.context = context;
        this.onDialogEvent = onDialogEvent;
        this.arrayList = arrayList;
        this.position = position;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.alert_dialog);

        init();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
    private void init(){
        dialog_recyclerView = findViewById(R.id.alert_recycler);
        findViewById(R.id.alert_close).setOnClickListener(listener);
        TextView title = findViewById(R.id.title_txt);
        if (position == 1){
            title.setText(" 선택해 주세요 ");
        }else if (position == 2){
            title.setText(" 이 게시물을 삭제하시겠어요? ");
        }

        //arrayList = new ArrayList();
        //arrayList.addAll(Arrays.asList(param));
        dialog_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        dialog_recyclerView.setAdapter(new Custom_Dialog_Adapter(arrayList));
    }

    private class Custom_Dialog_Adapter extends RecyclerView.Adapter<Custom_Dialog_Adapter.ViewHolder>{

        private ArrayList<String> list;

        private Custom_Dialog_Adapter(ArrayList<String> list){
            this.list = list;
        }

        @NonNull
        @Override
        public Custom_Dialog_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.custom_dialog, parent, false);
            return  new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textView.setText(list.get(position));

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private TextView textView;
            private LinearLayout item_background;

            private ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.textView = itemView.findViewById(R.id.text);
                this.item_background = itemView.findViewById(R.id.item_background);
                textView.setOnClickListener(this);
                item_background.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (list.get(getAdapterPosition()).equals("수정")){
                    Log.d("수정", "수정수정");
                    onDialogEvent.customDialogEvent("수정");
                    dismiss();
                }else if (list.get(getAdapterPosition()).equals("삭제")){
                    Log.d("삭제", "삭제삭제");
                    onDialogEvent.customDialogEvent("삭제");
                    dismiss();

                }else if (list.get(getAdapterPosition()).equals("확인")){
                    onDialogEvent.customDialogEvent("확인");
                    dismiss();
                }else if (list.get(getAdapterPosition()).equals("취소")){
                    onDialogEvent.customDialogEvent("취소");
                    dismiss();
                }
            }
        }
    }
}
