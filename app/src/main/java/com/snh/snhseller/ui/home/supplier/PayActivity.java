package com.snh.snhseller.ui.home.supplier;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.PayWxBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.order.OrderFragment;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.FinishActivityManager;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.StrUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：支付页面<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/8<p>
 * <p>changeTime：2019/3/8<p>
 * <p>version：1<p>
 */
public class PayActivity extends BaseActivity {

    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_integerl)
    TextView tvIntegerl;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.iv_02)
    ImageView iv02;
    @BindView(R.id.ll_02)
    LinearLayout ll02;
    @BindView(R.id.iv_03)
    ImageView iv03;
    @BindView(R.id.ll_03)
    LinearLayout ll03;
    @BindView(R.id.ll_04)
    LinearLayout ll04;
    @BindView(R.id.iv_04)
    ImageView iv04;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    private String orderid;
    private int syspaytype;
    private Bundle bundle;
    private DialogUtils dialogUtils;
    private double totalMoney;
    private double blance;
    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        showAlert(PayDemoActivity.this, getString(R.string.pay_success) + payResult);
                        bundle = new Bundle();
                        bundle.putString("result", "支付成功");
                        OrderFragment.updataView(2,1);
                        JumpUtils.dataJump(PayActivity.this, PayResultActivity.class, bundle, true);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        showAlert(PayDemoActivity.this, getString(R.string.pay_failed) + payResult);
                        bundle = new Bundle();
                        bundle.putString("result", "支付失败");
                        JumpUtils.dataJump(PayActivity.this, PayResultActivity.class, bundle, true);
                    }
                    break;
                }

                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay_layout);
        bundle = this.getIntent().getExtras();
        if (null != bundle) {
            orderid = bundle.getString("orderNo");
            totalMoney = bundle.getDouble("totalMoney");
        }
        dialogUtils = new DialogUtils(this);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead);
        FinishActivityManager.getManager().addActivity(this);
        TextView title = (TextView) findViewById(R.id.heard_title);
        title.setText("支付");
        tvOrderNo.setText(orderid);
        tvMoney.setText("￥" + StrUtils.moenyToDH(totalMoney+""));

    }

    @Override
    public void setUpLisener() {

    }


    public boolean isClick2 = false;
    public boolean isClick3 = false;
    public boolean isClick4 = false;

    @OnClick({R.id.heard_back, R.id.ll_02, R.id.ll_03, R.id.ll_04, R.id.tv_commit})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.ll_02:
                if ((blance >= totalMoney) && (isClick3 | isClick4)) {
                    isClick2 = true;
                    isClick3 = false;
                    isClick4 = false;
                    iv03.setBackgroundResource(R.drawable.pay_normall);
                    iv04.setBackgroundResource(R.drawable.pay_normall);
                    iv02.setBackgroundResource(R.drawable.pay_selete);
                } else {
                    if (isClick2) {
                        iv02.setBackgroundResource(R.drawable.pay_normall);
                        isClick2 = false;
                    } else {
                        iv02.setBackgroundResource(R.drawable.pay_selete);
                        isClick2 = true;
                    }
                }

                break;
            case R.id.ll_03:
                if ((blance >= totalMoney) && isClick2) {
                    dialogUtils.twoBtnDialog("余额充足，是否选择微信支付？", new DialogUtils.ChoseClickLisener() {
                        @Override
                        public void onConfirmClick(View v) {
                            isClick2 = false;
                            isClick4 = false;
                            isClick3 = true;
                            iv02.setBackgroundResource(R.drawable.pay_normall);
                            iv04.setBackgroundResource(R.drawable.pay_normall);
                            iv03.setBackgroundResource(R.drawable.pay_selete);
                            dialogUtils.dismissDialog();
                        }

                        @Override
                        public void onCancelClick(View v) {
                            dialogUtils.dismissDialog();
                        }
                    },false);

                } else {

                    if (isClick3) {
                        iv03.setBackgroundResource(R.drawable.pay_normall);
                        isClick3 = false;
                    } else {
                        iv03.setBackgroundResource(R.drawable.pay_selete);
                        isClick3 = true;
                        iv04.setBackgroundResource(R.drawable.pay_normall);
                        isClick4 = false;
                    }
                }

                break;
            case R.id.ll_04:
                if ((blance >= totalMoney) && isClick2) {
                    dialogUtils.twoBtnDialog("余额充足，是否选择支付宝支付？", new DialogUtils.ChoseClickLisener() {
                        @Override
                        public void onConfirmClick(View v) {
                            isClick2 = false;
                            isClick4 = true;
                            isClick3 = false;
                            iv02.setBackgroundResource(R.drawable.pay_normall);
                            iv03.setBackgroundResource(R.drawable.pay_normall);
                            iv04.setBackgroundResource(R.drawable.pay_selete);
                            dialogUtils.dismissDialog();
                        }

                        @Override
                        public void onCancelClick(View v) {
                            dialogUtils.dismissDialog();
                        }
                    },false);

                } else {
                    if (isClick4) {
                        iv04.setBackgroundResource(R.drawable.pay_normall);
                        isClick4 = false;
                    } else {
                        iv04.setBackgroundResource(R.drawable.pay_selete);
                        iv03.setBackgroundResource(R.drawable.pay_normall);
                        isClick3 = false;
                        isClick4 = true;
                    }
                }

                break;
            case R.id.tv_commit:
                if(isFastClick()){
                    return;
                }
                if (setPayData()) {
                    pay();
                }
                break;
        }
    }


    @Override
    public void getDataOnCreate() {
        super.getDataOnCreate();
        addSubscription(RequestClient.GetMoney(this, new NetSubscriber<BaseResultBean<String>>(this, false) {
            @Override
            public void onResultNext(BaseResultBean<String> model) {
                blance = Double.parseDouble(model.data);
                tvBalance.setText("账户余额(￥ " + StrUtils.moenyToDH(model.data+"") + ")");
            }
        }));
    }

    PayWxBean bean;

    private void pay() {
        if (syspaytype == 2 | syspaytype == 4) {

            addSubscription(RequestClient.Pay(orderid, syspaytype, this, new NetSubscriber<BaseResultBean<PayWxBean>>(this, true) {
                @Override
                public void onResultNext(BaseResultBean<PayWxBean> model) {
                    if (model.data instanceof PayWxBean) {
                        payWeChat(model.data);
                    }
                }
            }));
        }
        if (syspaytype == 3 | syspaytype == 5) {
            addSubscription(RequestClient.Pay2(orderid, syspaytype, this, new NetSubscriber<BaseResultBean<String>>(this, true) {
                @Override
                public void onResultNext(BaseResultBean<String> model) {
                    payAli(model.data);
                }
            }));
        }
        if (syspaytype == 1) {
            addSubscription(RequestClient.Pay1(orderid, syspaytype, this, new NetSubscriber<BaseResultBean>(this, true) {
                @Override
                public void onResultNext(BaseResultBean model) {
                    bundle = new Bundle();
                    bundle.putString("result", "支付成功");
                    OrderFragment.updataView(2,1);
                    JumpUtils.dataJump(PayActivity.this, PayResultActivity.class, bundle, true);
                }
            }));
        }
    }

    //想后台发起支付请求
    private boolean setPayData() {

        if (isClick2 && !isClick3 && !isClick4) {
            syspaytype = 1;
        }
        if (isClick3 && !isClick2 && !isClick4) {
            syspaytype = 4;
        }
        if (isClick4 && !isClick2 && !isClick3) {
            syspaytype = 5;
        }
        if (isClick2 && isClick3 && !isClick4) {
            syspaytype = 2;
        }
        if (isClick2 && isClick4 && !isClick3) {
            syspaytype = 3;
        }
        if (!(isClick2 | isClick3 | isClick4)) {
            dialogUtils.noBtnDialog("请选择支付方式");
            return false;
        }
        if (syspaytype == 2 | syspaytype == 3) {
            if (blance > totalMoney) {
                dialogUtils.noBtnDialog("账户资金足够，请勿混合支付");
                return false;
            }
        }
        return true;
    }

    //发起微信支付
    private void payWeChat(PayWxBean bean) {

        IWXAPI api = WXAPIFactory.createWXAPI(this, "wxbffbb975260288cc", false);//APPID
        api.registerApp("wxbffbb975260288cc");//APPID，注册本身APP
        PayReq req = new PayReq();//PayReq就是订单信息对象
        //给req对象赋值
        req.appId = "wxbffbb975260288cc";//APPID
        req.partnerId = bean.mch_id;//    商户号
        req.prepayId = bean.prepay_id;//  预付款ID
        req.nonceStr = bean.nonce_str;//随机数
        req.timeStamp = bean.timespan;//时间戳
        req.packageValue = "Sign=WXPay";//固定值Sign=WXPay
        req.sign = bean.app_sign;//签名
        api.sendReq(req);//将订单信息对象发送给微信服务器，即发送支付请求

    }

    //发起支付宝支付
    private void payAli(final String orderInfo) {
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
