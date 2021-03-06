package com.example.simplebottomnav.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.simplebottomnav.Adapter.UserPicAdapter;
import com.example.simplebottomnav.LoginActivity;
import com.example.simplebottomnav.MainActivity;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.databinding.AccountFragmentBinding;
import com.example.simplebottomnav.repository.MD5Utils;
import com.example.simplebottomnav.repository.UploadServerUtil;
import com.example.simplebottomnav.viewmodel.AccountViewModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import me.shaohui.bottomdialog.BottomDialog;

import static android.content.Context.MODE_PRIVATE;

public class AccountFragment extends Fragment {
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;
    private AccountViewModel mViewModel;
    AccountFragmentBinding binding;
    private UserPicAdapter picAdapter;
    private String url1 = MainActivity.ServerPath + "user/updateProfilePic";
    private LiveData<List<Picture>> allUserPics;
    //List<Picture> allUserPics;

    //    boolean isFlesh = true;
    NavController navController;

    public static AccountFragment newInstance(Bundle bundle) {
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("pic", "onViewCreated: --------------------------");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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


        SharedPreferences preference = Objects.requireNonNull(getActivity()).getSharedPreferences(MainActivity.login_shpName,
                MODE_PRIVATE);
        String pre_username = preference.getString("username", null);
        int uid = preference.getInt("UID", 0);
        binding.username.setText(pre_username);
        binding.fansNumber.setText(String.valueOf(preference.getInt("fans_number", 0)));
        binding.picNumber.setText(String.valueOf(preference.getInt("pic_number", 0)));
        binding.subNumber.setText(String.valueOf(preference.getInt("sub_number", 0)));
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
/*
加载头像
 */
        String profile_picture_url = preference.getString("profile_picture", null);
//        if (profile_picture_url != null) {
//            Glide.with(this)
//                    .load(profile_picture_url)
//                    .placeholder(R.drawable.logo)
//                    .into(binding.profilePicture);
//        }
        Picture profilePic = mViewModel.getProfilePic(uid);
        if (profilePic != null) {
            Glide.with(this)
                    .load(profilePic.getLocation())
                    .placeholder(R.drawable.logo)
                    .into(binding.profilePicture);
            Log.d("loadProfile", "from: sqlite");
        } else {
            Glide.with(this)
                    .load(profile_picture_url)
                    .placeholder(R.drawable.logo)
                    .into(binding.profilePicture);
            Log.d("loadProfile", "from: pref");

        }

        String profile_background_url = preference.getString("background_image", null);
        if (profile_background_url != null) {
            try {
                binding.toolbar6.setBackground(new OpenPic2().execute(profile_background_url).get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }

        //        if (profile_background_url!=null){
//            Glide.with(this)
//                    .load(profile_background_url)
//                    .addListener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            binding.toolbar6.setBackground(resource);
//
//                            return false;
//                        }
//                    })
//                    .placeholder(R.drawable.donut)
//                    ;
//        }
        binding.toolbar6.setClickable(true);
        binding.profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Picture picture = new Picture();
                Log.d("TAG", "onClick: " + profile_picture_url);
                picture.setLocation(profile_picture_url);
                bundle.putParcelable("Detail_Pic", picture);
                bundle.putBoolean("isProfile", true);
                navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_accountFragment_to_detailPicFragment2, bundle);
            }
        });
        /*
        点击background
         */
        binding.toolbar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Picture picture = new Picture();
                Log.d("TAG", "onClick: " + profile_background_url);
                picture.setLocation(profile_background_url);
                bundle.putParcelable("Detail_Pic", picture);
                bundle.putBoolean("isProfile", false);
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_accountFragment_to_detailPicFragment2, bundle);
            }
        });

        binding.toolbar6.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout_button:
                        Log.d("TAG", "onOptionsItemSelected: ");
                        BottomDialog.create(requireActivity().getSupportFragmentManager())

                                .setLayoutRes(R.layout.dialog_bottomsheet)
                                .setViewListener(new BottomDialog.ViewListener() {
                                    @Override
                                    public void bindView(View v) {
                                        initView(v);

                                    }
                                })// dialog layout
                                .show();
                        return true;
                    default:
                        return false;
                }
            }

        });


        binding.userPics.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        picAdapter = new UserPicAdapter("MINE");
        binding.userPics.setAdapter(picAdapter);
        allUserPics = mViewModel.getAllUserPic();


        allUserPics.observe(getViewLifecycleOwner(), pictures -> {
            Log.d("pic", "onChanged: " + pictures.size());
            Log.d("pic", "onChanged: onflesh");
            picAdapter.submitList(pictures);
        });


        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.setType("BACKGROUND");
                mainActivity.selectFromGalley();
            }
        });
        assert getArguments() != null;
        if (getArguments() != null) {
            if (getArguments().getString("IMAGEURL") != null) {
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.setType("POST");
                String x = getArguments().getString("IMAGEURL");
                /*
                判断mainActivity返回的bundle是设置什么的
                */
                if (getArguments().getBoolean("isBackGround")) {
//                    if (isAndroidQ) {
//                        binding.toolbar6.setbac.setImageURI(Uri.parse(x));
//                    } else {
//                        assert getArguments() != null;
//                        Log.d("TAG", "onActivityCreated: " + x);
//                        binding.profilePicture.setImageBitmap(BitmapFactory.decodeFile(x));
//                    }
//                    try {
//                        String result = (String) new PostPicTask().execute(x, uid, url2).get();
//                        SharedPreferences.Editor editor = preference.edit();
//                        editor.putString("profile_picture", x);
//                        editor.apply();
//                        if (!("false".equals(result))) {
//                            Toast.makeText(requireActivity(), "修改成功", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(requireActivity(), "修改失败", Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (ExecutionException | InterruptedException e) {
//                        e.printStackTrace();
//                    }
                } else {
                    Log.d("TAG", "onActivityCreated: qqqqqqqqqqqqqqq" + getArguments().getString("IMAGEURL"));


                    if (isAndroidQ) {
                        binding.profilePicture.setImageURI(Uri.parse(x));
                    } else {
                        assert getArguments() != null;
                        Log.d("TAG", "onActivityCreated: " + x);
                        binding.profilePicture.setImageBitmap(BitmapFactory.decodeFile(x));
                        mViewModel.updateProfilePic(x, String.valueOf(uid));

                    }
                    try {
                        String result = (String) new PostPicTask().execute(x, uid, url1).get();
                        SharedPreferences.Editor editor = preference.edit();
                        editor.putString("profile_picture", x);
                        editor.apply();
                        if (!("false".equals(result))) {
                            Toast.makeText(requireActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireActivity(), "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        /*
        修改个人信息
         */
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_accountFragment_to_editInfoFragment);
            }
        });
    }

    static class PostPicTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            String path = objects[0].toString();
            String uid = objects[1].toString();
            String url = objects[2].toString();
            Map<String, String> map = new HashMap<String, String>();
            Log.d("TAG", "doInBackground: " + path);
            map.put("uid", uid);
            Log.d("TAG", "doInBackground: " + uid);
            try {
                return UploadServerUtil.upLoadFilePost(url, map, new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "false";
        }


    }

    static class OpenPic2 extends AsyncTask<String, Void, Drawable> {

        @Override
        protected Drawable doInBackground(String... strings) {
            try {
                String location = strings[0];
                Log.d("TAG", "savePhoto: " + location);
                Drawable drawable = Drawable.createFromStream(new URL(location).openStream(), null);
                return drawable;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public void initView(final View view) {
        Button editPassword = view.findViewById(R.id.editpassword);
        Button logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preference = Objects.requireNonNull(getActivity()).getSharedPreferences(MainActivity.login_shpName,
                        MODE_PRIVATE);
                String pre_username = preference.getString("username", null);
                int uid = preference.getInt("UID", 0);
                if (uid != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    builder.setTitle("确定要退出吗?");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences preference = requireActivity().getSharedPreferences(MainActivity.login_shpName,
                                    MODE_PRIVATE);
                            SharedPreferences.Editor editor = preference.edit();
                            editor.clear();
                            editor.putBoolean("isNeedDownLoad", false);
                            editor.putInt("UID", uid);
                            editor.apply();
                            SharedPreferences likedPref = requireActivity().getSharedPreferences(MainActivity.liked_prefName, MODE_PRIVATE);
                            SharedPreferences.Editor likedEditor = likedPref.edit();
                            likedEditor.clear();
                            likedEditor.apply();
                            SharedPreferences relationPref = requireActivity().getSharedPreferences(MainActivity.relation_prefName, MODE_PRIVATE);
                            SharedPreferences.Editor relationEditor = relationPref.edit();
                            relationEditor.clear();
                            relationEditor.apply();
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    });
                    builder.setNegativeButton("取消", (dialog, which) -> {

                    });
                    builder.show();
                } else {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(requireActivity());
                inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
                inputServer.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                inputServer.setTransformationMethod(new PasswordTransformationMethod());
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("为了安全，请输入您原来的密码").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String _password = inputServer.getText().toString();
                        if (_password != null && !_password.isEmpty()) {
                            SharedPreferences preferences = getActivity().getSharedPreferences(MainActivity.login_shpName, MODE_PRIVATE);
                            if (preferences.getString("password", null).equals(MD5Utils.digest(_password))) {
                                Toast.makeText(requireActivity(), "密码为" + _password, Toast.LENGTH_SHORT).show();
                                NavController navController = Navigation.findNavController(requireView());
                                navController.navigate(R.id.action_accountFragment_to_editPasswordFragment);

                            } else {
                                Toast.makeText(requireActivity(), "密码错误", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(requireActivity(), "密码为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
            }
        });
    }
}
