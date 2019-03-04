package com.snh.snhseller.ui.merchantEntry;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.R;
import com.snh.snhseller.R;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.wediget.IdentifyCodeView;

import java.util.List;

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
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.identify_code_view)
    IdentifyCodeView identifyCodeView;

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
                if(check()){
                    loging();
                }
//                bundle = new Bundle();
//                bundle.putString("phone",etPhone.getText().toString().trim());
//                JumpUtils.dataJump(LogingActivity.this,EntryChoseActivity.class,bundle,false);
                break;
        }
    }

    private boolean check() {
        if(!identifyCodeView.isGetVertifyCode()){
            dialogUtils.noBtnDialog("请先获取短信验证码");
            return false;
        }
        if(StrUtils.isEmpty(etCode.getText().toString().trim())){
            dialogUtils.noBtnDialog("请输入验证码");
            return false;
        }
        return true;
    }

    private void getCode(){
        addSubscription(RequestClient.PostSms(etPhone.getText().toString().trim(), 2,this, new NetSubscriber<BaseResultBean>(LogingActivity.this,true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                showShortToast("短信发送成功，请注意查收！");
                identifyCodeView.StartCountDown(60000);
            }
        }));
    }
    private void loging(){
        addSubscription(RequestClient.MerchantLogin(etPhone.getText().toString().trim(), etCode.getText().toString().trim(), this, new NetSubscriber<BaseResultBean>(LogingActivity.this,true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                bundle = new Bundle();
                bundle.putString("phone",etPhone.getText().toString().trim());
                JumpUtils.dataJump(LogingActivity.this,EntryChoseActivity.class,bundle,false);
            }
        }));
    }
}
