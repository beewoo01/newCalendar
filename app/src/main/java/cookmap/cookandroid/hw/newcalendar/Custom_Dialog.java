package cookmap.cookandroid.hw.newcalendar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Custom_Dialog extends Dialog {

    private Context context;
    private ArrayList arrayList;
    private RecyclerView recyclerView;
    private ImageView closeImg;

    public Custom_Dialog(@NonNull Context context) {
        super(context);
        this.context = context;
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

    private void init(){
        arrayList = new ArrayList();
        String[] strings = {"수정", "삭제"};
        arrayList.add(strings);
        recyclerView.findViewById(R.id.alert_recycler);
        closeImg.findViewById(R.id.alert_close);
        closeImg.setOnClickListener((View.OnClickListener) dialogInterface);
        //이게 되려나?
    }

    //이게 되려나?

    DialogInterface.OnDismissListener dialogInterface = new DialogInterface.OnDismissListener(){

        @Override
        public void onDismiss(DialogInterface dialog) {
            dialog.dismiss();
        }
    };

    private class Adapter extends RecyclerView.Adapter{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
