package com.example.lxysss.ocrbi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxysss.ocrbi.Entity.Bean2;
import com.example.lxysss.ocrbi.Entity.Word;
import com.example.lxysss.ocrbi.Entity.getBook;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activity.BookDetailActivity;
import com.example.lxysss.ocrbi.activity.WordinfActivity;
import com.example.lxysss.ocrbi.activityTool.IGImageView;
import com.example.lxysss.ocrbi.activityTool.ShareUtils;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

/**
 * Created by Lxysss on 2019/4/6.
 */

public class WordInfAdapter extends RecyclerView.Adapter<WordInfAdapter.ViewHolder> {
    private List<Word> mBookList;
    private View mView;
    public WordInfAdapter(List<Word> BookList){
        mBookList=BookList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //IGImageView book_img;
        TextView word_word,word_pingyin,word_yisi1;
        ImageView word_delect;

        public ViewHolder(View view) {
            super(view);
            mView=view;
            word_word=view.findViewById(R.id.word_word);
            word_yisi1=view.findViewById(R.id.word_yisi1);
            word_pingyin=view.findViewById(R.id.word_pingyin);
            word_delect=view.findViewById(R.id.word_delect);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_item_recy,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        //点击整个item
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Word word=mBookList.get(position);
                Intent intent=new Intent(v.getContext(), WordinfActivity.class);
                intent.putExtra("word",word.getWord());
                intent.putExtra("gif",word.getGif());
                intent.putExtra("pinyin",word.getPinyin());
                intent.putExtra("bihua",word.getBihua());
                intent.putExtra("bushou",word.getBushou());
                intent.putExtra("yisi1",word.getYisi1());
                intent.putExtra("yisi2",word.getYisi2());
                intent.putExtra("yisi3",word.getYisi3());
                v.getContext().startActivity(intent);
            }
        });

        //点击确删除
        holder.word_delect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Word word=mBookList.get(position);
                RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"),
                        ShareUtils.getString(v.getContext(), "token", null));
                RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"),
                        ShareUtils.getString(v.getContext(), "username", "lxy"));
                RequestBody userword = RequestBody.create(MediaType.parse("multipart/form-data"),
                        word.getWord());
                Call<Bean2> deleteWord= RestrofitTool.getmApi().DeleteWord(token,username,userword);
                deleteWord.enqueue(new Callback<Bean2>() {
                    @Override
                    public void onResponse(Call<Bean2> call, Response<Bean2> response) {
                        if(response.body().getCode()==1001){
                            Toast.makeText(view.getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Bean2> call, Throwable t) {

                    }
                });
                mBookList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(0,mBookList.size());
            }
        });
        return holder;
    }

        public void onBindViewHolder(final ViewHolder holder, int position) {
            Word word = mBookList.get(position);

            //  displayImage(mView.getContext(),book.getBookpic(),holder.book_img);
            holder.word_word.setText(word.getWord());
            if(word.getYisi1().length()>10) {
                holder.word_yisi1.setText(word.getYisi1().substring(0, 10)+"...");
            }else {
                holder.word_yisi1.setText(word.getYisi1()+"...");
            }
            if(word.getPinyin().length()>10) {
                holder.word_pingyin.setText(word.getPinyin().substring(0, 10)+"...");
            }else {
                holder.word_pingyin.setText(word.getPinyin()+"...");
            }
        }
    @Override
    public int getItemCount() {
        return mBookList.size();
    }

   public void displayImage(Context context, String path, IGImageView imageView) {
        imageView.showImage(path);
    }

}
