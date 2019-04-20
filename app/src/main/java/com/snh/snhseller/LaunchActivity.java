package com.snh.snhseller;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.snh.snhseller.utils.JumpUtils;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/4/9<p>
 * <p>changeTime：2019/4/9<p>
 * <p>version：1<p>
 */
public class LaunchActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("LaunchActivity--onCreate："+System.currentTimeMillis());
        JumpUtils.simpJump(this,WelcomActivity.class,true);
    }
}
