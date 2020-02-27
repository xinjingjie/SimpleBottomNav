package com.example.simplebottomnav.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.simplebottomnav.Adapter.PicAdapter;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.PhotoItem;
import com.example.simplebottomnav.viewmodel.PicViewModel;

import java.util.List;

public class HomeFragment extends Fragment {
    private PicViewModel mViewModel;
    private RecyclerView recyclerView;
    private PicAdapter picAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView = view.findViewById(R.id.picsrecycleView);
        swipeRefreshLayout = view.findViewById(R.id.swipeHome);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(PicViewModel.class);
        // TODO: Use the ViewModel

        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 1));
        picAdapter = new PicAdapter();
        recyclerView.setAdapter(picAdapter);
//        mViewModel.fetchData(new VolleyCallBack() {
//            @Override
//            public void onSuccess(List<PhotoItem> result) {
//
//            }
//        });
//

        mViewModel.getPhotoListLive().observe(getViewLifecycleOwner(), new Observer<List<PhotoItem>>() {
            @Override
            public void onChanged(List<PhotoItem> photoItems) {
                Log.d("did", "onChanged: " + photoItems.size());
                picAdapter.submitList(photoItems);
                if ( swipeRefreshLayout.isRefreshing() ) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        if ( mViewModel.getPhotoListLive().getValue() == null ) {
            mViewModel.setPhotoListLive("cat");
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.setPhotoListLive("apple");
            }
        });

    }

//    @Override
//    public void onSuccess(List<PhotoItem> result) {
//        Log.d("did", "onSuccess: ??"+result);
//        this.result=result;
//        mViewModel.photoListLive.setValue(result);
//        Log.d("did", "onSuccess: "+mViewModel.photoListLive);
//        mViewModel.photoListLive.observe(getViewLifecycleOwner(), new Observer<List<PhotoItem>>() {
//            @Override
//            public void onChanged(List<PhotoItem> photoItems) {
//                picAdapter.submitList(photoItems);
//            }
//        });


//    }

}
