package com.snh.snhseller.ui.home.money;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.MoneyBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.StrUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/4/2<p>
 * <p>changeTime：2019/4/2<p>
 * <p>version：1<p>
 */
public class CapitalActivity extends BaseActivity {
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
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.ll_02)
    LinearLayout ll02;

    private int index = 1;
    public static boolean isForeground = true;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_capital_layout);
    }

    @Override
    public void setUpViews() {
        isForeground = false;
        heardTitle.setText("账户资金");
        IsBang.setImmerHeard(this,rlHead);
        rlMenu.setVisibility(View.VISIBLE);
        heardTvMenu.setText("明细");
    }

    @Override
    public void setUpLisener() {

    }

    @Override
    public void getDataOnCreate() {
        super.getDataOnCreate();
        addSubscription(RequestClient.GetAccountMoney(index, this, new NetSubscriber<BaseResultBean<MoneyBean>>(this,true) {
            @Override
            public void onResultNext(BaseResultBean<MoneyBean> model) {
                tvMoney.setText(StrUtils.moenyToDH(model.data.HavaMoney+""));
            }
        }));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.heard_back, R.id.ll_01, R.id.ll_02,R.id.rl_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.ll_01:
                jumpActivity(WithdrawRecordActivity.class);
                break;
            case R.id.ll_02:
                jumpActivity(MyBanksActivity.class);
                break;
            case R.id.rl_menu:
                jumpActivity(WithdrawListActivity.class);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = true;
    }
}
