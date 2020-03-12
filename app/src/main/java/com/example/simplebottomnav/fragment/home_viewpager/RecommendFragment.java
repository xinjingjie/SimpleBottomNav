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
import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.repository.GetPicKey;
import com.example.simplebottomnav.repository.LoadPic;
import com.example.simplebottomnav.viewmodel.PicViewModel;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

public class RecommendFragment extends Fragment {

    private PicViewModel mViewModel;
    private RecyclerView recyclerView;
    private PicAdapter picAdapter1;
    private PicAdapter picAdapter2;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionMenu addButton;

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recommerd_fragment, container, false);
        recyclerView = view.findViewById(R.id.home_recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeHome);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(PicViewModel.class);
        // TODO: Use the ViewModel
        addButton = requireActivity().findViewById(R.id.addButton);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 1));
        picAdapter1 = new PicAdapter(mViewModel, PicAdapter.NORMAL_VIEW);
        picAdapter2 = new PicAdapter(mViewModel, PicAdapter.CARD_VIEW);
        Log.d("what", "onActivityCreated: ");
        recyclerView.setAdapter(picAdapter1);
        mViewModel.getPhotoListLive().observe(getViewLifecycleOwner(), new Observer<List<Picture>>() {
            @Override
            public void onChanged(List<Picture> pictures) {
                Log.d("did", "onChanged: " + pictures.size());
                if (mViewModel.getIsToScrollTop()) {
                    Log.d("did", "scrollToTop");
                    recyclerView.smoothScrollToPosition(0);

                    mViewModel.setToScrollTop(false);
                }
                picAdapter2.submitList(pictures);
                picAdapter1.submitList(pictures);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        mViewModel.getDataState().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d("TAG", "onChanged:--- " + integer);
                picAdapter1.setFooter_state(integer);
                picAdapter2.setFooter_state(integer);
                picAdapter2.notifyItemChanged(picAdapter2.getItemCount() - 1);
                picAdapter1.notifyItemChanged(picAdapter1.getItemCount() - 1);
                if (integer == LoadPic.NETWORK_ERROR) {
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        });
        if (mViewModel.getPhotoListLive().getValue() == null) {
            mViewModel.setPhotoListLive(LoadPic.FIND_TYPE_RECOMMEND, GetPicKey.getFreshKey());
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("did", "onRefresh: scrollToTop");
                recyclerView.smoothScrollToPosition(0);
                mViewModel.resetData();
                mViewModel.setPhotoListLive(LoadPic.FIND_TYPE_RECOMMEND, GetPicKey.getFreshKey());

            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        addButton.setVisibility(View.VISIBLE);
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        addButton.setVisibility(View.INVISIBLE);
                        break;


                }
            }

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
                        mViewModel.setPhotoListLive(LoadPic.FIND_TYPE_RECOMMEND, GetPicKey.getLeastKey());
                    }
                    //mViewModel.setPhotoListLive(GetPicKey.getFreshKey());
                }
            }
        });
    }

}
