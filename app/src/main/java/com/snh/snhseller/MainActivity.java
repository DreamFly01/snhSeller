package com.snh.snhseller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.snh.library_base.utils.Contans;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.bean.NoticeNumBean;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.home.HomeFragment;
import com.snh.snhseller.ui.home.set.ChangePswActivity;
import com.snh.snhseller.ui.msg.MsgFragment;
import com.snh.snhseller.ui.order.OrderFragment;
import com.snh.snhseller.ui.product.ProductFragment;
import com.snh.snhseller.utils.BadgeUtils;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.SPUtils;
import com.snh.snhseller.utils.StrUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private DialogUtils dialogUtils;
    private String psw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        dialogUtils = new DialogUtils(this);
        setUpViews();
        ButterKnife.bind(this);
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        InitailFragments();
        getCount();
        SPUtils.getInstance(this).saveData(Contans.RED_COUNT,0+"");
        BadgeUtils.setBadgeCount(getApplicationContext(),0, R.mipmap.logo);
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

    public static void setCategories(int categoriesValue){
        Categories = categoriesValue;
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
                    transaction.add(R.id.main_frameLayout, productFragment,"tab1");
                } else {
                    transaction.show(productFragment);
                }
                transaction.commitAllowingStateLoss();
                break;
            case 1:
                iv02.setBackgroundResource(R.drawable.order1);
                if (orderFragment == null) {
                    orderFragment = new OrderFragment();
                    bundle = new Bundle();
                    bundle.putInt("categories",Categories);
                    orderFragment.setArguments(bundle);
                    transaction.add(R.id.main_frameLayout, orderFragment,"tab2");
                } else {
                    if (Categories==101||Categories==102) {
                        if(Categories == 101){
                            orderFragment.tabIndex = 1;
                        }
                        if(Categories == 102){
                            orderFragment.tabIndex = 2;
                        }
                        orderFragment.setType(0);

                    }else if(Categories==201||Categories==203||Categories==204){
                        if(Categories == 204){
                            orderFragment.tabIndex = 2;
                        }
                        orderFragment.setType(1);
                    }else if(Categories==202){
                        orderFragment.tabIndex = 3;
                        orderFragment.setType(2);
                    } else {
                        OrderFragment.updataView(0,1);
                        orderFragment.setType(0);
                    }
                    transaction.show(orderFragment);
                }
                transaction.commitAllowingStateLoss();
                break;
            case 2:
                iv04.setBackgroundResource(R.drawable.chat1);
                if (msgFragment == null) {
                    msgFragment = new MsgFragment();
                    transaction.add(R.id.main_frameLayout, msgFragment,"tab3");
                } else {
                    transaction.show(msgFragment);
                }
                transaction.commitAllowingStateLoss();
                break;
            case 3:
                iv05.setBackgroundResource(R.drawable.home1);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.main_frameLayout, homeFragment,"tab4");
                } else {
                    transaction.show(homeFragment);
                }
                transaction.commitAllowingStateLoss();
                break;
        }

    }

    private static void hideFragments() {
        productFragment = (ProductFragment) fragmentManager.findFragmentByTag("tab1");
        orderFragment = (OrderFragment) fragmentManager.findFragmentByTag("tab2");
        msgFragment = (MsgFragment) fragmentManager.findFragmentByTag("tab3");
        homeFragment = (HomeFragment) fragmentManager.findFragmentByTag("tab4");
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
                ll01.setEnabled(false);
                ll02.setEnabled(true);
                ll03.setEnabled(true);
                ll04.setEnabled(true);
                break;
            case R.id.ll_02:
                setTabSelection(1);
                ll01.setEnabled(true);
                ll02.setEnabled(false);
                ll03.setEnabled(true);
                ll04.setEnabled(true);
                break;
            case R.id.ll_03:
                setTabSelection(2);
                ll01.setEnabled(true);
                ll02.setEnabled(true);
                ll03.setEnabled(false);
                ll04.setEnabled(true);
                break;
            case R.id.ll_04:
                setTabSelection(3);
                ll01.setEnabled(true);
                ll02.setEnabled(true);
                ll03.setEnabled(true);
                ll04.setEnabled(false);
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
                int sumNum2 = model.data.UserDFH+model.data.CKDFH+model.data.JHDSH+model.data.JHDZF+model.data.UserDZF+model.data.CKDZF;
                OrderFragment.UserDFH = model.data.UserDFH;
                OrderFragment.UserDZF = model.data.UserDZF;
                OrderFragment.CKDFH = model.data.CKDFH;
                OrderFragment.CKDZF = model.data.CKDZF;
                OrderFragment.JHDZF = model.data.JHDZF;
                OrderFragment.JHDSH = model.data.JHDSH;
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
        ll01.setEnabled(true);
        ll02.setEnabled(true);
        ll03.setEnabled(true);
        ll04.setEnabled(true);
        if(SPUtils.getInstance(this).getBoolean(Contans.IS_REGIST)&&!StrUtils.isEmpty(SPUtils.getInstance(this).getString(Contans.PSW))){
            psw = SPUtils.getInstance(this).getString(Contans.PSW);
            String str = com.snh.library_base.db.DBManager.getInstance(this).getUserInfo().getContactsTel();
            String psw1 ="snh"+ str.substring(str.length()-4,str.length());
            if (!StrUtils.isEmpty(psw)&&psw.equals(psw1)) {
                dialogUtils.twoBtnDialog("当前密码为默认密码，为了您的账户安全，是否前去修改密码？", new DialogUtils.ChoseClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        dialogUtils.dismissDialog();
                        SPUtils.getInstance(MainActivity.this).savaBoolean(Contans.IS_REGIST,false).commit();
                        bundle = new Bundle();
                        bundle.putString("psw",psw);
                        JumpUtils.dataJump(MainActivity.this,ChangePswActivity.class,bundle,false);
                    }

                    @Override
                    public void onCancelClick(View v) {
                        dialogUtils.dismissDialog();
                        SPUtils.getInstance(MainActivity.this).savaBoolean(Contans.IS_REGIST,false).commit();
                    }
                },true);
            }
        }

    }

    private long lastClickTime = 0;
    private static final int MIN_DELAY_TIME = 500;  // 两次点击间隔不能少于1000ms
    public boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

}
