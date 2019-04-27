package com.example.lxysss.ocrbi.activity;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.lxysss.ocrbi.R;
import com.example.lxysss.ocrbi.activityTool.ActivityManager;
import com.example.lxysss.ocrbi.activityTool.CameraPreview;
import com.example.lxysss.ocrbi.activityTool.SettingsFragment;

public class RecogintionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recogintion);
        ActivityManager.getInstance().addActivity(this);  //加入活动容器
        view();
    }

    private void view() {
    /*    Button buttonSettings = (Button) findViewById(R.id.button_settings);
        //buttonSettings.setEnabled(false);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.camera_preview,
                        new SettingsFragment()).addToBackStack(null).commit();
            }
        });*/
        Button buttonStartPreview = (Button) findViewById(R.id.button_start_preview);
        buttonStartPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPreview();
            }
        });

        Button buttonStopPreview = (Button) findViewById(R.id.button_stop_preview);
        buttonStopPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPreview();
            }
        });

        Button btn_toolbar_delete_back=findViewById(R.id.btn_toolbar_delete_back);
        btn_toolbar_delete_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RecogintionActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    public void startPreview() {
        CameraPreview mPreview = new CameraPreview(this);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        PreferenceManager.setDefaultValues(this, R.xml.preference, false);
        SettingsFragment.passCamera(mPreview.getCameraInstance());
        SettingsFragment.setDefault(PreferenceManager.getDefaultSharedPreferences(this));
        SettingsFragment.init(PreferenceManager.getDefaultSharedPreferences(this));
    }

    public void stopPreview() {
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.removeAllViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.removeAllViews();
    }
}
