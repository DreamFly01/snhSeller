package com.snh.snhseller.ui.msg;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.snh.snhseller.utils.IsBang;

import java.util.ArrayList;
import java.util.List;

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
public class OrderNoticeActivity extends BaseActivity {
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
    @BindView(R.id.tab_vp)
    ViewPager tabVp;

    private int index = 1;

    private String[] titles = {"零售", "批发"};
    private Bundle bundle;
    private List<String> listTab = new ArrayList<>();
   public  static TabLayout tabOrder;

    private List<Fragment> list = new ArrayList<>();
    public static MyAdapter adapter;

   private static int userOrderNum;
   private static int supplierOrderNum;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_delivery_layout);
        bundle = getIntent().getExtras();
        if(null!=bundle){
            userOrderNum = bundle.getInt("userNum",0);
            supplierOrderNum = bundle.getInt("supplierNum",0);
        }
    }

    @Override
    public void setUpViews() {
        tabOrder = this.findView(R.id.tab_order);
        IsBang.setImmerHeard(this, rlHead);
        heardTitle.setText("订单通知");
        for (int i = 0; i < titles.length; i++) {
            OrderNoticeFragment  orderNoticeFragment = new OrderNoticeFragment();
            bundle = new Bundle();
            bundle.putInt("type",i+1);
            orderNoticeFragment.setArguments(bundle);
            list.add(orderNoticeFragment);
        }
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

        tabOrder.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tabOrder, 40, 40);
            }
        });
        tabVp.setOffscreenPageLimit(titles.length);

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

    public class MyAdapter extends FragmentPagerAdapter {


        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        public View getTabView(int position) {
            View v = LayoutInflater.from(OrderNoticeActivity.this).inflate(R.layout.tab_custom, null);
            TextView mTv_Title = (TextView) v.findViewById(R.id.mTv_Title);
            TextView tv_num = (TextView)v.findViewById(R.id.tv_num);
            ImageView mImg = (ImageView) v.findViewById(R.id.mImg);
            tv_num.setVisibility(View.VISIBLE);
            switch (position)
            {
                case 0:
//                    if(userOrderNum>99){
//                        tv_num.setText("99+");
//                    }else if(userOrderNum<=0){
                        tv_num.setVisibility(View.GONE);
//                    }else {
//                        tv_num.setText(userOrderNum+"");
//                    }
                    break;
                case 1:
                    if(supplierOrderNum>99){
                        tv_num.setText("99+");
                    }else if(supplierOrderNum<=0){
                        tv_num.setVisibility(View.GONE);
                    }else {
                        tv_num.setText(supplierOrderNum+"");
                    }
                    break;
            }
            mTv_Title.setText(titles[position]);
            mTv_Title.setTextColor(tabOrder.getTabTextColors());//
            return v;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }

    }

    public static void updataView(int type, int num){
        View v = tabOrder.getTabAt(type-1).getCustomView();
        TextView tv_num = (TextView)v.findViewById(R.id.tv_num);
        switch (type)
        {
            case 1:
                userOrderNum = userOrderNum-num;
                if(userOrderNum>99){
                    tv_num.setText("99+");
                }else if(userOrderNum<=0){
                    tv_num.setVisibility(View.GONE);
                }else {
                    tv_num.setText(userOrderNum+"");
                }
                break;
            case 2:
                supplierOrderNum = supplierOrderNum-num;
                if(supplierOrderNum>99){
                    tv_num.setText("99+");
                }else if(supplierOrderNum<=0){
                    tv_num.setVisibility(View.GONE);
                }else {
                    tv_num.setText(supplierOrderNum+"");
                }
                break;
        }
    }
}
