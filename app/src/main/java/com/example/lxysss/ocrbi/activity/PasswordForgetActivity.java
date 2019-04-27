package com.example.lxysss.ocrbi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxysss.ocrbi.Entity.Bean2;
import com.example.lxysss.ocrbi.Entity.Useriofm;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activityTool.ActivityManager;
import com.example.lxysss.ocrbi.activityTool.ShareUtils;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordForgetActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText forget_pass_phone, forget_pass_code, forget_pass_new, forget_pass_new_2;
    private TextView forget_codede;
    private Button comfire_forget_pass,btn_toolbar_delete_back;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forget);
        ActivityManager.getInstance().addActivity(this);  //加入活动容器
        initView();
    }

    private void initView() {
        forget_pass_phone = findViewById(R.id.forget_pass_phone);
        forget_pass_code = findViewById(R.id.forget_pass_code);
        forget_pass_new = findViewById(R.id.forget_pass_new);
        forget_pass_new_2 = findViewById(R.id.forget_pass_new_2);
        forget_codede = findViewById(R.id.forget_codede);
        forget_codede.setOnClickListener(this);
        comfire_forget_pass = findViewById(R.id.comfire_forget_pass);
        comfire_forget_pass.setOnClickListener(this);
        btn_toolbar_delete_back=findViewById(R.id.btn_toolbar_delete_back);
        btn_toolbar_delete_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_codede:
                if (forget_pass_phone.getText().toString().equals("")) {
                    Toast.makeText(PasswordForgetActivity.this, "手机号不可为空！", Toast.LENGTH_SHORT).show();
                } else {
                    Call<Bean2> getForget = RestrofitTool.getmApi().getlostPass(forget_pass_phone.getText().toString());
                    getForget.enqueue(new Callback<Bean2>() {
                        @Override
                        public void onResponse(Call<Bean2> call, Response<Bean2> response) {
                            if (response.body().getCode() == 1000) {
                                Toast.makeText(PasswordForgetActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            } else if (response.body().getCode() == 2000) {
                                Toast.makeText(PasswordForgetActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Bean2> call, Throwable t) {

                        }
                    });
                }
                break;
            case R.id.comfire_forget_pass:
                RequestBody code = RequestBody.create(MediaType.parse("multipart/form-data"), forget_pass_code.getText().toString());
                RequestBody phone = RequestBody.create(MediaType.parse("multipart/form-data"), forget_pass_phone.getText().toString());
                RequestBody pass = RequestBody.create(MediaType.parse("multipart/form-data"), forget_pass_new.getText().toString());
                RequestBody passnew = RequestBody.create(MediaType.parse("multipart/form-data"), forget_pass_new_2.getText().toString());
                if (forget_pass_phone.getText().toString().equals("") && forget_pass_code.getText().toString().equals("")) {
                    Toast.makeText(PasswordForgetActivity.this, "手机号和验证码不可为空！", Toast.LENGTH_SHORT).show();
                } else {
                    Call<Bean2> getForgetYZ = RestrofitTool.getmApi().getlostPassYZ(phone, code);
                    getForgetYZ.enqueue(new Callback<Bean2>() {
                        @Override
                        public void onResponse(Call<Bean2> call, Response<Bean2> response) {
                            if (response.body().getCode() == 1001) {
                                flag = 1;
                            } else if (response.body().getCode() == 2000) {
                                Toast.makeText(PasswordForgetActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            } else if (response.body().getCode() == 2001) {
                                Toast.makeText(PasswordForgetActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Bean2> call, Throwable t) {

                        }
                    });
                }
                if (flag == 1) {
                    if (forget_pass_new.getText().toString().equals("") || forget_pass_new_2.getText().toString().equals("")) {
                        Toast.makeText(PasswordForgetActivity.this, "密码不可为空！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (forget_pass_new.getText().toString().equals(forget_pass_new_2.getText().toString())) {
                            Call<Bean2> xiugaipass=RestrofitTool.getmApi().passchange(phone,pass);
                            xiugaipass.enqueue(new Callback<Bean2>() {
                                @Override
                                public void onResponse(Call<Bean2> call, Response<Bean2> response) {
                                    if (response.body().getCode() == 1001) {
                                        Toast.makeText(PasswordForgetActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else if (response.body().getCode() == 2000) {
                                        Toast.makeText(PasswordForgetActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Bean2> call, Throwable t) {

                                }
                            });
                        } else {
                            Toast.makeText(PasswordForgetActivity.this, "亲亲，两次密码必须一致哦！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

                }
            case R.id.btn_toolbar_delete_back:
                finish();
                break;
        }
    }
}
