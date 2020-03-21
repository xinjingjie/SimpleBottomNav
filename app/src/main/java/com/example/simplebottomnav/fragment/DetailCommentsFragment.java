package com.example.simplebottomnav.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.simplebottomnav.Adapter.CommentAdapter;
import com.example.simplebottomnav.MainActivity;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.Picture;
import com.example.simplebottomnav.databinding.FragmentDetailCommentsBinding;
import com.example.simplebottomnav.viewmodel.CommentsViewModel;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailCommentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailCommentsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FragmentDetailCommentsBinding binding;
    SharedPreferences preference;
    CommentsViewModel mViewModel;
    CommentAdapter commentAdapter;

    public DetailCommentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailCommentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailCommentsFragment newInstance(String param1, String param2) {
        DetailCommentsFragment fragment = new DetailCommentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        preference = Objects.requireNonNull(getActivity()).getSharedPreferences(MainActivity.login_shpName,
                MODE_PRIVATE);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_comments, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(CommentsViewModel.class);
        commentAdapter = new CommentAdapter();
        binding.allComments.setAdapter(commentAdapter);
        binding.allComments.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        mViewModel.setAllComments(getArguments().getInt("pid"));
        mViewModel.getAllComments().observe(requireActivity(), comments -> commentAdapter.submitList(comments));
        int uid = preference.getInt("UID", 0);
        String username = preference.getString("username", null);
        String profile_picture_url = preference.getString("profile_picture", null);
        Picture profilePic = mViewModel.getProfilePic(uid);
        if (profilePic != null) {
            Glide.with(this)
                    .load(profilePic.getLocation())
                    .placeholder(R.drawable.logo)
                    .into(binding.usericon);
            Log.d("loadProfile", "from: sqlite" + profilePic.getLocation());
        } else {
            Glide.with(this)
                    .load(profile_picture_url)
                    .placeholder(R.drawable.logo)
                    .into(binding.usericon);
            Log.d("loadProfile", "from: pref" + profile_picture_url);

        }

        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.commentMessage.getText() != null) {
                    int pid = getArguments().getInt("pid");
                    Log.d("pid", "onClick: " + pid);
                    mViewModel.uploadComment(pid, uid, username, binding.commentMessage.getText().toString());
                    binding.commentMessage.setText("");
                    mViewModel.setAllComments(pid);

                }

            }
        });
    }
}
