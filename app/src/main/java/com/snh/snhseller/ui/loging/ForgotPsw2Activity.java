package com.snh.snhseller.ui.loging;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.requestApi.RequestClient;
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
public class ForgotPsw2Activity extends BaseActivity {
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
    @BindView(R.id.et_user_phone)
    EditText etUserPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.ic_getCode)
    IdentifyCodeView icGetCode;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    private DialogUtils dialogUtils;

    private Bundle bundle;
    private String phone;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forgotpsw_layout);
        dialogUtils = new DialogUtils(this);
        bundle = getIntent().getExtras();
        if(null!=bundle){
            phone = bundle.getString("phone");
        }
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this,rlHead);
        heardTitle.setText("重置密码");
        btnCommit.setText("确定提交");
        etCode.setHint("请再次输入密码");
        etUserPhone.setHint("请输入新密码");
        etCode.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etUserPhone.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        InputFilter[] filters = {new InputFilter.LengthFilter(20)};
        etCode.setFilters(filters);
        etUserPhone.setFilters(filters);

        icGetCode.setVisibility(View.GONE);
    }

    @Override
    public void setUpLisener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.heard_back, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.btn_commit:
                if(check()){
                    commit();
                }
                break;
        }
    }

    private boolean check(){
        if(StrUtils.isEmpty(etUserPhone.getText().toString().trim())){
            dialogUtils.noBtnDialog("请输入新密码");
            return false;
        }
        if(StrUtils.isEmpty(etCode.getText().toString().trim())){
            dialogUtils.noBtnDialog("请再次输入密码");
            return false;
        }
        if(!StrUtils.equals(etUserPhone.getText().toString().trim(),etCode.getText().toString().trim())){
            dialogUtils.noBtnDialog("两次输入密码不一致");
            return false;
        }
        if(!StrUtils.isPsw(etUserPhone.getText().toString().trim())){
            dialogUtils.noBtnDialog("密码应为6-20位的中英文");
            return false;
        }
        return true;
    }

    private void commit(){
        addSubscription(RequestClient.ForgetPwd(phone, Md5Utils.md5(etCode.getText().toString().trim()), this, new NetSubscriber<BaseResultBean>(this,true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                jumpActivity(LogingActivity.class);
            }
        }));
    }
}
