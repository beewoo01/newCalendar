package cookmap.cookandroid.hw.newcalendar.utill;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


public class BaseViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder{
    private final T binding;

    public interface OnRCVItemListener {
        void onItemClick(View view, int position);
    }

    protected OnRCVItemListener listener;

    public void setOnItemClickListener(OnRCVItemListener listener) {
        this.listener = listener;
    }

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        this.binding = (T) DataBindingUtil.bind(itemView);

        itemView.setOnClickListener(v -> {
            int position = getAdapterPosition();
            Log.d("position", String.valueOf(position));
            if (position != RecyclerView.NO_POSITION){
                if (listener != null){
                    listener.onItemClick(v, position);
                }else {

                }

            }
        });
    }

    public T binding(){
        return binding;
    }


}
