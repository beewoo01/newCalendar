package cookmap.cookandroid.hw.newcalendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class label_dialog extends DialogFragment{
    private passColor passColor;

    public interface passColor{
        void getColor(String color);
    }

    label_dialog(passColor passColor){
        this.passColor = passColor;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.label_dialog, container);
        view.findViewById(R.id.back_btn_LD).setOnClickListener(listener);
        RecyclerView recyclerView = view.findViewById(R.id.label_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(new Label_Adapter(getList()));

        return view;
    }
    private ArrayList getList(){
        ArrayList list = new ArrayList();
        list.add("#FFF44336");
        list.add("#FF8BC34A");
        list.add("#FFFFEB3B");
        list.add("#FF9C27B0");
        list.add("#FF03A9F4");
        list.add("#FF3F51B5");
        list.add("#FF000000");
        list.add("#FF4CAF50");
        list.add("#FFFF9800");
        Log.d("LABEL", "getList");
        return list;

    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("Label_Dialog");
            DialogFragment dialogFragment = (DialogFragment) fragment;
            dialogFragment.dismiss();
        }
    };

    class Label_Adapter extends RecyclerView.Adapter<Label_Adapter.ViewHolder>{
        ArrayList<String> list;

        private Label_Adapter(ArrayList list){
            Log.d("LABEL", "Label_Adapter");
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d("LABEL", "onCreateViewHolder");
            Context context = parent.getContext();
            int width = (int) (parent.getResources().getDisplayMetrics().widthPixels / 3.25);
            /*LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.label_bg_img, parent, false);*/
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, width);
            lp.setMargins(2,2,2,2);
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(lp);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    width, width);
            imageView.setLayoutParams(params);
            return new ViewHolder(imageView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Log.d("LABEL", "onBindViewHolder");
            holder.imageView.setBackgroundColor(Color.parseColor(list.get(position)));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                Log.d("LABEL", "ViewHolder");
                imageView = (ImageView) itemView;
                imageView.setOnClickListener(listener);
            }

            private View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    passColor.getColor(list.get(getAdapterPosition()));
                    Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("Label_Dialog");
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismiss();
                }
            };
        }
    }
}
