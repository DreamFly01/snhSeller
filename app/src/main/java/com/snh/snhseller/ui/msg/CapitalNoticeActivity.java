package com.snh.snhseller.ui.msg;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.utils.IsBang;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/12<p>
 * <p>changeTime：2019/3/12<p>
 * <p>version：1<p>
 */
public class CapitalNoticeActivity extends BaseActivity {


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
    @BindView(R.id.main_frameLayout)
    FrameLayout mainFrameLayout;

   private OrderNoticeFragment  orderNoticeFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Bundle bundle;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_system_layout);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead);
        heardTitle.setText("资金通知");
        fragmentManager = this.getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        orderNoticeFragment = new OrderNoticeFragment();
        bundle = new Bundle();
        bundle.putInt("type",4);
        orderNoticeFragment.setArguments(bundle);
        transaction.add(R.id.main_frameLayout, orderNoticeFragment);
        transaction.commit();
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

    @OnClick(R.id.heard_back)
    public void onClick() {
        this.finish();
    }
}
