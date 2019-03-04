package com.snh.snhseller.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/19<p>
 * <p>changeTime：2019/2/19<p>
 * <p>version：1<p>
 */
public class JumpUtils {
    public static void simpJump(Activity fromActivity, Class toActivity, boolean isCloseActivity){
        Intent intent = new Intent(fromActivity,toActivity);
        fromActivity.startActivity(intent);
        if(isCloseActivity){
            fromActivity.finish();
        }
    }
    public static void dataJump(Activity fromActivity, Class toActivity, Bundle bundle, boolean isCloseActivity){
        Intent intent = new Intent(fromActivity,toActivity);
        intent.putExtras(bundle);
        fromActivity.startActivity(intent);
        if(isCloseActivity){
            fromActivity.finish();
        }
    }
}
