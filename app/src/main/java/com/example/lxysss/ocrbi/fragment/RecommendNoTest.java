package com.example.lxysss.ocrbi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activityTool.CustomDialog;

/**
 * Created by Lxysss on 2019/4/12.
 */

public class RecommendNoTest extends Fragment implements View.OnClickListener{
    private Button test_recommeng_button;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recommend_no_test,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        test_recommeng_button=view.findViewById(R.id.test_recommeng_button);
        test_recommeng_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.test_recommeng_button:
                test_recommeng_button.setEnabled(false);
                replacrFragment(new RecommendTest());
                Toast.makeText(getContext(),"进入词汇量初步测试！",Toast.LENGTH_SHORT).show();
                break;
        }

    }
    private void replacrFragment(Fragment fragment){
        FragmentManager fragmentManage=getChildFragmentManager();
        FragmentTransaction transaction=fragmentManage.beginTransaction();
        transaction.replace(R.id.no_test_page,fragment);
        transaction.commit();
    }
}
