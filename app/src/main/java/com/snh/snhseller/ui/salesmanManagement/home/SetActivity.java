package com.snh.snhseller.ui.salesmanManagement.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snh.snhseller.R;
import com.snh.snhseller.ui.loging.LogingActivity;
import com.snh.snhseller.ui.salesmanManagement.BaseActivity;
import com.snh.snhseller.db.DBManager;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.GlideCacheUtil;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/4<p>
 * <p>changeTime：2019/3/4<p>
 * <p>version：1<p>
 */
public class SetActivity extends BaseActivity {
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
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.tv_cahe)
    TextView tvCahe;
    @BindView(R.id.ll_02)
    LinearLayout ll02;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    private DialogUtils dialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_saleset_layout);
        dialogUtils = new DialogUtils(this);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this,rlHead,"");
        heardTitle.setText("设置");
        btnCommit.setText("退出登录");
        tvCahe.setText("(" + GlideCacheUtil.getInstance().getCacheSize(this) + ")");
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

    @OnClick({R.id.heard_back, R.id.ll_01, R.id.ll_02,R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.ll_01:
                jumpActivity(ChangePswActivity.class);
                break;
            case R.id.ll_02:
                dialogUtils.twoBtnDialog("确认清楚缓存么？", new DialogUtils.ChoseClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        GlideCacheUtil.getInstance().clearImageAllCache(SetActivity.this);
                        tvCahe.setText("(0.00K)");
                        dialogUtils.dismissDialog();
                    }

                    @Override
                    public void onCancelClick(View v) {
                        dialogUtils.dismissDialog();
                    }
                }, true);
                break;
            case R.id.btn_commit:
                String phone = DBManager.getInstance(this).getSaleInfo().PhoneNumber;
                DBManager.getInstance(this).cleanSale();
                Bundle bundle = new Bundle();
                bundle.putString("phone",phone);
                JumpUtils.dataJump(this, LogingActivity.class, bundle,true);
                break;
        }
    }

}
