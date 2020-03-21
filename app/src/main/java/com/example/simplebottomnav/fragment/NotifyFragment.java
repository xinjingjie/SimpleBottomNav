package com.example.simplebottomnav.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.simplebottomnav.AppDatabase;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.dao.PicDao;
import com.example.simplebottomnav.viewmodel.ExploreViewModel;

public class NotifyFragment extends Fragment {

    private ExploreViewModel mViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    ImageView imageView;
    TextView testwords;
    Button addButton, deleteButton;
    // PictureRepository pictureRepository;
    private static final String TAG = "FavoriteFragment";
    AppDatabase appDatabase;
    PicDao picDao;
    public static NotifyFragment newInstance() {
        return new NotifyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notify_fragment, container, false);
        imageView = view.findViewById(R.id.imageView6);
        swipeRefreshLayout = view.findViewById(R.id.swipeFav);
        testwords = view.findViewById(R.id.testwords);
        addButton = view.findViewById(R.id.addButton);
        deleteButton = view.findViewById(R.id.deleteButton);
//        appDatabase = Room.databaseBuilder(requireContext(), AppDatabase.class, "database")
//               .allowMainThreadQueries()
//                .build();
//        picDao = appDatabase.getPicDao();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ExploreViewModel.class);
        // TODO: Use the ViewModel
        //  pictureRepository=new PictureRepository(requireContext());
    }
}
