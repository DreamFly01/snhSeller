package com.snh.snhseller.ui.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.OrderSkuAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.OrderDetailBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.home.supplier.PayActivity;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.utils.TimeUtils;
import com.snh.snhseller.wediget.RecycleViewDivider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/9<p>
 * <p>changeTime：2019/3/9<p>
 * <p>version：1<p>
 */
public class OrderDetailsActivity extends BaseActivity {
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
    @BindView(R.id.tv_name_phone)
    TextView tvNamePhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_change_address)
    LinearLayout llChangeAddress;
    @BindView(R.id.iv_shop_logo)
    ImageView ivShopLogo;
    @BindView(R.id.tv_shopName)
    TextView tvShopName;
    @BindView(R.id.iv_product_logo)
    ImageView ivProductLogo;
    @BindView(R.id.tv_product_Name)
    TextView tvProductName;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_pay_method)
    TextView tvPayMethod;
    @BindView(R.id.tv_yunfei)
    TextView tvYunfei;
    @BindView(R.id.tv_order_price)
    TextView tvOrderPrice;
    @BindView(R.id.tv_liuyan)
    TextView tvLiuyan;
    @BindView(R.id.tv_state2)
    TextView tvState2;
    @BindView(R.id.tv_state1)
    TextView tvState1;
    @BindView(R.id.ll_btn)
    LinearLayout llBtn;
    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    @BindView(R.id.tv_creat_time)
    TextView tvCreatTime;
    @BindView(R.id.tv_pay_time)
    TextView tvPayTime;
    @BindView(R.id.tv_cancle_time)
    TextView tvCancleTime;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.tv_send_time)
    TextView tvSendTime;
    @BindView(R.id.tv_deal_time)
    TextView tvDealTime;


    private int type;
    private int orderId;
    private String title;
    private Bundle bundle;
    private int orderType;
    private OrderSkuAdapter adapter;

    private DialogUtils dialogUtils;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_orderdetails_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            type = bundle.getInt("type");
            orderId = bundle.getInt("orderId");
            orderType = bundle.getInt("orderType");
            title = bundle.getString("title");
        }
        dialogUtils = new DialogUtils(this);
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("订单详情");
        IsBang.setImmerHeard(this,rlHead);
        setRecyclerView();
        getData();
    }

    @Override
    public void setUpLisener() {

    }

    private void setRecyclerView() {
        adapter = new OrderSkuAdapter(R.layout.item_sku_order_layout, null);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL, R.drawable.line));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.heard_back, R.id.tv_state2, R.id.tv_state1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.tv_state2:
                break;
            case R.id.tv_state1:
                break;
        }
    }

    private void getData() {
        addSubscription(RequestClient.GetOrderDetail(orderId, orderType, this, new NetSubscriber<BaseResultBean<OrderDetailBean>>(this, true) {
            @Override
            public void onResultNext(BaseResultBean<OrderDetailBean> model) {
                fillView(model.data);
                adapter.setNewData(model.data.NormsList);
            }
        }));
    }

    private void fillView(final OrderDetailBean bean) {

        tvCancleTime.setText(TimeUtils.secToString(bean.DPayPastDue));
        tvNamePhone.setText(bean.CommTenantName + "  " + bean.CommTenantPhone);
        tvAddress.setText(bean.CommTenantAddress);
        ImageUtils.loadUrlImage(this, bean.SupplierIconUrl, ivShopLogo);
        tvShopName.setText(bean.SupplierName);
        ImageUtils.loadUrlImage(this, bean.CommodityIconUrl, ivProductLogo);
        tvProductName.setText(bean.CommodityName);
        tvOrderPrice.setText(bean.OrderSumPrice + "");
        tvLiuyan.setText(bean.CommTenantLeave);
        switch (bean.PayMethod) {
            case 0:
                tvPayMethod.setText("待支付");
                break;
            case 1:
                tvPayMethod.setText("余额支付");
                break;
            case 2:
                tvPayMethod.setText("混合支付（微信+余额）");
                break;
            case 3:
                tvPayMethod.setText("混合支付（支付+余额）");
                break;
            case 4:
                tvPayMethod.setText("微信");
                break;
            case 5:
                tvPayMethod.setText("支付宝");
                break;
            case 6:
                tvPayMethod.setText("货到付款");
                break;
            case 7:
                tvPayMethod.setText("其他");
                break;
        }
        if (bean.Freight == -1) {
            tvYunfei.setText("本地配送");
        } else {
            tvYunfei.setText(bean.Freight + "");
        }
        tvLiuyan.setText(bean.SuppLeave);

        tvOrderNo.setText("订单编号:" + bean.OrderNo);
        tvCreatTime.setText("创建时间：" + bean.CreateTime);
        tvPayTime.setText("付款时间：" + bean.PayTime);

        tvSendTime.setText("发货时间：" + bean.DeliverGoodsTime);
        if (StrUtils.isEmpty(bean.CancelTime)) {
            tvDealTime.setText("完成时间：" + bean.MakeTime);
        } else {
            tvDealTime.setText("关闭时间：" + bean.CancelTime);
        }
        if (orderType == 1) {
            switch (type) {
                case 1:
                    llTime.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    llBtn.setVisibility(View.VISIBLE);
                    tvPayTime.setVisibility(View.VISIBLE);
                    tvState1.setVisibility(View.VISIBLE);
                    tvState1.setText("发货");
                    tvState1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cOrDOrder(orderId,1,"发货成功");
                        }
                    });
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
        }
        if (orderType == 2) {

            switch (type) {
                case 1:
                    llTime.setVisibility(View.VISIBLE);
                    llBtn.setVisibility(View.VISIBLE);
                    tvState1.setVisibility(View.VISIBLE);
                    tvState2.setVisibility(View.VISIBLE);
                    tvState2.setText("取消");
                    tvState1.setText("待支付");
                    tvState1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("orderNo",bean.OrderNo);
                            bundle.putDouble("totalMoney",bean.OrderSumPrice);
                            JumpUtils.dataJump(OrderDetailsActivity.this,PayActivity.class,bundle,true);
                        }
                    });
                    tvState2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelOrder(orderId);
                        }
                    });
                    break;
                case 2:
                    llBtn.setVisibility(View.VISIBLE);
                    tvPayTime.setVisibility(View.VISIBLE);
                    tvState1.setVisibility(View.VISIBLE);
                    tvState1.setText("提醒发货");
                    tvState1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            remindShipment(orderId);
                        }
                    });
                    break;
                case 3:
                    llBtn.setVisibility(View.VISIBLE);
                    tvState1.setVisibility(View.VISIBLE);
                    tvState1.setText("确认收货");
                    tvState1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cOrDOrder(orderId,2,"收货成功");
                        }
                    });
                    break;
                case 4:
                    tvSendTime.setVisibility(View.VISIBLE);
                    tvDealTime.setVisibility(View.VISIBLE);
                    break;
            }
        }

    }

    private void cancelOrder(int orderId){
        RequestClient.CancelOrder(orderId, this, new NetSubscriber<BaseResultBean>(this,true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.simpleDialog("取消成功", new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        OrderDetailsActivity.this.finish();
                    }
                },false);
            }
        });
    }
    private void remindShipment(int orderId){
        RequestClient.Remind(orderId, this, new NetSubscriber<BaseResultBean>(this,true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                Toast.makeText(OrderDetailsActivity.this, "提醒成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //确认收发货
    private void cOrDOrder(int orderId, int type, final String content){
        RequestClient.AffirmSSStates(orderId, type, this, new NetSubscriber<BaseResultBean>(this,true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                Toast.makeText(OrderDetailsActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
