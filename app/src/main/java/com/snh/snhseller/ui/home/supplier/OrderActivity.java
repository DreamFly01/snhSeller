package com.snh.snhseller.ui.home.supplier;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.ui.order.OrderListFragment;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/11<p>
 * <p>changeTime：2019/3/11<p>
 * <p>version：1<p>
 */
public class OrderActivity extends BaseActivity {
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
    @BindView(R.id.tab_order)
    TabLayout tabOrder;
    @BindView(R.id.tab_vp)
    ViewPager tabVp;

    private String[] titles;
    private List<Fragment> list = new ArrayList<>();
    private Bundle bundle;
    private MyAdapter adapter;
    private List<String> listTab = new ArrayList<>();
    private DialogUtils dialogUtils;
    private int type;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            type = bundle.getInt("type");
        }
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead);
        dialogUtils = new DialogUtils(this);
        setType(type);
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


    private void setType(int orderType) {
        list.clear();

        switch (orderType) {
            case 2:
                heardTitle.setText("出库订单");
                titles = new String[]{"全部", "待支付", "待发货", "待收货", "已完成"};
                for (int i = 0; i < titles.length; i++) {
                    OrderListFragment fragment = new OrderListFragment();
                    bundle = new Bundle();
                    bundle.putInt("orderType", 1);
                    bundle.putInt("type", i);
                    fragment.setArguments(bundle);
                    list.add(fragment);
                }
                break;
            case 1:
                heardTitle.setText("进货订单");
                titles = new String[]{"全部", "待支付", "待发货", "已发货", "已完成"};
                for (int i = 0; i < titles.length; i++) {
                    OrderListFragment fragment = new OrderListFragment();
                    bundle = new Bundle();
                    bundle.putInt("orderType", 2);
                    bundle.putInt("type", i);
                    fragment.setArguments(bundle);
                    list.add(fragment);
                }
                break;
        }
        tabOrder.setTabMode(TabLayout.MODE_SCROLLABLE);
        adapter = new MyAdapter(getSupportFragmentManager());
        tabVp.setAdapter(adapter);
        tabOrder.setupWithViewPager(tabVp);
        for (int i = 0; i < tabOrder.getTabCount(); i++) {
            TabLayout.Tab tab = tabOrder.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
        for (int i = 0; i < listTab.size(); i++) {
            tabOrder.addTab(tabOrder.newTab().setText(listTab.get(i)));
        }

        tabVp.setOffscreenPageLimit(titles.length);
    }

    @OnClick(R.id.heard_back)
    public void onClick() {
        this.finish();
    }

    public class MyAdapter extends FragmentStatePagerAdapter {


        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        public View getTabView(int position) {
            View v = LayoutInflater.from(OrderActivity.this).inflate(R.layout.tab_custom, null);
            TextView mTv_Title = (TextView) v.findViewById(R.id.mTv_Title);
            ImageView mImg = (ImageView) v.findViewById(R.id.mImg);
            mTv_Title.setText(titles[position]);
//            mImg.setImageResource(images[position]);
            //添加一行，设置颜色
            mTv_Title.setTextColor(tabOrder.getTabTextColors());//
            return v;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }
}
