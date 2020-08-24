package cookmap.cookandroid.hw.newcalendar;

import android.app.Dialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import java.util.ArrayList;
import java.util.List;

import cookmap.cookandroid.hw.newcalendar.adpater.Gallery_Adapter;

public class Gallery_Dialog extends DialogFragment implements Gallery_Adapter.OnLoadMoreListener{
    RecyclerView recyclerView;
    private ImageView back_Image;
    private TextView addPhoto;
    private ArrayList<String> imgArry;
    private ArrayList<String> thumbsDataList;
    private ArrayList<String> thumbsIDList;
    private Gallery_Adapter adapter;

    private PassDataInterface passDataInterface;

    public interface PassDataInterface{
        void onDataReceived(ArrayList<String> imgAddress);
    }

    public Gallery_Dialog(PassDataInterface passDataInterface){
        this.passDataInterface = passDataInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_frag, container);
        back_Image = view.findViewById(R.id.back_btn_gf);
        addPhoto = view.findViewById(R.id.check_btn_gf);
        recyclerView = view.findViewById(R.id.gf_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        imgArry =new ArrayList<>(10);

        thumbsDataList = new ArrayList<>();
        thumbsIDList = new ArrayList<>();


        adapter = new Gallery_Adapter(this, getActivity());
        adapter.setGridLayoutManager(new GridLayoutManager(getActivity(),3));
        adapter.setRecyclerView(recyclerView);

        back_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.setItemImage(new Gallery_Adapter.getImgListner(){
                    @Override
                    public void onImg(List list) {
                        if (list.size() > 0){
                            for (int i = 0; i < list.size(); i++){
                                imgArry.add(String.valueOf(list.get(i))) ;
                            }
                        }
                        passDataInterface.onDataReceived(imgArry);
                        dismissDialog();


                    }
                });
            }
        });


        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }



        recyclerView.setAdapter(adapter);

        return view;
    }

    private void dismissDialog(){
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("Gallery_Dialog");

        if (fragment != null){
            DialogFragment dialogFragment = (DialogFragment) fragment;
            dialogFragment.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        loadData();
    }

    private void loadData() {
        thumbsIDList.clear();
        thumbsDataList.clear();
        getThumbInfo(thumbsIDList, thumbsDataList);
        adapter.addAll(thumbsDataList);
    }

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

    @Override
    public void onLoadMore() {

        adapter.setProgressMore(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                thumbsIDList.clear();
                thumbsDataList.clear();

                adapter.setProgressMore(false);

                getThumbInfo(thumbsIDList, thumbsDataList);

                adapter.addItemMore(thumbsDataList);
                adapter.setMoreLoading(false);
            }
        }, 2000);

    }


    private void getThumbInfo(ArrayList<String> thumbsIDs, ArrayList<String> thumbsDatas) {
        try {
            String[] proj = {MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.SIZE};
            ContentResolver contentResolver = getActivity().getContentResolver();
            String sortOrder = String.format("%s limit 150 "+ "OFFSET " +(adapter.getItemCount()), MediaStore.Images.Media.DATE_TAKEN + " DESC");

            Cursor imageCursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    proj , null, null, sortOrder);

            if (imageCursor != null && imageCursor.moveToFirst()) {
                String thumbsID;
                String thumbsImageID;
                String thumbsData;

                int thumbsIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
                int thumbsDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                int thumbsImageIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                do {
                    thumbsID = imageCursor.getString(thumbsIDCol);
                    thumbsData = imageCursor.getString(thumbsDataCol);
                    thumbsImageID = imageCursor.getString(thumbsImageIDCol);
                    if (thumbsImageID != null) {
                        thumbsIDs.add(thumbsID);
                        thumbsDatas.add(thumbsData);
                    }
                } while (imageCursor.moveToNext());
            }

            imageCursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }




    }



}
