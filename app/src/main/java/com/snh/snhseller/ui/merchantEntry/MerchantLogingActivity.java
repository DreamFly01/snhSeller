package com.snh.snhseller.ui.merchantEntry;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snh.library_base.utils.Contans;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.SPUtils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.wediget.IdentifyCodeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/1/22<p>
 * <p>changeTime：2019/1/22<p>
 * <p>version：1<p>
 */
public class MerchantLogingActivity extends BaseActivity {
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
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.identify_code_view)
    IdentifyCodeView identifyCodeView;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.et_referee)
    EditText etReferee;

    private DialogUtils dialogUtils;
    private Bundle bundle;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_loginmerchant_layout);
        dialogUtils = new DialogUtils(this);
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("商家入驻");
        IsBang.setImmerHeard(this, rlHead);
        identifyCodeView.setActivity(this);
        identifyCodeView.setEt_tel(etPhone);
        if (Contans.debug) {
            etPhone.setText("17688829959");
        }
    }

    @Override
    public void setUpLisener() {
        identifyCodeView.setIdentifyCodeViewLisener(new IdentifyCodeView.IdentifyCodeViewLisener() {
            @Override
            public void onYCIdentifyCodeViewClickLisener() {
                getCode();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.heard_back, R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.tv_commit:
                if (Contans.debug) {
                    bundle = new Bundle();
                    bundle.putString("phone", etPhone.getText().toString().trim());
                    SPUtils.getInstance(MerchantLogingActivity.this).saveData(Contans.REFEREE,etReferee.getText().toString().trim());
                    JumpUtils.dataJump(MerchantLogingActivity.this, EntryChoseActivity.class, bundle, false);
                } else {
                    if (check()) {
                        loging();
                    }
                }
                break;
        }
    }

    private boolean check() {
        if (!identifyCodeView.isGetVertifyCode()) {
            dialogUtils.noBtnDialog("请先获取短信验证码");
            return false;
        }
        if (StrUtils.isEmpty(etCode.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入验证码");
            return false;
        }
        if(StrUtils.equals(etPhone.getText().toString().trim(),etReferee.getText().toString().trim())){
            dialogUtils.noBtnDialog("自己不能成为自己的推荐人");
            return false;
        }
        return true;
    }

    private void getCode() {
        addSubscription(RequestClient.SmsCode(etPhone.getText().toString().trim(), this, new NetSubscriber<BaseResultBean>(MerchantLogingActivity.this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                showShortToast("短信发送成功，请注意查收！");
                identifyCodeView.StartCountDown(60000);
            }
        }));
    }

    private void loging() {
        addSubscription(RequestClient.MerchantLogin(etPhone.getText().toString().trim(), etCode.getText().toString().trim(), this, new NetSubscriber<BaseResultBean>(MerchantLogingActivity.this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                bundle = new Bundle();
                bundle.putString("phone", etPhone.getText().toString().trim());
                SPUtils.getInstance(MerchantLogingActivity.this).saveData(Contans.REFEREE,etReferee.getText().toString().trim());
                JumpUtils.dataJump(MerchantLogingActivity.this, EntryChoseActivity.class, bundle, false);
            }
        }));
    }
}
