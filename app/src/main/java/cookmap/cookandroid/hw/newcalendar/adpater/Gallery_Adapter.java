package cookmap.cookandroid.hw.newcalendar.adpater;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import java.util.List;

import cookmap.cookandroid.hw.newcalendar.R;
import cookmap.cookandroid.hw.newcalendar.databinding.GalleryItemBinding;

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

    private List selecedPosition = new ArrayList<>(10);

    public void setItemImage(getImgListner getImgListner) {
        List<String> result = new ArrayList();
        if (selecedPosition.size() > 0){
            for (int i = 0 ; i < selecedPosition.size(); i++ ){
                Log.d("SET_ITEM_IMG", String.valueOf(selecedPosition.get(i)));
                result.add(thumbsDataList.get((Integer) selecedPosition.get(i)));
                Log.d("SET_ITEM_IMG", thumbsDataList.get((Integer) selecedPosition.get(i)));
            }
        }

        getImgListner.onImg(result);
    }

    public interface getImgListner{
        void onImg(List list);
    }



    public interface OnLoadMoreListener {
        void onLoadMore();
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
            //View view = inflater.inflate(R.layout.gallery_item, parent, false);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, width);
            lp.setMargins(2,2,2,2);
            //view.setLayoutParams(lp);
            GalleryItemBinding viewHolder = GalleryItemBinding.inflate(inflater);
            viewHolder.getRoot().setLayoutParams(lp);
            //Gallery_Adapter.CustomViewHolder viewHolder = new Gallery_Adapter.CustomViewHolder(view);

            return new CutomViewHolderBinding(viewHolder);
        } else {

            return new ProgressViewHolder(new ProgressBar(context, null, android.R.attr.progressBarStyleSmall));
        }
    }

    public void addAll(List<String> lst) {
        thumbsDataList.addAll(lst);
        notifyDataSetChanged();
    }

    public void addItemMore(List<String> lst) {
        thumbsDataList.addAll(lst);
        notifyItemRangeChanged(0, thumbsDataList.size());
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CutomViewHolderBinding){
            ((CutomViewHolderBinding) holder).binding.pictureItem.setLayoutParams(new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ((CutomViewHolderBinding) holder).binding.numberDotItem.setVisibility(View.INVISIBLE);
            ((CutomViewHolderBinding) holder).binding.pickedNumberTxt.setVisibility(View.INVISIBLE);
            ((CutomViewHolderBinding) holder).binding.itemBgd.setPadding(4,4,4,4);

            ((CutomViewHolderBinding) holder).binding.pickedNumberTxt.setLayoutParams(new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            RelativeLayout.LayoutParams midd = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            ((CutomViewHolderBinding) holder).binding.pickedNumberTxt.setLayoutParams(midd);
            midd.addRule(RelativeLayout.CENTER_IN_PARENT);

            if (selecedPosition.size() > 0 && holder.getAdapterPosition() != RecyclerView.NO_POSITION){

                if (selecedPosition.contains(holder.getAdapterPosition())){
                    selecedPosition.indexOf(holder.getAdapterPosition());
                    selecedPosition.get(selecedPosition.indexOf(holder.getAdapterPosition()));

                    ((CutomViewHolderBinding) holder).binding.numberDotItem.setVisibility(View.VISIBLE);
                    ((CutomViewHolderBinding) holder).binding.pickedNumberTxt.setVisibility(View.VISIBLE);
                    ((CutomViewHolderBinding) holder).binding.pickedNumberTxt.setText(String.valueOf(selecedPosition.indexOf(holder.getAdapterPosition())+1));
                    ((CutomViewHolderBinding) holder).binding.pickedNumberTxt.setTextColor(Color.parseColor("#FFFFFF"));
                    ((CutomViewHolderBinding) holder).binding.pictureItem.setColorFilter(Color.parseColor("#7F000000"));
                }else {
                    ((CutomViewHolderBinding) holder).binding.pictureItem.setColorFilter(Color.parseColor("#7F000000"), PorterDuff.Mode.DST);
                }

            }

            Glide.with(((CutomViewHolderBinding) holder).binding.pictureItem.getContext())
                    .load(thumbsDataList.get(position))
                    .into(((CutomViewHolderBinding) holder).binding.pictureItem);


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
            new Handler().post(() ->  {
                    thumbsDataList.add(null);
                    notifyItemInserted(thumbsDataList.size() - 1);
            });
        } else {
            thumbsDataList.remove(thumbsDataList.size() - 1);
            notifyItemRemoved(thumbsDataList.size());
        }
    }

    class CutomViewHolderBinding extends RecyclerView.ViewHolder{
        GalleryItemBinding binding;
        public CutomViewHolderBinding(@NonNull GalleryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    if (selecedPosition.size() <= 0){
                        //처음 선택, 다 제거 후 선택
                        selecedPosition.add(position);

                    }else {
                        // 선택한 항목이 있는경우
                        if (selecedPosition.contains(position)){

                            selecedPosition.remove(selecedPosition.indexOf(position));
                            binding.numberDotItem.setVisibility(View.INVISIBLE);
                            binding.pickedNumberTxt.setVisibility(View.INVISIBLE);
                            binding.pictureItem.setColorFilter(Color.parseColor("#7F000000"), PorterDuff.Mode.DST);
                        } else if ( selecedPosition.size() < 10){
                            selecedPosition.add(position);

                        }
                    }

                    notifyDataSetChanged();
                }
            });
        }
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar bar;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            bar = (ProgressBar) itemView;
        }
    }
}
