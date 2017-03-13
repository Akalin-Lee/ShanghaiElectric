package com.shanghai_electric.shanghaielectric;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shanghai_electric.shanghaielectric.util.HttpCallbackListener;
import com.shanghai_electric.shanghaielectric.util.HttpUtil;
import com.shanghai_electric.shanghaielectric.util.LogUtil;
import com.shanghai_electric.shanghaielectric.util.NetWorkUtil;

import org.json.JSONObject;

import java.net.HttpURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoginActivity extends BaseActivity{
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private CheckBox rememberPass;
    private ProgressBar progressBar;

    String id = "";
    int status = 0;
    int authority = 0;
    String msg = "";
    String url;
    String account;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = (EditText)findViewById(R.id.account);
        passwordEdit = (EditText)findViewById(R.id.password);
        rememberPass = (CheckBox)findViewById(R.id.remember_pass);
        login = (Button)findViewById(R.id.login);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        boolean isRemember = pref.getBoolean("remember_password",false);
        if(isRemember){
            String account = pref.getString("account","");
            String password = pref.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                account = accountEdit.getText().toString();
                password = passwordEdit.getText().toString();
                if(!NetWorkUtil.isNetworkConnected(LoginActivity.this)){
                    Toast.makeText(LoginActivity.this, "网络不可用，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                }else {
                    if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                        Toast.makeText(LoginActivity.this, "账户或密码不能为空", Toast.LENGTH_SHORT).show();

                    } else {
                        url = getString(R.string.url)
                                + getString(R.string.primary_path) + "/First?name=" + account + "&password=" + password;
//                    Toast.makeText(LoginActivity.this,url, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    OkHttpClient client = new OkHttpClient();
                                    Request request = new Request.Builder()
                                            .url(url)
                                            .build();
                                    final Response response = client.newCall(request).execute();
                                    if (response.isSuccessful()) {
                                        LogUtil.e("aaaaaaa", "chenggongle");
                                        final String resoponseData = response.body().string();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                parseJson(resoponseData);
                                                login();
                                            }
                                        });
//                                        Toast.makeText(LoginActivity.this, "qingqiuwanbi2", Toast.LENGTH_SHORT).show();
                                    } else {
                                        LogUtil.e("aaaaaaa", "sibai");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
//                                        Toast.makeText(LoginActivity.this, "sibai", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    LogUtil.e("aaaaaaa", "dashibai1");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(LoginActivity.this, "登录失败，服务器不可用", Toast.LENGTH_SHORT).show();
                                        }
                                    });
//                                    Toast.makeText(LoginActivity.this, "cuodaojiale", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }

                            }
                        }).start();
                    }
                }

            }
        });
    }
    private void parseJson(String response){
        try{
                            JSONObject jsonObject = new JSONObject(response);
                            id = jsonObject.getString("ID");
                            status = jsonObject.getInt("status");
                            authority = jsonObject.getInt("authority");
                            msg = jsonObject.getString("msg");
//                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();

                        }catch(Exception e){
                            Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

    }
    private void login(){
        progressBar.setVisibility(View.GONE);
        if(status==2){
            editor = pref.edit();
            if(rememberPass.isChecked()){
                editor.putBoolean("remember_password",true);
                editor.putString("account",account);
                editor.putString("password",password);
            }else{
                editor.clear();
            }
            editor.apply();
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else if(status==1){
            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
        }

    }
}
