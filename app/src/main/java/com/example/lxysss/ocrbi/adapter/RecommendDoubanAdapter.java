package com.example.lxysss.ocrbi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lxysss.ocrbi.Entity.getBook;
import com.example.lxysss.ocrbi.Entity.getDoubanBook;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activity.BookDetailActivity;
import com.example.lxysss.ocrbi.activityTool.IGImageView;

import java.util.List;

/**
 * Created by Lxysss on 2019/4/6.
 */

public class RecommendDoubanAdapter extends RecyclerView.Adapter<RecommendDoubanAdapter.ViewHolder> {
    private List<getDoubanBook> mBookList;
    private View mView;
    public RecommendDoubanAdapter(List<getDoubanBook> BookList){
        mBookList=BookList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //IGImageView book_img;
        IGImageView douban_book_img;
        TextView douban_book_title,douban_book_author,douban_book_rate,douabn_book_tuijian;


        public ViewHolder(View view) {
            super(view);
            mView=view;
            douban_book_img=view.findViewById(R.id.douban_book_img);
            douban_book_title=view.findViewById(R.id.douban_book_title);
            douban_book_author=view.findViewById(R.id.douban_book_author);
            douban_book_rate=view.findViewById(R.id.douban_book_rate);
            douabn_book_tuijian=view.findViewById(R.id.douabn_book_tuijian);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recommend_douban_item_recy,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        //点击整个item
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                getDoubanBook book=mBookList.get(position);
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
            getDoubanBook getBook = mBookList.get(position);

            //  displayImage(mView.getContext(),book.getBookpic(),holder.book_img);
            holder.douban_book_title.setText(getBook.getTitle());
            holder.douban_book_author.setText("作者： "+getBook.getAuthor());
            holder.douban_book_rate.setText("豆瓣评分： "+getBook.getRating());
            holder.douabn_book_tuijian.setText("豆瓣推荐数： "+getBook.getVotes());
            displayImage(mView.getContext(),getBook.getSimage(),holder.douban_book_img);
        }
    @Override
    public int getItemCount() {
        return mBookList.size();
    }

   public void displayImage(Context context, String path, IGImageView imageView) {
        imageView.showImage(path);
    }

}
