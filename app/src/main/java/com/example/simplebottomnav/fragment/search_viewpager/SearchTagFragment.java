package com.example.simplebottomnav.fragment.search_viewpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplebottomnav.Adapter.SearchPicAdapter;
import com.example.simplebottomnav.R;

public class SearchTagFragment extends Fragment {
    private static String search_word;
    private static SearchPicViewModel mViewModel;
    private SearchPicAdapter picAdapter;
    private RecyclerView recyclerView;

    public static SearchTagFragment newInstance() {
        return new SearchTagFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_tag_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(SearchPicViewModel.class);
        // recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

    }

}
