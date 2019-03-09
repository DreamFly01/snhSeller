package com.snh.snhseller;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.netease.nim.uikit.common.activity.UI;
import com.snh.snhseller.ui.home.HomeFragment;
import com.snh.snhseller.ui.msg.MsgFragment;
import com.snh.snhseller.ui.order.OrderFragment;
import com.snh.snhseller.ui.product.ProductFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends UI {
    @BindView(R.id.main_frameLayout)
    FrameLayout mainFrameLayout;
    @BindView(R.id.radio_chat)
    RadioButton radioChat;
    @BindView(R.id.radio_order)
    RadioButton radioOrder;
    @BindView(R.id.radio_store)
    RadioButton radioStore;
    @BindView(R.id.radio_home)
    RadioButton radioHome;
    @BindView(R.id.radio_group_button)
    RadioGroup radioGroupButton;
    private long mExitTime = 0;
    private Toast toast;
    private Bundle bundle;
    private FragmentManager fragmentManager;
    public static final String SHOW_FRAGMENT_INDEX = "show_fragment_index";
    private int index = -1;
    /**
     * 退出间隔时间 单位毫秒
     */
    public static final int EXIT_TIME = 2000;
    private FragmentTransaction transaction;
    private MsgFragment msgFragment;
    private OrderFragment orderFragment;
    private ProductFragment productFragment;
    private HomeFragment homeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ImmersionBar.with(this).init();
        InitailFragments();
        setUpViews();

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
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent != null) {
            bundle = getIntent().getExtras();
            index = bundle.getInt(SHOW_FRAGMENT_INDEX, 0);
            setTabSelection(index);
        }
    }

    public void setUpViews() {

        Drawable drawable1 = getResources().getDrawable(R.drawable.tab_chat_selector);
        drawable1.setBounds(0, 0, 56, 52);
        Drawable drawable2 = getResources().getDrawable(R.drawable.tab_order_selector);
        drawable2.setBounds(0, 0, 56, 52);
        Drawable drawable3 = getResources().getDrawable(R.drawable.tab_store_selector);
        drawable3.setBounds(0, 0, 56, 52);
        Drawable drawable4 = getResources().getDrawable(R.drawable.tab_home_selector);
        drawable4.setBounds(0, 0, 56, 52);
        radioChat.setCompoundDrawables(null, drawable1, null, null);
        radioOrder.setCompoundDrawables(null, drawable2, null, null);
        radioStore.setCompoundDrawables(null, drawable3, null, null);
        radioHome.setCompoundDrawables(null, drawable4, null, null);
        if (index == -1 | index == 0) {
            radioGroupButton.check(R.id.radio_store);
        }
        radioGroupButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_chat:
                        setTabSelection(2);
                        break;
                    case R.id.radio_order:
                        setTabSelection(1);
                        break;
                    case R.id.radio_store:
                        setTabSelection(0);
                        break;
                    case R.id.radio_home:
                        setTabSelection(3);
                        break;
                }
            }
        });
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

    public void setTabSelection(int index) {
        // 开启一个Fragment事务
        transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments();
        switch (index) {
            case 0:
                if(productFragment == null){
                    productFragment = new ProductFragment();
                    transaction.add(R.id.main_frameLayout,productFragment);
                }else {
                    transaction.show(productFragment);
                }
                break;
            case 1:
                if(orderFragment == null){
                    orderFragment = new OrderFragment();
                    transaction.add(R.id.main_frameLayout,orderFragment);
                }else {
                    transaction.show(orderFragment);
                }
                break;
            case 2:
                if (msgFragment == null) {
                    msgFragment = new MsgFragment();
                    transaction.add(R.id.main_frameLayout, msgFragment);
                }else {
                    transaction.show(msgFragment);
                }

                break;
            case 3:
                if(homeFragment == null){
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.main_frameLayout,homeFragment);
                }else {
                    transaction.show(homeFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void hideFragments() {
        if (msgFragment != null) {
            if (!msgFragment.isHidden()) {
                transaction.hide(msgFragment);
            }
        }
        if(orderFragment !=null){
            if(!orderFragment.isHidden()){
                transaction.hide(orderFragment);
            }
        }
        if(productFragment != null){
            if(!productFragment.isHidden()){
                transaction.hide(productFragment);
            }
        }
        if(homeFragment != null){
            if(!homeFragment.isHidden()){
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
}
