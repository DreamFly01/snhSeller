package com.snh.snhseller.ui.salesmanManagement;

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
import com.snh.snhseller.R;
import com.snh.snhseller.ui.salesmanManagement.cardRecord.RecordFragment;
import com.snh.snhseller.ui.salesmanManagement.home.HomeFragment;
import com.snh.snhseller.ui.salesmanManagement.operation.OperationFragment;
import com.snh.snhseller.ui.salesmanManagement.operationList.OperationListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/25<p>
 * <p>changeTime：2019/2/25<p>
 * <p>version：1<p>
 */
public class SalesmanMainActivity extends UI {
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
    private FragmentTransaction transaction;
    /**
     * 退出间隔时间 单位毫秒
     */
    public static final int EXIT_TIME = 2000;

    private OperationFragment operationFragment;
    private HomeFragment homeFragment;
    private OperationListFragment operationListFragment;
    private RecordFragment recordFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(savedInstanceState);
    }

    protected void initContentView(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_salesmain_layout);
        ButterKnife.bind(this);
        ImmersionBar.with(this).titleBar(R.id.rl_head).statusBarColor(R.color.blue).init();
        InitailFragments();
        setUpViews();
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

        Drawable drawable1 = getResources().getDrawable(R.drawable.tab_sale1_selector);
        drawable1.setBounds(0, 0, 70, 70);
        Drawable drawable2 = getResources().getDrawable(R.drawable.tab_sale2_selector);
        drawable2.setBounds(0, 0, 70, 70);
        Drawable drawable3 = getResources().getDrawable(R.drawable.tab_sale3_selector);
        drawable3.setBounds(0, 0, 56, 70);
        Drawable drawable4 = getResources().getDrawable(R.drawable.tab_sale4_selector);
        drawable4.setBounds(0, 0, 56, 70);
        radioChat.setCompoundDrawables(null, drawable1, null, null);
        radioOrder.setCompoundDrawables(null, drawable2, null, null);
        radioStore.setCompoundDrawables(null, drawable3, null, null);
        radioHome.setCompoundDrawables(null, drawable4, null, null);
        if (index == -1 | index == 0) {
            radioGroupButton.check(R.id.radio_chat);
        }
        radioGroupButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_chat:
                        setTabSelection(0);
                        break;
                    case R.id.radio_order:
                        setTabSelection(1);
                        break;
                    case R.id.radio_store:
                        setTabSelection(2);
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
                if (operationFragment == null) {
                    operationFragment = new OperationFragment();
                    transaction.add(R.id.main_frameLayout, operationFragment);
                }else {
                    transaction.show(operationFragment);
                }
                break;
            case 1:
                if (operationListFragment == null) {
                    operationListFragment = new OperationListFragment();
                    transaction.add(R.id.main_frameLayout, operationListFragment);
                }else {
                    transaction.show(operationListFragment);
                }
                break;
            case 2:
                if(recordFragment == null){
                    recordFragment = new RecordFragment();
                    transaction.add(R.id.main_frameLayout,recordFragment);
                }else {
                    transaction.show(recordFragment);
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
        if (operationFragment != null) {
            if (!operationFragment.isHidden()) {
                transaction.hide(operationFragment);
            }
        }
        if(recordFragment !=null){
            if(!recordFragment.isHidden()){
                transaction.hide(recordFragment);
            }
        }
        if(operationListFragment != null){
            if(!operationListFragment.isHidden()){
                transaction.hide(operationListFragment);
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
}
