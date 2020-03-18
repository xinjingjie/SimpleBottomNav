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
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    EditText _nameText;
    EditText _emailText;
    EditText _mobileText;
    EditText _passwordText;
    EditText _reEnterPasswordText;
    Button _signupButton;
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        _nameText = findViewById(R.id.input_name);
        _emailText = findViewById(R.id.input_phone);
        _mobileText = findViewById(R.id.input_mobile);
        _passwordText = findViewById(R.id.input_password);
        _reEnterPasswordText = findViewById(R.id.input_reEnterPassword);
        _loginLink = findViewById(R.id.link_login);
        _signupButton = findViewById(R.id.btn_signup);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        LogUpTask logUpTask = new LogUpTask();
        String message = null;
        try {
            message = (String) logUpTask.execute(name, email, mobile, password).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "signup: " + message);
        if ("error".equals(message)) {
            Toast.makeText(this, "网络出错！", Toast.LENGTH_SHORT).show();
            onSignupFailed();
        } else {
            Gson gson1 = new Gson();
            JsonData jsonData = gson1.fromJson(message, JsonData.class);
            Log.d(TAG, "signup: " + jsonData.getCode());
            if (jsonData.getCode() == -1) {
                switch (jsonData.getMsg()) {
                    case "信息未填写完整":
                        break;
                    case "该号码已注册！":
                        Toast.makeText(this, "该号码已注册！", Toast.LENGTH_SHORT).show();
                        break;
                    case "该邮箱已注册":
                        Toast.makeText(this, "该邮箱已注册！", Toast.LENGTH_SHORT).show();
                        break;
                    case "出错了":
                        Toast.makeText(this, "服务器出错！", Toast.LENGTH_SHORT).show();
                        break;
                }
                onSignupFailed();
            } else if (jsonData.getCode() == 0) {


                SharedPreferences preference = getSharedPreferences("login_info",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = preference.edit();
                editor.putString("phoneNumber", mobile);
                Toast.makeText(this, "注册成功！返回登录", Toast.LENGTH_SHORT).show();
                onSignupSuccess();
            }
        }
        progressDialog.dismiss();
        // TODO: Implement your own signup logic here.

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onSignupSuccess or onSignupFailed
//                        // depending on success
//                        onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 11) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    static class LogUpTask extends AsyncTask {
        // private final static String path = "http://112.124.44.175:8080/api/user/testRegister";
        private final static String path = "http://192.168.2.107:8080/api/user/testRegister";

        @Override
        protected Object doInBackground(Object[] objects) {
            String name = objects[0].toString();
            String email = objects[1].toString();
            String phone = objects[2].toString();
            String pwd = objects[3].toString();
            //Gson dateGson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); dateGson.toJson(new Date())
            User user = new User(name, "null", pwd, 0, null, phone, email, 0, null, null);
            Gson gson = new Gson();
            String jsonData = gson.toJson(user);
            Log.d(TAG, "doInBackground: " + jsonData);
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
}