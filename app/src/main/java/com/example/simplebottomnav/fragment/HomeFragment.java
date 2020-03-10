package com.example.simplebottomnav.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.simplebottomnav.MainActivity;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.fragment.home_viewpager.RecommerdFragment;
import com.example.simplebottomnav.fragment.home_viewpager.SubscribedFragment;
import com.example.simplebottomnav.viewmodel.PicViewModel;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {
    private PicViewModel mViewModel;
    private FloatingActionButton camera;
    private FloatingActionButton gallery;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    // 申请相机权限的requestCode
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;
    //用于保存拍照图片的uri
    private Uri mCameraUri;

    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;

    // 是否是Android 10以上手机
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        camera = view.findViewById(R.id.camera);
        gallery = view.findViewById(R.id.gallery);
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
                if (position == 0) {
                    return new RecommerdFragment();
                }
                return new SubscribedFragment();
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
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.selectFromGalley();
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndCamera();

                Log.d("TAG", "camera is onClicked: ");
            }
        });
    }

    private void checkPermissionAndCamera() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(requireActivity().getApplication(),
                Manifest.permission.CAMERA);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            //有调起相机拍照。
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.openCamera();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA},
                    PERMISSION_CAMERA_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //允许权限，有调起相机拍照。
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.openCamera();
            } else {
                //拒绝权限，弹出提示框。
                Toast.makeText(requireActivity(), "拍照权限被拒绝", Toast.LENGTH_LONG).show();
            }
        }
    }


}

