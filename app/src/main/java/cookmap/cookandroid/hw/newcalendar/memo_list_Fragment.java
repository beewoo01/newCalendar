package cookmap.cookandroid.hw.newcalendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cookmap.cookandroid.hw.newcalendar.Database.Content_Room;
import cookmap.cookandroid.hw.newcalendar.Database.Database_Room;

public class memo_list_Fragment extends Fragment{
    private List<Content_Room> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.memo_list_fragment, container, false);
        list = new ArrayList<>();

        Bundle bundle = getArguments();
        int position = bundle.getInt("position");
        String date = bundle.getString("date");
        Log.d("FragDate?", date+ ", "+ position);

        list = Database_Room.getInstance(getActivity()).getDao().getClickMemo(date);



        View backBtn = view.findViewById(R.id.back_in_mfrag);
        RecyclerView recyclerView = view.findViewById(R.id.memo_recycler_mfrag);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new Memo_List_Adapter(getActivity(), list));
        backBtn.setOnClickListener(onClickListener);


        return view;
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // backBtn = 종료
        }
    };
}
