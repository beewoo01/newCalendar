package cookmap.cookandroid.hw.newcalendar;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private ArrayList<DayInfo> mDayList;
    private Context mContext;

    public CalendarAdapter(Context context, ArrayList<DayInfo> dayList) {
        this.mContext = context;
        this.mDayList = dayList;
    }


    @NonNull
    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.day, parent, false);
        CalendarAdapter.ViewHolder vh = new CalendarAdapter.ViewHolder(view);


        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DayInfo day = mDayList.get(position);

        if (day != null) {
            holder.tvDay.setText(day.getDay());

            if (day.isInMonth()) {
                if (position % 7 == 0) {
                    holder.tvDay.setTextColor(Color.RED);
                } else if (position % 7 == 6) {
                    holder.tvDay.setTextColor(Color.BLUE);
                } else {
                    holder.tvDay.setTextColor(Color.BLACK);
                }
            } else {
                //전, 다음 달 일짜는 투명도 40%줌 (66)
                if (position % 7 == 0) {
                    holder.tvDay.setTextColor(Color.parseColor("#66FF0000"));
                } else if (position % 7 == 6) {
                    holder.tvDay.setTextColor(Color.parseColor("#660000FF"));

                } else {
                    holder.tvDay.setTextColor(Color.parseColor("#66000000"));
                }

            }
        }


        if (day.getIsMemo() > 0){
            holder.imgMemo.setVisibility(View.VISIBLE);
            int backColor = 000000;
            if (day.getIsMemo() >= 5) {
                //빨강
                Log.d("색별추출", "빨강");
                backColor = mContext.getResources().getColor(R.color.colorRed);

            } else if (day.getIsMemo() >= 3 || day.getIsMemo() > 2){
                backColor = mContext.getResources().getColor(R.color.colorYellow);
            } else if (day.getIsMemo() <= 2) {
                backColor = mContext.getResources().getColor(R.color.colorGreen);
                Log.d("카운트@@", "2개이상 14일?");
            }
            holder.imgMemo.setBackgroundColor(backColor);
        }else holder.imgMemo.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return mDayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout llBackground;
        public TextView tvDay;
        public ImageView imgMemo;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            llBackground = itemView.findViewById(R.id.day_cell_ll_background);
            tvDay = itemView.findViewById(R.id.day_cell_tv_day);
            imgMemo = itemView.findViewById(R.id.img_item_dot);
        }
    }

}
