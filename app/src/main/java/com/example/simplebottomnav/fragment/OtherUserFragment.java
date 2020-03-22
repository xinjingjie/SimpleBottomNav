package com.example.simplebottomnav.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.simplebottomnav.Adapter.UserPicAdapter;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.User;
import com.example.simplebottomnav.databinding.OtherUserFragmentBinding;
import com.example.simplebottomnav.viewmodel.OtherUserViewModel;

import java.util.concurrent.ExecutionException;

public class OtherUserFragment extends Fragment {

    private OtherUserViewModel mViewModel;
    private OtherUserFragmentBinding binding;
    private UserPicAdapter mAdapter;

    public static OtherUserFragment newInstance() {
        return new OtherUserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.other_user_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OtherUserViewModel.class);
        // TODO: Use the ViewModel
        if (getArguments() != null) {
            int uid = getArguments().getInt("uid");
            Log.d("OtherUserFragment", "onActivityCreated: " + uid);
            mViewModel.getFoundUser(uid).observe(requireActivity(), new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    binding.fansNumber.setText(String.valueOf(user.getFans_number()));
                    binding.picNumber.setText(String.valueOf(user.getPic_number()));
                    binding.subNumber.setText(String.valueOf(user.getSub_number()));
                    binding.username.setText(user.getUsername());
                    Glide.with(requireView()).load(user.getProfile_picture()).placeholder(R.drawable.logo).into(binding.profilePicture);
                    // Glide.with(requireView()).load(user.getBackground_image()).placeholder(R.drawable.geranium).into(binding.);
                    if (user.getBackground_image() != null) {
                        try {
                            binding.toolbar6.setBackground(new AccountFragment.OpenPic2().execute(user.getBackground_image()).get());
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            mAdapter = new UserPicAdapter("OTHER");
            binding.userPics.setAdapter(mAdapter);
            binding.userPics.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mViewModel.getAllFoundUserPic(uid).observe(getViewLifecycleOwner(), pictures -> {
                Log.d("pic", "onChanged: " + pictures.size());
                Log.d("pic", "onChanged: onflesh");
                mAdapter.submitList(pictures);
            });
        }
    }

}
