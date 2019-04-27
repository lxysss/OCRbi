package com.example.lxysss.ocrbi.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activityTool.ActivityManager;
import com.example.lxysss.ocrbi.fragment.Recogintion;
import com.example.lxysss.ocrbi.fragment.Recommend;
import com.example.lxysss.ocrbi.fragment.UserManager;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity{

    private TabLayout tl_main;
    private ViewPager vp_main;
    private String[] mTitles = new String[]{"文字识别", "书籍推荐", "个人中心"};
    private List<Fragment> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActivityManager.getInstance().addActivity(this);  //加入活动容器
        initViews();
        initDatas();
    }

    private void initDatas() {
        vp_main.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mList.get(position);
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });
//        tl_main.addTab(tl_main.newTab().setText(mTitles[0]));
//        tl_main.addTab(tl_main.newTab().setText(mTitles[1]));
//        tl_main.addTab(tl_main.newTab().setText(mTitles[2]));
//        tl_main.addTab(tl_main.newTab().setText(mTitles[3]));

        // 跟随ViewPager的页面切换
        tl_main.setupWithViewPager(vp_main);

        tl_main.getTabAt(0).setIcon(R.drawable.selector_ico01);
        tl_main.getTabAt(1).setIcon(R.drawable.selector_ico02);
        tl_main.getTabAt(2).setIcon(R.drawable.selector_ico03);


        tl_main.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
//                if (position == 0) {
//
//                } else if (position == 1) {
//
//                } else if (position == 2) {
//
//                } else if (position == 3) {
//
//                }
                // 设置ViewPager的页面切换
                vp_main.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
//                if (position == 0) {
//
//                } else if (position == 1) {
//
//                } else if (position == 2) {
//
//                } else if (position == 3) {
//
//                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void initViews() {
        tl_main = (TabLayout) findViewById(R.id.mTabLayout);
        vp_main = (ViewPager) findViewById(R.id.mViewPager);
        mList = new ArrayList<>();

     mList.add(new Recogintion());
     mList.add(new Recommend());
     mList.add(new UserManager());
    }


}