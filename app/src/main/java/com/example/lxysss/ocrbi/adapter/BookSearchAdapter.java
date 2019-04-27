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
import com.example.lxysss.ocrbi.Entity.getBook;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activity.BookDetailActivity;
import com.example.lxysss.ocrbi.activityTool.IGImageView;
import com.example.lxysss.ocrbi.activityTool.ShareUtils;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lxysss on 2019/4/6.
 */

public class BookSearchAdapter extends RecyclerView.Adapter<BookSearchAdapter.ViewHolder> {
    private List<getBook> mBookList;
    private View mView;
    public BookSearchAdapter(List<getBook> BookList){
        mBookList=BookList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //IGImageView book_img;
        IGImageView book_img;
        TextView book_name;
        TextView book_author;
        ImageView delete_book;
        public ViewHolder(View view) {
            super(view);
            mView=view;
            book_img=view.findViewById(R.id.book_search_img);

            book_name=view.findViewById(R.id.book_search_name);

            book_author=view.findViewById(R.id.book_search_author);
           // delete_book=view.findViewById(R.id.delete_book);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_search_item,parent,false);
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

        //点击确删除
       /* holder.delete_book.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                getBook book=mBookList.get(position);
                RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"),
                        ShareUtils.getString(v.getContext(), "token", null));
                RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"),
                        ShareUtils.getString(v.getContext(), "username", "lxy"));
                RequestBody isbn = RequestBody.create(MediaType.parse("multipart/form-data"),
                        book.getIsbn());
                Call<Bean2> DeleteBook= RestrofitTool.getmApi().DeleteBook(token,username,isbn);
                DeleteBook.enqueue(new Callback<Bean2>() {
                    @Override
                    public void onResponse(Call<Bean2> call, Response<Bean2> response) {
                        if(response.body().getCode()==1001){
                            Toast.makeText(view.getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                        }
                        else if(response.body().getCode()==2000){
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
        });*/
        return holder;
    }

        public void onBindViewHolder(final ViewHolder holder, int position) {
            getBook getBook = mBookList.get(position);

            //  displayImage(mView.getContext(),book.getBookpic(),holder.book_img);
            holder.book_name.setText(getBook.getTitle());
            holder.book_author.setText("作者："+getBook.getAuthor());

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
