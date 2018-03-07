package com.weareone.findlost.login;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.weareone.findlost.MainActivity;
import com.weareone.findlost.R;
import com.weareone.findlost.entities.User;
import com.weareone.findlost.entities.Userinfo;
import com.weareone.findlost.utils.ActivityCollector;
import com.weareone.findlost.utils.HttpUtil;

import java.io.IOException;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private TextView mTextViewRegister;
    private TextView mTextViewForgetPW;
    private EditText mEtUsername;
    private EditText mEtPassWd;
    private RegisterFragment mRegisterFragment;
    private CheckBox remeberPasswd;
    private ImageButton loginButton;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String password;
    private String username;
    private Userinfo userInfo;
    private User user;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    editor = sp.edit();
                    if (remeberPasswd.isChecked()) {
                        editor.putString("user", username);
                        editor.putString("pass", password);
                        editor.apply();
                    } else {
                        editor.putString("user", "");
                        editor.putString("pass", "");
                        editor.apply();
                    }
                    Bundle userBundle = new Bundle();
                    userBundle.putParcelable("userinfo", userInfo);
                    Log.d(TAG, "handleMessage: " + userInfo.getUsername());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userBundle", userBundle);
                    startActivity(intent);
                    break;
                case 2:
                    mEtUsername.setText(user.getUsername());
                    mEtPassWd.setText(user.getPassword());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCollector.activities.add(this);
        mTextViewForgetPW = findViewById(R.id.tv_forgetpw);
        mTextViewRegister = findViewById(R.id.tv_register);
        mTextViewForgetPW = findViewById(R.id.tv_forgetpw);
        mEtUsername = findViewById(R.id.username_log);
        mEtPassWd = findViewById(R.id.passwd_log);
        loginButton = findViewById(R.id.login_button);
        remeberPasswd = findViewById(R.id.remeber_passwd);
        //在版本19以上状态栏透明，更美观
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        //运行时权限检查
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                    (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUser();
            }
        });
        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegisterFragment = RegisterFragment.getInstance();
                mRegisterFragment.show(getFragmentManager(), "test");
            }
        });
        mTextViewForgetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "请联系管理员", Toast.LENGTH_SHORT).show();
            }
        });
        //如果之前选择了记住密码，直接将帐号密码输入到edittext
        sp = getSharedPreferences("passwd", MODE_PRIVATE);
        if (!"".equals(sp.getString("user", ""))) {
            mEtUsername.setText(sp.getString("user", ""));
            mEtPassWd.setText(sp.getString("pass", ""));
            remeberPasswd.setChecked(true);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    //授权失败，退出应用；
                    Toast.makeText(getApplicationContext(), "授权被拒绝", Toast.LENGTH_SHORT);
                    ActivityCollector.finishAllActivity();
                }

        }
    }

    private void checkUser() {
        username = mEtUsername.getText().toString();
        password = mEtPassWd.getText().toString();
        JMessageClient.login(username, password, new BasicCallback() {

            @Override
            public void gotResult(int arg0, String arg1) {
                if (arg0 != 0) {
                    Log.d(TAG, "gotResult: " + "jmessage fail");
                }
                Log.d(TAG, "gotResult: " + arg1);


            }
        });
        if ((!"".equals(username)) && (!"".equals(password))) {
            OkHttpClient client = new OkHttpClient();
            FormBody formBody = new FormBody.Builder().add("username", username).add("password", password).build();
            final Request request = new Request
                    .Builder()
                    .post(formBody)//Post请求的参数传递
                    .url(HttpUtil.BASEUEL + "/api/user/login")
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: ");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (response != null) {
                            userInfo = JSON.parseObject(response.body().string(), new TypeReference<Userinfo>() {
                            });
                            mHandler.sendEmptyMessage(1);
                            Log.d(TAG, "onResponse: " + userInfo.getUsername());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
        }

    }

    public void registerUser(User registerUser, Userinfo userinfo) {
        user = registerUser;
        OkHttpClient client = new OkHttpClient();
        String userData = JSON.toJSONString(registerUser);
        String userinfoData = JSON.toJSONString(userinfo);
        FormBody formBody = new FormBody.Builder().add("user", userData).add("userinfo", userinfoData).build();
        final Request request = new Request
                .Builder()
                .post(formBody)//Post请求的参数传递
                .url(HttpUtil.BASEUEL + "/api/user/register")
                .build();
        JMessageClient.register(registerUser.getUsername(), registerUser.getPassword(), new BasicCallback() {

            @Override
            public void gotResult(int arg0, String arg1) {
                Log.d(TAG, "gotResult: " + arg0);
            }
        });
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                    }
                });
                //
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d(TAG, "onResponse: " + response.body().string());
                mHandler.sendEmptyMessage(2);
                mRegisterFragment.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {
        ActivityCollector.finishAllActivity();
    }

}
