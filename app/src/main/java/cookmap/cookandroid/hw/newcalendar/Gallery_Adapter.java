package cookmap.cookandroid.hw.newcalendar;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Gallery_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private ArrayList<String> thumbsDataList;
    private Context context;

    private OnLoadMoreListener onLoadMoreListener;
    private GridLayoutManager mGridLayoutManager;

    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    int firstVisibleItem, visibleItemCount, totalItemCount, lastVisibleItem;

    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public Gallery_Adapter(OnLoadMoreListener onLoadMoreListener, Context context) {
        this.context = context;
        this.onLoadMoreListener=onLoadMoreListener;
        thumbsDataList = new ArrayList<>();
    }

    public void setGridLayoutManager(GridLayoutManager gridLayoutManager){
        this.mGridLayoutManager = gridLayoutManager;
    }

    public void setRecyclerView(RecyclerView mView){
        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mGridLayoutManager.getItemCount();
                firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = mGridLayoutManager.findLastVisibleItemPosition();
                Log.d("total", totalItemCount + "");
                Log.d("visible", visibleItemCount + "");

                Log.d("first", firstVisibleItem + "");
                Log.d("last", lastVisibleItem + "");



                if (!isMoreLoading && (totalItemCount - visibleItemCount)<= (firstVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isMoreLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return thumbsDataList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {

            int width = (int) (parent.getResources().getDisplayMetrics().widthPixels / 3.25);
            RelativeLayout relativeLayout = new RelativeLayout(parent.getContext());


            ImageView imageView = new ImageView(parent.getContext());
            //imageView.setLayoutParams(new LinearLayoutCompat.LayoutParams(width, width));
            imageView.setAdjustViewBounds(false);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);

            relativeLayout.addView(imageView);
            //relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(width, width));
            RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(width, width);
            relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            imageView.setLayoutParams(relativeParams);







            /*int width = (int) (parent.getResources().getDisplayMetrics().widthPixels / 3.25);
            ImageView imageView = new ImageView(parent.getContext());
            imageView.setLayoutParams(new LinearLayoutCompat.LayoutParams(width, width));
            imageView.setAdjustViewBounds(false);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);*/

            return new CustomViewHolder(imageView);
        } else {

            return new ProgressViewHolder(new ProgressBar(context, null, android.R.attr.progressBarStyleSmall));
        }
    }

    public void addAll(List<String> lst){
        //thumbsDataList.clear();
        thumbsDataList.addAll(lst);
        notifyDataSetChanged();
    }

    public void addItemMore(List<String> lst){
        thumbsDataList.addAll(lst);
        notifyItemRangeChanged(0,thumbsDataList.size());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CustomViewHolder) {
            Log.d("Ga_A_onBind","if 옴");
            Glide.with(((CustomViewHolder) holder).imageView.getContext())
                    .load(thumbsDataList.get(position))
                    .into(((CustomViewHolder) holder).imageView);
        }else {
            Log.d("Ga_A_onBind","else 옴");
        }

    }

    public void setMoreLoading(boolean isMoreLoading) {
        this.isMoreLoading=isMoreLoading;
    }


    @Override
    public int getItemCount() {
        return thumbsDataList.size();
    }

    public void setProgressMore(final boolean isProgress) {
        if (isProgress) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    thumbsDataList.add(null);
                    notifyItemInserted(thumbsDataList.size() - 1);
                }
            });
        } else {
            thumbsDataList.remove(thumbsDataList.size() - 1);
            notifyItemRemoved(thumbsDataList.size());
        }
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder{
        //RelativeLayout relativeLayout;
        ImageView imageView;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            //relativeLayout = (RelativeLayout) itemView;
            imageView = (ImageView) itemView;

        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder{

        public ProgressBar bar;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            bar = (ProgressBar) itemView;
        }
    }
}
