package com.snh.snhseller.ui.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.OrderDetailsAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.OrderDetailsBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.home.supplier.PayActivity;
import com.snh.snhseller.utils.ClipUtils;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.StrUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/1/7<p>
 * <p>changeTime：2019/1/7<p>
 * <p>version：1<p>
 */
public class MyOrderDetailsActivity extends BaseActivity {


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
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone1)
    TextView tvPhone1;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_extra)
    TextView tvExtra;
    @BindView(R.id.tv_integerl)
    TextView tvIntegerl;
    @BindView(R.id.tv_money2)
    TextView tvMoney2;
    @BindView(R.id.tv_tell)
    TextView tvTell;
    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    @BindView(R.id.tv_copy)
    TextView tvCopy;
    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;
    @BindView(R.id.tv_chose1)
    TextView tvChose1;
    @BindView(R.id.tv_chose2)
    TextView tvChose2;
    @BindView(R.id.tv_chose3)
    TextView tvChose3;
    @BindView(R.id.ll_state)
    LinearLayout llState;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.ll_02)
    LinearLayout ll02;
    @BindView(R.id.tv_pay_state)
    TextView tvPayState;
    @BindView(R.id.tv_pay_time)
    TextView tvPayTime;
    @BindView(R.id.tv_01)
    TextView tv01;
    @BindView(R.id.ll_03)
    LinearLayout ll03;
    @BindView(R.id.ll_04)
    LinearLayout ll04;
    private Bundle bundle;
    private String orderid;
    private OrderDetailsAdapter adapter;
    private DialogUtils dialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_myorderdetails_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            orderid = bundle.getString("orderid");
        }
        dialogUtils = new DialogUtils(this);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead);
        heardTitle.setText("订单详情");
    }

    @Override
    public void setUpLisener() {

    }


    @OnClick({R.id.heard_back, R.id.tv_copy, R.id.tv_chose1, R.id.tv_chose2, R.id.tv_chose3, R.id.tv_tell})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.tv_copy:
                ClipUtils.copyText(this, orderDetailsBean.OrderNo, "复制成功");
                break;
            case R.id.tv_chose1:
                dialogUtils.simpleDialog("确定取消订单？", new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
//                        cancelOrder(orderid);
                    }
                }, true);
                break;
            case R.id.tv_chose2:
                bundle = new Bundle();
                bundle.putString("orderid", orderid);
                bundle.putString("TotalMoney", orderDetailsBean.ToTalOrderNo);
                bundle.putString("Integral", orderDetailsBean.Integral + "");
                bundle.putString("Balance", orderDetailsBean.BalanceOne + "");
                JumpUtils.dataJump(this, PayActivity.class, bundle, false);
                break;
            case R.id.tv_chose3:
                ShareWechat("好友代付", orderDetailsBean.FriendPayUrl);
                break;
            case R.id.tv_tell:
                if (StrUtils.isEmpty(orderDetailsBean.ServiceTel)) {
                    dialogUtils.noBtnDialog("商家未留联系号码");
                } else {
                    Intent myIntentDial = new Intent("android.intent.action.CALL", Uri.parse("tel:" + orderDetailsBean.ServiceTel));
                    startActivity(myIntentDial);
                }
                break;
        }
    }

    OrderDetailsBean orderDetailsBean;

    @Override
    public void getDataOnCreate() {
        super.getDataOnCreate();
        addSubscription(RequestClient.GetMyOrderDetail(orderid, this, new NetSubscriber<BaseResultBean<OrderDetailsBean>>(MyOrderDetailsActivity.this, true) {
            @Override
            public void onResultNext(BaseResultBean<OrderDetailsBean> model) {
                orderDetailsBean = model.data;
                fillView(model.data);
            }
        }));
    }

    private void fillView(OrderDetailsBean bean) {
        if ("6".equals(bean.ShopType)) {
            llAddress.setVisibility(View.GONE);
            tvExtra.setText("到店自取");
        } else {
            llAddress.setVisibility(View.VISIBLE);
            if (bean.ExpressPrice > 0) {

                tvExtra.setText("￥" + bean.ExpressPrice + "");
            } else {
                tvExtra.setText("包邮");
            }
        }
        if (bean.OrderState == 0 | bean.OrderState == 1) {
            ll01.setVisibility(View.GONE);
            ll03.setVisibility(View.GONE);
            ll04.setVisibility(View.GONE);
            tv01.setText("待付款");
        } else {
            tv01.setText("实付款");
        }
        switch (bean.PayType) {
            //微信支付
            case 1:
                tvPayState.setText("微信支付");
                break;
            //余额支付
            case 2:
                tvPayState.setText("余额支付");
                break;
            //混合支付
            case 5:
                tvPayState.setText("混合支付(微信+余额)");
                break;
        }
        tvPayTime.setText(bean.PayTime);
        tvAddress.setText(bean.ReceiverAddress);
        tvIntegerl.setText("￥" + bean.Integral / 100 + "");
        tvMoney.setText("￥" + StrUtils.moenyToDH(bean.TotalPrice + ""));
        tvMoney2.setText("￥" + StrUtils.moenyToDH((bean.TotalPrice) + ""));
        tvName.setText(bean.ReceiverName);
        tvPhone1.setText(bean.ReceiverTelPhone);
        tvOrderNo.setText(bean.OrderNo);
        tvOrderTime.setText(bean.CreateTime);
        adapter = new OrderDetailsAdapter(this, R.layout.item_myorder_item_layout, bean.goodslist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                bundle = new Bundle();
//                bundle.putInt("goodsId", bean.goodslist.get(position).GoodsId);
//                JumpUtils.dataJump(MyOrderDetailsActivity.this, ProductDetailsActivity.class, bundle, false);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    //好友代付
    private void ShareWechat(String title, String url) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setText(getString(R.string.app_name));
        sp.setUrl(url);
        sp.setTitle(title);
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.share(sp);
    }

    //取消订单
//    private void cancelOrder(String orderid) {
//        RequestClient.CancelOrder(orderid, this, new NetSubscriber<BaseResultBean<List<OrderDataBean>>>(MyOrderDetailsActivity.this, true) {
//            @Override
//            public void onResultNext(BaseResultBean<List<OrderDataBean>> model) {
//                ToastUtil.shortShow("取消成功~");
//                dialogUtils.dismissDialog();
//                MyOrderDetailsActivity.this.finish();
//            }
//        });
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
