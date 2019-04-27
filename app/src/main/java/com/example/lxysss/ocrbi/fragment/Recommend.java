package com.example.lxysss.ocrbi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lxysss.ocrbi.Entity.UserIof;
import com.example.lxysss.ocrbi.Entity.UserWord;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activityTool.ShareUtils;
import com.example.lxysss.ocrbi.adapter.RecommendAdapter;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;
import com.facebook.drawee.backends.pipeline.Fresco;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lxysss on 2019/4/6.
 */

public class Recommend extends Fragment implements View.OnClickListener{

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recommned_fragment,container,false);
        Fresco.initialize(view.getContext());   //注意初始化  不然自定义控件会出错
        initView(view);
        return view;
    }

     public void initView(View view){

        Call<UserWord> userword = RestrofitTool.getmApi().getUserWord(ShareUtils.getString(getContext(),"token",null),
                ShareUtils.getString(getContext(),"username","lxy"));
        userword.enqueue(new Callback<UserWord>() {
            @Override
            public void onResponse(Call<UserWord> call, Response<UserWord> response) {
               if(response.body().getWordsnum()!=0){
                   replacrFragment(new RecommendUser());
               }else{
                   replacrFragment(new RecommendNoTest());
               }
            }
            @Override
            public void onFailure(Call<UserWord> call, Throwable t) {

            }
        });
    }
    private void replacrFragment(Fragment fragment){
        FragmentManager fragmentManage=getChildFragmentManager();
        FragmentTransaction transaction=fragmentManage.beginTransaction();
        transaction.replace(R.id.com_chg_frag,fragment);
        transaction.commit();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {

        }
    }
}
