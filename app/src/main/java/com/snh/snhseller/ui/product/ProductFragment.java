package com.snh.snhseller.ui.product;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.snh.snhseller.BaseFragment;
import com.snh.snhseller.R;
import com.snh.snhseller.utils.DBManager;
import com.snh.snhseller.utils.IsBang;
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
 * <p>creatTime：2019/2/22<p>
 * <p>changeTime：2019/2/22<p>
 * <p>version：1<p>
 */
public class ProductFragment extends BaseFragment {

    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.rl_head1)
    LinearLayout rlHead;
    @BindView(R.id.tab_order)
    TabLayout tabOrder;
    @BindView(R.id.tab_vp)
    ViewPager tabVp;
    Unbinder unbinder;
    @BindView(R.id.fab)
    ImageView fab;
    private String[] titles = {"出售中", "审核中", "已下架"};
    private List<Fragment> list = new ArrayList<>();
    private Bundle bundle;
    private MyAdapter adapter;
    private List<String> listTab = new ArrayList<>();

    @Override
    public int initContentView() {
        return R.layout.fragment_product_layout;
    }

    @Override
    public void setUpViews(View view) {
        IsBang.setImmerHeard(getContext(), rlHead);
        ImmersionBar.setTitleBar(getActivity(), rlHead);
        heardTitle.setText("商品");
        heardBack.setVisibility(View.GONE);
        for (int i = 0; i < titles.length; i++) {
            ProductListFragment fragment = new ProductListFragment();
            bundle = new Bundle();
            switch (i) {
                case 0:
                    bundle.putInt("type", 1);
                    break;
                case 1:
                    bundle.putInt("type", 3);
                    break;
                case 2:
                    bundle.putInt("type", 2);
                    break;
            }

            fragment.setArguments(bundle);
            list.add(fragment);
        }
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

        tabOrder.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tabOrder, 20, 20);
            }
        });
        tabVp.setOffscreenPageLimit(titles.length);
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

    @OnClick({R.id.fab, R.id.heard_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                if (DBManager.getInstance(getContext()).getUserInfo().suppType.equals("商超士多")) {
                    bundle = new Bundle();
                    bundle.putInt("type", 1);
                    JumpUtils.dataJump(getActivity(), EditProductActivity.class, bundle, false);
                }else {
                    Toast.makeText(getContext(),"请前往电脑端添加",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.heard_menu:
                break;
        }
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
            return list.size();
        }

        public View getTabView(int position) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.tab_custom, null);
            TextView mTv_Title = (TextView) v.findViewById(R.id.mTv_Title);
            ImageView mImg = (ImageView) v.findViewById(R.id.mImg);
            mTv_Title.setText(titles[position]);
            mTv_Title.setTextColor(tabOrder.getTabTextColors());//
            return v;
        }

    }

}
