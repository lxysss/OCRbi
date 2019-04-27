package com.example.lxysss.ocrbi.activityTool;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Lxysss on 2018/10/8.
 */

    public class IGImageView extends SimpleDraweeView {

        public IGImageView(Context context, GenericDraweeHierarchy hierarchy) {
            super(context, hierarchy);
        }

        public IGImageView(Context context) {
            super(context);
        }

        public IGImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public IGImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public IGImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public void showImage(String url) {
            setImageURI(url);
        }
    }

