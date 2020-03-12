package com.example.simplebottomnav.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.simplebottomnav.Adapter.UserPicAdapter;
import com.example.simplebottomnav.LoginActivity;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.databinding.AccountFragmentBinding;
import com.example.simplebottomnav.viewmodel.AccountViewModel;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AccountFragment extends Fragment {

    private AccountViewModel mViewModel;
    AccountFragmentBinding binding;
    private UserPicAdapter picAdapter;
    boolean isFlesh = true;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("pic", "onViewCreated: --------------------------");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //  View view = inflater.inflate(R.layout.account_fragment, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.account_fragment, container, false);
        View view = binding.getRoot();
        return view;


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(AccountViewModel.class);
        // TODO: Use the ViewModel

        SharedPreferences preference = getActivity().getSharedPreferences("login_info",
                MODE_PRIVATE);
        String pre_username = preference.getString("username", null);
        binding.username.setText(pre_username);
        binding.fansNumber.setText(String.valueOf(preference.getInt("fans_number", 0)));
        binding.picNumber.setText(String.valueOf(preference.getInt("pic_number", 0)));
        binding.subNumber.setText(String.valueOf(preference.getInt("sub_number", 0)));
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.toolbar6.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout_button:
                        SharedPreferences preference = getActivity().getSharedPreferences("login_info",
                                MODE_PRIVATE);
                        String pre_username = preference.getString("username", null);
                        Log.d("TAG", "onOptionsItemSelected: ");
                        if (pre_username != null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                            builder.setTitle("确定要退出吗?");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.show();
                        } else {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        return true;
                    default:
                        return false;
                }
            }

        });
        int uid = preference.getInt("UID", 0);
        binding.userPics.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        mViewModel.setPhotoListLive(String.valueOf(uid));
        picAdapter = new UserPicAdapter();
        binding.userPics.setAdapter(picAdapter);

        mViewModel.getSearchPhotoLiveData().observe(getViewLifecycleOwner(), new Observer<List<Picture>>() {
            @Override
            public void onChanged(List<Picture> pictures) {
                Log.d("pic", "onChanged: " + pictures.size());
                if (isFlesh == true) {
                    Log.d("pic", "onChanged: onflesh");
                    picAdapter.submitList(pictures);
                }

                isFlesh = false;
            }
        });
    }

}
