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

import com.example.simplebottomnav.Adapter.SearchTagAdapter;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.Tag;

import java.util.List;

public class SearchTagFragment extends Fragment {
    private static String search_word;
    private static SearchTagViewModel mViewModel;
    private SearchTagAdapter tagAdapter;
    private RecyclerView recyclerView;

    public static SearchTagFragment newInstance() {
        return new SearchTagFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_tag_fragment, container, false);
        recyclerView = view.findViewById(R.id.search_tag_recyclerView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(SearchTagViewModel.class);
        // recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        tagAdapter = new SearchTagAdapter();
        recyclerView.setAdapter(tagAdapter);
        mViewModel.getSearchTagLiveData().observe(getViewLifecycleOwner(), new Observer<List<Tag>>() {
            @Override
            public void onChanged(List<Tag> tags) {
                Log.d("did", "onChanged: " + tags.size());
                tagAdapter.submitList(tags);
            }
        });
    }

}
