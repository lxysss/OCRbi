package com.example.lxysss.ocrbi.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxysss.ocrbi.Entity.Bean2;
import com.example.lxysss.ocrbi.Entity.Favorite;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activityTool.ActivityManager;
import com.example.lxysss.ocrbi.activityTool.IGImageView;
import com.example.lxysss.ocrbi.activityTool.ShareUtils;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;
import com.facebook.drawee.backends.pipeline.Fresco;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener{
   private TextView book_title,book_auther,book_summary,book_publisher;
   private IGImageView book_pic;
   private ImageView iv_goods_back,book_love;
   private boolean fag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ActivityManager.getInstance().addActivity(this);  //加入活动容器
        Fresco.initialize(this);

        book_title=findViewById(R.id.book_title);
        book_auther=findViewById(R.id.book_auther);
        book_summary=findViewById(R.id.book_summary);
        book_publisher=findViewById(R.id.book_publisher);
        book_love=findViewById(R.id.book_love);
        book_love.setOnClickListener(this);
        book_pic=findViewById(R.id.book_pic);
        book_title.setText(getIntent().getStringExtra("book_title"));
        book_auther.setText(getIntent().getStringExtra("book_author"));
        book_summary.setText(getIntent().getStringExtra("book_summary"));
        book_publisher.setText(getIntent().getStringExtra("book_publisher"));
        displayImage(this,getIntent().getStringExtra("book_pic"),book_pic);
        iv_goods_back=findViewById(R.id.iv_goods_back);
        iv_goods_back.setOnClickListener(this);

        Call<Favorite> favoriteCall =RestrofitTool.getmApi().isFavorite(ShareUtils.getString(BookDetailActivity.this, "token", null),
                ShareUtils.getString(BookDetailActivity.this, "username", "lxy"),
                getIntent().getStringExtra("book_isbn"));
        favoriteCall.enqueue(new Callback<Favorite>() {
            @Override
            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                if(response.body().getIsfavorate()==1){
                    book_love.setImageResource(R.drawable.book_love_you);
                    fag=true;

                }else if(response.body().getIsfavorate()==0){
                    book_love.setImageResource(R.drawable.love);
                    fag=false;
                }
            }
            @Override
            public void onFailure(Call<Favorite> call, Throwable t) {

            }
        });
    }

    public void displayImage(Context context, String path, IGImageView imageView) {
        imageView.showImage(path);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_goods_back:
                finish();
                break;
            case R.id.book_love:
                if(!fag) {
                    RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"),
                            ShareUtils.getString(BookDetailActivity.this, "token", null));
                    RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"),
                            ShareUtils.getString(BookDetailActivity.this, "username", "lxy"));
                    RequestBody isbn = RequestBody.create(MediaType.parse("multipart/form-data"),
                            getIntent().getStringExtra("book_isbn"));
                    Call<Bean2> putbook = RestrofitTool.getmApi().PutBook(token, username, isbn);
                    putbook.enqueue(new Callback<Bean2>() {
                        @Override
                        public void onResponse(Call<Bean2> call, Response<Bean2> response) {
                            if (response.body().getCode() == 1001) {
                                Toast.makeText(BookDetailActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            } else if (response.body().getCode() == 2000) {
                                Toast.makeText(BookDetailActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Bean2> call, Throwable t) {

                        }
                    });
                    book_love.setImageResource(R.drawable.book_love_you);
                    fag=true;
                }
                else {
                    RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"),
                            ShareUtils.getString(BookDetailActivity.this, "token", null));
                    RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"),
                            ShareUtils.getString(BookDetailActivity.this, "username", "lxy"));
                    RequestBody isbn = RequestBody.create(MediaType.parse("multipart/form-data"),
                            getIntent().getStringExtra("book_isbn"));
                    Call<Bean2> DeleteBook= RestrofitTool.getmApi().DeleteBook(token,username,isbn);
                    DeleteBook.enqueue(new Callback<Bean2>() {
                        @Override
                        public void onResponse(Call<Bean2> call, Response<Bean2> response) {
                            if(response.body().getCode()==1001){
                                Toast.makeText(BookDetailActivity.this,"取消收藏",Toast.LENGTH_LONG).show();
                            }
                            else if(response.body().getCode()==2000){
                                Toast.makeText(BookDetailActivity.this,response.body().getMsg(),Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Bean2> call, Throwable t) {

                        }
                    });
                    book_love.setImageResource(R.drawable.love);
                    fag=false;
                }
                break;
        }
    }


}
