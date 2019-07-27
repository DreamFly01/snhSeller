package com.snh.moudle_coupons.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.snh.library_base.BaseActivity;
import com.snh.library_base.router.RouterActivityPath;
import com.snh.library_base.utils.ToastUtils;
import com.snh.library_base.wediget.RecycleViewDivider;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.moudle_coupons.R;
import com.snh.moudle_coupons.R2;
import com.snh.moudle_coupons.adapter.AvailableCouponsAdapter;
import com.snh.moudle_coupons.bean.SupplierCouponBean;
import com.snh.moudle_coupons.netapi.RequestClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/6/5<p>
 * <p>changeTime：2019/6/5<p>
 * <p>version：1<p>
 */
@Route(path = RouterActivityPath.Coupons.PATH_AVAILABLE_COUPONS)
public class AvailableCouponsActivity extends BaseActivity {
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

    private AvailableCouponsAdapter adapter;

    @Autowired(name = "id")
    int merchantsId;
    @Autowired(name = "name")
    String goodsName;
    @Autowired(name = "money")
    double money;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.coupons_activity_availablecoupons_layout);
        ARouter.getInstance().inject(this);
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("选择优惠劵");
        setRecycleView();
        getData();
    }

    private void setRecycleView() {
        adapter = new AvailableCouponsAdapter(R.layout.coupons_item_available_coupons_layout, null);
        couponsRcl.setLayoutManager(new LinearLayoutManager(this));
        couponsRcl.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL, R.drawable.line));
        couponsRcl.setAdapter(adapter);
    }

    @Override
    public void setUpLisener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (money>datas.get(position).TotalAmount) {
                    Intent intent = new Intent();
                    intent.putExtra("data", datas.get(position));
                    setResult(100, intent);
                    AvailableCouponsActivity.this.finish();
                }else {
                    ToastUtils.toast("当前购物价值（"+getResources().getString(R.string.moneyStr)+money+"）不能小于等于优惠金额("+getResources().getString(R.string.moneyStr)+datas.get(position).TotalAmount+")！");
                }
            }
        });
    }

    private List<SupplierCouponBean> datas = new ArrayList<>();

    private void getData() {
        addSubscription(RequestClient.GetSupplierCoupon(merchantsId, goodsName, this, new NetSubscriber<BaseResultBean<List<SupplierCouponBean>>>(this, true) {
            @Override
            public void onResultNext(BaseResultBean<List<SupplierCouponBean>> model) {
                if (model.data.size() > 0) {
                    datas = model.data;
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
        if (view.getId() == R.id.heard_back) {
            this.finish();
        }
    }
}
