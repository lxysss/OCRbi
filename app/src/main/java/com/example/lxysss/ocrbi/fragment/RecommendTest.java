package com.example.lxysss.ocrbi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxysss.ocrbi.Entity.UserIof;
import com.example.lxysss.ocrbi.Entity.WordTest;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activity.LoginActivity;
import com.example.lxysss.ocrbi.activityTool.CustomDialog;
import com.example.lxysss.ocrbi.activityTool.ShareUtils;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lxysss on 2019/4/12.
 */

public class RecommendTest extends Fragment implements View.OnClickListener{
    private TextView test_word,test_commnet;
    private View test_word_yes,test_word_no,dialog_yes;
    private List<String> wordList=new ArrayList<>();
    private int flag=0, item=0;
    private CustomDialog dialog;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recommend_test,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        test_word_yes=view.findViewById(R.id.test_word_yes);
        test_word_no=view.findViewById(R.id.test_word_no);
        test_word=view.findViewById(R.id.test_word);
        test_commnet=view.findViewById(R.id.test_commnet);
        test_word_yes.setOnClickListener(this);
        test_word_no.setOnClickListener(this);
        test_word.setOnClickListener(this);
        test_commnet.setOnClickListener(this);
        test_word_yes.setEnabled(true);
        test_word_no.setEnabled(true);
         //初始化dialog
        dialog = new CustomDialog(getActivity(), 0, 0,
                R.layout.dialog_test_1, R.style.MyDialog, Gravity.CENTER, 0);
        //提示框以外点击无效
        dialog.setCancelable(false);
        dialog_yes=dialog.findViewById(R.id.dialog_yes);
        dialog_yes.setOnClickListener(this);
        Call<List<WordTest>> testWordf1 = RestrofitTool.getmApi().testWordf1(ShareUtils.getString(getContext(),"token",null));
        testWordf1.enqueue(new Callback<List<WordTest>>() {
            @Override
            public void onResponse(Call<List<WordTest>> call, Response<List<WordTest>> response) {
                for(int i=0;i<response.body().size();i++){
                  //  test_word.setText(response.body().get(i).getWord());
                   // if(test_word_yes.callOnClick())
                    wordList.add(response.body().get(i).getWord());
                    Log.i("word:",response.body().get(i).getWord());
                }
                test_word.setText(wordList.get(item));
            }

            @Override
            public void onFailure(Call<List<WordTest>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test_word_yes:
                flag++;
                item++;
                if(item!=30){test_word.setText(wordList.get(item));}
                if(item==30){
                    test_word_yes.setEnabled(false);
                    test_word_no.setEnabled(false);
                    //Toast.makeText(getContext(),String.valueOf(flag),Toast.LENGTH_SHORT).show();
                    ShareUtils.putInt(getContext(),"wordtestf1",flag);
                   // Toast.makeText(getContext(),"进入词汇量精确测试！",Toast.LENGTH_SHORT).show();
                    dialog.show();
                }
                break;
            case R.id.test_word_no:
                item++;
                if(item!=30){test_word.setText(wordList.get(item));}
                if(item==30){
                    test_word_yes.setEnabled(false);
                    test_word_no.setEnabled(false);
                    ShareUtils.putInt(getContext(),"wordtestf1",flag);
                    //Toast.makeText(getContext(),"进入词汇量精确测试！",Toast.LENGTH_SHORT).show();
                    dialog.show();
                }
                break;
            case R.id.dialog_yes:
                replacrFragment(new RecommendTest2());
                dialog.dismiss();
                break;
        }
    }

    private void replacrFragment(Fragment fragment){
        FragmentManager fragmentManage=getChildFragmentManager();
        FragmentTransaction transaction=fragmentManage.beginTransaction();
        transaction.replace(R.id.test_page,fragment);
        transaction.commit();
    }
}

