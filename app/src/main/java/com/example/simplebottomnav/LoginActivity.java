package com.example.simplebottomnav;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simplebottomnav.bean.JsonData;
import com.example.simplebottomnav.bean.User;
import com.example.simplebottomnav.repository.MD5Utils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText _phoneText;
    EditText _passwordText;
    Button _loginButton;
    TextView _signupLink;
    SharedPreferences preference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

        preference = getSharedPreferences("login_info",
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
                    editor.putString("profile_picture", loginUser.getProfile_picture());
                    editor.putString("background_image", loginUser.getBackground_image());
                    editor.apply();

                    onLoginSuccess();
                }
            }
        }
        progressDialog.dismiss();
        // TODO: Implement your own authentication logic here.

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                preference = getSharedPreferences("login_info",
                        MODE_PRIVATE);
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
