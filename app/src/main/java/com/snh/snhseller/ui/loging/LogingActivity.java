package com.snh.snhseller.ui.loging;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.UserBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.salesmanManagement.SalesmanMainActivity;
import com.snh.snhseller.utils.AnimUtil;
import com.snh.snhseller.utils.DBManager;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.Md5Utils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.wediget.IdentifyCodeView;

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

    private boolean accountOrPhone = true;//true 为账号密码登录 false 为手机验证码登录
    private DialogUtils dialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_loging_layout);
        dialogUtils = new DialogUtils(this);
    }

    //业务员账号：18374975750
    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead);
//        tvOpreation.setVisibility(View.GONE);
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

    @OnClick({R.id.tv_foget_psw, R.id.btn_commit, R.id.tv_phone, R.id.tv_regist,R.id.tv_opreation})
    public void onClick(View view) {
        switch (view.getId()) {
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
                jumpActivity(com.snh.snhseller.ui.merchantEntry.LogingActivity.class);
                break;
            case R.id.tv_opreation:
                jumpActivity(SalesmanMainActivity.class);
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
        addSubscription(RequestClient.PostSms(etPhone.getText().toString().trim(), 1, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                icGetCode.StartCountDown(60000);
            }
        }));
    }

    private void loggin() {
        if (accountOrPhone) {
            addSubscription(RequestClient.LoginPhone(etUserName.getText().toString().trim(), "", Md5Utils.md5(etUserPsw.getText().toString().trim()), this, new NetSubscriber<BaseResultBean<UserBean>>(this, true) {
                @Override
                public void onResultNext(BaseResultBean<UserBean> model) {
                    DBManager.getInstance(LogingActivity.this).logingSuccess(model.data, LogingActivity.this);
                }
            }));
        } else {
            addSubscription(RequestClient.LoginPhone(etPhone.getText().toString().trim(), etCode.getText().toString().trim(), "", this, new NetSubscriber<BaseResultBean<UserBean>>(this, true) {
                @Override
                public void onResultNext(BaseResultBean<UserBean> model) {
                    DBManager.getInstance(LogingActivity.this).logingSuccess(model.data, LogingActivity.this);
                }
            }));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
