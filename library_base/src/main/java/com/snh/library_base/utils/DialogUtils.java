package com.snh.library_base.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.snh.library_base.R;


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
    private EditClickLisener editClickLisener;
    public static DialogUtils instance;


    public interface ConfirmClickLisener {
        void onConfirmClick(View v);
    }


    public interface ChoseClickLisener {
        void onConfirmClick(View v);

        void onCancelClick(View v);
    }

    public interface EditClickLisener {
        void onCancelClick(View v);

        void onConfirmClick(View v, String content);
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
            synchronized (DialogUtils.class) {
                if (instance == null) {
                    instance = new DialogUtils(context);
                }
            }
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
