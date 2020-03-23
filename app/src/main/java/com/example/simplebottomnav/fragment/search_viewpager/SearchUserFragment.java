package com.example.simplebottomnav.fragment.search_viewpager;

import android.os.Bundle;
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

import com.example.simplebottomnav.Adapter.SearchUserAdapter;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.User;

import java.util.List;

public class SearchUserFragment extends Fragment {
    private SearchUserViewModel mViewModel;
    private RecyclerView recyclerView;
    private SearchUserAdapter searchUserAdapter;
    public static SearchUserFragment newInstance() {
        return new SearchUserFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_user_fragment, container, false);
        recyclerView = view.findViewById(R.id.search_user_recyclerView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(SearchUserViewModel.class);
        // TODO: Use the ViewModel
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        searchUserAdapter = new SearchUserAdapter(mViewModel);
        recyclerView.setAdapter(searchUserAdapter);

        mViewModel.getSearchUserLivaData().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                searchUserAdapter.submitList(users);
            }
        });
    }

}
