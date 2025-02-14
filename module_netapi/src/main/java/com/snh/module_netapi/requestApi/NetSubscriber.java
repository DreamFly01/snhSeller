package com.snh.module_netapi.requestApi;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.snh.library_base.utils.DialogUtils;

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
            if (TextUtils.isEmpty(erro.msg)) {
                erro.msg = "网络连接失败,请检查网络";
            }

            if(!TextUtils.isEmpty(erro.msg)&&erro.msg.contains("未将对象引用设置到对象的实例")){
                erro.msg = "操作失败，请稍后再试";
            }
//        Log.d(HttpLogger.LOGKYE, "erro: " + erro.msg);

//            if ("100".equals(erro.code)) {
//                dialogUtils.simpleDialog(erro.msg, new DialogUtils.ConfirmClickLisener() {
//                    @Override
//                    public void onConfirmClick(View v) {
//
//                        dialogUtils.dismissDialog();
//                        JumpUtils.simpJump((Activity) context, SupplyNoticeActivity.class, false);
//                    }
//                }, true);
//
//            } else {
                dialogUtils.simpleDialog(erro.msg, new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {

                        dialogUtils.dismissDialog();
                    }
                }, true);
            }

//        }
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
