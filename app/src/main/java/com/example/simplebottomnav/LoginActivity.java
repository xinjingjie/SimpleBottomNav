package com.example.simplebottomnav;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.toolbox.StringRequest;
import com.example.simplebottomnav.bean.JsonData;
import com.example.simplebottomnav.bean.User;
import com.example.simplebottomnav.repository.MD5Utils;
import com.example.simplebottomnav.repository.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    EditText _phoneText;
    EditText _passwordText;
    Button _loginButton;
    TextView _signupLink;
    SharedPreferences preference;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        preference = this.getApplication().getSharedPreferences(MainActivity.login_shpName,
                MODE_PRIVATE);
        boolean isLogin = preference.getBoolean("isLogin", false);
        if (isLogin) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        setContentView(R.layout.activity_login);
        _phoneText = findViewById(R.id.input_phone);
        _passwordText = findViewById(R.id.input_password);
        _loginButton = findViewById(R.id.btn_login);
        _signupLink = findViewById(R.id.link_signup);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });


        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        preference = getSharedPreferences(MainActivity.login_shpName,
                MODE_PRIVATE);
        String pre_phoneNumber = preference.getString("phoneNumber", null);
        // String pre_pwd=preference.getString("password","null");
        if (pre_phoneNumber != null) {
            _phoneText.setText(pre_phoneNumber);
        }
        //  _passwordText.setText(pre_pwd);
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String name = _phoneText.getText().toString();
        String password = _passwordText.getText().toString();
        Log.d(TAG, "login: ?-----" + name + password);
        LoginTask loginTask = new LoginTask();
        String message = null;
        try {
            message = (String) loginTask.execute(name, password).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "login: " + message);
        if ("error".equals(message)) {
            Toast.makeText(this, "网络出错！", Toast.LENGTH_SHORT).show();
            onLoginFailed();
        } else {
            Gson gson1 = new Gson();
            JsonData jsonData = gson1.fromJson(message, JsonData.class);
            Log.d(TAG, "login: " + jsonData.getCode());
            if (jsonData.getCode() == -1) {
                Toast.makeText(this, "账号不存在！", Toast.LENGTH_SHORT).show();
                onLoginFailed();
            } else if (jsonData.getCode() == 0) {
                int begin = message.indexOf(",");
                int end = message.lastIndexOf(",");
                Log.d(TAG, "login: " + begin + "--" + end);
                String userJson = message.substring(begin + 8, end);
                Gson gson2 = new Gson();
                User loginUser = gson2.fromJson(userJson, User.class);
                Log.d(TAG, "login: " + loginUser);

                if (!loginUser.getPassword().equals(password)) {
                    Toast.makeText(this, "密码错误！", Toast.LENGTH_SHORT).show();
                    onLoginFailed();
                } else {

                    SharedPreferences.Editor editor = preference.edit();
                    if (preference.getInt("UID", 0) != loginUser.getUid()) {
                        editor.putBoolean("isNeedDownLoad", true);
                        editor.remove("UID");
                        editor.apply();
                    }
                    editor.putString("username", loginUser.getUsername());
                    editor.putString("email", loginUser.getEmail());
                    editor.putString("phoneNumber", loginUser.getTelephone());
                    editor.putString("password", MD5Utils.digest(loginUser.getPassword()));
                    editor.putString("gender", loginUser.getGender());
                    editor.putInt("age", loginUser.getAge());
                    editor.putString("create_time", loginUser.getCreate_time());
                    editor.putInt("pic_number", loginUser.getPic_number());
                    editor.putInt("UID", loginUser.getUid());
                    editor.putInt("fans_number", loginUser.getFans_number());
                    editor.putInt("sub_number", loginUser.getSub_number());
                    String profilePic = loginUser.getProfile_picture() == null ? "http://192.168.2.107:8080/profilePicture/logo.png" : loginUser.getProfile_picture();
                    editor.putString("profile_picture", profilePic);
                    String backgroundPic = loginUser.getBackground_image() == null ? "http://192.168.2.107:8080/backgroundPic/donut.jpg" : loginUser.getBackground_image();
                    editor.putString("background_image", backgroundPic);
                    editor.putBoolean("isLogin", true);
                    editor.apply();
                    new FindAllFollowId().execute(loginUser.getUid());
                    onLoginSuccess();
                }
            }
        }
        progressDialog.dismiss();
        // TODO: Implement your own authentication logic here.

    }

    /*
    查找所有关注的人的id
     */
    class FindAllFollowId extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            int uid = integers[0];
            StringRequest stringRequest = new StringRequest(
                    StringRequest.Method.GET,
                    MainActivity.ServerPath + "relation/findAlFollowId?uid=" + uid,
                    response -> {
                        List<Integer> allFollowId = new Gson().fromJson(response, new TypeToken<List<Integer>>() {
                        }.getType());
                        Log.d("did", "FOLLOW RESULT::" + allFollowId.size());
                        SharedPreferences relationPreference = getSharedPreferences(MainActivity.relation_prefName,
                                MODE_PRIVATE);
                        for (int i = 0; i < allFollowId.size(); i++) {
                            SharedPreferences.Editor editor = relationPreference.edit();
                            editor.putString("" + allFollowId.get(i), "关注");
                            editor.apply();
                        }
                    },
                    error -> Log.d("did", "onErrorResponse: " + error.toString())

            );
            VolleySingleton.getINSTANCE(getApplication()).getQueue().add(stringRequest);
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //给予权限后
                } else {
                    Toast.makeText(this, "保存失败！请给予权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                String pre_phoneNumber = preference.getString("phoneNumber", null);
                // String pre_pwd=preference.getString("password","null");
                if (pre_phoneNumber != null) {
                    _phoneText.setText(pre_phoneNumber);
                }
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically

            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String mobile = _phoneText.getText().toString();
        String password = _passwordText.getText().toString();

        if (mobile.isEmpty() || mobile.length() != 11) {
            _phoneText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _phoneText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    static class LoginTask extends AsyncTask {
        //        private final static String path = "http://112.124.44.175:8080/api/user/testSend";
        private final static String path = "http://192.168.2.107:8080/api/user/testSend";

        @Override
        protected Object doInBackground(Object[] objects) {
            String name = objects[0].toString();
            String pwd = objects[1].toString();
            try {
                URL url = new URL(path + "?name=" + name + "&pwd=" + pwd);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
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

}
