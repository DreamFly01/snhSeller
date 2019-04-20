package com.snh.snhseller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.netease.nim.uikit.common.activity.UI;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.NoticeNumBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.home.HomeFragment;
import com.snh.snhseller.ui.msg.MsgFragment;
import com.snh.snhseller.ui.order.OrderFragment;
import com.snh.snhseller.ui.product.ProductFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends UI {

    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    @BindView(R.id.main_frameLayout)
    FrameLayout mainFrameLayout;
    static ImageView iv01;
    @BindView(R.id.tv_01)
    TextView tv01;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    static ImageView iv02;
    @BindView(R.id.tv_02)
    TextView tv02;
    @BindView(R.id.ll_02)
    LinearLayout ll02;
    static ImageView iv04;
    @BindView(R.id.tv_04)
    TextView tv04;
    @BindView(R.id.ll_03)
    LinearLayout ll03;
    static ImageView iv05;
    @BindView(R.id.tv_05)
    TextView tv05;
    @BindView(R.id.ll_04)
    LinearLayout ll04;
    @BindView(R.id.ll_tab)
    LinearLayout llTab;
    private long mExitTime = 0;
    private Toast toast;
    private static Bundle bundle;
    private static FragmentManager fragmentManager;
    public static final String SHOW_FRAGMENT_INDEX = "show_fragment_index";
    private int index = -1;
    public static boolean isForeground = false;
    /**
     * 退出间隔时间 单位毫秒
     */
    public static final int EXIT_TIME = 2000;
    private static FragmentTransaction transaction;
    private static MsgFragment msgFragment;
    private static OrderFragment orderFragment;
    private static ProductFragment productFragment;
    private static HomeFragment homeFragment;
    public static TextView tvNum,tvNum2;
    public static RelativeLayout rl04;
    private static int Categories = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setUpViews();
        ButterKnife.bind(this);
        ImmersionBar.with(this).init();
        InitailFragments();
        getCount();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > EXIT_TIME) {
                toast = Toast.makeText(this, "再次点击退出应用", Toast.LENGTH_SHORT);
                toast.show();
                mExitTime = System.currentTimeMillis();
            } else {
                if (null != toast) {
                    toast.cancel();
                }
                finishAffinity();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
            bundle = getIntent().getExtras();
        if (bundle != null) {
            index = bundle.getInt(SHOW_FRAGMENT_INDEX, 0);
            Categories = bundle.getInt("categories",0);
            setTabSelection(index);
        }
    }

    public void setUpViews() {
        tvNum = this.findView(R.id.tv_num);
        tvNum2 = this.findView(R.id.tv_num2);
        iv01 = this.findView(R.id.iv_01);
        iv02 = this.findView(R.id.iv_02);
        iv04 = this.findView(R.id.iv_04);
        iv05 = this.findView(R.id.iv_05);
        rl04 = this.findView(R.id.rl_04);

    }

    private void InitailFragments() {
        fragmentManager = this.getSupportFragmentManager();
        bundle = getIntent().getExtras();
        if (null != bundle) {
            index = bundle.getInt(SHOW_FRAGMENT_INDEX, 0);
            setTabSelection(index);
        } else {
            setTabSelection(0);
        }
    }

    public static void setTabSelection(int index) {
        // 开启一个Fragment事务
        transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments();
        resetBtn();
        switch (index) {
            case 0:
                iv01.setBackgroundResource(R.drawable.store1);
                if (productFragment == null) {
                    productFragment = new ProductFragment();
                    transaction.add(R.id.main_frameLayout, productFragment);
                } else {
                    transaction.show(productFragment);
                }
                transaction.commit();
                break;
            case 1:
                iv02.setBackgroundResource(R.drawable.order1);
                if (orderFragment == null) {
                    orderFragment = new OrderFragment();
                    bundle = new Bundle();
                    bundle.putInt("categories",Categories);
                    orderFragment.setArguments(bundle);
                    transaction.add(R.id.main_frameLayout, orderFragment);
                } else {
                    if (Categories==101||Categories==102) {
                        orderFragment.setType(0);
                    }else if(Categories==201||Categories==203||Categories==204){
                        orderFragment.setType(1);
                    }else if(Categories==202){
                        orderFragment.setType(2);
                    } else {
                        orderFragment.setType(0);
                    }
                    transaction.show(orderFragment);
                }
                transaction.commit();
                break;
            case 2:
                iv04.setBackgroundResource(R.drawable.chat1);
                if (msgFragment == null) {
                    msgFragment = new MsgFragment();
                    transaction.add(R.id.main_frameLayout, msgFragment);
                } else {
                    transaction.show(msgFragment);
                }
                transaction.commit();
                break;
            case 3:
                iv05.setBackgroundResource(R.drawable.home1);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.main_frameLayout, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                transaction.commit();
                break;
        }

    }

    private static void hideFragments() {
        if (msgFragment != null) {
            if (!msgFragment.isHidden()) {
                transaction.hide(msgFragment);
            }
        }
        if (orderFragment != null) {
            if (!orderFragment.isHidden()) {
                transaction.hide(orderFragment);
            }
        }
        if (productFragment != null) {
            if (!productFragment.isHidden()) {
                transaction.hide(productFragment);
            }
        }
        if (homeFragment != null) {
            if (!homeFragment.isHidden()) {
                transaction.hide(homeFragment);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 必须调用该方法，防止内存泄漏
        ImmersionBar.with(this).destroy();
    }

    @OnClick({R.id.ll_01, R.id.ll_02, R.id.ll_03, R.id.ll_04})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_01:
                setTabSelection(0);
                break;
            case R.id.ll_02:
                setTabSelection(1);
                break;
            case R.id.ll_03:
                setTabSelection(2);
                break;
            case R.id.ll_04:
                setTabSelection(3);
                break;
        }
    }

    private static void resetBtn() {
        iv01.setBackgroundResource(R.drawable.store0);
        iv02.setBackgroundResource(R.drawable.order0);
        iv04.setBackgroundResource(R.drawable.chat0);
        iv05.setBackgroundResource(R.drawable.home0);
    }

    private void getCount() {
        RequestClient.GetSupplierNoticeUnreadCount(this, new NetSubscriber<BaseResultBean<NoticeNumBean>>(this) {
            @Override
            public void onResultNext(BaseResultBean<NoticeNumBean> model) {
                int sumNum =  model.data.ApplyNRC + model.data.MoneyNoticeNRC + model.data.SystemNiticeNRC + model.data.UserOrderNRC + model.data.SupplierOrderNRC;
                int sumNum2 = model.data.UserDFH+model.data.CKDFH+model.data.JHDSH+model.data.JHDZF;


                if (sumNum > 99) {
                    tvNum.setVisibility(View.VISIBLE);
                    tvNum.setText("99+");
                } else if (sumNum <= 0) {
                    tvNum.setVisibility(View.GONE);
                } else {
                    tvNum.setVisibility(View.VISIBLE);
                    tvNum.setText(sumNum + "");
                }
                if (sumNum2 > 99) {
                    tvNum2.setVisibility(View.VISIBLE);
                    tvNum2.setText("99+");
                } else if (sumNum2 <= 0) {
                    tvNum2.setVisibility(View.GONE);
                } else {
                    tvNum2.setVisibility(View.VISIBLE);
                    tvNum2.setText(sumNum2 + "");
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = true;
    }

    private int opend;
    @Override
    protected void onResume() {
        super.onResume();
        if(null!=bundle){
            opend = bundle.getInt(MainActivity.KEY_TITLE);
            if(opend==1){
                setTabSelection(0);
            }
        }
    }
}
