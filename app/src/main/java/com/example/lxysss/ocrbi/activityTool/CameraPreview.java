package com.example.lxysss.ocrbi.activityTool;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxysss.ocrbi.Entity.Bean;
import com.example.lxysss.ocrbi.Entity.Bean2;
import com.example.lxysss.ocrbi.Entity.Img;
import com.example.lxysss.ocrbi.Entity.Word;
import com.example.lxysss.ocrbi.Entity.WordTest;
import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.internetapi.RestrofitTool;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lxysss on 2019/4/19.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback , Camera.PreviewCallback {
    private static final String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int Camerawidth,Cameraheight;
    private boolean flag=true,isPic=false,isPoint=false;
    private long startTime,endTime;
    private Context mcontext;
    private  Bitmap bitmap;
    //
    private RenderScript rs;
    private ScriptIntrinsicYuvToRGB yuvToRgbIntrinsic;
    private Type.Builder yuvType, rgbaType;
    private Allocation in, out;
    //
    private CustomDialog dialog;
    private TextView dialog_word,dialog_pinyin,dialog_yisi1,dialog_yisi2,dialog_yisi3;
    //TTS
    private SpeechSynthesizer mTts;
    public CameraPreview(Context context) {
        super(context);
        mcontext=context;
        //初始化RenderScript
        rs = RenderScript.create(context);
        yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
        startTime = System.currentTimeMillis(); //起始时间
        //初始化dialog
        dialog = new CustomDialog(context, 0, 0,
                R.layout.dialog_word, R.style.MyDialog, Gravity.CENTER, 0);
        dialog_word=dialog.findViewById(R.id.dialog_word);
        dialog_pinyin=dialog.findViewById(R.id.dialog_pinyin);
        dialog_yisi1=dialog.findViewById(R.id.dialog_yisi1);
        dialog_yisi2=dialog.findViewById(R.id.dialog_yisi2);
        dialog_yisi3=dialog.findViewById(R.id.dialog_yisi3);
        //初始化TTS
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(context, null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端


        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public Camera getCameraInstance() {
        if (mCamera == null) {
            CameraHandlerThread mThread = new CameraHandlerThread("camera thread");
            synchronized (mThread) {
                mThread.openCamera();
            }
        }
        return mCamera;
    }

    public void surfaceCreated(SurfaceHolder holder) {

        mCamera = getCameraInstance();
        //
        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        Camerawidth=previewSize.width;
        Cameraheight=previewSize.height;
        //
        rs = RenderScript.create(getContext());
        yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (dialog!=null && dialog.isShowing()) {
            dialog.dismiss();
        }
        mHolder.removeCallback(this);
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        mCamera = getCameraInstance();
        mCamera.setPreviewCallback(this);
        try {
            int rotation = getDisplayOrientation();
            mCamera.setDisplayOrientation(rotation);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            adjustDisplayRatio(rotation);
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public int getDisplayOrientation() {
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        android.hardware.Camera.CameraInfo camInfo =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, camInfo);

        int result = (camInfo.orientation - degrees + 360) % 360;
        return result;
    }

    private void adjustDisplayRatio(int rotation) {
        ViewGroup parent = ((ViewGroup) getParent());
        Rect rect = new Rect();
        parent.getLocalVisibleRect(rect);
        int width = rect.width();
        int height = rect.height();
        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        int previewWidth;
        int previewHeight;
        if (rotation == 90 || rotation == 270) {
            previewWidth = previewSize.height;
            previewHeight = previewSize.width;
        } else {
            previewWidth = previewSize.width;
            previewHeight = previewSize.height;
        }

        if (width * previewHeight > height * previewWidth) {
            final int scaledChildWidth = previewWidth * height / previewHeight;

            layout((width - scaledChildWidth) / 2, 0,
                    (width + scaledChildWidth) / 2, height);
        } else {
            final int scaledChildHeight = previewHeight * width / previewWidth;
            layout(0, (height - scaledChildHeight) / 2,
                    width, (height + scaledChildHeight) / 2);
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if(flag) {
            flag=false;
       /* try {
            String res = new String(data,"UTF-8");
            Log.e("data:",res);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
            //  Toast.makeText(mcontext,String.valueOf(data),Toast.LENGTH_LONG).show();
            endTime = System.currentTimeMillis(); //结束时间
            if (!isPic) {
                if (endTime - startTime > 2000) {
                    bitmap = SpeedDataToBmp(data);
                    bmptowen(rotaingImageView(90, bitmap));

                    isPic = true;
                }
            }
            if (isPic && !isPoint) {
                Log.i(TAG, "processing frame");
                bitmap = SpeedDataToBmp(data);
                getBmpRed(bitmap);
            }
            flag=true;
        }
    }

    private class CameraHandlerThread extends HandlerThread {
        Handler mHandler;

        public CameraHandlerThread(String name) {
            super(name);
            start();
            mHandler = new Handler(getLooper());
        }

        synchronized void notifyCameraOpened() {
            notify();
        }

        void openCamera() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    openCameraOriginal();
                    notifyCameraOpened();
                }
            });
            try {
                wait();
            } catch (InterruptedException e) {
                Log.w(TAG, "wait was interrupted");
            }
        }
    }

    private void openCameraOriginal() {
        try {
            mCamera = Camera.open();
           // mCamera = Camera.open(1);  调用前置摄像头
        } catch (Exception e) {
            Log.d(TAG, "camera is not available");
        }
    }


   /* public Bitmap onDataToBmp(byte[] data){

        ByteArrayOutputStream baos;
        byte[] rawImage;
        Bitmap bitmap;
      //  mCamera.setOneShotPreviewCallback(null);
        //处理data
        //Camera.Size previewSize = mCamera.getParameters().getPreviewSize();//获取尺寸,格式转换的时候要用到
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        YuvImage yuvimage = new YuvImage(
                data,
                ImageFormat.NV21,
                Camerawidth,
                Cameraheight,
                null);
        baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, Camerawidth, Cameraheight), 100, baos);// 80--JPG图片的质量[0-100],100最高
        rawImage = baos.toByteArray();
        //将rawImage转换成bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options);
        return bitmap;
    }*/

    public Bitmap SpeedDataToBmp(byte[] data){
        if (yuvType == null)
        {
            yuvType = new Type.Builder(rs, Element.U8(rs)).setX(data.length);
            in = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);

            rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs)).setX(Camerawidth).setY(Cameraheight);
            out = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT);
        }

        in.copyFrom(data);

        yuvToRgbIntrinsic.setInput(in);
        yuvToRgbIntrinsic.forEach(out);

       // Bitmap bmpout = Bitmap.createBitmap(Camerawidth, Cameraheight, Bitmap.Config.ARGB_8888);
        Bitmap bmpout = Bitmap.createBitmap(Camerawidth, Cameraheight, Bitmap.Config.ARGB_8888);
        out.copyTo(bmpout);
        return bmpout;
    }

     public void getBmpRed(Bitmap bitmap1){
        int i,j,r,g,b,flag=0,color;
        Bitmap bitmap;
        bitmap= rotaingImageView(90,bitmap1);
         Log.i(TAG," "+bitmap.getWidth()+" "+bitmap.getHeight());
        for( i=0;i<bitmap.getWidth();i++){
            for( j=0;j<bitmap.getHeight();j++){
                 color = bitmap.getPixel(i,j);
               //   Log.i(TAG,color+" ");
                 r = Color.red(color);
                 g = Color.green(color);
                 b = Color.blue(color);
               //  Log.i("RGB",r+" "+g+" "+b);
                if((140<=r && r<=255) && (0<=g && g<=100) && (0<=b && b<=100)){
                   // Toast.makeText(getContext(),"找到红色了！",Toast.LENGTH_SHORT).show();
                  //  bmptowen(bitmap);
                    Log.i(TAG,"找到红色了"+r+"　"+g+"　"+b);
                    //(bitmap.getWidth()-j,i)
                    Log.i("位置：",i+"　，"+j);
                    Call<Word> wordCall= RestrofitTool.getmApi().getwordMsgPhoto(i,j,
                            ShareUtils.getString(getContext(),"username","lxy"));
                    wordCall.enqueue(new Callback<Word>() {
                        @Override
                        public void onResponse(Call<Word> call, Response<Word> response) {
                            if(response.body().getCode()==1001) {
                                dialog.show();
                                dialog_word.setText(response.body().getWord());
                                dialog_pinyin.setText(response.body().getPinyin());
                                dialog_yisi1.setText(response.body().getYisi1());
                                dialog_yisi2.setText(response.body().getYisi2());
                                dialog_yisi3.setText(response.body().getYisi3());
                                startSpeak("汉字" + response.body().getWord());
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        dialog.dismiss();
                                    }
                                }, 3000);
                            }else {
                                Toast.makeText(mcontext,"没有找到汉字！",Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Word> call, Throwable t) {

                        }
                    });
                    flag=1;
                    break;
                }
            }
            if(flag==1){
                break;
            }
        }
    }

    public void bmptowen (Bitmap bitmap){
        /**
         * 保存本地
         */
        /** 保存方法 */

        //   Log.e(TAG, "保存图片");
        /** 保存方法 */

        String filename = String.valueOf("images.png");
        File f = new File("/sdcard/1/", filename);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i(TAG, "已经保存"+filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), f);
        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("images", f.getName(), requestFile);
        Call<Img> putimg=RestrofitTool.getmApi().putimg(body);
        putimg.enqueue(new Callback<Img>() {
            @Override
            public void onResponse(Call<Img> call, Response<Img> response) {
                if(response.body().getMsg().equals("success")){
                    Toast.makeText(mcontext,"上传文件成功！",Toast.LENGTH_LONG).show();
                    //Toast.makeText(mcontext,response.body().getLen(),Toast.LENGTH_LONG).show();
                    //Toast.makeText(mcontext,response.body().getPrint(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Img> call, Throwable t) {
                Toast.makeText(mcontext,t.toString(),Toast.LENGTH_LONG).show();
            }
        });

    }
    //旋转摄像头
    public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //前置摄像头270度，后置摄像头90度
        //旋转图片 动作
        Matrix matrix = new Matrix();;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    //开始说话
    private void startSpeak(String text) {
        //3.开始合成
        mTts.startSpeaking(text, mSynListener);
    }

    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };

}