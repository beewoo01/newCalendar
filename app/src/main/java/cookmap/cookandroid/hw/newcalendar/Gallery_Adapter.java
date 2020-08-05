package cookmap.cookandroid.hw.newcalendar;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
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

    static List selecedPosition = new ArrayList<>(10);

    static private OnItemClickListener onItemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    static String TAG = "GALL_ADAPTER";

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public Gallery_Adapter(OnLoadMoreListener onLoadMoreListener, Context context) {
        this.context = context;
        this.onLoadMoreListener = onLoadMoreListener;
        thumbsDataList = new ArrayList<>();
    }

    public void setGridLayoutManager(GridLayoutManager gridLayoutManager) {
        this.mGridLayoutManager = gridLayoutManager;
    }

    public void setRecyclerView(RecyclerView mView) {
        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mGridLayoutManager.getItemCount();
                firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = mGridLayoutManager.findLastVisibleItemPosition();
                /*Log.d("total", totalItemCount + "");
                Log.d("visible", visibleItemCount + "");

                Log.d("first", firstVisibleItem + "");
                Log.d("last", lastVisibleItem + "");*/


                if (!isMoreLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.gallery_item, parent, false);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, width);
            lp.setMargins(2,2,2,2);
            view.setLayoutParams(lp);
            Gallery_Adapter.CustomViewHolder viewHolder = new Gallery_Adapter.CustomViewHolder(view);


            /*int width = (int) (parent.getResources().getDisplayMetrics().widthPixels / 3.25);
            ImageView imageView = new ImageView(parent.getContext());
            imageView.setLayoutParams(new LinearLayoutCompat.LayoutParams(width, width));
            imageView.setAdjustViewBounds(false);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);*/

            return viewHolder;
            //return new CustomViewHolder(imageView);
        } else {

            return new ProgressViewHolder(new ProgressBar(context, null, android.R.attr.progressBarStyleSmall));
        }
    }

    public void addAll(List<String> lst) {
        //thumbsDataList.clear();
        thumbsDataList.addAll(lst);
        notifyDataSetChanged();
    }

    public void addItemMore(List<String> lst) {
        thumbsDataList.addAll(lst);
        notifyItemRangeChanged(0, thumbsDataList.size());
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CustomViewHolder) {
           // Log.d("Ga_A_onBind", "if 옴");


            ((CustomViewHolder) holder).imageView.setLayoutParams(new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ((CustomViewHolder) holder).dot_layout.setVisibility(View.INVISIBLE);
            ((CustomViewHolder) holder).Picked_number_TextView.setVisibility(View.INVISIBLE);

            //RelativeLayout.LayoutParams ld = new RelativeLayout.LayoutParams(50,50);
            //ld.setMargins(5,5,5,5);
            //ld.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            //((CustomViewHolder) holder).dot_layout.setLayoutParams(ld);
            //RelativeLayout.LayoutParams ld = (RelativeLayout.LayoutParams) ((CustomViewHolder) holder).bg_main.getLayoutParams();
            //ld.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            //((CustomViewHolder) holder).dot_layout.setLayoutParams(ld);
            //((RelativeLayout.LayoutParams) ((CustomViewHolder) holder).bg_main.getLayoutParams()).addRule();
            ((CustomViewHolder) holder).bg_main.setPadding(2,2,2,2);


            ((CustomViewHolder) holder).Picked_number_TextView.setLayoutParams(new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            //((CustomViewHolder) holder).bg_main.

            Log.d("사이즈머고", String.valueOf(selecedPosition.size()));

            if (selecedPosition.size() > 0){
                for (int i = 0; i < selecedPosition.size(); i++){
                    if (position == (Integer)selecedPosition.get(i) ){
                        ((CustomViewHolder) holder).dot_layout.setVisibility(View.VISIBLE);
                        ((CustomViewHolder) holder).Picked_number_TextView.setVisibility(View.VISIBLE);
                        ((CustomViewHolder) holder).Picked_number_TextView.setText(String.valueOf(i+1));
                        ((CustomViewHolder) holder).Picked_number_TextView.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (selecedPosition.size() <= 0){
                        //처음 선택, 다 제거 후 선택
                        selecedPosition.add(position);
                        ((CustomViewHolder) holder).dot_layout.setVisibility(View.VISIBLE);
                        ((CustomViewHolder) holder).Picked_number_TextView.setVisibility(View.VISIBLE);
                        ((CustomViewHolder) holder).Picked_number_TextView.setText(String.valueOf(selecedPosition.size()));
                        ((CustomViewHolder) holder).Picked_number_TextView.setTextColor(Color.parseColor("#FFFFFF"));
                    }else {
                        // 선택한 항목이 있는경우
                        for (int i = 0; i < selecedPosition.size(); i++ ){
                            Log.d(TAG, "i: "+ i +"sele size: "+ selecedPosition.size());
                            if (position == (Integer) selecedPosition.get(i)){
                                selecedPosition.remove(i);
                                ((CustomViewHolder) holder).dot_layout.setVisibility(View.INVISIBLE);
                                ((CustomViewHolder) holder).Picked_number_TextView.setVisibility(View.INVISIBLE);

                                break;

                            }else if (i == selecedPosition.size()-1 && selecedPosition.size() < 10){
                                Log.d(TAG, "i는?" + i);
                                selecedPosition.add(position);
                                ((CustomViewHolder) holder).dot_layout.setVisibility(View.VISIBLE);
                                ((CustomViewHolder) holder).Picked_number_TextView.setVisibility(View.VISIBLE);
                                ((CustomViewHolder) holder).Picked_number_TextView.setText(String.valueOf(selecedPosition.size()));
                                ((CustomViewHolder) holder).Picked_number_TextView.setTextColor(Color.parseColor("#FFFFFF"));;
                                break;
                            }
                        }
                    }
                   notifyDataSetChanged();
                }
            });

            Glide.with(((CustomViewHolder) holder).imageView.getContext())
                    .load(thumbsDataList.get(position))
                    .into(((CustomViewHolder) holder).imageView);

        } else {
            Log.d("Ga_A_onBind", "else 옴");
        }

    }

    public void setMoreLoading(boolean isMoreLoading) {
        this.isMoreLoading = isMoreLoading;
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

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        //RelativeLayout relativeLayout;
        ImageView imageView;
        RelativeLayout dot_layout, bg_main;
        TextView Picked_number_TextView;


        public CustomViewHolder(@NonNull final View itemView) {
            super(itemView);
            //relativeLayout = (RelativeLayout) itemView;
            bg_main = (RelativeLayout) itemView.findViewById(R.id.item_bgd);
            imageView = (ImageView) itemView.findViewById(R.id.picture_item);
            dot_layout = (RelativeLayout) itemView.findViewById(R.id.number_dot_item);
            Picked_number_TextView = (TextView) itemView.findViewById(R.id.picked_number_txt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        /*selecedPosition++;
                        Log.d(TAG, "onClick_onClick: "+ selecedPosition);*/
                        /*dot_layout.setVisibility(View.VISIBLE);
                        Picked_number_TextView.setVisibility(View.VISIBLE);
                        Picked_number_TextView.setText(String.valueOf(1));*/
                        /*if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(v, position);
                        }*/
                    }
                }
            });
            /*imageView = (ImageView) itemView;*/
/*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Log.d(TAG, "포지션: " + position+ ", getID?:"+ v.getId());


                }
            });*/

        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar bar;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            bar = (ProgressBar) itemView;
        }
    }
}
