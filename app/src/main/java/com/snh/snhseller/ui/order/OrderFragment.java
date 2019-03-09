package com.snh.snhseller.ui.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.snh.snhseller.BaseFragment;
import com.snh.snhseller.R;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/21<p>
 * <p>changeTime：2019/2/21<p>
 * <p>version：1<p>
 */
public class OrderFragment extends BaseFragment {

    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.tab_order)
    TabLayout tabOrder;
    @BindView(R.id.tab_vp)
    ViewPager tabVp;
    Unbinder unbinder;
    private String[] titles;
    private List<Fragment> list = new ArrayList<>();
    private Bundle bundle;
    private MyAdapter adapter;
    private List<String> listTab = new ArrayList<>();

    private int[] viewLocation = new int[2];
    private DialogUtils dialogUtils;

    private List<String> data1 = new ArrayList<>();
    private List<Integer> data2 = new ArrayList<>();
    @Override
    public int initContentView() {
        return R.layout.fragment_order_fragment;
    }

    @Override
    public void setUpViews(View view) {
        IsBang.setImmerHeard(getContext(), rlHead);
        ImmersionBar.setTitleBar(getActivity(), rlHead);

        heardBack.setVisibility(View.GONE);
        heardMenu.setVisibility(View.VISIBLE);
        heardMenu.setBackgroundResource(R.drawable.menu_bg);

        dialogUtils = new DialogUtils(getContext());
        setType(0);

    }

    @Override
    public void setUpLisener() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.heard_menu)
    public void onClick() {
        heardMenu.getLocationInWindow(viewLocation);
        data1.clear();
        data2.clear();
        data1.add("用户订单");
        data1.add("出库订单");
        data1.add("进货订单");
        data2.add(R.drawable.order1_bg);
        data2.add(R.drawable.order2_bg);
        data2.add(R.drawable.order3_bg);
        dialogUtils.customDialog(new DialogUtils.OnItemClick() {
            @Override
            public void onItemClick(View v, int position) {
                setType(position);
                dialogUtils.dismissDialog();
            }
        },viewLocation[1] + heardMenu.getMeasuredHeight() / 2, viewLocation[0], heardMenu.getWidth(), data1,data2);
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
            View v = LayoutInflater.from(getContext()).inflate(R.layout.tab_custom, null);
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

    private void setType(int orderType) {
        list.clear();

        switch (orderType) {
            case 0:
                heardTitle.setText("用户订单");
                titles = new String[]{"全部", "待支付", "待发货", "退款中", "已发货", "已完成"};
                for (int i = 0; i < titles.length; i++) {
                    OrderListFragment fragment = new OrderListFragment();
                    bundle = new Bundle();
                    bundle.putInt("orderType", 0);
                    switch (i) {
                        case 0:
                            bundle.putInt("type", -1);
                            break;
                        case 1:
                            bundle.putInt("type", 0);
                            break;
                        case 2:
                            bundle.putInt("type", 2);
                            break;
                        case 3:
                            bundle.putInt("type", 5);
                            break;
                        case 4:
                            bundle.putInt("type", 3);
                            break;
                        case 5:
                            bundle.putInt("type", 4);
                            break;
                    }

                    fragment.setArguments(bundle);
                    list.add(fragment);
                }
                break;
            case 1:
                heardTitle.setText("出库订单");
                titles = new String[]{"全部", "待支付", "待发货", "已发货", "已完成"};
                for (int i = 0; i < titles.length; i++) {
                    OrderListFragment fragment = new OrderListFragment();
                    bundle = new Bundle();
                    bundle.putInt("orderType", 1);
                    bundle.putInt("type", i);
                    fragment.setArguments(bundle);
                    list.add(fragment);
                }
                break;
            case 2:
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
        adapter = new MyAdapter(getChildFragmentManager());
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
}
