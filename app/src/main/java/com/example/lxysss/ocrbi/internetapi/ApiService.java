package com.example.lxysss.ocrbi.internetapi;

import android.content.Intent;
import android.graphics.Bitmap;

import com.example.lxysss.ocrbi.Entity.Bean;
import com.example.lxysss.ocrbi.Entity.Bean2;
import com.example.lxysss.ocrbi.Entity.Favorite;
import com.example.lxysss.ocrbi.Entity.Img;
import com.example.lxysss.ocrbi.Entity.IsbnBook;
import com.example.lxysss.ocrbi.Entity.Rank;
import com.example.lxysss.ocrbi.Entity.UserIof;
import com.example.lxysss.ocrbi.Entity.UserWord;
import com.example.lxysss.ocrbi.Entity.Useriofm;
import com.example.lxysss.ocrbi.Entity.Word;
import com.example.lxysss.ocrbi.Entity.WordResult;
import com.example.lxysss.ocrbi.Entity.WordTest;
import com.example.lxysss.ocrbi.Entity.getBook;
import com.example.lxysss.ocrbi.Entity.getDoubanBook;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Lxysss on 2019/4/10.
 */

public interface ApiService {
    //注册
    @Multipart
    @POST("api/usr/register/")
    Call<Useriofm> register(@Part("username") RequestBody username, @Part("password") RequestBody password,
                            @Part("phone") RequestBody phone, @Part("mail") RequestBody mail,
                            @Part("sex") RequestBody sex, @Part("age") RequestBody age);

    //登录
    @Multipart
    @POST("api/usr/auth/")
    Call<Bean> login(@Part("username") RequestBody username, @Part("password") RequestBody password);

    //用户信息
    @GET("api/usr/info/")
    Call<UserIof> getUserInfo(@Query("token")String token, @Query("username")String username);

    // 用户词汇量获取
    @GET("api/word/info/")
    Call<UserWord> getUserWord(@Query("token")String token, @Query("username")String username);

    //修改用户信息
    @Multipart
    @POST("api/usr/info/edit/")
    Call<Useriofm> edit(@Part("token") RequestBody token,@Part("username") RequestBody username,
                        @Part("phone") RequestBody phone, @Part("mail") RequestBody mail
                                 , @Part("age") RequestBody age , @Part("sex") RequestBody sex);

    //修改密码
    @Multipart
    @POST("api/usr/pwd/modification/")
    Call<Useriofm> passedit(@Part("token") RequestBody token,@Part("username") RequestBody username, @Part("oldpassword") RequestBody oldpassword
                            ,@Part("password") RequestBody password);

    //用户词汇量初步测试
    @GET("api/word/test/step1/")
    Call<List<WordTest>> testWordf1(@Query("token")String token);


    //用户词汇量精确测试
    @GET("api/word/test/step2/")
    Call<List<WordTest>> testWordf2(@Query("token")String token,@Query("wordfirst")Integer wordfirst);

    //用户词汇量测试结果
    @GET("api/word/test/result/")
    Call<WordResult> testResult(@Query("token")String token,@Query("username")String username, @Query("wordfirst")Integer wordfirst
               , @Query("wordsecond")Integer wordsecond);

    // 用户书籍推荐
    @GET("api/book/recommend/")
    Call<List<getBook>> getBook(@Query("token")String token, @Query("rank")Integer rank);

    // 豆瓣书籍推荐
    @GET("api/book/douban/kids/")
    Call<List<getDoubanBook>> getDoubanBook(@Query("token")String token, @Query("username")String username);

    //获取验证码
    @GET("api/usr/register/msg/")
    Call<Bean2> getPhoneYZ(@Query("phone")String phone);

    //验证验证码
    @Multipart
    @POST("api/usr/register/msg/")
    Call<Bean2> getPhoneYZYZ(@Part("phone") RequestBody phone,@Part("code") RequestBody code);

    //获取验证码
    @GET("api/usr/pwd/lost/")
    Call<Bean2> getlostPass(@Query("phone")String phone);

    //验证验证码
    @Multipart
    @POST("api/usr/pwd/lost/")
    Call<Bean2> getlostPassYZ(@Part("phone") RequestBody phone,@Part("code") RequestBody code);

    //修改密码
    @Multipart
    @POST("api/usr/pwd/lost/change/")
    Call<Bean2> passchange(@Part("phone") RequestBody phone, @Part("password") RequestBody password);

    // 用户等级查询
    @GET("api/book/rank/")
    Call<Rank> getRank(@Query("token")String token, @Query("username")String username);

    // 用户书籍查询
    @GET("api/book/favorite/")
    Call<List<getBook>> getBookManage(@Query("token")String token, @Query("username")String username);

    //删除书籍
    @Multipart
    @POST("api/book/favorite/del/")
    Call<Bean2> DeleteBook(@Part("token") RequestBody token,@Part("username") RequestBody username,@Part("isbn") RequestBody isbn);

    //添加书籍
    @Multipart
    @POST("api/book/favorite/")
    Call<Bean2> PutBook(@Part("token") RequestBody token,@Part("username") RequestBody username,@Part("isbn") RequestBody isbn);

    // 判断书籍是否收藏
    @GET("api/book/favorite/is/")
    Call<Favorite> isFavorite(@Query("token")String token, @Query("username")String username,@Query("isbn")String isbn);

    // 通过ISBN获取书籍信息
    @GET("api/book/isbn/")
    Call<IsbnBook> IsbnToBook(@Query("token")String token, @Query("isbn")String isbn);

    // 书架中搜索书籍信息
    @GET("api/book/favorite/search/")
    Call<List<getBook>> getBookSearch(@Query("token")String token, @Query("username")String username,@Query("bookname")String bookname);

    // 书库中搜索书籍信息
    @GET("api/book/search/")
    Call<List<getBook>> getBookSearchSK(@Query("token")String token,@Query("bookname")String bookname);

    // 获得识字历史记录
    @GET("api/ocr/history/")
    Call<List<Word>> getHistoryWord(@Query("token")String token, @Query("username")String username);

    //添加识字记录
    @Multipart
    @POST("api/ocr/history/")
    Call<Word> putHistoryWord(@Part("token") RequestBody token,@Part("username") RequestBody username,@Part("word") RequestBody word);

    // 获得汉字信息
    @GET("api/ocr/info/")
    Call<Word> getwordMsg(@Query("token")String token, @Query("word")String word,@Query("username")String username);

    //删除用户历史记录
    @Multipart
    @POST("api/ocr/history/del/")
    Call<Bean2> DeleteWord(@Part("token") RequestBody token,@Part("username") RequestBody username,@Part("word") RequestBody word);

    // 上传图片
    @Multipart
    @POST("tool/img/1/")
    Call<Img> putimg(@Part MultipartBody.Part file);

    // 相机获取汉字信息
    @GET("tool/img/point/")
    Call<Word> getwordMsgPhoto(@Query("point_x")Integer point_x, @Query("point_y")Integer point_y,@Query("username")String username);
}
