package cookmap.cookandroid.hw.newcalendar;

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
    final static int DISTANCE = 200;
    final static int VELOCITY = 350;
    private String TAG = "RvClLis";

    /*public RecyclerItemClickListener(final Context context, final RecyclerView recyclerView, final OnItemClickListener listener){
        detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                *//*for (int i = 0; i < recyclerView.getChildCount(); i++){
                    if (recyclerView.getChildAdapterPosition(child) == i) {
                        recyclerView.getChildAt(i).setBackgroundColor(Color.parseColor("#FFE0AB"));
                    }
                    else{
                        recyclerView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }

                }*//*
                return true;
            }
        });

    }*/

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
        Log.d(TAG,"onInterceptTouchEvent");
        /*child = rv.findChildViewUnder(ev.getX(), ev.getY());
        detector.onTouchEvent(ev)*/;

        View childView = rv.findChildViewUnder(ev.getX(), ev.getY());
        if (childView != null && listener != null && detector.onTouchEvent(ev)) {
            try {
                listener.OnItemClick(childView, rv.getChildAdapterPosition(childView));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //return false;
        }

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
