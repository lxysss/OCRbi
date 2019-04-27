package com.example.lxysss.ocrbi.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activityTool.ActivityManager;
import com.example.lxysss.ocrbi.activityTool.IGImageView;
import com.example.lxysss.ocrbi.activityTool.LoadGifUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class WordinfActivity extends AppCompatActivity {
   private TextView word_inf_word,word_inf_pinyin,word_inf_bihua,word_inf_yisi1,word_inf_yisi2,word_inf_yisi3,word_inf_bushou;
   private ImageView word_inf_yuyin;
   private Button btn_toolbar_delete_back;
   private String YU_YIN_KEY="5cbe704d";
   private GifImageView word_inf_gif;
    //TTS
    private SpeechSynthesizer mTts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordinf);
        ActivityManager.getInstance().addActivity(this);  //加入活动容器
        initview();
    }

    private void initview() {
        word_inf_word=findViewById(R.id.word_inf_word);
        word_inf_word.setText(getIntent().getStringExtra("word"));
        word_inf_pinyin=findViewById(R.id.word_inf_pinyin);
        word_inf_pinyin.setText(getIntent().getStringExtra("pinyin"));
        word_inf_bihua=findViewById(R.id.word_inf_bihua);
        word_inf_bihua.setText(getIntent().getStringExtra("bihua"));
        word_inf_yisi1=findViewById(R.id.word_inf_yisi1);
        word_inf_yisi1.setText(getIntent().getStringExtra("yisi1"));
        word_inf_yisi2=findViewById(R.id.word_inf_yisi2);
        word_inf_yisi2.setText(getIntent().getStringExtra("yisi2"));
        word_inf_yisi3=findViewById(R.id.word_inf_yisi3);
        word_inf_yisi3.setText(getIntent().getStringExtra("yisi3"));
        word_inf_bushou=findViewById(R.id.word_inf_bushou);
        word_inf_bushou.setText(getIntent().getStringExtra("bushou"));
        word_inf_gif=findViewById(R.id.word_inf_gif);

        if(getIntent().getStringExtra("gif").equals("")){
            word_inf_gif.setBackgroundResource(R.drawable.default_word);
        }else {
            LoadGifUtils loadGifUtils = new LoadGifUtils();
            loadGifUtils.setListener(new LoadGifUtils.onCompltedListener() {
                @Override
                public void onComplted(byte[] bt) {
                    try {
                        GifDrawable drawable = new GifDrawable(bt);
                        word_inf_gif.setBackgroundDrawable(drawable);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            loadGifUtils.loadGif(getIntent().getStringExtra("gif"));
        }
        /**
         * TTS
         */
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(this, null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        //mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");

        word_inf_yuyin=findViewById(R.id.word_inf_yuyin);
        word_inf_yuyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeak("汉字"+getIntent().getStringExtra("word"));
            }
        });

        btn_toolbar_delete_back=findViewById(R.id.btn_toolbar_delete_back);
        btn_toolbar_delete_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(WordinfActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
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
    public void displayImage(Context context, String path, IGImageView imageView) {
        imageView.showImage(path);
    }
}
