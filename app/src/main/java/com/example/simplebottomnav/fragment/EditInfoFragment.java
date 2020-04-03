package com.example.simplebottomnav.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.simplebottomnav.MainActivity;
import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.User;
import com.example.simplebottomnav.databinding.EditInfoFragmentBinding;
import com.example.simplebottomnav.viewmodel.EditInfoViewModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class EditInfoFragment extends Fragment {

    private EditInfoViewModel mViewModel;
    private EditInfoFragmentBinding binding;
    private Date birthday;

    //在TextView上显示的字符
    public static EditInfoFragment newInstance() {
        return new EditInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_info_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(EditInfoViewModel.class);
        // TODO: Use the ViewModel
        SharedPreferences preference = Objects.requireNonNull(getActivity()).getSharedPreferences(MainActivity.login_shpName,
                MODE_PRIVATE);
        int uid = preference.getInt("UID", 0);
        String username = preference.getString("username", null);
        String email = preference.getString("email", null);
        String phone = preference.getString("phoneNumber", null);
        String gender = preference.getString("gender", null);
        String birthday_message = preference.getString("birthday", null);
        binding.username.setText(username);
        binding.gender.setText(gender);
        binding.email.setText(email);
        binding.telenumber.setText(phone);
        binding.birthday.setText(birthday_message);
        binding.birthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (binding.birthday.isFocused()) {
                    InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    TimePickerView pvTime = new TimePickerBuilder(requireActivity(), new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            String dateString = formatter.format(date);
                            binding.birthday.setText(dateString);
                            birthday = date;
                        }
                    }).build();
                    pvTime.show();
                }
            }
        });

        binding.submit.setOnClickListener(v -> {
            SharedPreferences.Editor editor = preference.edit();
            List<String> list = new ArrayList<String>();
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = formatter.format(birthday);

                list.add(String.valueOf(binding.username.getText()));
                list.add(String.valueOf(binding.email.getText()));
                list.add(String.valueOf(binding.telenumber.getText()));
                list.add(String.valueOf(binding.gender.getText()));
                list.add(String.valueOf(uid));
                list.add(dateString);
                list.add(String.valueOf(getAge(birthday)));
            } catch (Exception e) {
                e.printStackTrace();
            }

            editor.putString("username", list.get(0));
            editor.putString("email", list.get(1));
            editor.putString("phoneNumber", list.get(2));
            editor.putString("gender", list.get(3));
            editor.putString("birthday", list.get(4));
            editor.apply();
            new EditTask().execute(list);


            NavController navController = Navigation.findNavController(v);
            navController.navigateUp();
        });
    }


    static class EditTask extends AsyncTask {
        private final static String path = MainActivity.ServerPath + "user/updateInfo";

        @Override
        protected Object doInBackground(Object[] objects) {
            String username = objects[0].toString();
            String gender = objects[1].toString();
            String email = objects[2].toString();
            String phone = objects[3].toString();
            int uid = Integer.parseInt(objects[4].toString());
            String birthday = objects[5].toString();
            int age = Integer.parseInt(objects[6].toString());
            Log.d("TAG", "doInBackground: " + uid + "--" + age);
            User user = new User(uid, username, gender, null, age, null, phone, email, 0, 0, 0, null, null, birthday);
            Gson gson = new Gson();
            String jsonData = gson.toJson(user);
            Log.d("TAG", "doInBackground: " + jsonData);
            try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(5000);
                conn.getOutputStream().write(String.valueOf(jsonData).getBytes());
                int code = conn.getResponseCode();
                if (code == 200) {
                    InputStream is = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    return br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "error";
        }

    }

    //由出生日期获得年龄
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        return age;
    }
}