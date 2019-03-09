package com.snh.snhseller.ui.order;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.OrderDetailBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.ImageUtils;

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

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_orderdetails_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            type = bundle.getInt("type");
            orderId = bundle.getInt("orderId");
            orderType = bundle.getInt("orderType");
        }
    }

    @Override
    public void setUpViews() {
        heardTitle.setText(title);
        getData();
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
        addSubscription(RequestClient.GetOrderDetail(orderId, type, this, new NetSubscriber<BaseResultBean<OrderDetailBean>>(this, true) {
            @Override
            public void onResultNext(BaseResultBean<OrderDetailBean> model) {
                fillView(model.data);
            }
        }));
    }

    private void fillView(OrderDetailBean bean) {
        tvNamePhone.setText(bean.CommTenantName + "  " + bean.CommTenantPhone);
        tvAddress.setText(bean.CommTenantAddress);
        ImageUtils.loadUrlImage(this, bean.SupplierIconUrl, ivShopLogo);
        tvShopName.setText(bean.SupplierName);
        ImageUtils.loadUrlImage(this, bean.CommodityIconUrl, ivProductLogo);
        tvProductName.setText(bean.CommodityName);
        switch (bean.PayMethod) {
            case 1:
                tvPayMethod.setText("微信支付");
                break;
            case 2:
                tvPayMethod.setText("支付宝支付");
                break;
            case 3:
                tvPayMethod.setText("银联支付");
                break;
            case 4:
                tvPayMethod.setText("货到付款");
                break;
            case 5:
                tvPayMethod.setText("其他");
                break;
        }
        if (bean.Freight == -1) {
            tvYunfei.setText("本地配送");
        } else {
            tvYunfei.setText(bean.Freight + "");
        }
        tvLiuyan.setText(bean.SuppLeave);

        tvOrderNo.setText("订单编号" + bean.OrderNo);
        tvCreatTime.setText("创建时间：" + bean.CreateTime);
        tvPayTime.setText("付款时间：" + bean.PayTime);
        tvSendTime.setText("发货时间：" + bean.DeliverGoodsTime);
        tvDealTime.setText("完成时间：" + bean.MakeTime);
        tvCancleTime.setText(bean.CancelTime+"");
        if (type == 1) {
            switch (orderType)
            {
                case 1:
                    llTime.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    llBtn.setVisibility(View.VISIBLE);
                    tvPayTime.setVisibility(View.VISIBLE);
                    tvState1.setVisibility(View.VISIBLE);
                    tvState1.setText("发货");
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
        }
        if (type == 2) {

            switch (orderType) {
                case 1:
                    llTime.setVisibility(View.VISIBLE);
                    llBtn.setVisibility(View.VISIBLE);
                    tvState1.setVisibility(View.VISIBLE);
                    tvState2.setVisibility(View.VISIBLE);
                    tvState1.setText("取消");
                    tvState2.setText("待支付");
                    break;
                case 2:
                    llBtn.setVisibility(View.VISIBLE);

                    tvPayTime.setVisibility(View.VISIBLE);
                    llTime.setVisibility(View.VISIBLE);
                    tvState1.setVisibility(View.VISIBLE);
                    tvState1.setText("提醒发货");
                    break;
                case 3:
                    llBtn.setVisibility(View.VISIBLE);

                    tvState1.setVisibility(View.VISIBLE);
                    tvState1.setText("确认收货");
                    break;
                case 4:
                    tvSendTime.setVisibility(View.VISIBLE);
                    tvDealTime.setVisibility(View.VISIBLE);
                    break;
            }
        }

    }
}
