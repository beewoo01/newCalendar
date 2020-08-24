package cookmap.cookandroid.hw.newcalendar.utill;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private GestureDetector detector;
    private OnItemClickListener listener;
    private View child;

    public interface OnItemClickListener{
        void OnItemClick(View view, int position);
    }


    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener){
        this.listener = listener;
        detector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                recyclerView.getChildAdapterPosition(child);
                return true;
            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent ev) {
        View childView = rv.findChildViewUnder(ev.getX(), ev.getY());
        if (childView != null && listener != null && detector.onTouchEvent(ev)) {
            try {
                listener.OnItemClick(childView, rv.getChildAdapterPosition(childView));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
