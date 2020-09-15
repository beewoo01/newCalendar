package cookmap.cookandroid.hw.newcalendar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import cookmap.cookandroid.hw.newcalendar.databinding.LabelDialogBinding;

public class Label_dialog extends DialogFragment{
    private passColor passColor;

    public interface passColor{
        void getColor(String color);
    }

    public Label_dialog(passColor passColor){
        this.passColor = passColor;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LabelDialogBinding binding = DataBindingUtil.inflate(inflater, R.layout.label_dialog, container, false);
        binding.setFragment(this);
        View view = binding.getRoot();
        binding.labelRecycler.setAdapter(new Label_Adapter(getList()));

        return view;
    }
    private ArrayList<String> getList(){
        ArrayList<String> list = new ArrayList();
        Collections.addAll(list, getResources().getStringArray(R.array.label_string));
        return list;
    }

    public void onClick(View view){
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("Label_Dialog");
        DialogFragment dialogFragment = (DialogFragment) fragment;
        dialogFragment.dismiss();
    }

    class Label_Adapter extends RecyclerView.Adapter<Label_Adapter.ViewHolder>{
        ArrayList<String> list;

        private Label_Adapter(ArrayList list){
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            int width = (int) (parent.getResources().getDisplayMetrics().widthPixels / 3.25);
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
                imageView = (ImageView) itemView;
                imageView.setOnClickListener(listener);
            }

            private View.OnClickListener listener = v ->  {
                    passColor.getColor(list.get(getAdapterPosition()));
                    Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("Label_Dialog");
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismiss();
            };
        }
    }
}
