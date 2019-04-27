package com.example.lxysss.ocrbi.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lxysss.ocrbi.Entity.getBook;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.Entity.Book;
import com.example.lxysss.ocrbi.activity.BookDetailActivity;
import com.example.lxysss.ocrbi.activityTool.IGImageView;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by Lxysss on 2019/4/6.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {
    private List<getBook> mBookList;
    private View mView;
    public RecommendAdapter(List<getBook> BookList){
        mBookList=BookList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //IGImageView book_img;
        IGImageView book_img;
        TextView book_name;


        public ViewHolder(View view) {
            super(view);
            mView=view;
            book_img=view.findViewById(R.id.book_img);

            book_name=view.findViewById(R.id.book_name);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recommend_item_recy,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        //点击整个item
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                getBook book=mBookList.get(position);
                Intent intent=new Intent(v.getContext(), BookDetailActivity.class);
                intent.putExtra("book_title",book.getTitle());
                intent.putExtra("book_pic",book.getSimage());
                intent.putExtra("book_author",book.getAuthor());
                intent.putExtra("book_summary",book.getSummary());
                intent.putExtra("book_publisher",book.getPublisher());
                intent.putExtra("book_isbn",book.getIsbn());
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

        public void onBindViewHolder(final ViewHolder holder, int position) {
            getBook getBook = mBookList.get(position);

            //  displayImage(mView.getContext(),book.getBookpic(),holder.book_img);
            holder.book_name.setText(getBook.getTitle());

            displayImage(mView.getContext(),getBook.getSimage(),holder.book_img);
        }
    @Override
    public int getItemCount() {
        return mBookList.size();
    }

   public void displayImage(Context context, String path, IGImageView imageView) {
        imageView.showImage(path);
    }

}
