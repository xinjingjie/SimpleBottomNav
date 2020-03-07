package com.example.simplebottomnav.fragment.search_viewpager;

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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.simplebottomnav.Adapter.SearchPicAdapter;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.PhotoItem;

import java.util.List;

public class SearchPicFragment extends Fragment {
    private static String search_word;
    private static SearchPicViewModel mViewModel;
    private SearchPicAdapter picAdapter;
    private RecyclerView recyclerView;
    public static int result;

    public static SearchPicFragment newInstance() {
        return new SearchPicFragment();
    }

    public static void setSearch_word(String search_word) {
        SearchPicFragment.search_word = search_word;
        Log.d("search", "onActivityCreated: " + search_word);

        mViewModel.setPhotoListLive(search_word);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_pic_fragment, container, false);
        recyclerView = view.findViewById(R.id.search_pic_recyclerView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(SearchPicViewModel.class);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        picAdapter = new SearchPicAdapter(mViewModel);
        recyclerView.setAdapter(picAdapter);
        mViewModel.getPhotoListLive().observe(getViewLifecycleOwner(), new Observer<List<PhotoItem>>() {
            @Override
            public void onChanged(List<PhotoItem> photoItems) {
                Log.d("did", "onChanged: " + photoItems.size());
                result = photoItems.size();
                picAdapter.submitList(photoItems);
            }
        });
//        String s=getActivity().getIntent().getStringExtra("search_word");


    }

}
