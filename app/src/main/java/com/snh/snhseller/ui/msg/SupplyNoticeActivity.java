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
public class SupplyNoticeActivity extends BaseActivity {

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
    private Bundle bundle;
    private SupplyNoticeFragment applyNoticeFragment;
    private FragmentManager fragmentManager;


    int applyNum;
    int systemNum;
    public static boolean isForeground = true;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_supplynotice_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            applyNum = bundle.getInt("applyNum", 0);
            systemNum = bundle.getInt("systemNum", 0);
        }
    }

    private FragmentTransaction transaction;

    @Override
    public void setUpViews() {
        isForeground = false;
        IsBang.setImmerHeard(this, rlHead);
        heardTitle.setText("申请通知");
        fragmentManager = this.getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        applyNoticeFragment = new SupplyNoticeFragment();
        transaction.add(R.id.main_frameLayout, applyNoticeFragment);
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

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = true;
    }
}
