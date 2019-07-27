package com.snh.snhseller.ui.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseFragment;
import com.snh.snhseller.MainActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.NoticeNumBean;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.JumpUtils;

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
    Unbinder unbinder;

    @BindView(R.id.rl_head)
    LinearLayout rlHead;

    private static String[] titles;
    private static List<Fragment> list = new ArrayList<>();
    private static Bundle bundle;
    private static MyAdapter adapter;
    private static List<String> listTab = new ArrayList<>();
    static TabLayout tabOrder;
    static ViewPager tabVp;
    static RelativeLayout rlState;
    static TextView tvState;
    static TextView tv01;
    static LinearLayout ll01;
    static TextView tv02;
    static LinearLayout ll02;

    static RelativeLayout rlSearch;
    private int[] viewLocation = new int[2];
    private DialogUtils dialogUtils;

    private static List<String> data1 = new ArrayList<>();
    private static List<Integer> data2 = new ArrayList<>();

    private int categories = 0;
    static TextPaint paint1, paint2;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categories = getArguments().getInt("categories");
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_order_fragment;
    }

    @Override
    public void setUpViews(View view) {
//        IsBang.setImmerHeard(getContext(), rlHead);
//        ImmersionBar.setTitleBar(getActivity(), rlHead);
        ImmersionBar.with(getActivity()).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        dialogUtils = new DialogUtils(getContext());
        tabVp = view.findViewById(R.id.tab_vp);
        tabOrder = view.findViewById(R.id.tab_order);
        rlState = view.findViewById(R.id.rl_state);
        tvState = view.findViewById(R.id.tv_state);
        rlSearch = view.findViewById(R.id.rl_search);
        tv01 = view.findViewById(R.id.tv_01);
        tv02 = view.findViewById(R.id.tv_02);
        ll01 = view.findViewById(R.id.ll_01);
        ll02 = view.findViewById(R.id.ll_02);
        paint1 = tv01.getPaint();
        paint2 = tv02.getPaint();
        paint1.setFakeBoldText(true);
        paint2.setFakeBoldText(false);
        getCount();
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

    static int type = 0;

    @OnClick({R.id.ll_01, R.id.ll_02, R.id.rl_state,R.id.rl_search})
    public void onClick(View view) {
        if (isFastClick()) {
            return;
        }
        switch (view.getId()) {

            case R.id.ll_01:
                type = 0;

                setType(0);
                break;
            case R.id.ll_02:

                type = 1;
                setType(1);
                break;
            case R.id.rl_state:
                if (type == 1) {
                    type = 2;
                    setType(2);
                } else {
                    type = 1;
                    setType(1);
                }
                break;
            case R.id.rl_search:
                    bundle= new Bundle();
                if(type == 0){
                    JumpUtils.simpJump(getActivity(),BToCOrderAllActivity.class,false);
                }else {
                    JumpUtils.simpJump(getActivity(),BToBOrderAllActivity.class,false);
                }
                break;
        }
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
            TextView tv_num = (TextView) v.findViewById(R.id.tv_num);
            mTv_Title.setText(titles[position]);
//            mImg.setImageResource(images[position]);
            //添加一行，设置颜色
            mTv_Title.setTextColor(tabOrder.getTabTextColors());//

            tv_num.setVisibility(View.GONE);
            switch (type) {
                case 0:
                    switch (position) {
                        case 1:
                            tv_num.setVisibility(View.VISIBLE);
                            if (UserDZF > 99) {
                                tv_num.setText("99+");
                            } else if (UserDZF <= 0) {
                                tv_num.setVisibility(View.GONE);
                            } else {
                                tv_num.setText(UserDZF + "");
                            }
                            break;
                        case 2:
                            tv_num.setVisibility(View.VISIBLE);
                            if (UserDFH > 99) {
                                tv_num.setText("99+");
                            } else if (UserDFH <= 0) {
                                tv_num.setVisibility(View.GONE);
                            } else {
                                tv_num.setText(UserDFH + "");
                            }
                            break;
                    }
                    break;
                case 1:
                    switch (position) {
                        case 1:
                            tv_num.setVisibility(View.VISIBLE);
                            if (CKDZF > 99) {
                                tv_num.setText("99+");
                            } else if (CKDZF <= 0) {
                                tv_num.setVisibility(View.GONE);
                            } else {
                                tv_num.setText(CKDZF + "");
                            }
                            break;
                        case 2:
                            tv_num.setVisibility(View.VISIBLE);
                            if (CKDFH > 99) {
                                tv_num.setText("99+");
                            } else if (CKDFH <= 0) {
                                tv_num.setVisibility(View.GONE);
                            } else {
                                tv_num.setText(CKDFH + "");
                            }
                            break;
                    }
                    break;
                case 2:
                    switch (position) {
                        case 1:
                            tv_num.setVisibility(View.VISIBLE);
                            if (JHDZF > 99) {
                                tv_num.setText("99+");
                            } else if (JHDZF <= 0) {
                                tv_num.setVisibility(View.GONE);
                            } else {
                                tv_num.setText(JHDZF + "");
                            }
                            break;
                        case 3:
                            tv_num.setVisibility(View.VISIBLE);
                            if (JHDSH > 99) {
                                tv_num.setText("99+");
                            } else if (JHDSH <= 0) {
                                tv_num.setVisibility(View.GONE);
                            } else {
                                tv_num.setText(JHDSH + "");
                            }
                            break;
                    }
                    break;
            }

            return v;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

    public static int UserDFH;
    public static int UserDZF;
    public static int CKDZF;
    public static int CKDFH;
    public static int JHDZF;
    public static int JHDSH;
    public static int sunNum;
    public static int tabIndex = 0;

    public static void setType(int orderType) {
        list.clear();

        switch (orderType) {
            case 0:
                type= 0;
                paint1.setFakeBoldText(true);
                paint2.setFakeBoldText(false);
                rlSearch.setVisibility(View.VISIBLE);
                ll01.setEnabled(false);
                ll02.setEnabled(true);

                tv02.setTextColor(Color.parseColor("#6E6E6E"));
                tv01.setTextColor(Color.parseColor("#1e1e1e"));
//                heardTitle.setText("用户订单");
                titles = new String[]{"全部", "待支付", "待备货", "退款中", "已备货", "已完成"};
//                iv01.setVisibility(View.VISIBLE);
//                iv02.setVisibility(View.INVISIBLE);
//                iv03.setVisibility(View.INVISIBLE);
                rlState.setVisibility(View.GONE);
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
//                heardTitle.setText("出库订单");
//                iv01.setVisibility(View.INVISIBLE);
//                iv02.setVisibility(View.VISIBLE);
//                iv03.setVisibility(View.INVISIBLE);
                type=1;
                paint1.setFakeBoldText(false);
                paint2.setFakeBoldText(true);
                rlSearch.setVisibility(View.GONE);
                ll01.setEnabled(true);
                ll02.setEnabled(false);
                tv01.setTextColor(Color.parseColor("#6E6E6E"));
                tv02.setTextColor(Color.parseColor("#1e1e1e"));
                rlState.setVisibility(View.VISIBLE);
                tvState.setText("出库");
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
            case 2:
                type=2;
                rlSearch.setVisibility(View.GONE);
//                heardTitle.setText("进货订单");
//                iv01.setVisibility(View.INVISIBLE);
//                iv02.setVisibility(View.INVISIBLE);
//                iv03.setVisibility(View.VISIBLE);
                rlState.setVisibility(View.VISIBLE);
                tvState.setText("进货");
                titles = new String[]{"全部", "待支付", "待发货", "待收货", "已完成"};
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
        if (tabIndex != 0) {
            tabOrder.getTabAt(tabIndex).select();
        }
    }


    private void getCount() {
        RequestClient.GetSupplierNoticeUnreadCount(getContext(), new NetSubscriber<BaseResultBean<NoticeNumBean>>(getContext()) {
            @Override
            public void onResultNext(BaseResultBean<NoticeNumBean> model) {
                UserDFH = model.data.UserDFH;
                UserDZF = model.data.UserDZF;
                CKDFH = model.data.CKDFH;
                CKDZF = model.data.CKDZF;
                JHDZF = model.data.JHDZF;
                JHDSH = model.data.JHDSH;
                adapter = new MyAdapter(getChildFragmentManager());
                if (categories == 101 || categories == 102) {
                    setType(0);
                } else if (categories == 201 || categories == 203 || categories == 204) {
                    setType(1);
                } else if (categories == 202) {
                    setType(2);
                } else {
                    setType(0);
                }

            }
        });
    }

    public static void updataView(int type, int position) {
        try {
            View v = tabOrder.getTabAt(position).getCustomView();
            TextView tv_num = (TextView) v.findViewById(R.id.tv_num);

            switch (type) {
                case 0:
                    switch (position) {
                        case 1:
                            tv_num.setVisibility(View.VISIBLE);
                            if (UserDZF > 99) {
                                tv_num.setText("99+");
                            } else if (UserDZF <= 0) {
                                tv_num.setVisibility(View.GONE);
                            } else {
                                tv_num.setText(UserDZF + "");
                            }
                            break;
                        case 2:
                            tv_num.setVisibility(View.VISIBLE);
                            if (UserDFH > 99) {
                                tv_num.setText("99+");
                            } else if (UserDFH <= 0) {
                                tv_num.setVisibility(View.GONE);
                            } else {
                                tv_num.setText(UserDFH + "");
                            }
                            break;
                    }
                    break;
                case 1:
                    switch (position) {
                        case 1:
                            tv_num.setVisibility(View.VISIBLE);
                            if (CKDZF > 99) {
                                tv_num.setText("99+");
                            } else if (CKDZF <= 0) {
                                tv_num.setVisibility(View.GONE);
                            } else {
                                tv_num.setText(CKDZF + "");
                            }
                            break;
                        case 2:

                            tv_num.setVisibility(View.VISIBLE);
                            if (CKDFH > 99) {
                                tv_num.setText("99+");
                            } else if (CKDFH <= 0) {
                                tv_num.setVisibility(View.GONE);
                            } else {
                                tv_num.setText(CKDFH + "");
                            }
                            break;
                    }
                    break;
                case 2:
                    switch (position) {
                        case 1:

                            tv_num.setVisibility(View.VISIBLE);
                            if (JHDZF > 99) {
                                tv_num.setText("99+");
                            } else if (JHDZF <= 0) {
                                tv_num.setVisibility(View.GONE);
                            } else {
                                tv_num.setText(JHDZF + "");
                            }
                            break;
                        case 3:

                            tv_num.setVisibility(View.VISIBLE);
                            if (JHDSH > 99) {
                                tv_num.setText("99+");
                            } else if (JHDSH <= 0) {
                                tv_num.setVisibility(View.GONE);
                            } else {
                                tv_num.setText(JHDSH + "");
                            }
                            break;
                    }
                    break;
            }


            int sunNum1 = UserDFH + CKDFH + JHDSH + JHDZF + UserDZF + CKDZF;
            if (sunNum1 > 99) {
                MainActivity.tvNum2.setText("99+");
            } else if (sunNum1 <= 0) {
                MainActivity.tvNum2.setVisibility(View.GONE);
            } else {
                MainActivity.tvNum2.setText(sunNum1 + "");
            }
        } catch (Exception e) {

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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            ImmersionBar.with(getActivity()).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        }
    }
}
