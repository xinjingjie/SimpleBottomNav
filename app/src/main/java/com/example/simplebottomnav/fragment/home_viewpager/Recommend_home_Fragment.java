package com.example.simplebottomnav.fragment.home_viewpager;

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
import com.example.simplebottomnav.repository.GetPicKey;
import com.example.simplebottomnav.repository.LoadPic;
import com.example.simplebottomnav.viewmodel.PicViewModel;

import java.util.List;

public class Recommend_home_Fragment extends Fragment {

    private PicViewModel mViewModel;
    private RecyclerView recyclerView;
    private PicAdapter picAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static Recommend_home_Fragment newInstance() {
        return new Recommend_home_Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recommend_home__fragment, container, false);
        recyclerView = view.findViewById(R.id.home_recommed_recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeHome_recom);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(PicViewModel.class);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 1));
        picAdapter = new PicAdapter(mViewModel);
        Log.d("what", "onActivityCreated: ");
        recyclerView.setAdapter(picAdapter);

        mViewModel.getPhotoListLive().observe(getViewLifecycleOwner(), new Observer<List<PhotoItem>>() {
            @Override
            public void onChanged(List<PhotoItem> photoItems) {
                Log.d("did", "onChanged: " + photoItems.size());
                if (mViewModel.getIsToScrollTop()) {
                    Log.d("did", "scrollToTop");
                    recyclerView.scrollToPosition(0);
                    mViewModel.setToScrollTop(false);
                }

                picAdapter.submitList(photoItems);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        mViewModel.getDataState().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d("TAG", "onChanged:--- " + integer);
                picAdapter.setFooter_state(integer);
                picAdapter.notifyItemChanged(picAdapter.getItemCount() - 1);
                if (integer == LoadPic.NETWORK_ERROR) {
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        });
        if (mViewModel.getPhotoListLive().getValue() == null) {
            mViewModel.setPhotoListLive(GetPicKey.getFreshKey());
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("did", "onRefresh: scrollToTop");
                recyclerView.scrollToPosition(0);
                mViewModel.resetData();
                mViewModel.setPhotoListLive(GetPicKey.getFreshKey());

            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
                    return;
                }
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int position = layoutManager.findLastVisibleItemPosition();
                if (position == recyclerView.getAdapter().getItemCount() - 1) {
                    if (mViewModel.getDataState().getValue() == LoadPic.CAN_LOAD_MORE || mViewModel.getDataState().getValue() == LoadPic.INIT_STATE) {
                        mViewModel.setPhotoListLive(GetPicKey.getLeastKey());
                    }
                    //mViewModel.setPhotoListLive(GetPicKey.getFreshKey());
                }
            }
        });

    }

}
