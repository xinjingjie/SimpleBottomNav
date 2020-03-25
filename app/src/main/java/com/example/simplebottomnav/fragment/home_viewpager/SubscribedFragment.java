package com.example.simplebottomnav.fragment.home_viewpager;

import android.content.SharedPreferences;
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

import com.example.simplebottomnav.Adapter.SubscribedAdapter;
import com.example.simplebottomnav.MainActivity;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.repository.LoadPic;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

public class SubscribedFragment extends Fragment {

    private SubscribedViewModel mViewModel;
    private RecyclerView recyclerView;
    private SubscribedAdapter subscribedAdapter;
    // private PicAdapter picAdapter2;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionMenu addButton;
    SharedPreferences preferences;
    public static SubscribedFragment newInstance() {
        return new SubscribedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.subscribed_fragment, container, false);
        recyclerView = view.findViewById(R.id.home_recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeHome);
        preferences = requireActivity().getSharedPreferences(MainActivity.login_shpName, requireContext().MODE_PRIVATE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(SubscribedViewModel.class);
        // TODO: Use the ViewModel
        addButton = requireActivity().findViewById(R.id.addButton);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 1));
        subscribedAdapter = new SubscribedAdapter();
        // picAdapter2 = new PicAdapter(mViewModel, PicAdapter.CARD_VIEW);
        Log.d("what", "onActivityCreated: ");
        recyclerView.setAdapter(subscribedAdapter);
        mViewModel.getPhotoLiveData().observe(getViewLifecycleOwner(), new Observer<List<Picture>>() {
            @Override
            public void onChanged(List<Picture> pictures) {
                Log.d("fuckyou", "onChanged: " + pictures.size());
                recyclerView.setItemViewCacheSize(pictures.size());
                //  picAdapter2.submitList(pictures);
                subscribedAdapter.submitList(pictures);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        if (mViewModel.getPhotoLiveData().getValue() == null) {
            mViewModel.setPhotoListLive(LoadPic.FIND_TYPE_SUBSCRIBED, String.valueOf(preferences.getInt("UID", 0)));
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("did", "onRefresh: scrollToTop");
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                manager.scrollToPosition(0);
                mViewModel.setPhotoListLive(LoadPic.FIND_TYPE_SUBSCRIBED, String.valueOf(preferences.getInt("UID", 0)));
            }
        });
    }

}
