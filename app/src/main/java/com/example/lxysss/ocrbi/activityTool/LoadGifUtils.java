package com.example.lxysss.ocrbi.activityTool;

import android.os.Handler;
import android.os.Message;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * Created by Lxysss on 2019/4/24.
 */

public class LoadGifUtils {
    private onCompltedListener listener;

    public void loadGif(String url) {
        MyRunnable myRunnable = new MyRunnable(url);
        new Thread(myRunnable).start();
    }

    class MyRunnable implements Runnable {
        String url;
        MyRunnable(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            byte[] bt=new byte[1024];
            try {
                HttpClient client = new HttpClient();
                GetMethod get = new GetMethod(url);
                client.executeMethod(get);
                bt = get.getResponseBody();

                sendMsg(1,bt);
            } catch (Throwable ex) {
                System.out.println(ex.toString());
            }
        }
    }
    private void sendMsg(int what, Object mess) {
        Message m = handler.obtainMessage();
        m.what = what;
        m.obj = mess;
        m.sendToTarget();
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: // 开始下载
                    byte[] bt = (byte[]) msg.obj;
                    if(listener!=null) {
                        listener.onComplted(bt);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

   public interface onCompltedListener {
        void onComplted(byte[] bt);
    }

  public   void setListener(onCompltedListener listener){
        this.listener=listener;
    }
}