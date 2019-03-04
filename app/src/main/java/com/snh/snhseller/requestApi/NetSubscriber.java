package com.snh.snhseller.requestApi;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.wediget.CustomProgress;

import rx.Subscriber;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/19<p>
 * <p>changeTime：2019/2/19<p>
 * <p>version：1<p>
 */
public abstract class NetSubscriber <T> extends Subscriber<T> {
    private DialogUtils dialogUtils;
    private Context context;
    /***
     * 菊花转,默认情况下是关闭状态
     */
    private CustomProgress mCustomProgress;

    /**
     * 是否显示菊花转
     */
    private boolean mIsShowLoading = false;

    public NetSubscriber(Context context) {
        this.context = context;
    }

    public NetSubscriber(Context context, boolean isShowLoading) {
        this.context = context;
        this.mIsShowLoading = isShowLoading;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (context != null && mIsShowLoading) {
            mCustomProgress = CustomProgress.show((Activity) context, null);
        }
    }


    @Override
    public void onError(Throwable e) {
        onResultErro(new APIException(e.getMessage()));
    }

    @Override
    public void onNext(T t) {
        dialogUtils = new DialogUtils(context, (Activity) context);
        if (t instanceof BaseResultBean) {
            BaseResultBean bean = (BaseResultBean) t;
            if(null!=bean.code){
                if (bean.code.equals("01")|bean.code.equals("1")) {
                    onResultNext(t);
                } else {
                    onResultErro(new APIException(bean.msg));
                }
            }else {
                onResultNext(t);
            }

        } else {
            onError(new Throwable());
        }

    }

    public abstract void onResultNext(T model);

    public void onResultErro(final APIException erro) {
        dialogUtils = new DialogUtils(context, (Activity) context);
        if (StrUtils.isEmpty(erro.msg)) {
            erro.msg = "连接服务器失败";
        }
//        Log.d(HttpLogger.LOGKYE, "erro: " + erro.msg);
        dialogUtils.simpleDialog(erro.msg, new DialogUtils.ConfirmClickLisener() {
            @Override
            public void onConfirmClick(View v) {

                dialogUtils.dismissDialog();
                dismissLoadingView();
            }
        }, true);
    }

    public void dismissLoadingView() {
        if (mIsShowLoading && null != mCustomProgress) {
            mCustomProgress.dismiss();
        }
    }

    @Override
    public void onCompleted() {
        dismissLoadingView();
    }

}
