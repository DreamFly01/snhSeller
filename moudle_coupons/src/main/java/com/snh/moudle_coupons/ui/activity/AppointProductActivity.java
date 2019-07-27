package com.snh.moudle_coupons.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snh.library_base.BaseActivity;
import com.snh.library_base.wediget.RecycleViewDivider;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.moudle_coupons.R;
import com.snh.moudle_coupons.R2;
import com.snh.moudle_coupons.adapter.MyAppointProductAdapter;
import com.snh.moudle_coupons.bean.CouponsProductIdBean;
import com.snh.moudle_coupons.netapi.RequestClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/6/4<p>
 * <p>changeTime：2019/6/4<p>
 * <p>version：1<p>
 */
public class AppointProductActivity extends BaseActivity {
    @BindView(R2.id.heard_back)
    LinearLayout heardBack;
    @BindView(R2.id.heard_title)
    TextView heardTitle;
    @BindView(R2.id.heard_menu)
    ImageView heardMenu;
    @BindView(R2.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R2.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R2.id.rl_head)
    LinearLayout rlHead;
    @BindView(R2.id.coupons_rcl)
    RecyclerView couponsRcl;

    private MyAppointProductAdapter adapter;
    private Bundle bundle;
    private int couponsId;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.coupons_activity_appointproduct_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            couponsId = bundle.getInt("couponsId");
        }
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("查看指定产品");
        setRecyclerView();
        getData();
    }

    private void setRecyclerView() {
        adapter = new MyAppointProductAdapter(R.layout.coupons_item_myapponitproduct_layout, null);
        couponsRcl.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL, R.drawable.line1));
        couponsRcl.setLayoutManager(new LinearLayoutManager(this));
        couponsRcl.setAdapter(adapter);
    }

    @Override
    public void setUpLisener() {

    }

    private void getData() {
        addSubscription(RequestClient.GetCouponsGoods(couponsId, this, new NetSubscriber<BaseResultBean<List<CouponsProductIdBean>>>(this, true) {
            @Override
            public void onResultNext(BaseResultBean<List<CouponsProductIdBean>> model) {
                if (model.data.size() > 0) {

                    adapter.setNewData(model.data);
                } else {
                    adapter.setEmptyView(R.layout.empty_layout, couponsRcl);
                }
            }
        }));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R2.id.heard_back)
    public void onClick(View view) {
        if(view.getId()==R.id.heard_back){
            this.finish();
        }
    }
}
