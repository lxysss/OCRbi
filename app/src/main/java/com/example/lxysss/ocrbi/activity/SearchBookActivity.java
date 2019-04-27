package com.example.lxysss.ocrbi.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lxysss.ocrbi.Entity.getBook;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activityTool.ActivityManager;
import com.example.lxysss.ocrbi.activityTool.ShareUtils;
import com.example.lxysss.ocrbi.adapter.BookManageAdapter;
import com.example.lxysss.ocrbi.adapter.BookSearchAdapter;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchBookActivity extends AppCompatActivity {
    private EditText search_book_favorite_1;
    private ImageView search_book_favorite_search_1;
    private List<getBook> mListBook=new ArrayList<>();
    private Button btn_toolbar_delete_back;
    private  RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        ActivityManager.getInstance().addActivity(this);  //加入活动容器
        recyclerView = findViewById(R.id.book_search_recy);
        search_book_favorite_1=findViewById(R.id.search_book_favorite_1);
        search_book_favorite_search_1=findViewById(R.id.search_book_favorite_search_1);
        search_book_favorite_search_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search_book_favorite_1.getText().toString().equals("")){
                    Toast.makeText(SearchBookActivity.this,"输入不能为空！",Toast.LENGTH_SHORT).show();
                }else {
                    //关闭输入法
                    InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    mListBook.clear();
                    searchBook();
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
    private void searchBook(){
        Call<List<getBook>> getbook = RestrofitTool.getmApi().getBookSearchSK(ShareUtils.getString(this,"token",null)
                ,search_book_favorite_1.getText().toString());
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
                LinearLayoutManager layoutManager = new LinearLayoutManager(SearchBookActivity.this);
                //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);//2列
                recyclerView.setLayoutManager(layoutManager);
                BookSearchAdapter adapter = new BookSearchAdapter(mListBook);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<getBook>> call, Throwable t) {
                Log.i("书籍列表：",t.getMessage());
                Toast.makeText(SearchBookActivity.this,"没有获取到书籍信息",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
