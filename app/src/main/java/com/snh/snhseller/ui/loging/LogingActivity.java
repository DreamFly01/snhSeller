package com.snh.snhseller.ui.loging;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.AllUserBean;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.UserBean;
import com.snh.snhseller.bean.WithdrawDetailsBean;
import com.snh.snhseller.bean.salebean.SaleUserBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.merchantEntry.MerchantLogingActivity;
import com.snh.snhseller.ui.salesmanManagement.SalesmanMainActivity;
import com.snh.snhseller.utils.AnimUtil;
import com.snh.snhseller.db.DBManager;
import com.snh.snhseller.utils.Contans;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.Md5Utils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.wediget.IdentifyCodeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/19<p>
 * <p>changeTime：2019/2/19<p>
 * <p>version：1<p>
 */
public class LogingActivity extends BaseActivity {
    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_user_psw)
    EditText etUserPsw;
    @BindView(R.id.tv_foget_psw)
    TextView tvFogetPsw;
    @BindView(R.id.ll_loging_1)
    LinearLayout llLoging1;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.ic_getCode)
    IdentifyCodeView icGetCode;
    @BindView(R.id.ll_loging_2)
    LinearLayout llLoging2;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_regist)
    TextView tvRegist;
    @BindView(R.id.tv_opreation)
    TextView tvOpreation;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.tv_rz)
    TextView tvRz;

    private boolean accountOrPhone = true;//true 为账号密码登录 false 为手机验证码登录
    private DialogUtils dialogUtils;
    private int type = 1;//1:商家，2业务员
    private Bundle bundle;
    private String phone = "";

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_loging_layout);
        dialogUtils = new DialogUtils(this);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            phone = bundle.getString("phone");
        }
    }

    //业务员账号：18374975750  13637315267  123456
    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead);
//        tvOpreation.setVisibility(View.GONE);
        etUserName.setText(phone);
//        heardTvMenu.setText("入驻");
//        heardTvMenu.setTextColor(Color.WHITE);
        heardBack.setVisibility(View.GONE);
        heardTitle.setText("登录");
        tvPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvRegist.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        icGetCode.setActivity(this);
        icGetCode.setEt_tel(etPhone);
    }

    @Override
    public void setUpLisener() {
        icGetCode.setIdentifyCodeViewLisener(new IdentifyCodeView.IdentifyCodeViewLisener() {
            @Override
            public void onYCIdentifyCodeViewClickLisener() {
                getCode();
            }
        });
    }

    @OnClick({R.id.tv_rz, R.id.tv_foget_psw, R.id.btn_commit, R.id.tv_phone, R.id.tv_regist, R.id.tv_opreation, R.id.tv_store})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_rz:
                jumpActivity(MerchantLogingActivity.class);
                break;
            case R.id.tv_foget_psw:
                jumpActivity(ForgotPswActivity.class);
                break;

            case R.id.btn_commit:
                if (check()) {
                    loggin();
                }
                break;
            case R.id.tv_phone:
                cleanData();
                if (accountOrPhone) {
                    AnimUtil.FlipAnimatorXViewShow(llLoging1, llLoging2, 200);
                    tvPhone.setText("使用账号登录");


                    accountOrPhone = false;
                } else {
                    AnimUtil.FlipAnimatorXViewShow(llLoging2, llLoging1, 200);
                    tvPhone.setText("使用手机验证码登录");
                    accountOrPhone = true;
                }
                break;
            case R.id.tv_regist:
//                isLogin();
                jumpActivity(MerchantLogingActivity.class);
                break;
            case R.id.tv_opreation:
                type = 2;
                tvOpreation.setBackgroundResource(R.drawable.shape_soild_red_right_bg);
                tvStore.setBackgroundResource(R.drawable.shape_range_red_left_bg);
                tvOpreation.setTextColor(Color.WHITE);
                tvStore.setTextColor(Color.BLACK);
                tvFogetPsw.setText("");
                tvPhone.setText("");
                tvFogetPsw.setEnabled(false);
                tvPhone.setEnabled(false);
                break;
            case R.id.tv_store:
                type = 1;
                tvOpreation.setBackgroundResource(R.drawable.shape_range_red_right_bg);
                tvStore.setBackgroundResource(R.drawable.shape_soild_red_left_bg);
                tvOpreation.setTextColor(Color.BLACK);
                tvStore.setTextColor(Color.WHITE);
                tvFogetPsw.setText("忘记密码？");
                tvPhone.setText("用手机验证码登录");
                tvFogetPsw.setEnabled(true);
                tvPhone.setEnabled(true);
                break;
        }
    }

    private boolean check() {
        if (accountOrPhone) {
            if (StrUtils.isEmpty(etUserName.getText().toString().trim())) {
                dialogUtils.noBtnDialog("请输入用户名");
                return false;
            }
            if (StrUtils.isEmpty(etUserPsw.getText().toString().trim())) {
                dialogUtils.noBtnDialog("请输入密码");
                return false;
            }
        } else {

            if (StrUtils.isEmpty(etPhone.getText().toString().trim())) {
                dialogUtils.noBtnDialog("请输入手机号码");
                return false;
            }
            if (!icGetCode.isGetVertifyCode()) {
                dialogUtils.noBtnDialog("请获取短信验证码");
                return false;
            }
            if (StrUtils.isEmpty(etCode.getText().toString().trim())) {
                dialogUtils.noBtnDialog("请输入短信验证码");
                return false;
            }
        }

        return true;
    }

    private void cleanData() {
        etCode.setText("");
        etPhone.setText("");
        etUserName.setText("");
        etUserPsw.setText("");
    }

    private void getCode() {
        if (type == 1) {
            addSubscription(RequestClient.PostSms(etPhone.getText().toString().trim(), 1, this, new NetSubscriber<BaseResultBean>(this, true) {
                @Override
                public void onResultNext(BaseResultBean model) {
                    icGetCode.StartCountDown(60000);
                }
            }));
        }
        if (type == 2) {
            addSubscription(RequestClient.SaleSendSms(etPhone.getText().toString().trim(), 1, this, new NetSubscriber<BaseResultBean>(this) {
                @Override
                public void onResultNext(BaseResultBean model) {
                    icGetCode.StartCountDown(60000);
                }
            }));
        }
    }

    private void loggin() {
        if (accountOrPhone) {
            addSubscription(RequestClient.Login(etUserName.getText().toString().trim(), Md5Utils.md5(etUserPsw.getText().toString().trim()), etCode.getText().toString().trim(), this, new NetSubscriber<BaseResultBean<AllUserBean>>(this, true) {
                @Override
                public void onResultNext(BaseResultBean<AllUserBean> model) {
                    if (model.data.type == 1) {
                        DBManager.getInstance(LogingActivity.this).logingSuccess(model.data, LogingActivity.this);
                    } else if (model.data.type == 2) {
                        SaleUserBean bean = new SaleUserBean();
                        bean.NickName = model.data.NickName;
                        bean.PhoneNumber = model.data.PhoneNumber;
                        bean.RealName = model.data.RealName;
                        bean.SalesmanId = model.data.SalesmanId;
                        bean.SalesmanLogo = model.data.SalesmanLogo;
                        DBManager.getInstance(LogingActivity.this).saveSaleUser(bean);
                        JumpUtils.simpJump(LogingActivity.this, SalesmanMainActivity.class, true);
                    }

                }
            }));
        }else {
            addSubscription(RequestClient.Login(etPhone.getText().toString().trim(), "", etCode.getText().toString().trim(), this, new NetSubscriber<BaseResultBean<AllUserBean>>(this, true) {
                @Override
                public void onResultNext(BaseResultBean<AllUserBean> model) {
                    if (model.data.type == 1) {
                        DBManager.getInstance(LogingActivity.this).logingSuccess(model.data, LogingActivity.this);
                    } else if (model.data.type == 2) {
                        SaleUserBean bean = new SaleUserBean();
                        bean.NickName = model.data.NickName;
                        bean.PhoneNumber = model.data.PhoneNumber;
                        bean.RealName = model.data.RealName;
                        bean.SalesmanId = model.data.SalesmanId;
                        bean.SalesmanLogo = model.data.SalesmanLogo;
                        DBManager.getInstance(LogingActivity.this).saveSaleUser(bean);
                        JumpUtils.simpJump(LogingActivity.this, SalesmanMainActivity.class, true);
                    }

                }
            }));
        }

//        if (type == 1) {
//            if (accountOrPhone) {
//                addSubscription(RequestClient.LoginPhone(etUserName.getText().toString().trim(), "", Md5Utils.md5(etUserPsw.getText().toString().trim()), this, new NetSubscriber<BaseResultBean<UserBean>>(this, true) {
//                    @Override
//                    public void onResultNext(BaseResultBean<UserBean> model) {
//                        DBManager.getInstance(LogingActivity.this).logingSuccess(model.data, LogingActivity.this);
//                    }
//                }));
//            } else {
//                addSubscription(RequestClient.LoginPhone(etPhone.getText().toString().trim(), etCode.getText().toString().trim(), "", this, new NetSubscriber<BaseResultBean<UserBean>>(this, true) {
//                    @Override
//                    public void onResultNext(BaseResultBean<UserBean> model) {
//                        DBManager.getInstance(LogingActivity.this).logingSuccess(model.data, LogingActivity.this);
//                    }
//                }));
//            }
//        }
//        if (type == 2) {
//            if (accountOrPhone) {
//                addSubscription(RequestClient.LoginSale(etUserName.getText().toString().trim(), Md5Utils.md5(etUserPsw.getText().toString().trim()), this, new NetSubscriber<BaseResultBean<SaleUserBean>>(this, true) {
//                    @Override
//                    public void onResultNext(BaseResultBean<SaleUserBean> model) {
//                        DBManager.getInstance(LogingActivity.this).saveSaleUser(model.data);
//                        JumpUtils.simpJump(LogingActivity.this, SalesmanMainActivity.class, true);
//                    }
//                }));
//            } else {
//                addSubscription(RequestClient.SalePhoneLogin(etPhone.getText().toString().trim(), etCode.getText().toString().trim(), this, new NetSubscriber<BaseResultBean<SaleUserBean>>(this, true) {
//                    @Override
//                    public void onResultNext(BaseResultBean<SaleUserBean> model) {
//                        DBManager.getInstance(LogingActivity.this).saveSaleUser(model.data);
//                        JumpUtils.simpJump(LogingActivity.this, SalesmanMainActivity.class, true);
//                    }
//                }));
//            }
//
//        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private long mExitTime = 0;
    public static final int EXIT_TIME = 2000;
    private Toast toast;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > EXIT_TIME) {
                toast = Toast.makeText(this, "再次点击退出应用", Toast.LENGTH_SHORT);
                toast.show();
                mExitTime = System.currentTimeMillis();
            } else {
                if (null != toast) {
                    toast.cancel();
                }
                finishAffinity();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
