package com.example.lxysss.ocrbi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxysss.ocrbi.Entity.UserIof;
import com.example.lxysss.ocrbi.Entity.Useriofm;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activityTool.ActivityManager;
import com.example.lxysss.ocrbi.activityTool.IGImageView;
import com.example.lxysss.ocrbi.activityTool.ShareUtils;
import com.example.lxysss.ocrbi.activityTool.UtilTools;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_username;
    private EditText et_sex;
    private EditText et_age;
    private EditText et_tel;
    private EditText et_eamil;
    private Button edit_user,btn_update_ok,btn_toolbar_delete_back;
    private ImageView profile_image_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ActivityManager.getInstance().addActivity(this);  //加入活动容器
        initview();
    }

    private void initview() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_sex = (EditText) findViewById(R.id.edit_sex);
        et_age = (EditText) findViewById(R.id.edit_age);
        et_tel =findViewById(R.id.et_tel);
        et_eamil=findViewById(R.id.et_eamil);
        edit_user = findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);
        btn_update_ok = findViewById(R.id.btn_update_ok);
        btn_update_ok.setOnClickListener(this);
        profile_image_1=findViewById(R.id.profile_image_1);
        UtilTools.getImageToShare(UserInfoActivity.this,profile_image_1);
        btn_toolbar_delete_back=findViewById(R.id.btn_toolbar_delete_back);
        btn_toolbar_delete_back.setOnClickListener(this);
        user();
        setEnabled(false);
        UtilTools.getImageToShare(this,profile_image_1);
    }
    //控制焦点
    private void setEnabled(boolean is) {
        et_username.setEnabled(is);
        et_sex.setEnabled(is);
        et_age.setEnabled(is);
        et_tel.setEnabled(is);
        et_eamil.setEnabled(is);
    }
    public void user(){
        Call<UserIof> user = RestrofitTool.getmApi().getUserInfo(ShareUtils.getString(this,"token",null),
                ShareUtils.getString(this,"username","lxy"));
        Log.i("token",ShareUtils.getString(this,"token",null));
        user.enqueue(new Callback<UserIof>() {
            @Override
            public void onResponse(Call<UserIof> call, Response<UserIof> response) {
             //   Toast.makeText(UserInfoActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                et_username.setText(response.body().getUsername());
                if (response.body().getSex().equals("0")){
                    et_sex.setText("男");
                }else if(response.body().getSex().equals("1")){
                    et_sex.setText("女");
                }else {
                    et_sex.setText("您还未公布性别哦");
                }
                et_age.setText(response.body().getAge());
                et_tel.setText(response.body().getPhone());
                et_eamil.setText(response.body().getMail());
                // et_desc.setText("12");
            }

            @Override
            public void onFailure(Call<UserIof> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_user:
                btn_update_ok.setVisibility(View.VISIBLE);
                edit_user.setVisibility(View.GONE);
                setEnabled(true);
                break;
            case R.id.btn_update_ok:
                if(et_username.getText().toString().equals("")){
                    Toast.makeText(UserInfoActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                }
                else{
                    RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), et_username.getText().toString());
                    RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"), ShareUtils.getString(this, "token", null));
                    RequestBody phonenumber = RequestBody.create(MediaType.parse("multipart/form-data"), et_tel.getText().toString());
                    RequestBody email=RequestBody.create(MediaType.parse("multipart/form-data"), et_eamil.getText().toString());
                    RequestBody sex;
                    if (et_sex.getText().toString().equals("男")){
                        sex=RequestBody.create(MediaType.parse("multipart/form-data"), "0");
                    }else if(et_sex.getText().toString().equals("女")){
                        sex=RequestBody.create(MediaType.parse("multipart/form-data"), "1");
                    }else {
                        sex=RequestBody.create(MediaType.parse("multipart/form-data"), "2");
                    }
                    RequestBody age = RequestBody.create(MediaType.parse("multipart/form-data"), et_age.getText().toString());
                    Call<Useriofm> edit = RestrofitTool.getmApi().edit(token, username, phonenumber, email, age, sex);
                    edit.enqueue(new Callback<Useriofm>() {
                        @Override
                        public void onResponse(Call<Useriofm> call, Response<Useriofm> response) {
                            if (response.body().code == 1001) {
                                Toast.makeText(UserInfoActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                et_username.setText(et_username.getText().toString());
                                et_sex.setText(et_sex.getText().toString());
                                et_age.setText(et_age.getText().toString());
                                et_tel.setText(et_tel.getText().toString());
                                et_eamil.setText(et_eamil.getText().toString());
                            } else {
                                Toast.makeText(UserInfoActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Useriofm> call, Throwable t) {

                        }
                    });
                }
                setEnabled(false);
                edit_user.setVisibility(View.VISIBLE);
                btn_update_ok.setVisibility(View.GONE);
                break;
            case R.id.btn_toolbar_delete_back:
                finish();
        }
    }
}
