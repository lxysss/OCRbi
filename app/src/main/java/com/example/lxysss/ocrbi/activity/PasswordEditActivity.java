package com.example.lxysss.ocrbi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lxysss.ocrbi.Entity.Bean;
import com.example.lxysss.ocrbi.Entity.Useriofm;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activityTool.ActivityManager;
import com.example.lxysss.ocrbi.activityTool.ShareUtils;
import com.example.lxysss.ocrbi.internetapi.ApiService;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordEditActivity extends AppCompatActivity {
    private EditText pass_edit,pass_edit_new,old_password;
    private Button comfire_pass_btn,btn_toolbar_delete_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_edit);
        ActivityManager.getInstance().addActivity(this);  //加入活动容器
        pass_edit=findViewById(R.id.pass_edit);
        pass_edit_new=findViewById(R.id.pass_edit_new);
        old_password=findViewById(R.id.old_password);
        comfire_pass_btn=findViewById(R.id.comfire_pass_btn);
        comfire_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService mapi = RestrofitTool.getmApi();
                if (pass_edit_new.getText().toString().equals("") || pass_edit.getText().toString().equals("")) {
                    Toast.makeText(PasswordEditActivity.this, "密码不可为空！", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass_edit_new.getText().toString().equals(pass_edit.getText().toString())) {
                        RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"), ShareUtils.getString(PasswordEditActivity.this, "token", null));
                        RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), ShareUtils.getString(PasswordEditActivity.this, "username", "lxy"));
                        RequestBody oldpassword = RequestBody.create(MediaType.parse("multipart/form-data"), old_password.getText().toString());
                        RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(pass_edit_new.getText()));
                        Call<Useriofm> edit = mapi.passedit(token, requestBody1, oldpassword, requestBody2);
                        edit.enqueue(new Callback<Useriofm>() {
                            @Override
                            public void onResponse(Call<Useriofm> call, Response<Useriofm> response) {
                                if (response.body().code == 1001) {
                                    Toast.makeText(PasswordEditActivity.this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(PasswordEditActivity.this, "密码修改失败！", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Useriofm> call, Throwable t) {

                            }
                        });
                    } else {
                        Toast.makeText(PasswordEditActivity.this, "亲亲，两次密码必须一致哦！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btn_toolbar_delete_back=findViewById(R.id.btn_toolbar_delete_back);
        btn_toolbar_delete_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
