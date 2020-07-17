package cookmap.cookandroid.hw.newcalendar;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CalendarAdapter extends BaseAdapter {
    private ArrayList<DayInfo> mDayList;
    private Context mContext;
    private int mResource;
    private LayoutInflater mLiInflater;

    /**
     * Adpater 생성자
     *
     * @param context
     *            컨텍스트
     * @param textResource
     *            레이아웃 리소스
     * @param dayList
     *            날짜정보가 들어있는 리스트
     */
    public CalendarAdapter(Context context, int textResource, ArrayList<DayInfo> dayList)
    {
        this.mContext = context;
        this.mDayList = dayList;
        this.mResource = textResource;
        this.mLiInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }


    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mDayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return mDayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        DayInfo day = mDayList.get(position);

        DayViewHolde dayViewHolder;

        if(convertView == null)
        {
            convertView = mLiInflater.inflate(mResource, null);
            dayViewHolder = new DayViewHolde();
            dayViewHolder.llBackground = (LinearLayout)convertView.findViewById(R.id.day_cell_ll_background);
            dayViewHolder.tvDay = (TextView) convertView.findViewById(R.id.day_cell_tv_day);
            dayViewHolder.dot_img = (ImageView) convertView.findViewById(R.id.img_item_dot);

            convertView.setTag(dayViewHolder);
        }
        else
        {
            dayViewHolder = (DayViewHolde) convertView.getTag();
        }

        if(day != null)
        {
            dayViewHolder.tvDay.setText(day.getDay());
            //여기로와
            //if (day.getDay() == )

            if(day.isInMonth())
            {
                if(position % 7 == 0)
                {
                    dayViewHolder.tvDay.setTextColor(Color.RED);
                }
                else if(position % 7 == 6)
                {
                    dayViewHolder.tvDay.setTextColor(Color.BLUE);
                }
                else
                {
                    dayViewHolder.tvDay.setTextColor(Color.BLACK);
                }
            }
            else
            {
                //전, 다음 달 일짜는 투명도 40%줌 (66)
                if(position % 7 == 0) {
                    dayViewHolder.tvDay.setTextColor(Color.parseColor("#66FF0000"));
                } else if(position % 7 == 6) {
                    dayViewHolder.tvDay.setTextColor(Color.parseColor("#660000FF"));

                } else{
                    dayViewHolder.tvDay.setTextColor(Color.parseColor("#66000000"));
                }

            }

        }

        return convertView;
    }

    private Date today(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd");
        return null;
    }


    public class DayViewHolde
    {
        public LinearLayout llBackground;
        public TextView tvDay;
        public ImageView dot_img;

    }

    private int getCellWidthDP()
    {
//      int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int cellWidth = 480/7;

        return cellWidth;
    }

    private int getRestCellWidthDP()
    {
//      int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int cellWidth = 480%7;

        return cellWidth;
    }

    private int getCellHeightDP()
    {
//      int height = mContext.getResources().getDisplayMetrics().widthPixels;
        //int cellHeight = 480/6;
        int cellHeight = 700/6;
        // 셀의 높이 최종 결정지

        return cellHeight;
    }

}
