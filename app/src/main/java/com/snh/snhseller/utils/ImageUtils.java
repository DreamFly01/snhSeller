package com.snh.snhseller.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.snh.snhseller.R;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/20<p>
 * <p>changeTime：2019/2/20<p>
 * <p>version：1<p>
 */
public class ImageUtils {
    /**
     * 一般图片加载
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadUrlImage(Context context, String url, ImageView imageView) {
        StringBuffer sbf = new StringBuffer(url);
        if (sbf.toString().contains("Http://")) {
            String url1 = sbf.replace(0, 1, "h").toString();
            url = url1;
        }
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.img_load_error)
                .placeholder(R.drawable.img_load_error);
        com.snh.library_base.utils.GlideApp.with(context).load(url).apply(options).into(imageView);
    }

    public static void loadUrlImage1(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.img_load_error)
                .placeholder(R.drawable.img_load_error)
                .override(187, 300);
        com.snh.library_base.utils.GlideApp.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * 根据图片宽高加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void WHloadUrlImage(Context context, String url, ImageView imageView) {
        int[] wh = getImageWidthHeight(url);
        if (wh[0] < wh[1]) {

            RequestOptions options = new RequestOptions()
                    .transform(new RotateTransformation(90f));
            Glide.with(context).load(url)
                    .apply(options)
                    .into(imageView);
        } else {
            Glide.with(context).load(url)
                    .into(imageView);
        }
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadUrlCircleImage(Context context, String url, ImageView imageView) {
        StringBuffer sbf = new StringBuffer(url);
        if (sbf.toString().contains("Http://")) {
            String url1 = sbf.replace(0, 1, "h").toString();
            url = url1;
        }
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .error(R.drawable.img_load_error)
                .placeholder(R.drawable.img_load_error);
        com.snh.library_base.utils.GlideApp.with(context).load(url).apply(RequestOptions.bitmapTransform(new CircleCrop())).apply(options).into(imageView);
    }

    /**
     * 加载失败时候显示预览图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadUrlHolderImage(Context context, String url, ImageView imageView) {
//        Glide.with(context).load(url).apply(RequestOptions.placeholderOf())

    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadUrlCorners(Context context, String url, ImageView imageView) {
        StringBuffer sbf = new StringBuffer(url);
        if (sbf.toString().contains("Http://")) {
            String url1 = sbf.replace(0, 1, "h").toString();
            url = url1;
        }
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(20);
        //通过RequestOptions扩展功能
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300).error(R.drawable.img_load_error)
                .placeholder(R.drawable.img_load_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        com.snh.library_base.utils.GlideApp.with(context).load(url).apply(options).into(imageView);


    }

    /**
     * 获取图片的宽高
     */

    public static int[] getImageWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth, options.outHeight};
    }

}
