package cookmap.cookandroid.hw.newcalendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class testRecyclerGrid extends RecyclerView.Adapter<testRecyclerGrid.ViewHolder>
{
    private ArrayList<DayInfo> mDayList;
    private Context mContext;
    private int height;
    private int mResource;
    private LayoutInflater mLiInflater;

    public testRecyclerGrid(Context context, int textResource, ArrayList<DayInfo> dayList){
        this.mContext = context;
        this.mDayList = dayList;
        this.mResource = textResource;
        this.mLiInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public testRecyclerGrid.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.day, parent, false);
        testRecyclerGrid.ViewHolder vh = new testRecyclerGrid.ViewHolder(view);


        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DayInfo day = mDayList.get(position);
        if (height != 0){
            holder.itemView.getLayoutParams().height = height;
        }


        if (day != null){
            holder.tvDay.setText(day.getDay());

            if(day.isInMonth())
            {
                if(position % 7 == 0)
                {
                    holder.tvDay.setTextColor(Color.RED);
                }
                else if(position % 7 == 6)
                {
                    holder.tvDay.setTextColor(Color.BLUE);
                }
                else
                {
                    holder.tvDay.setTextColor(Color.BLACK);
                }
            }
            else
            {
                //전, 다음 달 일짜는 투명도 40%줌 (66)
                if(position % 7 == 0) {
                    holder.tvDay.setTextColor(Color.parseColor("#66FF0000"));
                } else if(position % 7 == 6) {
                    holder.tvDay.setTextColor(Color.parseColor("#660000FF"));

                } else{
                    holder.tvDay.setTextColor(Color.parseColor("#66000000"));
                }

            }
        }


    }

    @Override
    public int getItemCount() {
        return mDayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout llBackground;
        public TextView tvDay;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            llBackground = itemView.findViewById(R.id.day_cell_ll_background);
            tvDay = itemView.findViewById(R.id.day_cell_tv_day);

        }
    }

    public void setLength(int height){
        this.height = height;
    }

}
