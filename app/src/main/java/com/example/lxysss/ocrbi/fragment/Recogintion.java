package com.example.lxysss.ocrbi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lxysss.ocrbi.Entity.Word;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activity.RecogintionActivity;
import com.example.lxysss.ocrbi.activity.WordinfActivity;
import com.example.lxysss.ocrbi.activityTool.ShareUtils;
import com.example.lxysss.ocrbi.adapter.WordInfAdapter;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Lxysss on 2019/4/6.
 */

public class Recogintion extends Fragment implements View.OnClickListener{
    private ImageView photo_recogintion;
    private String YU_YIN_KEY="5cbe704d";
    private EditText word_edit_text;
    private Button word_edit_text_find;
    private List<Word> mListWord=new ArrayList<>();
    private  RecyclerView recyclerView;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recogintion_page,container,false);
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(getActivity(), SpeechConstant.APPID +"=5cbe704d");
        recyclerView = view.findViewById(R.id.word_recycler);
        initView(view);
        return view;
    }

    public void initView(View view){
        final View mview=view;
        photo_recogintion=view.findViewById(R.id.photo_recogintion);
        photo_recogintion.setOnClickListener(this);
        word_edit_text=view.findViewById(R.id.word_edit_text);
        word_edit_text_find=view.findViewById(R.id.word_edit_text_find);
        word_edit_text_find.setOnClickListener(this);

        mListWord.clear();
        Call<List<Word>> wordhistory= RestrofitTool.getmApi().getHistoryWord(ShareUtils.getString(getContext(),"token",null),
                ShareUtils.getString(getContext(),"username","lxy"));
        wordhistory.enqueue(new Callback<List<Word>>() {
            @Override
            public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                for(int i=0;i<response.body().size();i++){
                    Word word=new Word(1001,
                            response.body().get(i).getWord(),
                            response.body().get(i).getGif(),
                            response.body().get(i).getPinyin(),
                            response.body().get(i).getBihua(),
                            response.body().get(i).getBushou(),
                            response.body().get(i).getYisi1(),
                            response.body().get(i).getYisi2(),
                            response.body().get(i).getYisi3());
                    mListWord.add(word);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    //  GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);//2列
                    recyclerView.setLayoutManager(layoutManager);
                    WordInfAdapter adapter = new WordInfAdapter(mListWord);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Word>> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.photo_recogintion:
                Intent intent =new Intent(v.getContext(), RecogintionActivity.class);
                startActivity(intent);
                break;
            case R.id.word_edit_text_find:
                if(word_edit_text.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"输入不能为空哦！",Toast.LENGTH_LONG).show();
                }else{
                    Call<Word> wordCall= RestrofitTool.getmApi().getwordMsg(ShareUtils.getString(getContext(),"token",null),
                            word_edit_text.getText().toString().substring(0,1),
                            ShareUtils.getString(getContext(),"username","lxy"));
                    wordCall.enqueue(new Callback<Word>() {
                        @Override
                        public void onResponse(Call<Word> call, Response<Word> response) {
                            if(response.body().getCode()==1001){
                                Intent intent=new Intent(getContext(), WordinfActivity.class);
                                intent.putExtra("word",response.body().getWord());
                                intent.putExtra("gif",response.body().getGif());
                                intent.putExtra("pinyin",response.body().getPinyin());
                                intent.putExtra("bihua",response.body().getBihua());
                                intent.putExtra("bushou",response.body().getBushou());
                                intent.putExtra("yisi1",response.body().getYisi1());
                                intent.putExtra("yisi2",response.body().getYisi2());
                                intent.putExtra("yisi3",response.body().getYisi3());
                                if(response.body().getWord()==null){
                                    Toast.makeText(getActivity(),"没有找到这个汉字哦！",Toast.LENGTH_LONG).show();
                                }else{
                                    startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Word> call, Throwable t) {

                        }
                    });
                }
        }
    }
}
