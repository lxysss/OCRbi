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
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxysss.ocrbi.Entity.Rank;
import com.example.lxysss.ocrbi.Entity.UserWord;
import com.example.lxysss.ocrbi.Entity.getBook;
import com.example.lxysss.ocrbi.R;
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
 * Created by Lxysss on 2019/4/14.
 */

public class Shizishutuijian extends Fragment implements View.OnClickListener{
    private List<getBook> mListBook=new ArrayList<>();
    private Button rank_1,rank_2,rank_3,rank_4,rank_5,rank_6;
    private TextView rank_text;
    private Integer rank=0;
    private   RecyclerView recyclerView ;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.shi_zi_tui_jian,container,false);
       // Fresco.initialize(view.getContext());   //注意初始化  不然自定义控件会出错
        rank_1=view.findViewById(R.id.rank_1);
        rank_2=view.findViewById(R.id.rank_2);
        rank_3=view.findViewById(R.id.rank_3);
        rank_4=view.findViewById(R.id.rank_4);
        rank_5=view.findViewById(R.id.rank_5);
        rank_6=view.findViewById(R.id.rank_6);
        rank_1.setOnClickListener(this);
        rank_2.setOnClickListener(this);
        rank_3.setOnClickListener(this);
        rank_4.setOnClickListener(this);
        rank_5.setOnClickListener(this);
        rank_6.setOnClickListener(this);
        rank_text=view.findViewById(R.id.rank_text_tz);
        recyclerView = view.findViewById(R.id.recommend_recycler);
        initView(view);
        return view;
}

    public void initView(View view){
        final View mView=view;
        Integer wordnum=ShareUtils.getInt(getContext(),"wordnum",0);
        if(0<wordnum && wordnum<1000){
            rank=1;
        }else if(1000<=wordnum && wordnum<1800){
            rank=2;
        }else if(1800<=wordnum && wordnum<2150){
            rank=3;
        }else if(2150<=wordnum && wordnum<2500){
            rank=4;
        }else if(2500<=wordnum && wordnum<2750){
            rank=5;
        }else if(2750<=wordnum && wordnum<=3000){
            rank=6;
        }else rank=0;
        ranktobook(rank);

        Log.e("Rank",String.valueOf(rank));
            if (rank==1) {
                rank_1.setBackgroundResource(R.drawable.btn_shape_4);
                rank_text.setText("根据您的识字数为您推荐A档书籍");
            } else if (rank==2) {
                rank_2.setBackgroundResource(R.drawable.btn_shape_4);
                rank_text.setText("根据您的识字数为您推荐B档书籍");
            } else if (rank==3) {
                rank_3.setBackgroundResource(R.drawable.btn_shape_4);
                rank_text.setText("根据您的识字数为您推荐C档书籍");
            } else if (rank==4) {
                rank_4.setBackgroundResource(R.drawable.btn_shape_4);
                rank_text.setText("根据您的识字数为您推荐D档书籍");
            } else if (rank==5) {
                rank_5.setBackgroundResource(R.drawable.btn_shape_4);
                rank_text.setText("根据您的识字数为您推荐E档书籍");
            } else if(rank==6){
                rank_6.setBackgroundResource(R.drawable.btn_shape_4);
                rank_text.setText("根据您的识字数为您推荐F档书籍");
            }else{
                rank_text.setText("出错了呢！");
            }

            //   ranktobook(rank);
        }




    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.rank_1:
                if(rank>1){
                    Toast.makeText(getContext(),"这个等级的书籍对您来说有点简单呢！",Toast.LENGTH_SHORT).show();
                }
                rank_1.setBackgroundResource(R.drawable.btn_shape_4);
                rank_2.setBackgroundResource(R.drawable.btn_shape_3);
                rank_3.setBackgroundResource(R.drawable.btn_shape_3);
                rank_4.setBackgroundResource(R.drawable.btn_shape_3);
                rank_5.setBackgroundResource(R.drawable.btn_shape_3);
                rank_6.setBackgroundResource(R.drawable.btn_shape_3);
                mListBook.clear();
                ranktobook(1);
                break;
            case R.id.rank_2:
                if(rank>2){
                    Toast.makeText(getContext(),"这个等级的书籍对您来说有点简单呢！",Toast.LENGTH_SHORT).show();
                }else if(rank<2){
                    Toast.makeText(getContext(),"这个等级的书籍对您来说有点难呢！",Toast.LENGTH_SHORT).show();
                }
                rank_1.setBackgroundResource(R.drawable.btn_shape_3);
                rank_2.setBackgroundResource(R.drawable.btn_shape_4);
                rank_3.setBackgroundResource(R.drawable.btn_shape_3);
                rank_4.setBackgroundResource(R.drawable.btn_shape_3);
                rank_5.setBackgroundResource(R.drawable.btn_shape_3);
                rank_6.setBackgroundResource(R.drawable.btn_shape_3);
                mListBook.clear();
                ranktobook(2);
                break;
            case R.id.rank_3:
                if(rank>3){
                    Toast.makeText(getContext(),"这个等级的书籍对您来说有点简单呢！",Toast.LENGTH_SHORT).show();
                }else if(rank<3){
                    Toast.makeText(getContext(),"这个等级的书籍对您来说有点难呢！",Toast.LENGTH_SHORT).show();
                }
                rank_1.setBackgroundResource(R.drawable.btn_shape_3);
                rank_2.setBackgroundResource(R.drawable.btn_shape_3);
                rank_3.setBackgroundResource(R.drawable.btn_shape_4);
                rank_4.setBackgroundResource(R.drawable.btn_shape_3);
                rank_5.setBackgroundResource(R.drawable.btn_shape_3);
                rank_6.setBackgroundResource(R.drawable.btn_shape_3);
                mListBook.clear();
                ranktobook(3);
                break;
            case R.id.rank_4:
                if(rank>4){
                    Toast.makeText(getContext(),"这个等级的书籍对您来说有点简单呢！",Toast.LENGTH_SHORT).show();
                }else if(rank<4){
                    Toast.makeText(getContext(),"这个等级的书籍对您来说有点难呢！",Toast.LENGTH_SHORT).show();
                }
                rank_1.setBackgroundResource(R.drawable.btn_shape_3);
                rank_2.setBackgroundResource(R.drawable.btn_shape_3);
                rank_3.setBackgroundResource(R.drawable.btn_shape_3);
                rank_4.setBackgroundResource(R.drawable.btn_shape_4);
                rank_5.setBackgroundResource(R.drawable.btn_shape_3);
                rank_6.setBackgroundResource(R.drawable.btn_shape_3);
                mListBook.clear();
                ranktobook(4);
                break;
            case R.id.rank_5:
                if(rank>5){
                    Toast.makeText(getContext(),"这个等级的书籍对您来说有点简单呢！",Toast.LENGTH_SHORT).show();
                }else if(rank<5){
                    Toast.makeText(getContext(),"这个等级的书籍对您来说有点难呢！",Toast.LENGTH_SHORT).show();
                }
                rank_1.setBackgroundResource(R.drawable.btn_shape_3);
                rank_2.setBackgroundResource(R.drawable.btn_shape_3);
                rank_3.setBackgroundResource(R.drawable.btn_shape_3);
                rank_4.setBackgroundResource(R.drawable.btn_shape_3);
                rank_5.setBackgroundResource(R.drawable.btn_shape_4);
                rank_6.setBackgroundResource(R.drawable.btn_shape_3);
                mListBook.clear();
                ranktobook(5);
                break;
            case R.id.rank_6:
               if(rank<6){
                    Toast.makeText(getContext(),"这个等级的书籍对您来说有点难呢！",Toast.LENGTH_SHORT).show();
                }
                rank_1.setBackgroundResource(R.drawable.btn_shape_3);
                rank_2.setBackgroundResource(R.drawable.btn_shape_3);
                rank_3.setBackgroundResource(R.drawable.btn_shape_3);
                rank_4.setBackgroundResource(R.drawable.btn_shape_3);
                rank_5.setBackgroundResource(R.drawable.btn_shape_3);
                rank_6.setBackgroundResource(R.drawable.btn_shape_4);
                mListBook.clear();
                ranktobook(6);
                break;
        }
    }

    public void ranktobook(Integer rank){
         Call<List<getBook>> getbook = RestrofitTool.getmApi().getBook(ShareUtils.getString(getContext(),"token",null),
                rank);
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
                // LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);//2列
                recyclerView.setLayoutManager(gridLayoutManager);
                RecommendAdapter adapter = new RecommendAdapter(mListBook);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<getBook>> call, Throwable t) {
                Log.i("书籍列表：",t.getMessage());
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
