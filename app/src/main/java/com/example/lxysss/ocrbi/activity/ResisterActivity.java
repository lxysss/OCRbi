package com.example.lxysss.ocrbi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxysss.ocrbi.Entity.Bean2;
import com.example.lxysss.ocrbi.Entity.Useriofm;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activityTool.ActivityManager;
import com.example.lxysss.ocrbi.internetapi.ApiService;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResisterActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_register_close,button_register_register;
    private EditText edit_register_username,edit_register_password,edit_register_phonenumber,
            edit_register_phonenumber_yz;
    private TextView edit_register_phonenumber_yz_hq;
    private int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resister);
        ActivityManager.getInstance().addActivity(this);  //加入活动容器
        btn_register_close = findViewById(R.id.btn_toolbar_delete_back);
        button_register_register = findViewById(R.id.button_register_register);
        btn_register_close.setOnClickListener(this);
        button_register_register.setOnClickListener(this);
        //初始化EditText
        edit_register_username = findViewById(R.id.edit_register_username);
      //  edit_register_sex = findViewById(R.id.edit_register_sex);
        edit_register_password = findViewById(R.id.edit_register_password);
        edit_register_phonenumber = findViewById(R.id.edit_register_phonenumber);
      //  edit_register_age = findViewById(R.id.edit_register_age);
       // edit_register_email = findViewById(R.id.edit_register_email);
        edit_register_phonenumber_yz=findViewById(R.id.edit_register_phonenumber_yz);

        edit_register_phonenumber_yz_hq=findViewById(R.id.edit_register_phonenumber_yz_hq);
        edit_register_phonenumber_yz_hq.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_toolbar_delete_back:
               finish();
                break;
            case R.id.button_register_register:
                ApiService mapi= RestrofitTool.getmApi();
             final   RequestBody username= RequestBody.create(MediaType.parse("multipart/form-data"),edit_register_username.getText().toString());
                final RequestBody password= RequestBody.create(MediaType.parse("multipart/form-data"),edit_register_password.getText().toString());
                final RequestBody phonenumber= RequestBody.create(MediaType.parse("multipart/form-data"),edit_register_phonenumber.getText().toString());
                 RequestBody phonenumberyz= RequestBody.create(MediaType.parse("multipart/form-data"),edit_register_phonenumber_yz.getText().toString());
                final  RequestBody email= RequestBody.create(MediaType.parse("multipart/form-data")," ");
                final RequestBody sex= RequestBody.create(MediaType.parse("multipart/form-data"),"2");
                final RequestBody age= RequestBody.create(MediaType.parse("multipart/form-data"),"0");
                if(edit_register_username.getText().toString().equals("")&& edit_register_password.getText().toString().equals("")
                        && edit_register_phonenumber.getText().toString().equals("") && edit_register_phonenumber_yz.getText().toString().equals("")) {
                    Toast.makeText(ResisterActivity.this, "输入不能为空！", Toast.LENGTH_SHORT).show();
                }
                else{
                    /**
                     * 验证验证码
                     */
                    Call<Bean2> getPhoneYzYZ = mapi.getPhoneYZYZ(phonenumber, phonenumberyz);
                    getPhoneYzYZ.enqueue(new Callback<Bean2>() {
                        @Override
                        public void onResponse(Call<Bean2> call, Response<Bean2> response) {
                            if (response.body().getCode() == 1000) {
                                Call<Useriofm> register = RestrofitTool.getmApi().register(username, password, phonenumber, email, sex, age);
                                //发送网络请求(异步)
                                register.enqueue(new Callback<Useriofm>() {
                                    //请求成功时回调
                                    @Override
                                    public void onResponse(Call<Useriofm> call, Response<Useriofm> response) {
                                        //请求处理,输出结果-response.body().show();
                                        if (response.body().code == 1001) {
                                            //   Toast.makeText(ResisterActivity.this, response.body().code+response.body().msg+"", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(ResisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(ResisterActivity.this, "稍后请在用户个人中心完善信息哦！", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //  Toast.makeText(ResisterActivity.this, response.body().code+response.body().msg, Toast.LENGTH_SHORT).show();
                                            Toast.makeText(ResisterActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Useriofm> call, Throwable t) {
                                        Toast.makeText(ResisterActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
                                        //请求失败时候的回调
                                    }
                                });
                                finish();
                            } else if (response.body().getCode() == 2000) {
                                Toast.makeText(ResisterActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Bean2> call, Throwable t) {

                        }
                    });
                }
                break;
            case R.id.edit_register_phonenumber_yz_hq:
                if(edit_register_phonenumber.getText().toString().equals("")) {
                    Toast.makeText(ResisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
                else {
                    Call<Bean2> getphoneyz = RestrofitTool.getmApi().getPhoneYZ(edit_register_phonenumber.getText().toString());
                    getphoneyz.enqueue(new Callback<Bean2>() {
                        @Override
                        public void onResponse(Call<Bean2> call, Response<Bean2> response) {
                            if (response.body().getCode() == 1001) {
                                Toast.makeText(ResisterActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            } else if (response.body().getCode() == 2000) {
                                Toast.makeText(ResisterActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Bean2> call, Throwable t) {

                        }
                    });
                }
                break;
        }
    }
}
