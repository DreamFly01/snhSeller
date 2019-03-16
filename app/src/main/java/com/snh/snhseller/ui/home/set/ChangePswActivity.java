package com.snh.snhseller.ui.home.set;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.loging.LogingActivity;
import com.snh.snhseller.utils.DBManager;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.Md5Utils;
import com.snh.snhseller.utils.StrUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/1/3<p>
 * <p>changeTime：2019/1/3<p>
 * <p>version：1<p>
 */
public class ChangePswActivity extends BaseActivity {


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
    @BindView(R.id.et_old)
    EditText etOld;
    @BindView(R.id.et_new)
    EditText etNew;
    @BindView(R.id.et_new1)
    EditText etNew1;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    private DialogUtils dialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_changepsw_layout);
        dialogUtils = new DialogUtils(this);
    }

    @Override
    public void setUpViews() {
        btnCommit.setText("提交");
        heardTitle.setText("修改密码");
    }

    @Override
    public void setUpLisener() {
        IsBang.setImmerHeard(this, rlHead);
    }


    @OnClick({R.id.heard_back, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.btn_commit:
                if (check()) {
                    changePsw();
                }
                break;
        }
    }

    private void changePsw() {
        String oldPsw = Md5Utils.md5(etOld.getText().toString().trim());
        String newPsw = Md5Utils.md5(etNew.getText().toString().trim());
        addSubscription(RequestClient.OldPwdToNewPwd(oldPsw, newPsw, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.simpleDialog("密码修改成功", new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        dialogUtils.dismissDialog();
                        DBManager.getInstance(ChangePswActivity.this).cleanUser();
                        NIMClient.getService(AuthService.class).logout();
                        JumpUtils.simpJump(ChangePswActivity.this, LogingActivity.class, true);
                    }
                }, false);
            }
        }));
    }

    private boolean check() {
        if (StrUtils.isEmpty(etNew.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入原密码");
            return false;
        }
        if (StrUtils.isEmpty(etNew.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入新密码");
            return false;
        }
        if (StrUtils.isEmpty(etNew1.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请再次输入新密码");
            return false;
        }
        if (!etNew.getText().toString().trim().equals(etNew1.getText().toString().trim())) {
            dialogUtils.noBtnDialog("两次输入新密码不一致");
            return false;
        }
        if(StrUtils.isPsw(etNew.getText().toString().trim())){
            dialogUtils.noBtnDialog("请输入6-20位数字密码组合的密码");
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
