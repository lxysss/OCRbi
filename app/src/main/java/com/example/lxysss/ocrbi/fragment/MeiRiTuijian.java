package com.example.lxysss.ocrbi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lxysss.ocrbi.Entity.getBook;
import com.example.lxysss.ocrbi.Entity.getDoubanBook;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activityTool.ShareUtils;
import com.example.lxysss.ocrbi.adapter.RecommendAdapter;
import com.example.lxysss.ocrbi.adapter.RecommendDoubanAdapter;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lxysss on 2019/4/14.
 */

public class MeiRiTuijian extends Fragment {
    private List<getDoubanBook> mListBook=new ArrayList<>();
    private RecyclerView recyclerView ;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.mei_ri_tui_jian,container,false);
      //  Fresco.initialize(view.getContext());   //注意初始化  不然自定义控件会出错
        recyclerView = view.findViewById(R.id.daily_recommend_recycler);
        initView(view);
        return view;
}

    private void initView(View view) {
        final View mView=view;
        //书籍推荐获取
        final Call<List<getDoubanBook>> getbook = RestrofitTool.getmApi().getDoubanBook(ShareUtils.getString(getContext(),"token",null),
                ShareUtils.getString(getContext(),"username","lxy"));
        getbook.enqueue(new Callback<List<getDoubanBook>>() {
            @Override
            public void onResponse(Call<List<getDoubanBook>> call, Response<List<getDoubanBook>> response) {
              //  Toast.makeText(getContext(),"我来了 "+response.body(),Toast.LENGTH_SHORT).show();
                for(int i=0;i<response.body().size();i++){
                    getDoubanBook getbook=new getDoubanBook(response.body().get(i).getTitle(),
                            response.body().get(i).getAuthor(),
                            response.body().get(i).getRating(),
                            response.body().get(i).getVotes(),
                            response.body().get(i).getPublisher(),
                            response.body().get(i).getIsbn(),
                            response.body().get(i).getSummary(),
                            response.body().get(i).getSimage(),
                            response.body().get(i).getMimage(),
                            response.body().get(i).getLimage());
                    mListBook.add(getbook);
                }
                 LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
              //  GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);//2列
                recyclerView.setLayoutManager(layoutManager);
                RecommendDoubanAdapter adapter = new RecommendDoubanAdapter(mListBook);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<getDoubanBook>> call, Throwable t) {
                Log.i("书籍列表：",t.getMessage());
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}

