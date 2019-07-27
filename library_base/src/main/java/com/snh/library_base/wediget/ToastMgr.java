package com.snh.library_base.wediget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.snh.library_base.R;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/6/14<p>
 * <p>changeTime：2019/6/14<p>
 * <p>version：1<p>
 */
public enum ToastMgr{
    builder;
    private View view;
    private TextView tv;
    private Toast toast;

    /**
     * 初始化Toast
     * @param context
     */
    public void init(Context context){
        view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        tv = (TextView) view.findViewById(R.id.tv_toast_msg);
        toast = new Toast(context);
        toast.setView(view);
    }
    /**
     * 显示Toast
     * @param content
     * @param duration Toast持续时间
     */
    public void display(CharSequence content , int duration){
        if (content.length()!=0) {
            tv.setText(content);
            toast.setDuration(duration);
            //动态设置toast显示的位置
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
