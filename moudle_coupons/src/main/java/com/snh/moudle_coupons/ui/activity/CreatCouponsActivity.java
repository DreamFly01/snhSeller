package com.snh.moudle_coupons.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snh.library_base.BaseActivity;
import com.snh.moudle_coupons.R;
import com.snh.moudle_coupons.R2;
import com.snh.moudle_coupons.bean.MsgEventBean;
import com.snh.moudle_coupons.ui.fragment.CreatMyCouponsFragment;
import com.snh.moudle_coupons.ui.fragment.CreatSupplierCouponsFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/5/31<p>
 * <p>changeTime：2019/5/31<p>
 * <p>version：1<p>
 */
public class CreatCouponsActivity extends BaseActivity {
    @BindView(R2.id.heard_back)
    LinearLayout heardBack;
    @BindView(R2.id.heard_title)
    TextView heardTitle;
    @BindView(R2.id.heard_menu)
    ImageView heardMenu;
    @BindView(R2.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R2.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R2.id.rl_head)
    LinearLayout rlHead;
    @BindView(R2.id.frame_coupons)
    FrameLayout frameCoupons;
    @BindView(R2.id.coupons_tv_01)
    TextView tv01;
    @BindView(R2.id.coupons_tv_02)
    TextView tv02;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private CreatMyCouponsFragment myCouponsFragment;
    private CreatSupplierCouponsFragment supplierCouponsFragment;
    private int type = 0;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.coupons_activity_creatcoupons_layout);
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("创建优惠劵");
        heardTvMenu.setText("确定");
        InitailFragments();
    }

    @Override
    public void setUpLisener() {

    }

    private void InitailFragments() {
        fragmentManager = this.getSupportFragmentManager();
        setTabSelection(0);
    }

    public void setTabSelection(int index) {
        // 开启一个Fragment事务
        transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments();

        switch (index) {
            case 0:
                setBtn(tv01);
                if (myCouponsFragment == null) {
                    myCouponsFragment = new CreatMyCouponsFragment();
                    transaction.add(R.id.frame_coupons, myCouponsFragment, "ccoupons1");
                } else {
                    transaction.show(myCouponsFragment);
                }
                transaction.commitAllowingStateLoss();
                break;
            case 1:
                setBtn(tv02);
                if (supplierCouponsFragment == null) {
                    supplierCouponsFragment = new CreatSupplierCouponsFragment();
                    transaction.add(R.id.frame_coupons, supplierCouponsFragment, "ccoupons2");
                } else {
                    transaction.show(supplierCouponsFragment);
                }
                transaction.commitAllowingStateLoss();
                break;

        }

    }

    private void setBtn(TextView tv) {
        tv01.setTextColor(getResources().getColor(R.color.txt_dark));
        tv02.setTextColor(getResources().getColor(R.color.txt_dark));
        tv01.setBackgroundResource(R.drawable.coupons_select_bg);
        tv02.setBackgroundResource(R.drawable.coupons_select_bg);

        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setBackgroundColor(getResources().getColor(R.color.txt_red));
    }

    private void hideFragments() {
        myCouponsFragment = (CreatMyCouponsFragment) fragmentManager.findFragmentByTag("ccoupons1");
        supplierCouponsFragment = (CreatSupplierCouponsFragment) fragmentManager.findFragmentByTag("ccoupons2");

        if (myCouponsFragment != null) {
            if (!myCouponsFragment.isHidden()) {
                transaction.hide(myCouponsFragment);
            }
        }
        if (supplierCouponsFragment != null) {
            if (!supplierCouponsFragment.isHidden()) {
                transaction.hide(supplierCouponsFragment);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R2.id.heard_back, R2.id.heard_tv_menu, R2.id.coupons_tv_02, R2.id.coupons_tv_01})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.heard_back) {
            this.finish();
        } else if (i == R.id.heard_tv_menu) {
            if (type == 0) {
                EventBus.getDefault().post(new MsgEventBean("1"));
            }else {
                EventBus.getDefault().post(new MsgEventBean("2"));
            }
        } else if (i == R.id.coupons_tv_01) {
            type = 0;
            setTabSelection(0);
        } else if (i == R.id.coupons_tv_02) {
            type = 1;
            setTabSelection(1);
        }
    }
}
