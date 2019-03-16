package com.snh.snhseller.ui.product;

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
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：商家商品<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/11<p>
 * <p>changeTime：2019/3/11<p>
 * <p>version：1<p>
 */
public class BusinessProductActivity extends BaseActivity {
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


    private String[] titles = {"全部", "无规格", "有规格"};
//    private List<Fragment> list = new ArrayList<>();
    private Bundle bundle;
    private MyAdapter adapter;
    private List<String> listTab = new ArrayList<>();

    private DialogUtils dialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_businessproduct_layout);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead);
        heardTitle.setText("批发商品");

//        for (int i = 0; i < titles.length; i++) {
////            ProductListFragment fragment = new ProductListFragment();
//
//            list.add(fragment);
//        }
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
                    setIndicator(tabOrder, 20, 20);
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
            BusinessProductFragment fragment = new BusinessProductFragment();
            bundle = new Bundle();
            if(position == 1){

            bundle.putInt("type", 2);
            }
            if(position == 2){
                bundle.putInt("type", 1);
            }
            if(position == 0){
                bundle.putInt("type", 0);
            }
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        public View getTabView(int position) {
            View v = LayoutInflater.from(BusinessProductActivity.this).inflate(R.layout.tab_custom, null);
            TextView mTv_Title = (TextView) v.findViewById(R.id.mTv_Title);
            ImageView mImg = (ImageView) v.findViewById(R.id.mImg);
            mTv_Title.setText(titles[position]);
            mTv_Title.setTextColor(tabOrder.getTabTextColors());//
            return v;
        }
        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }

    }
}
