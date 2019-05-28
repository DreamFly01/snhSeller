package com.snh.snhseller.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.snh.snhseller.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/4/20<p>
 * <p>changeTime：2019/4/20<p>
 * <p>version：1<p>
 */
public class WaterImgUtils {

    /**
     * 设置水印图片到中间
     * @return
     */
    public static Bitmap createWaterMaskCenter(String file, Context context) {
        Bitmap src= BitmapFactory.decodeFile(file);
        Resources res = context.getResources();
        Bitmap watermark = BitmapFactory.decodeResource(res, R.drawable.waterpic_bg);

        return createWaterMaskBitmap(src, watermark,
                (src.getWidth() - watermark.getWidth()) / 2,
                (src.getHeight() - watermark.getHeight()) / 2);
    }

    private static Bitmap createWaterMaskBitmap(Bitmap src, Bitmap watermark,
                                                int paddingLeft, int paddingTop) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        try {

            Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图

            //将该图片作为画布
            Canvas canvas = new Canvas(newb);
            //在画布 0，0坐标上开始绘制原始图片
            canvas.drawBitmap(src, 0, 0, null);
            //在画布上绘制水印图片
            canvas.drawBitmap(watermark, paddingLeft, paddingTop, null);
            // 保存
            canvas.save(Canvas.ALL_SAVE_FLAG);
            // 存储
            canvas.restore();
            return newb;

        }catch (Exception e)
        {
        //创建失败 返回原图
            return src;
        }
    }

    public static void saveBitmap(Bitmap bitmap,String path) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = path;
        } else {
            return;
        }
        try {
            filePic = new File(savePath + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }
    private static final String SD_PATH = "/sdcard/dskqxt/pic/";
    private static final String IN_PATH = "/dskqxt/pic/";

    /**
     * 随机生产文件名
     *
     * @return
     */
    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }

    public static String saveBitmap(Context context, Bitmap mBitmap,String path) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        try {
            filePic = new File(savePath + generateFileName() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return path;
        }

        return filePic.getAbsolutePath();
    }

}
