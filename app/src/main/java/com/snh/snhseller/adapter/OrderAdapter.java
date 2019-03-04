package com.snh.snhseller.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.OrderBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.order.SendActivity;
import com.snh.snhseller.utils.DBManager;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.JumpUtils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/21<p>
 * <p>changeTime：2019/2/21<p>
 * <p>version：1<p>
 */
public class OrderAdapter extends BaseQuickAdapter<OrderBean, BaseViewHolder> {
    private Map<String, Object> map;

    public OrderAdapter(int layoutResId, @Nullable List<OrderBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OrderBean item) {
        helper.setText(R.id.tv_shopName, item.UserName);
        helper.setText(R.id.tv_all_num, "共" + item.OrderGoodsList.size() + "件商品 合计：");
        double totalMoney = 0;
        for (int i = 0; i < item.OrderGoodsList.size(); i++) {
            totalMoney = totalMoney + item.OrderGoodsList.get(i).Price;
        }
        helper.setText(R.id.tv_TotalMoney1, "￥" + totalMoney);
        if (item.Freight > 0) {
            helper.setText(R.id.tv_youfei, "（含运费：￥" + item.Freight + "）");
        } else {
            helper.setText(R.id.tv_youfei, "（包邮）");
        }
        switch (item.OrderStates) {
            case 0:
                helper.setText(R.id.tv_state, "待支付");
                helper.getView(R.id.tv_state1).setVisibility(View.GONE);
                break;
            case 1:
                helper.getView(R.id.tv_state1).setVisibility(View.GONE);
                break;
            case 2:
                helper.setText(R.id.tv_state, "待发货");
                helper.getView(R.id.tv_state1).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_state1, "发货");
                break;
            case 3:
                helper.setText(R.id.tv_state, "已发货");
                helper.getView(R.id.tv_state1).setVisibility(View.GONE);
                break;
            case 4:
                helper.setText(R.id.tv_state, "已完成");
                helper.getView(R.id.tv_state1).setVisibility(View.GONE);
                break;
            case 5:
                helper.setText(R.id.tv_state, "退款中");
                helper.getView(R.id.tv_state1).setVisibility(View.GONE);
                break;
        }
        ImageView logo = helper.getView(R.id.iv_shop_logo);
        ImageUtils.loadUrlImage(mContext, item.UserIcon, logo);
        OrderItemAdapter adapter = new OrderItemAdapter(R.layout.item_order_item_layout, null);
        if (item.OrderStates > 1) {
            adapter.setType(item.OrderStates);
        } else {
            adapter.setType(0);
        }
        RecyclerView recyclerView = helper.getView(R.id.recyclerView_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
        for (int i = 0; i < item.OrderGoodsList.size(); i++) {
            item.OrderGoodsList.get(i).shopLogo = item.UserIcon;
            item.OrderGoodsList.get(i).shopName = item.UserName;
            item.OrderGoodsList.get(i).state = "待支付";
            item.OrderGoodsList.get(i).Freight = item.Freight;
            item.OrderGoodsList.get(i).OrderId = item.OrderId;
        }
        helper.getView(R.id.tv_state1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DBManager.getInstance(mContext).getUserInfo().suppType.equals("商超士多")) {
                    setData(item);
                    commit(helper.getPosition());
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("data", item);
                    JumpUtils.dataJump((Activity) mContext, SendActivity.class, bundle, false);

                }
            }
        });
        adapter.setNewData(item.OrderGoodsList);
    }

    private void setData(OrderBean bean) {

        map = new TreeMap<>();
        map.put("OrderId", bean.OrderId);
        map.put("UserId", bean.UserId);
        map.put("SupplierId", bean.SupplierId);
//        map.put("UserName", bean.ReceivingName);
//        map.put("PhoneNumber", bean.ReceivingPhone);
//        map.put("Address", bean.ReceivingAddress);
    }

    private void commit(final int positon) {
        RequestClient.ConfirmShipment(map, mContext, new NetSubscriber<BaseResultBean>(mContext, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                mData.remove(positon);
                Toast.makeText(mContext, "发货成功", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
    }

}
