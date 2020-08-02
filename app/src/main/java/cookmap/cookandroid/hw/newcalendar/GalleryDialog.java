package cookmap.cookandroid.hw.newcalendar;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GalleryDialog extends DialogFragment {
    private RecyclerView recyclerView;
    private ImageView back_Image;
    private TextView ok_TxT;
    Dialog dlg;
    private String TAG = "GD_Frag";
    private static final int PICK_FROM_ALBUM = 1;
    ArrayList<String> ImageArray;


    private Context context;

    /*public GalleryDialog(Context context){
        Log.d(TAG, "GalleryDialog");
        this.context = context;
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onResume() {
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(params);
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.gallery_frag, container);
        recyclerView = view.findViewById(R.id.gf_recycler);
        ok_TxT = view.findViewById(R.id.check_btn_gf);
        back_Image = view.findViewById(R.id.back_btn_gf);
        ImageArray = new ArrayList<>();
        int mItemCount= 15; // 화면에 보여줄 항목 수

        //권한 요청 하는 부분
        ActivityCompat.requestPermissions
                (getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        /*Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME };
        final Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, MediaStore.MediaColumns.DATE_ADDED + " desc");
        final int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);*/
        /*if (cursor != null) {

            new Runnable() {
                @Override
                public void run() {
                    while (cursor.moveToNext()) {
                        String absolutePathOfImage = cursor.getString(columnIndex);

                        if (!TextUtils.isEmpty(absolutePathOfImage)) {
                            ImageArray.add(absolutePathOfImage);
                        }
                    }
                }
            };


        }*/

        //recyclerView.setAdapter(new GalleryDialog_RecyclerView_Adapter());
        //recyclerView.setOnScrollChangeListener();
        //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        return view;
    }

    /*RecyclerView.OnScrollListener onScroll = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView view, int scrollState) {

        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
            if (lastVisibleItemPosition == itemTotalCount) {
                //TODO 하실작업을 적어주시면됩니다.
            }

        }
    };*/


    class GalleryDialog_RecyclerView_Adapter extends RecyclerView.Adapter<GalleryDialog_RecyclerView_Adapter.CustomViewholder> {

        private ArrayList<String> thumbsDataList;
        private ArrayList<String> thumbsIDList;

        GalleryDialog_RecyclerView_Adapter(){
            thumbsDataList = new ArrayList<>();
            thumbsIDList = new ArrayList<>();

            //getThumbInfo(thumbsIDList, thumbsDataList);


            notifyDataSetChanged();
        }

        private void getThumbInfo(ArrayList<String> thumbsIDs, ArrayList<String> thumbsDatas) {
            String[] proj = {MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.SIZE};
            ContentResolver contentResolver = getActivity().getContentResolver();
            String sortOrder = String.format("%s limit 2 ", MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

            Cursor imageCursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    proj , null, null, sortOrder);


            /*if (imageCursor != null && imageCursor.moveToFirst()) {
                String thumbsID;
                String thumbsImageID;
                String thumbsData;
                String imgSize;

                int thumbsIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
                int thumbsDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                int thumbsImageIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                int thumbsSizeCol = imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE);
                do {
                    Log.d(TAG,"롲대ㅑㅙㄷ쟈ㅗ핻쟈ㅐㅑ호ㅑㅐㄷ조해ㅑㄷ조ㅑ핻ㅈ");
                    thumbsID = imageCursor.getString(thumbsIDCol);
                    thumbsData = imageCursor.getString(thumbsDataCol);
                    thumbsImageID = imageCursor.getString(thumbsImageIDCol);
                    imgSize = imageCursor.getString(thumbsSizeCol);
                    if (thumbsImageID != null) {
                        thumbsIDs.add(thumbsID);
                        thumbsDatas.add(thumbsData);
                    }
                } while (imageCursor.moveToNext());
            }*/
            if (imageCursor != null && imageCursor.getCount() == 0 || imageCursor == null) {
                Toast.makeText(getActivity(), "기기에 이미지 파일이 없습니다.", Toast.LENGTH_SHORT).show();
                // 이미지 없으니 바로 이전 Mypage Fragment 로 결과값 없다고 전달
            }

            imageCursor.close();
        }

        @NonNull
        @Override
        public GalleryDialog_RecyclerView_Adapter.CustomViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            int width = parent.getResources().getDisplayMetrics().widthPixels / 3;
            ImageView imageView = new ImageView(parent.getContext());
            imageView.setLayoutParams(new LinearLayoutCompat.LayoutParams(width, width));
            imageView.setAdjustViewBounds(false);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);

            return new CustomViewholder(imageView);
        }

        @Override
        public void onBindViewHolder(@NonNull GalleryDialog_RecyclerView_Adapter.CustomViewholder holder, int position) {
            /*Glide.with(holder.imageView.getContext())
                    .load(thumbsDataList.get(position))
                    .into(holder.imageView);*/
        }

        @Override
        public int getItemCount() {
            return thumbsIDList.size();
        }

        private class CustomViewholder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public CustomViewholder(@NonNull ImageView imageView) {
                super(imageView);
                this.imageView = imageView;
            }
        }
    }

    /*@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_frag, container,false);
        //ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.gallery_frag, container, false);
        recyclerView = view.findViewById(R.id.gf_recycler);
        back_Image = view.findViewById(R.id.back_btn_gf);
        ok_TxT = view.findViewById(R.id.check_btn_gf);
        Log.d(TAG, "onCreateView");


        return view;
    }*/
}
