package com.example.simplebottomnav.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.simplebottomnav.R;
import com.example.simplebottomnav.fragment.home_viewpager.Recommend_home_Fragment;
import com.example.simplebottomnav.fragment.home_viewpager.Subscribed_home_Fragment;
import com.example.simplebottomnav.viewmodel.PicViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {
    private PicViewModel mViewModel;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        viewPager2 = view.findViewById(R.id.viewPager2_home);
        tabLayout = view.findViewById(R.id.tab_layout_home);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(PicViewModel.class);
        viewPager2.setAdapter(new FragmentStateAdapter(requireActivity()) {
            @Override
            public int getItemCount() {
                return 2;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return new Recommend_home_Fragment();
                    default:
                        return new Subscribed_home_Fragment();
                }
            }
        });
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("推荐");
                        break;
                    case 1:
                        tab.setText("关注");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
    }

}
