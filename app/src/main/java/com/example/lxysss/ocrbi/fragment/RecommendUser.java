package com.example.lxysss.ocrbi.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxysss.ocrbi.Entity.UserWord;
import com.example.lxysss.ocrbi.Entity.WordResult;
import com.example.lxysss.ocrbi.Entity.getBook;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activityTool.CustomDialog;
import com.example.lxysss.ocrbi.activityTool.ShareUtils;
import com.example.lxysss.ocrbi.adapter.RecommendAdapter;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lxysss on 2019/4/12.
 */

public class RecommendUser extends Fragment implements View.OnClickListener{
  private Button shijituijian,meirijinping,dialog_yes,dialog_no;
  private ImageView  re_test_word;
    private TextView user_word_result;
    private  List<getBook> mListBook=new ArrayList<>();
    private FrameLayout frameLayout;
    private CustomDialog dialog;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recommend_page, container, false);
         Fresco.initialize(view.getContext());   //注意初始化  不然自定义控件会出错
        meirijinping = view.findViewById(R.id.meirijinping);
        shijituijian = view.findViewById(R.id.shijituijian);
        replacrFragment1(new Shizishutuijian());
        meirijinping.setBackgroundResource(R.drawable.button_bg_2);
        meirijinping.setTextColor(Color.BLACK);
        shijituijian.setBackgroundResource(R.drawable.button_bg);
        shijituijian.setTextColor(Color.WHITE);
        frameLayout =view.findViewById(R.id.recommend_chage);
        //初始化dialog
        dialog = new CustomDialog(getActivity(), 0, 0,
                R.layout.dialog_change, R.style.MyDialog, Gravity.CENTER, 0);
        //提示框以外点击无效
        dialog.setCancelable(false);
        dialog_yes=dialog.findViewById(R.id.dialog_yes);
        dialog_yes.setOnClickListener(this);
        dialog_no=dialog.findViewById(R.id.dialog_no);
        dialog_no.setOnClickListener(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                replacrFragment1(new Shizishutuijian());
            }
        }, 200);

        initView(view);
        return view;
    }

    private void initView(final View view) {
        shijituijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meirijinping.setBackgroundResource(R.drawable.button_bg_2);
                meirijinping.setTextColor(Color.BLACK);
                shijituijian.setBackgroundResource(R.drawable.button_bg);
                shijituijian.setTextColor(Color.WHITE);
                replacrFragment1(new Shizishutuijian());
            }
        });
        meirijinping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meirijinping.setBackgroundResource(R.drawable.button_bg);
                meirijinping.setTextColor(Color.WHITE);
                shijituijian.setBackgroundResource(R.drawable.button_bg_2);
                shijituijian.setTextColor(Color.BLACK);
                replacrFragment1(new MeiRiTuijian());
            }
        });


        user_word_result=view.findViewById(R.id.user_word_result);
        Call<UserWord> userword = RestrofitTool.getmApi().getUserWord(ShareUtils.getString(getContext(),"token",null),
                ShareUtils.getString(getContext(),"username","lxy"));
        userword.enqueue(new Callback<UserWord>() {
            @Override
            public void onResponse(Call<UserWord> call, Response<UserWord> response) {
                user_word_result.setText(String.valueOf(response.body().getWordsnum()));
                ShareUtils.putInt(getContext(),"wordnum",response.body().getWordsnum());
            }

            @Override
            public void onFailure(Call<UserWord> call, Throwable t) {

            }
        });
        re_test_word=view.findViewById(R.id.re_test_word);
        re_test_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   dialog.show();
                  /* frameLayout.setVisibility(View.GONE);
                   replacrFragment(new RecommendTest());*/

            }
        });
    }


    private void replacrFragment(Fragment fragment){
        FragmentManager fragmentManage=getChildFragmentManager();
        FragmentTransaction transaction=fragmentManage.beginTransaction();
        transaction.replace(R.id.uset_recommend,fragment);
        transaction.commit();
    }

    private void replacrFragment1(Fragment fragment){
        FragmentManager fragmentManage=getChildFragmentManager();
        FragmentTransaction transaction=fragmentManage.beginTransaction();
        transaction.replace(R.id.recommend_chage,fragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.dialog_yes:
                frameLayout.setVisibility(View.GONE);
                replacrFragment(new RecommendTest());
                dialog.dismiss();
                break;
            case R.id.dialog_no:
                dialog.dismiss();
                break;
        }
    }
}
