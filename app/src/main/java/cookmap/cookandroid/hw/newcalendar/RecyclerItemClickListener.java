package cookmap.cookandroid.hw.newcalendar;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private GestureDetector detector;
    private OnItemClickListener listener;
    private View child;
    final static int DISTANCE = 200;
    final static int VELOCITY = 350;
    private String TAG = "RvClLis";

    public RecyclerItemClickListener(final Context context, final RecyclerView recyclerView, final OnItemClickListener listener){
        detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            int i = 0;

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d("클릭 리스너"," 왔네");
                Log.d("onSingleTapUp 리스너", String.valueOf(recyclerView.getChildAdapterPosition(child)));
                for (int i = 0; i < recyclerView.getChildCount(); i++){
                    if (recyclerView.getChildAdapterPosition(child) == i) {
                        recyclerView.getChildAt(i).setBackgroundColor(Color.parseColor("#FFE0AB"));
                    }
                    else{
                        recyclerView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }

                }
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() - e2.getX() < DISTANCE && Math.abs(velocityX) > VELOCITY) {
                    Log.d(TAG, "e1 - e2");

                }
                if (e2.getX() - e1.getX() < DISTANCE && Math.abs(velocityX) > VELOCITY) {
                    Log.d(TAG, "e2 - e1");
                }
                return true;
            }
        });

    }

    public interface OnItemClickListener{
        void OnItemClick(View view, int position);
    }


    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        Log.d(TAG,"onInterceptTouchEvent");
        child = rv.findChildViewUnder(e.getX(), e.getY());
        detector.onTouchEvent(e);

        /*if(child!=null&&detector.onTouchEvent(e))
        {
            Log.d(TAG, "getChildAdapterPosition=>" + rv.getChildAdapterPosition(child));
            Log.d(TAG,"getChildLayoutPosition=>"+rv.getChildLayoutPosition(child));
            Log.d(TAG,"getChildViewHolder=>" + rv.getChildViewHolder(child));

        }*/
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        Log.d(TAG, "onTouchEvent=>" + rv.getChildAdapterPosition(child));

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
