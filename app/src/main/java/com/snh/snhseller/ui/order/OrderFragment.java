package com.snh.snhseller.ui.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.snh.snhseller.MainActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.NoticeNumBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
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
    Unbinder unbinder;
    @BindView(R.id.tv_01)
    TextView tv01;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.tv_02)
    TextView tv02;
    @BindView(R.id.ll_02)
    LinearLayout ll02;
    @BindView(R.id.tv_03)
    TextView tv03;
    @BindView(R.id.ll_03)
    LinearLayout ll03;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    private static String[] titles;
    private static List<Fragment> list = new ArrayList<>();
    private static Bundle bundle;
    private static MyAdapter adapter;
    private static List<String> listTab = new ArrayList<>();
    static TabLayout tabOrder;
    static ViewPager tabVp;
    static ImageView iv01;
    static ImageView iv02;
    static ImageView iv03;
    private int[] viewLocation = new int[2];
    private DialogUtils dialogUtils;

    private static List<String> data1 = new ArrayList<>();
    private static List<Integer> data2 = new ArrayList<>();

    private int categories = 0;

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
        IsBang.setImmerHeard(getContext(), rlHead);
        ImmersionBar.setTitleBar(getActivity(), rlHead);
        dialogUtils = new DialogUtils(getContext());
        tabVp = view.findViewById(R.id.tab_vp);
        tabOrder = view.findViewById(R.id.tab_order);
        iv01 = view.findViewById(R.id.iv_01);
        iv02 = view.findViewById(R.id.iv_02);
        iv03 = view.findViewById(R.id.iv_03);
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
 int type = 0;
    @OnClick({R.id.ll_01, R.id.ll_02, R.id.ll_03})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_01:
                type = 0;
                setType(0);
                break;
            case R.id.ll_02:
                type = 1;
                setType(1);
                break;
            case R.id.ll_03:
                type = 2;
                setType(2);
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
            TextView tv_num = (TextView)v.findViewById(R.id.tv_num);
            mTv_Title.setText(titles[position]);
//            mImg.setImageResource(images[position]);
            //添加一行，设置颜色
            mTv_Title.setTextColor(tabOrder.getTabTextColors());//

            tv_num.setVisibility(View.GONE);
            switch (type){
                case 0:
                    switch (position)
                    {
                        case 2:
                            tv_num.setVisibility(View.VISIBLE);
                            if(UserDFH>99){
                                tv_num.setText("99+");
                            }else if(UserDFH<=0){
                                tv_num.setVisibility(View.GONE);
                            }else {
                                tv_num.setText(UserDFH+"");
                            }
                            break;
                    }
                    break;
                case 1:
                    switch (position)
                    {
                        case 2:
                            tv_num.setVisibility(View.VISIBLE);
                            if(CKDFH>99){
                                tv_num.setText("99+");
                            }else if(CKDFH<=0){
                                tv_num.setVisibility(View.GONE);
                            }else {
                                tv_num.setText(CKDFH+"");
                            }
                            break;
                    }
                    break;
                case 2:
                    switch (position)
                    {
                        case 1:
                            tv_num.setVisibility(View.VISIBLE);
                            if(JHDZF>99){
                                tv_num.setText("99+");
                            }else if(JHDZF<=0){
                                tv_num.setVisibility(View.GONE);
                            }else {
                                tv_num.setText(JHDZF+"");
                            }
                            break;
                        case 3:
                            tv_num.setVisibility(View.VISIBLE);
                            if(JHDSH>99){
                                tv_num.setText("99+");
                            }else if(JHDSH<=0){
                                tv_num.setVisibility(View.GONE);
                            }else {
                                tv_num.setText(JHDSH+"");
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
    static int UserDFH;
    static int CKDFH;
    static int JHDZF;
    static int JHDSH;
    static int sunNum;
    public static void setType(int orderType) {
        list.clear();

        switch (orderType) {
            case 0:
//                heardTitle.setText("用户订单");
                titles = new String[]{"全部", "待支付", "待发货", "退款中", "待收货", "已完成"};
                iv01.setVisibility(View.VISIBLE);
                iv02.setVisibility(View.INVISIBLE);
                iv03.setVisibility(View.INVISIBLE);
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
                iv01.setVisibility(View.INVISIBLE);
                iv02.setVisibility(View.VISIBLE);
                iv03.setVisibility(View.INVISIBLE);
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
//                heardTitle.setText("进货订单");
                iv01.setVisibility(View.INVISIBLE);
                iv02.setVisibility(View.INVISIBLE);
                iv03.setVisibility(View.VISIBLE);
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
    }


    private void getCount() {
        RequestClient.GetSupplierNoticeUnreadCount(getContext(), new NetSubscriber<BaseResultBean<NoticeNumBean>>(getContext()) {
            @Override
            public void onResultNext(BaseResultBean<NoticeNumBean> model) {
                UserDFH = model.data.UserDFH;
                CKDFH = model.data.CKDFH;
                JHDZF = model.data.JHDZF;
                JHDSH = model.data.JHDSH;
                adapter = new MyAdapter(getChildFragmentManager());
                if (categories==101||categories==102) {
                    setType(0);
                }else if(categories==201||categories==203||categories==204){
                    setType(1);
                }else if(categories==202){
                    setType(2);
                } else {
                    setType(0);
                }

            }
        });
    }

    public static void updataView(int type, int position){
        View v = tabOrder.getTabAt(position).getCustomView();
        TextView tv_num = (TextView)v.findViewById(R.id.tv_num);

        switch (type){
            case 0:
                switch (position)
                {
                    case 2:
                        UserDFH = UserDFH-1;
                        tv_num.setVisibility(View.VISIBLE);
                        if(UserDFH>99){
                            tv_num.setText("99+");
                        }else if(UserDFH<=0){
                            tv_num.setVisibility(View.GONE);
                        }else {
                            tv_num.setText(UserDFH+"");
                        }
                        break;
                }
                break;
            case 1:
                switch (position)
                {
                    case 2:
                        CKDFH = CKDFH-1;
                        tv_num.setVisibility(View.VISIBLE);
                        if(CKDFH>99){
                            tv_num.setText("99+");
                        }else if(CKDFH<=0){
                            tv_num.setVisibility(View.GONE);
                        }else {
                            tv_num.setText(CKDFH+"");
                        }
                        break;
                }
                break;
            case 2:
                switch (position)
                {
                    case 1:
                        JHDZF = JHDZF-1;
                        tv_num.setVisibility(View.VISIBLE);
                        if(JHDZF>99){
                            tv_num.setText("99+");
                        }else if(JHDZF<=0){
                            tv_num.setVisibility(View.GONE);
                        }else {
                            tv_num.setText(JHDZF+"");
                        }
                        break;
                    case 3:
                        JHDSH = JHDSH-1;
                        tv_num.setVisibility(View.VISIBLE);
                        if(JHDSH>99){
                            tv_num.setText("99+");
                        }else if(JHDSH<=0){
                            tv_num.setVisibility(View.GONE);
                        }else {
                            tv_num.setText(JHDSH+"");
                        }
                        break;
                }
                break;
        }


        int sunNum1 = UserDFH+CKDFH+JHDSH+JHDZF;
        MainActivity.tvNum2.setText(sunNum1+"");
    }

}
