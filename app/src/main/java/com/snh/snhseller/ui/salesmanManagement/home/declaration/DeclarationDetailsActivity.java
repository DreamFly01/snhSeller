package com.snh.snhseller.ui.salesmanManagement.home.declaration;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snh.snhseller.R;
import com.snh.snhseller.ui.salesmanManagement.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/5<p>
 * <p>changeTime：2019/3/5<p>
 * <p>version：1<p>
 */
public class DeclarationDetailsActivity extends BaseActivity {
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
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.iv_01)
    ImageView iv01;
    @BindView(R.id.iv_02)
    ImageView iv02;
    @BindView(R.id.iv_03)
    ImageView iv03;
    @BindView(R.id.iv_state)
    ImageView ivState;
    @BindView(R.id.iv_logo1)
    ImageView ivLogo1;
    @BindView(R.id.tv_state1)
    TextView tvState1;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.ll_02)
    LinearLayout ll02;
    @BindView(R.id.ll_03)
    LinearLayout ll03;
    @BindView(R.id.ll_menu)
    LinearLayout llMenu;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_declarationdetails_layout);
    }

    @Override
    public void setUpViews() {

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

    @OnClick({R.id.heard_back, R.id.ll_01, R.id.ll_02, R.id.ll_03})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.ll_01:
                break;
            case R.id.ll_02:
                break;
            case R.id.ll_03:
                break;
        }
    }
}
