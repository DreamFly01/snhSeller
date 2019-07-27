package com.snh.moudle_coupons.ui.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.snh.library_base.BaseActivity;
import com.snh.library_base.router.RouterActivityPath;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.moudle_coupons.R;
import com.snh.moudle_coupons.R2;
import com.snh.moudle_coupons.bean.CouponsNumBean;
import com.snh.moudle_coupons.bean.MsgEventBean;
import com.snh.moudle_coupons.netapi.RequestClient;
import com.snh.moudle_coupons.ui.fragment.MyCouponsFragment;
import com.snh.moudle_coupons.ui.fragment.SupplierCouponsFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/5/29<p>
 * <p>changeTime：2019/5/29<p>
 * <p>version：1<p>
 */
@Route(path = RouterActivityPath.Coupons.PATH_MY_COUPONS)
public class MyCouponsActivity extends BaseActivity {
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
    @BindView(R2.id.tv_01)
    TextView tv01;
    @BindView(R2.id.tv_02)
    TextView tv02;
    @BindView(R2.id.frame_coupons)
    FrameLayout frameCoupons;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private MyCouponsFragment myCouponsFragment;
    private SupplierCouponsFragment storeCouponsFragment;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.coupons_activity_mycoupons_layout);
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("优惠劵管理");
        heardTvMenu.setText("创建");
        InitailFragments();

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
        resetBtn();
        switch (index) {
            case 0:
                tv01.setTextColor(Color.parseColor("#FD1A4F"));
                if (myCouponsFragment == null) {
                    myCouponsFragment = new MyCouponsFragment();
                    transaction.add(R.id.frame_coupons, myCouponsFragment, "coupons1");
                } else {
                    transaction.show(myCouponsFragment);
                }
                transaction.commitAllowingStateLoss();
                break;
            case 1:
                tv02.setTextColor(Color.parseColor("#FD1A4F"));
                if (storeCouponsFragment == null) {
                    storeCouponsFragment = new SupplierCouponsFragment();

                    transaction.add(R.id.frame_coupons, storeCouponsFragment, "coupons2");
                } else {

                    transaction.show(storeCouponsFragment);
                }
                transaction.commitAllowingStateLoss();
                break;

        }

    }

    private void hideFragments() {
        myCouponsFragment = (MyCouponsFragment) fragmentManager.findFragmentByTag("coupons1");
        storeCouponsFragment = (SupplierCouponsFragment) fragmentManager.findFragmentByTag("coupons2");

        if (myCouponsFragment != null) {
            if (!myCouponsFragment.isHidden()) {
                transaction.hide(myCouponsFragment);
            }
        }
        if (storeCouponsFragment != null) {
            if (!storeCouponsFragment.isHidden()) {
                transaction.hide(storeCouponsFragment);
            }
        }
    }

    private  void resetBtn() {
        tv01.setTextColor(Color.parseColor("#1e1e1e"));
        tv02.setTextColor(Color.parseColor("#1e1e1e"));

    }

    @Override
    public void setUpLisener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @OnClick({R2.id.tv_01, R2.id.tv_02,R2.id.heard_back,R2.id.heard_tv_menu})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_01) {
            setTabSelection(0);

        } else if (i == R.id.tv_02) {
            setTabSelection(1);

        } else if (i == R.id.heard_back) {
            this.finish();

        }else if(i == R.id.heard_tv_menu){
            jumpActivity(CreatCouponsActivity.class);
        }
    }

    private void getData(){
//        addSubscription(RequestClient.GetCouponsCount(this, new NetSubscriber<BaseResultBean<CouponsNumBean>>(this) {
//            @Override
//            public void onResultNext(BaseResultBean<CouponsNumBean> model) {
//                tv01.setText("发放的优惠券("+model.data.CouponsCount+")");
//                tv02.setText("参与的优惠券("+model.data.SupplierCouponsCount+")");
//            }
//        }));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updataTitle(MsgEventBean bean){
        if("updataTitle".equals(bean.getMsg())){
            getData();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
