package com.example.lxysss.ocrbi.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxysss.ocrbi.Entity.IsbnBook;
import com.example.lxysss.ocrbi.Entity.UserIof;
import com.example.lxysss.ocrbi.Entity.UserWord;
import com.example.lxysss.ocrbi.Entity.Useriofm;
import com.example.lxysss.ocrbi.Entity.getBook;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activity.BookDetailActivity;
import com.example.lxysss.ocrbi.activity.BookManageActivity;
import com.example.lxysss.ocrbi.activity.LoginActivity;
import com.example.lxysss.ocrbi.activity.PasswordEditActivity;
import com.example.lxysss.ocrbi.activity.SearchBookActivity;
import com.example.lxysss.ocrbi.activity.UserInfoActivity;
import com.example.lxysss.ocrbi.activityTool.CustomDialog;
import com.example.lxysss.ocrbi.activityTool.ScanQRCodeActivity;
import com.example.lxysss.ocrbi.activityTool.ShareUtils;
import com.example.lxysss.ocrbi.activityTool.UtilTools;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static android.app.Activity.RESULT_OK;
import static android.provider.DocumentsContract.findDocumentPath;
import static android.provider.DocumentsContract.getDocumentId;

/**
 * Created by Lxysss on 2019/4/6.
 */

public class UserManager extends Fragment implements View.OnClickListener{
    private View btn_exit_user;

    //圆形头像
    private CircleImageView profile_image;
private ImageView get_image;
    private CustomDialog dialog,dialog_exit;

    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;
    private View password_edit;
    private View go_to_user;
    private TextView user_name;
    private View go_to_book;
    private View scan_book,search_shuku_book;
    private Button exit_no,exit_yes;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.user_page,container,false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        btn_exit_user = view.findViewById(R.id.btn_exit_user);
        btn_exit_user.setOnClickListener(this);
        user_name = view.findViewById(R.id.user_name);
        user_name.setText(ShareUtils.getString(getContext(), "username", "lxy"));
        password_edit = view.findViewById(R.id.password_edit);
        password_edit.setOnClickListener(this);
        go_to_user = view.findViewById(R.id.go_to_user);
        go_to_user.setOnClickListener(this);
        get_image = view.findViewById(R.id.get_image);
        get_image.setOnClickListener(this);
        go_to_book=view.findViewById(R.id.go_to_book);
        go_to_book.setOnClickListener(this);
        scan_book=view.findViewById(R.id.scan_book);
        scan_book.setOnClickListener(this);
        search_shuku_book=view.findViewById(R.id.search_shuku_book);
        search_shuku_book.setOnClickListener(this);
        //设置数值
      /*  et_username.setText("小明");
        et_sex.setText("男");
        et_age.setText("12");
        et_tel.setText("123456789");
        et_desc.setText("12");*/
        //  user();

        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);

        //初始化dialog
        dialog = new CustomDialog(getActivity(), 0, 0,
                R.layout.dialog_photo, R.style.pop_anim_style, Gravity.BOTTOM, 0,0);
        //提示框以外点击无效
        dialog.setCancelable(false);
        //初始化dialog
        dialog_exit = new CustomDialog(getActivity(), 0, 0,
                R.layout.dialog_exit, R.style.MyDialog, Gravity.CENTER, 0);
        //提示框以外点击无效
        dialog_exit.setCancelable(false);
        exit_no=dialog_exit.findViewById(R.id.exit_no);
        exit_no.setOnClickListener(this);
        exit_yes=dialog_exit.findViewById(R.id.exit_yes);
        exit_yes.setOnClickListener(this);
        btn_camera = dialog.findViewById(R.id.btn_camera);
        btn_picture = dialog.findViewById(R.id.btn_picture);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_camera.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        UtilTools.getImageToShare(getActivity(), profile_image);
    }
  /*  public void user(){
        Call<UserIof> user = RestrofitTool.getmApi().getUserInfo(ShareUtils.getString(getContext(),"token",null),
                ShareUtils.getString(getContext(),"username","lxy"));
        Log.i("token",ShareUtils.getString(getContext(),"token",null));
        user.enqueue(new Callback<UserIof>() {
            @Override
            public void onResponse(Call<UserIof> call, Response<UserIof> response) {
                Toast.makeText(getContext(), response.body().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), ShareUtils.getString(getContext(),"username","lxy")
                        +ShareUtils.getString(getContext(),"token",null), Toast.LENGTH_SHORT).show();
                et_username.setText(response.body().getUsername());
                et_sex.setText(response.body().getSex());
                et_age.setText(response.body().getAge());
                et_tel.setText(response.body().getPhone());
                et_eamil.setText(response.body().getMail());
               // et_desc.setText("12");
            }

            @Override
            public void onFailure(Call<UserIof> call, Throwable t) {

            }
        });
    }*/

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_exit_user:
                dialog_exit.show();
                break;
            case R.id.exit_yes:
                Intent intent=new Intent(v.getContext(),LoginActivity.class);
                dialog_exit.dismiss();
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.exit_no:
                dialog_exit.dismiss();
                break;
            case R.id.get_image:
                dialog.show();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_camera:
              //  getPermission();
                OpenCamera();
                dialog.dismiss();
                break;
            case R.id.btn_picture:
               // getPermission();
                toPicture();
                dialog.dismiss();
                break;
            case R.id.password_edit:
                Intent intent1=new Intent(getContext(), PasswordEditActivity.class);
                startActivity(intent1);
                break;
            case R.id.go_to_user:
                Intent gotouser=new Intent(getContext(), UserInfoActivity.class);
                startActivity(gotouser);
                break;
            case R.id.go_to_book:
                Intent intent2=new Intent(getContext(), BookManageActivity.class);
                startActivity(intent2);
                break;
            case R.id.scan_book:
                //Toast.makeText(getActivity(), "我来了", Toast.LENGTH_SHORT).show();
                // new IntentIntegrator(getActivity()).setCaptureActivity(ScanQRCodeActivity.class).initiateScan();
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
                // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
                //  integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setCaptureActivity(ScanQRCodeActivity.class);
                integrator.setPrompt("请将书籍条形码放入框内"); //底部的提示文字，设为""可以置空
                integrator.setCameraId(0); //前置或者后置摄像头
                //  integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
                break;
            case R.id.search_shuku_book:
                Intent intent3 =new Intent(getContext(), SearchBookActivity.class);
                startActivity(intent3);
                break;
        }
    }


    private static final int TAKE_PHOTO = 101;
    private static final int CHOOSE_PHOTO = 102;
    private static final int CROP_PHOTO =3;
    private Uri imageUri;

    private void toCamera() {
        File outputImage =new File(getActivity().getExternalCacheDir(),"output_image.jpg");
        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }
       if(Build.VERSION.SDK_INT>=24){
           imageUri=FileProvider.getUriForFile(getActivity(),"com.example.lxysss.ocrbi.fileprovider",outputImage);
       }
       else{
           imageUri=Uri.fromFile(outputImage);
       }
       Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
       intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
       startActivityForResult(intent,TAKE_PHOTO);
    }

    private void OpenCamera(){
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},2);
        }else{
            toCamera();
        }
    }

    private void toPicture() {
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{
            openAlbum();
        }
    }

    private void openAlbum() {
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }
                else{
                    Toast.makeText(getActivity(),"您失去了权限",Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    toCamera();
                }
                else{
                    Toast.makeText(getActivity(),"您失去了权限",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK) {
                  //  Toast.makeText(getActivity(),"我进来了",Toast.LENGTH_SHORT).show();
                  /*  try{
                        Bitmap bitmap=BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        profile_image.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }*/
                    //Toast.makeText(getActivity(),"imageUri"+imageUri.toString(),Toast.LENGTH_SHORT).show();
                    startCrop(imageUri);//照相完毕裁剪处理
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                   /* if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }
                    else{
                        handleImageBeforeKitKat(data);
                    }*/
                    startCrop(data.getData());
                }
                break;
            //裁剪后的效果
            case UCrop.REQUEST_CROP:
               // Toast.makeText(getActivity(),"我在外面",Toast.LENGTH_SHORT).show();
                if(resultCode == RESULT_OK){
                    Uri resultUri=UCrop.getOutput(data);
                //    Toast.makeText(getActivity(),"我进来了！"+resultUri.toString(),Toast.LENGTH_SHORT).show();
                    try {
                        Bitmap bitmap=BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(resultUri));
                        profile_image.setImageBitmap(bitmap);
                        UtilTools.putImageToShare(getActivity(),profile_image);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            //错误裁剪的结果
            case UCrop.RESULT_ERROR:
                if(resultCode==RESULT_OK){
                    final Throwable cropError = UCrop.getError(data);
                    handleCropError(cropError);
                }
                break;
            case 49374:
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null) {
                    if (result.getContents() == null) {
                        //Log.d(getClass().getName(), "Cancelled");
                        Toast.makeText(getContext(), "扫描结果为空", Toast.LENGTH_LONG).show();
                    } else {
                       // Log.d(getClass().getName(), "Scanned: " + result.getContents());
                        IsbnToBook(result.getContents());
                       // Toast.makeText(getContext(), result.getContents(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                break;
        }

    }

    private void IsbnToBook(String contents) {
     Call<IsbnBook> isbntobook=RestrofitTool.getmApi().IsbnToBook( ShareUtils.getString(getContext(), "token", null),contents);
     isbntobook.enqueue(new Callback<IsbnBook>() {
         @Override
         public void onResponse(Call<IsbnBook> call, Response<IsbnBook> response) {
             Intent intent=new Intent(getContext(),BookDetailActivity.class);
            // Toast.makeText(getContext(),response.body().toString(),Toast.LENGTH_LONG).show();
             Log.e("书籍扫描：",response.body().toString());
             intent.putExtra("book_title",response.body().getTitle());
             intent.putExtra("book_pic",response.body().getSimage());
             intent.putExtra("book_author",response.body().getAuthor());
             intent.putExtra("book_summary",response.body().getSummary());
             intent.putExtra("book_publisher",response.body().getPublisher());
             intent.putExtra("book_isbn",response.body().getIsbn());
             if(response.body().getTitle()==null){
                 Toast.makeText(getContext(),"没有获取到书籍信息呀！",Toast.LENGTH_LONG).show();
             }else {
                 startActivity(intent);
             }
         }
         @Override
         public void onFailure(Call<IsbnBook> call, Throwable t) {
            Toast.makeText(getContext(),"没有获取到书籍信息呀！",Toast.LENGTH_LONG).show();
         }
     });
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(getActivity(),uri)){
            String docId = getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selsction =MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selsction);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        displayImage(imagePath);
    }



    private void handleImageBeforeKitKat(Intent data) {
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }

    private void displayImage(String imagePath) {
        if(imagePath!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            profile_image.setImageBitmap(bitmap);
        }else{
            Toast.makeText(getActivity(),"获得图片失败！",Toast.LENGTH_LONG).show();
        }
    }

    private String getImagePath(Uri uri, String selsction) {
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selsction, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void startCrop(Uri uri){
        UCrop.Options options = new UCrop.Options();
        //裁剪后图片保存在文件夹中
        Uri destinationUri = Uri.fromFile(new File(getActivity().getExternalCacheDir(), "uCrop.jpg"));
        UCrop uCrop = UCrop.of(uri, destinationUri);//第一个参数是裁剪前的uri,第二个参数是裁剪后的uri
        uCrop.withAspectRatio(1,1);//设置裁剪框的宽高比例
        //下面参数分别是缩放,旋转,裁剪框的比例
        options.setAllowedGestures(UCropActivity.ALL,UCropActivity.NONE,UCropActivity.ALL);
        options.setToolbarTitle("移动和缩放");//设置标题栏文字
        options.setCropGridStrokeWidth(2);//设置裁剪网格线的宽度(我这网格设置不显示，所以没效果)
        options.setCropFrameStrokeWidth(10);//设置裁剪框的宽度
        options.setMaxScaleMultiplier(3);//设置最大缩放比例
        options.setHideBottomControls(true);//隐藏下边控制栏
        options.setShowCropGrid(false);  //设置是否显示裁剪网格
        options.setOvalDimmedLayer(true);//设置是否为圆形裁剪框
        options.setShowCropFrame(false); //设置是否显示裁剪边框(true为方形边框)
        options.setToolbarWidgetColor(Color.parseColor("#ffffff"));//标题字的颜色以及按钮颜色
        options.setDimmedLayerColor(Color.parseColor("#AA000000"));//设置裁剪外颜色
        options.setToolbarColor(Color.parseColor("#000000")); // 设置标题栏颜色
        options.setStatusBarColor(Color.parseColor("#000000"));//设置状态栏颜色
        options.setCropGridColor(Color.parseColor("#ffffff"));//设置裁剪网格的颜色
        options.setCropFrameColor(Color.parseColor("#ffffff"));//设置裁剪框的颜色
        uCrop.withOptions(options);
        uCrop.start(getContext(), this);
    }
    //处理剪切失败的返回值
    private void handleCropError(Throwable cropError) {
        deleteTempPhotoFile();
        if (cropError != null) {
            Toast.makeText(getActivity(), cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 删除拍照临时文件
     */
    private void deleteTempPhotoFile() {
        File tempFile = new File(Environment.getExternalStorageDirectory() + File.separator + "output_iamge.jpg");
        if (tempFile.exists() && tempFile.isFile()) {
            tempFile.delete();
        }
    }

}



