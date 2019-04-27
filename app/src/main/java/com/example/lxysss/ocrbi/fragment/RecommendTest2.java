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

import com.example.lxysss.ocrbi.Entity.Bean2;
import com.example.lxysss.ocrbi.Entity.WordResult;
import com.example.lxysss.ocrbi.Entity.WordTest;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activityTool.CustomDialog;
import com.example.lxysss.ocrbi.activityTool.ShareUtils;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lxysss on 2019/4/12.
 */

public class RecommendTest2 extends Fragment implements View.OnClickListener{
    private TextView test_word_2,test_commnet_2;
    private View test_word_yes_2,test_word_no_2,dialog_yes;
    private int Length;
    private List<String> wordList=new ArrayList<>();
    private int flag=0, item=0;
    private CustomDialog dialog;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recommend_test_2,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        test_word_yes_2=view.findViewById(R.id.test_word_yes_2);
        test_word_no_2=view.findViewById(R.id.test_word_no_2);
        test_word_2=view.findViewById(R.id.test_word_2);
        test_commnet_2=view.findViewById(R.id.test_commnet_2);
        test_word_yes_2.setOnClickListener(this);
        test_word_no_2.setOnClickListener(this);
        test_word_2.setOnClickListener(this);
        test_commnet_2.setOnClickListener(this);
        test_word_yes_2.setEnabled(true);
        test_word_no_2.setEnabled(true);
        //初始化dialog
        dialog = new CustomDialog(getActivity(), 0, 0,
                R.layout.dialog_test_2, R.style.MyDialog, Gravity.CENTER, 0);
        //提示框以外点击无效
        dialog.setCancelable(false);
        dialog_yes=dialog.findViewById(R.id.dialog_yes);
        dialog_yes.setOnClickListener(this);
        Call<List<WordTest>> testWordf2 = RestrofitTool.getmApi().testWordf2(ShareUtils.getString(getContext(),"token",null),
                ShareUtils.getInt(getContext(),"wordtestf1",0));
        testWordf2.enqueue(new Callback<List<WordTest>>() {
            @Override
            public void onResponse(Call<List<WordTest>> call, Response<List<WordTest>> response) {
                Length=response.body().size();
                for(int i=0;i<response.body().size();i++){
                    //  test_word.setText(response.body().get(i).getWord());
                    // if(test_word_yes.callOnClick())
                    wordList.add(response.body().get(i).getWord());
                    Log.i("word:",response.body().get(i).getWord());
                }
                test_word_2.setText(wordList.get(item));
            }

            @Override
            public void onFailure(Call<List<WordTest>> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test_word_yes_2:
                flag++;
                item++;
                if(item!=Length){test_word_2.setText(wordList.get(item));}
                if(item==Length){
                    test_word_yes_2.setEnabled(false);
                    test_word_no_2.setEnabled(false);
                   // Toast.makeText(getContext(),String.valueOf(flag),Toast.LENGTH_SHORT).show();
                    ShareUtils.putInt(getContext(),"wordtestf2",flag);
                    userword();
                   // Toast.makeText(getContext(), "恭喜！词汇量测试完成啦！", Toast.LENGTH_SHORT).show();
                  /*  Fragment fragment = RecommendTest2.this.getParentFragment(); // getParentFragment() 是 Android 内置的方法
                    RecommendUser frag = (RecommendUser) fragment; // 注意一定要进行这个类型转化，使 fragment 泛型变为某个特定的父碎片
                    frag.finishMyChild();
                    */
                  dialog.show();
                }
                break;
            case R.id.test_word_no_2:
                item++;
                if(item!=Length){test_word_2.setText(wordList.get(item));}
                if(item==Length){
                    test_word_yes_2.setEnabled(false);
                    test_word_no_2.setEnabled(false);
                    ShareUtils.putInt(getContext(),"wordtestf2",flag);
                    userword();
                  //  Toast.makeText(getContext(), "恭喜！词汇量测试完成啦！", Toast.LENGTH_SHORT).show();
                    dialog.show();
                }
                break;
            case R.id.dialog_yes:
                replacrFragment(new RecommendUser());
                dialog.dismiss();
                break;
        }
    }

    private void replacrFragment(Fragment fragment){
        FragmentManager fragmentManage=getChildFragmentManager();
        FragmentTransaction transaction=fragmentManage.beginTransaction();
        transaction.replace(R.id.test_page_2,fragment);
        transaction.commit();
    }

    private void userword(){
        Call<WordResult> wordResult= RestrofitTool.getmApi().testResult(ShareUtils.getString(getContext(),"token",null),
                ShareUtils.getString(getContext(),"username","lxy"),
                ShareUtils.getInt(getContext(),"wordtestf1",0),
                ShareUtils.getInt(getContext(),"wordtestf2",0));

        wordResult.enqueue(new Callback<WordResult>() {
            @Override
            public void onResponse(Call<WordResult> call, Response<WordResult> response) {
                if(response.body().getCode()==1001){
                   // Toast.makeText(getContext(), response.body().getMsg2()+response.body().getWordnum().toString(), Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<WordResult> call, Throwable t) {

            }
        });

    }
}
