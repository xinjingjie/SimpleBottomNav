package com.example.simplebottomnav.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.toolbox.StringRequest;
import com.example.simplebottomnav.LoginActivity;
import com.example.simplebottomnav.MainActivity;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.JsonData;
import com.example.simplebottomnav.repository.VolleySingleton;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPasswordFragment extends Fragment {
    private TextView password, rePassword;
    private Button submit;
    private VolleySingleton volleySingleton;

    public EditPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_password, container, false);
        password = view.findViewById(R.id.password);
        rePassword = view.findViewById(R.id.repassword);
        submit = view.findViewById(R.id.done);
        volleySingleton = VolleySingleton.getINSTANCE(view.getContext());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _password = password.getText().toString();
                String _rePassword = rePassword.getText().toString();
                if (_password != null && _password.length() != 0 && _password.equals(_rePassword)) {
                    SharedPreferences preference = requireActivity().getSharedPreferences(MainActivity.login_shpName, Context.MODE_PRIVATE);

                    int uid = preference.getInt("UID", 0);
                    editPassword(_password, String.valueOf(uid));
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

            }
        });
    }

    public void editPassword(String password, String uid) {
        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                MainActivity.ServerPath + "user/editPassword?password=" + password + "&uid=" + uid,
                response -> {
                    JsonData result1 = new Gson().fromJson(response, JsonData.class);

                    Log.d("did", "fetchData: success,TotalHits:" + result1.getCode());
                },
                error -> Log.d("did", "onErrorResponse: " + error.toString())

        );
        volleySingleton.getQueue().add(stringRequest);
    }
}
