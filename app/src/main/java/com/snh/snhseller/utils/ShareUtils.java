package com.snh.snhseller.utils;

import android.graphics.Bitmap;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/25<p>
 * <p>changeTime：2019/2/25<p>
 * <p>version：1<p>
 */
public class ShareUtils {

    public static void ShareWechat(String title, String url, String name, String imgUrl) {
        Platform.ShareParams sp = new Platform.ShareParams();

        if(!StrUtils.isEmpty(imgUrl)){
            sp.setImageUrl(imgUrl);
        }
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setText(name);
        sp.setUrl(url);
        sp.setTitle(title);
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.share(sp);
    }
    public static void ShareWechatMom(String title, String url, String name, String imgUrl) {
        Platform.ShareParams sp = new Platform.ShareParams();
        if (!StrUtils.isEmpty(imgUrl)) {
            sp.setImageUrl(imgUrl);
        }
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setText(name);
        sp.setUrl(url);
        sp.setTitle(title);
        Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
            }

            @Override
            public void onCancel(Platform platform, int i) {
            }
        });
        wechat.share(sp);
    }


    public static void ShareWechat(String title, String name, Bitmap bitmap) {
        Platform.ShareParams sp = new Platform.ShareParams();

        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setText(name);
        sp.setImageData(bitmap);
        sp.setTitle(title);
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.share(sp);
    }
    public static void ShareWechatMom(String title, String name, Bitmap bitmap) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setText(name);
        sp.setImageData(bitmap);
        sp.setTitle(title);
        Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
            }

            @Override
            public void onCancel(Platform platform, int i) {
            }
        });
        wechat.share(sp);
    }
}
