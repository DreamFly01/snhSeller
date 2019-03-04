package com.snh.snhseller.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.snh.snhseller.R;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/19<p>
 * <p>changeTime：2019/2/19<p>
 * <p>version：1<p>
 */
public class DialogUtils {
    private Activity mActivity;
    private Context mContext;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private View v;
    private Dialog dialog;
    private boolean flag;
    private ConfirmClickLisener confirmClickLisener;
    private ChoseClickLisener choseClickLisener;
    private HeadImgChoseLisener headImgChoseLisener;
    private OnItemClick onItemClick;

    public static DialogUtils instance;


    public interface ConfirmClickLisener {
        void onConfirmClick(View v);
    }

    public interface ChoseClickLisener {
        void onConfirmClick(View v);

        void onCancelClick(View v);
    }

    public interface HeadImgChoseLisener {
        void onCancelClick(View v);

        void onPhotoClick(View v);

        void onAlumClick(View v);
    }

    public interface OnItemClick {
        void onItemClick(View v, int position);
    }

    public static DialogUtils getInstance(Context context) {
        if (instance == null) {
            instance = new DialogUtils(context);
        }
        return instance;
    }

    public DialogUtils(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
    }

    public DialogUtils(Context context) {
        this.mContext = context;
        this.mActivity = (Activity) context;
    }


    public DialogUtils noBtnDialog(String content) {

        builder = new AlertDialog.Builder(mContext);
        inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.dialog_simple_layout, null);
        Button comfirm = (Button) v.findViewById(R.id.btn_comfirm);


        TextView txcontent = (TextView) v.findViewById(R.id.iv_dialog_content);
        txcontent.setText(content);

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(flag);
        dialog.setCancelable(flag);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) (display.getWidth() * 0.8);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setContentView(v);

        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        return this;
    }

    /**
     * 简单弹窗
     *
     * @param content
     * @param flag
     */
    public DialogUtils simpleDialog(String content, final ConfirmClickLisener confirmClickLisener, boolean flag) {
        this.confirmClickLisener = confirmClickLisener;
        builder = new AlertDialog.Builder(mContext);
        inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.dialog_simple_layout, null);
        Button comfirm = (Button) v.findViewById(R.id.btn_comfirm);


        TextView txcontent = (TextView) v.findViewById(R.id.iv_dialog_content);
        txcontent.setText(content);

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(flag);
        dialog.setCancelable(flag);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) (display.getWidth() * 0.8);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setContentView(v);
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmClickLisener.onConfirmClick(view);
            }
        });


        return this;
    }

    /**
     * 有确定取消按钮弹窗
     *
     * @param content
     * @param choseClickLisener
     * @param flag
     * @return
     */
    public DialogUtils twoBtnDialog(String content, final ChoseClickLisener choseClickLisener, boolean flag) {
        this.choseClickLisener = choseClickLisener;
        builder = new AlertDialog.Builder(mContext);
        inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.dialog_two_btn_layout, null);
        Button comfirm = (Button) v.findViewById(R.id.btn_comfirm);
        Button cancle = (Button) v.findViewById(R.id.btn_cancel);


        TextView txcontent = (TextView) v.findViewById(R.id.iv_dialog_content);
        txcontent.setText(content);


        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(flag);
        dialog.setCancelable(flag);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) (display.getWidth() * 0.8);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setContentView(v);
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choseClickLisener.onConfirmClick(view);
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choseClickLisener.onCancelClick(view);
            }
        });

        return this;
    }

    /**
     * 头像选择弹窗
     *
     * @param
     * @param headImgChoseLisener
     * @param flag
     */

    public DialogUtils headImgDialog(final HeadImgChoseLisener headImgChoseLisener, boolean flag) {
        this.headImgChoseLisener = headImgChoseLisener;
        //自定义样式使得弹窗铺满全屏
        builder = new AlertDialog.Builder(mContext, R.style.Dialog_FS);
        inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.dialog_headimg_layout, null);
        TextView cancel = (TextView) v.findViewById(R.id.btn_cancel);
        TextView photo = (TextView) v.findViewById(R.id.btn_photograph);
        TextView album = (TextView) v.findViewById(R.id.btn_album);


        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(flag);
        dialog.setCancelable(flag);
        dialog.show();

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) display.getWidth();
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setContentView(v);
        dialog.getWindow().setWindowAnimations(R.style.MyDialogAlpha);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headImgChoseLisener.onPhotoClick(view);
            }
        });
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headImgChoseLisener.onAlumClick(view);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headImgChoseLisener.onCancelClick(view);
            }
        });
        return this;
    }

    /**
     * 分享弹窗
     *
     * @return
     */
    public DialogUtils ShareDialog(final String title, final String name, final Bitmap bitmap) {
        this.confirmClickLisener = confirmClickLisener;
        //自定义样式使得弹窗铺满全屏
        builder = new AlertDialog.Builder(mContext, R.style.Dialog_FS);
        inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.dialog_buttom_layout, null);
        TextView cancel = (TextView) v.findViewById(R.id.btn_cancel);
        ImageView friend = (ImageView) v.findViewById(R.id.share_friend);
        ImageView commends = (ImageView) v.findViewById(R.id.share_commends);

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(flag);
        dialog.setCancelable(flag);
        dialog.show();

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) display.getWidth();
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setContentView(v);

        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.ShareWechat(title,  name, bitmap);
                dialog.dismiss();
            }
        });

        commends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.ShareWechatMom(title,  name, bitmap);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return this;
    }


    /**
     * 弹窗消失
     *
     * @return
     */
    public DialogUtils dismissDialog() {
        if (null != dialog) {

            dialog.dismiss();
        }

        return this;
    }

    public DialogUtils outSideIsDismiss(boolean flag) {
        dialog.setCanceledOnTouchOutside(flag);
        dialog.setCancelable(flag);
        return this;
    }
}
