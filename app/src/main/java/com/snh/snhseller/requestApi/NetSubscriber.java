package com.snh.snhseller.requestApi;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.snh.snhseller.MyApplication;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.ui.msg.SupplyNoticeActivity;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.wediget.CustomProgress;
import com.snh.snhseller.wediget.LoadingDialog;

import rx.Subscriber;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/19<p>
 * <p>changeTime：2019/2/19<p>
 * <p>version：1<p>
 */
public abstract class NetSubscriber<T> extends Subscriber<T> {
    private DialogUtils dialogUtils;
    private Context context;
    /***
     * 菊花转,默认情况下是关闭状态
     */
    private CustomProgress mCustomProgress;
    private LoadingDialog loadingDialog;

    /**
     * 是否显示菊花转
     */
    private boolean mIsShowLoading = false;

    public NetSubscriber(Context context) {
        if (null != context) {
            this.context = context;
        } else {
//            this.context = MyApplication.getInstance().getApplicationContext();
        }
    }

    public NetSubscriber(Context context, boolean isShowLoading) {
        this.context = context;
        this.mIsShowLoading = isShowLoading;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (context != null && mIsShowLoading) {
//            mCustomProgress = CustomProgress.show((Activity) context, null);
            loadingDialog = LoadingDialog.getInstance(context);
            loadingDialog.show();
        }
    }


    @Override
    public void onError(Throwable e) {


        onResultErro(new APIException(e.getMessage()));
    }

    @Override
    public void onNext(T t) {
        dialogUtils = new DialogUtils(context);
        dismissLoadingView();
        if (t instanceof BaseResultBean) {
            BaseResultBean bean = (BaseResultBean) t;
            if (null != bean.code) {
                if (bean.code.equals("01") | bean.code.equals("1")) {
                    onResultNext(t);
                } else {
                    onResultErro(new APIException(bean.msg, bean.code));
                }
            } else {
                onResultNext(t);
            }

        } else {
            onError(new Throwable());
        }

    }

    public abstract void onResultNext(T model);

    public void onResultErro(final APIException erro) {
        if (null != context) {

            dialogUtils = new DialogUtils(context);
            if (StrUtils.isEmpty(erro.msg)) {
                erro.msg = "网络连接失败,请检查网络";
            }
//        Log.d(HttpLogger.LOGKYE, "erro: " + erro.msg);

            if ("100".equals(erro.code)) {
                dialogUtils.simpleDialog(erro.msg, new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {

                        dialogUtils.dismissDialog();
                        JumpUtils.simpJump((Activity) context, SupplyNoticeActivity.class, false);
                    }
                }, true);

            } else {
                dialogUtils.simpleDialog(erro.msg, new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {

                        dialogUtils.dismissDialog();
                    }
                }, true);
            }

        }
    }

    public void dismissLoadingView() {
//        if (mIsShowLoading && null != mCustomProgress) {
//            mCustomProgress.dismiss();
//        }
        if (mIsShowLoading && null != loadingDialog) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onCompleted() {
        dismissLoadingView();
    }

}
