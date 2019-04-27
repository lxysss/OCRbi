package com.example.lxysss.ocrbi.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxysss.ocrbi.Entity.getBook;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activityTool.ActivityManager;
import com.example.lxysss.ocrbi.activityTool.ScanQRCodeActivity;
import com.example.lxysss.ocrbi.activityTool.ShareUtils;
import com.example.lxysss.ocrbi.activityTool.UtilTools;
import com.example.lxysss.ocrbi.adapter.BookManageAdapter;
import com.example.lxysss.ocrbi.adapter.RecommendAdapter;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookManageActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView profile_image_2,search_book_favorite_search;
    private List<getBook> mListBook=new ArrayList<>();
    public static BookManageActivity mactivity;
    public static Context mContext;
    private EditText search_book_favorite;
    private Button btn_toolbar_delete_back;
    private TextView book_num,name_user;
    private RecyclerView recyclerView ;
    private  LinearLayoutManager layoutManager ;
    private  BookManageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ActivityManager.getInstance().addActivity(this);  //加入活动容器
        mactivity=this;
        recyclerView = findViewById(R.id.book_manage_recy);
        mContext = this.getBaseContext();
        layoutManager = new LinearLayoutManager(BookManageActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);// 设置 recyclerview 布局方式为竖布局
        recyclerView.setLayoutManager(layoutManager);
        initView();
    }

    private void initView() {
        profile_image_2=findViewById(R.id.profile_image_2);
        search_book_favorite=findViewById(R.id.search_book_favorite);
        search_book_favorite_search=findViewById(R.id.search_book_favorite_search);
        search_book_favorite_search.setOnClickListener(this);
        btn_toolbar_delete_back=findViewById(R.id.btn_toolbar_delete_back);
        btn_toolbar_delete_back.setOnClickListener(this);
        name_user=findViewById(R.id.name_user);
        //book_num=findViewById(R.id.book_num);
        getbook();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_book_favorite_search:
                //关闭输入法
                InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                if(search_book_favorite.getText().toString().equals("")){
                    mListBook.clear();
                    getbook();
                }
                else{
                    mListBook.clear();
                    searchBook();
                }
                break;
            case R.id.btn_toolbar_delete_back:
                finish();
        }
    }

    private void getbook(){
        Call<List<getBook>> getbook = RestrofitTool.getmApi().getBookManage(ShareUtils.getString(this,"token",null),
                ShareUtils.getString(this,"username","lxy"));
        getbook.enqueue(new Callback<List<getBook>>() {
            @Override
            public void onResponse(Call<List<getBook>> call, Response<List<getBook>> response) {
                // Toast.makeText(getContext(),"我来了 "+response.body(),Toast.LENGTH_SHORT).show();
                if (response.body().size() == 0) {
                    Toast.makeText(BookManageActivity.this, "您目前还没有收藏书籍哦！", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < response.body().size(); i++) {
                        getBook getbook = new getBook(response.body().get(i).getTitle(),
                                response.body().get(i).getAuthor(),
                                response.body().get(i).getPublisher(),
                                response.body().get(i).getIsbn(),
                                response.body().get(i).getSummary(),
                                response.body().get(i).getSimage(),
                                response.body().get(i).getMimage(),
                                response.body().get(i).getLimage());
                        mListBook.add(getbook);
                    }
                    //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);//2列
                    adapter = new BookManageAdapter(mListBook);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<getBook>> call, Throwable t) {
                Log.i("书籍列表：",t.getMessage());
                Toast.makeText(BookManageActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void searchBook(){
            Call<List<getBook>> getbook = RestrofitTool.getmApi().getBookSearch(ShareUtils.getString(this,"token",null),
                    ShareUtils.getString(this,"username","lxy"),search_book_favorite.getText().toString());
            getbook.enqueue(new Callback<List<getBook>>() {
                @Override
                public void onResponse(Call<List<getBook>> call, Response<List<getBook>> response) {
                    // Toast.makeText(getContext(),"我来了 "+response.body(),Toast.LENGTH_SHORT).show();
                    for(int i=0;i<response.body().size();i++){
                        getBook getbook=new getBook(response.body().get(i).getTitle(),
                                response.body().get(i).getAuthor(),
                                response.body().get(i).getPublisher(),
                                response.body().get(i).getIsbn(),
                                response.body().get(i).getSummary(),
                                response.body().get(i).getSimage(),
                                response.body().get(i).getMimage(),
                                response.body().get(i).getLimage());
                        mListBook.add(getbook);
                    }

                    LinearLayoutManager layoutManager = new LinearLayoutManager(BookManageActivity.this);
                    //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);//2列
                    recyclerView.setLayoutManager(layoutManager);
                    BookManageAdapter adapter = new BookManageAdapter(mListBook);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<getBook>> call, Throwable t) {
                    Log.i("书籍列表：",t.getMessage());
                    Toast.makeText(BookManageActivity.this,"没有获取到书籍信息",Toast.LENGTH_SHORT).show();
                }
            });
    }
}
