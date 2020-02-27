package com.example.simplebottomnav.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.simplebottomnav.R;
import com.example.simplebottomnav.fragment.viewpager.SearchPicFragment;
import com.example.simplebottomnav.fragment.viewpager.SearchTagFragment;
import com.example.simplebottomnav.fragment.viewpager.SearchUserFragment;
import com.example.simplebottomnav.viewmodel.SearchViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener {
    // private MySuggestionsAdapter mySuggestionsAdapter;
    private SearchViewModel mViewModel;
    private String search_word;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private MaterialSearchBar searchBar;
    private List<String> suggestions = new ArrayList<>();

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        viewPager2 = view.findViewById(R.id.viewpager2);
        tabLayout = view.findViewById(R.id.tab_layout);
        searchBar = view.findViewById(R.id.searchBar);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        menu.clear();
//        inflater.inflate(R.menu.bottom_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        // TODO: Use the ViewModel
//        MyFragmentStateAdapter adapter=new MyFragmentStateAdapter(getActivity());
        viewPager2.setAdapter(new FragmentStateAdapter(requireActivity()) {
            @Override
            public int getItemCount() {
                return 3;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return new SearchPicFragment();
                    case 1:
                        return new SearchTagFragment();
                    default:
                        return new SearchUserFragment();
                }
            }
        });

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("图片");
                        break;
                    case 1:
                        tab.setText("标签");
                        break;
                    case 2:
                        tab.setText("用户");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
        //  LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // mySuggestionsAdapter=new MySuggestionsAdapter(inflater);
        //  searchBar.setCustomSuggestionAdapter(mySuggestionsAdapter);
        searchBar.setMaxSuggestionCount(2);
        searchBar.setHint("Find Pictures..");
        searchBar.setCardViewElevation(10);
        //   mySuggestionsAdapter.setSuggestions(suggestions);
        searchBar.onClick(requireView());
        searchBar.setOnSearchActionListener(this);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("LOG_TAG", getClass().getSimpleName() + " text changed " + searchBar.getText());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
//        Bundle bundle=new Bundle();
//        bundle.putString("search_word",searchBar.getText());
//        Log.d("TAG", "onSearchConfirmed: 搜索"+searchBar.getText());
//        NavController navController = Navigation.findNavController(getView());
//        navController.navigate(R.id.action_searchFragment_to_searchPicFragment, bundle);
        search_word = searchBar.getText();
        SearchPicFragment.setSearch_word(search_word);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                if ( SearchPicFragment.result == 0 ) {
                    Toast toast = Toast.makeText(requireActivity(), "没有搜索到！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER | Gravity.FILL_HORIZONTAL, 0, 0);
                    toast.show();
                }
            }
        };
        handler.postDelayed(runnable, 2000);

    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_BACK:
                Navigation.findNavController(requireView()).navigate(R.id.action_searchFragment_self);
                break;
//                searchBar.closeSearch();
//                break;
            case MaterialSearchBar.BUTTON_NAVIGATION:
                Toast.makeText(requireActivity(), "暂时没用", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onButtonClicked: ");
        }
    }


//static class MyFragmentStateAdapter extends FragmentStateAdapter{
//    FragmentActivity fragmentActivity;
//
//    public MyFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity) {
//        super(fragmentActivity);
//        this.fragmentActivity=fragmentActivity;
//    }
//
//    @NonNull
//    @Override
//    public Fragment createFragment(int position) {
//        switch (position){
//            case 0:
//                if ( fragmentActivity==null )
//                Log.d("TAG", "fragmentFragment is null ");
//                return new SearchPicFragment();
//            case 1:
//                return new SearchTagFragment();
//            default:
//                return new SearchUserFragment();
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return 3;
//    }
//}
}
