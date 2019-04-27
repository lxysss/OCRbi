package com.example.lxysss.ocrbi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.lxysss.ocrbi.Entity.Bean;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activityTool.ActivityManager;
import com.example.lxysss.ocrbi.activityTool.CustomDialog;
import com.example.lxysss.ocrbi.activityTool.ShareUtils;
import com.example.lxysss.ocrbi.internetapi.ApiService;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_login_login,btn_login_close;
    private CustomDialog dialog;
    private EditText edit_login_name,edit_login_password;
    private TextView password_forget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityManager.getInstance().addActivity(this);  //加入活动容器
        btn_login_login=findViewById(R.id.btn_login_login);
        btn_login_login.setOnClickListener(this);
        btn_login_close=findViewById(R.id.btn_login_close);
        btn_login_close.setOnClickListener(this);
        password_forget=findViewById(R.id.password_forget);
        password_forget.setOnClickListener(this);
        TextView tv_register_register=findViewById(R.id.tv_register_register);
        tv_register_register.setOnClickListener(this);
        dialog=new CustomDialog(this,100,100,R.layout.dialog_loding,R.style.Theme_dialog,
                Gravity.CENTER,R.style.pop_anim_style);
        dialog.setCancelable(false);
        edit_login_name=findViewById(R.id.edit_login_name);
        edit_login_password=findViewById(R.id.edit_login_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_login:

                ApiService mapi= RestrofitTool.getmApi();
                RequestBody requestBody1= RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(edit_login_name.getText()));
                RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(edit_login_password.getText()));
                Call<Bean> login = mapi.login(requestBody1,requestBody2);
               Log.i("login:",login.toString()) ;
                dialog.show();
                //发送网络请求(异步)
                login.enqueue(new Callback<Bean>() {
                    //请求成功时回调
                    @Override
                    public void onResponse(Call<Bean> call, Response<Bean> response) {
                        //请求处理,输出结果-response.body().show();
                        if (response.body().getCode()==1000) {
                            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                          //  Toast.makeText(LoginActivity.this, response.body().getToken(), Toast.LENGTH_SHORT).show();
                            //存储用户名和token
                            ShareUtils.putString(LoginActivity.this,"username",String.valueOf(edit_login_name.getText()));
                            ShareUtils.putString(LoginActivity.this,"token",response.body().getToken());
                            dialog.dismiss();
                            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }
                        else{
                          //  Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(LoginActivity.this, response.body().getCode()+"", Toast.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                    @Override
                    public void onFailure(Call<Bean> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
                        Log.i("t:",t.toString());
                        //请求失败时候的回调
                        dialog.dismiss();
                    }
                });
               // Intent intent=new Intent(this,HomeActivity.class);
               // startActivity(intent);

                break;
            case R.id.tv_register_register:
                Intent intent1=new Intent(this,ResisterActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_login_close:
                ActivityManager.getInstance().exit();
                break;
            case R.id.password_forget:
                Intent intent=new Intent(LoginActivity.this,PasswordForgetActivity.class);
                startActivity(intent);
                break;
        }
    }
}
