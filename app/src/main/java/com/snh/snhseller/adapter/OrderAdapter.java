package com.snh.snhseller.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.NoticeNumBean;
import com.snh.snhseller.bean.OrderBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.home.supplier.PayActivity;
import com.snh.snhseller.ui.order.FixPriceActivity;
import com.snh.snhseller.ui.order.MyOrderDetailsActivity;
import com.snh.snhseller.ui.order.OrderFragment;
import com.snh.snhseller.ui.order.OrderListFragment;
import com.snh.snhseller.ui.order.SendActivity;
import com.snh.snhseller.db.DBManager;
import com.snh.snhseller.utils.Contans;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.SPUtils;
import com.snh.snhseller.utils.StrUtils;

import java.util.ArrayList;
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
    private int type; //0:我的订单  1：出库订单 2：进货订单

    public OrderAdapter(int layoutResId, @Nullable List<OrderBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OrderBean item) {
        switch (type) {
            case 0:
                helper.getView(R.id.ll_product).setVisibility(View.GONE);
                helper.setText(R.id.tv_shopName, "订单号：" + item.OrderNo);
                helper.setText(R.id.tv_all_num, "共" + item.OrderGoodsList.size() + "件商品 合计：");
                TextView tvState = helper.getView(R.id.tv_state1);

                helper.setText(R.id.tv_TotalMoney1, "¥" + item.OrderPrice);
                if (item.Freight > 0) {
                    helper.setText(R.id.tv_youfei, "（含运费：¥" + StrUtils.moenyToDH(item.Freight + "") + "）");
                } else {
                    if (item.ShopType.equals("6")) {
                        helper.setText(R.id.tv_youfei, "（到店自取）");
                    } else {
                        helper.setText(R.id.tv_youfei, "（包邮）");
                    }
                }
                switch (item.OrderStates) {
                    case 0:
                        if (item.PayStates == 0) {
                            helper.setText(R.id.tv_state, "待支付");
                            helper.getView(R.id.tv_state1).setVisibility(View.VISIBLE);
                            if (item.IsChangePrice == 0) {
                                tvState.setTextColor(Color.parseColor("#F81131"));
                                tvState.setEnabled(true);
                            } else {
                                tvState.setTextColor(Color.parseColor("#6E6E6E"));
                                tvState.setEnabled(false);
                            }
                        }
                        if (item.PayStates == 1) {
                            helper.setText(R.id.tv_state, "已支付");
                            helper.getView(R.id.tv_state1).setVisibility(View.GONE);
                        }
                        break;
                    case 1:
                        helper.setText(R.id.tv_state, "已取消");
                        helper.getView(R.id.tv_state1).setVisibility(View.GONE);
                        break;
                    case 2:
                        helper.setText(R.id.tv_state, "待发货");
                        helper.getView(R.id.tv_state1).setVisibility(View.VISIBLE);
                        tvState.setTextColor(Color.parseColor("#F81131"));
                        helper.setText(R.id.tv_state1, "发货");
                        break;
                    case 3:
                        helper.setText(R.id.tv_state, "待收货");
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
                        if (item.PayStates == 0) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("data", item);
                            JumpUtils.dataJump((Activity) mContext, FixPriceActivity.class, bundle, false);
                        } else {
                            if (DBManager.getInstance(mContext).getUserInfo().suppType.equals("6")) {
                                setData(item);
                                commit(helper.getPosition());
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("data", item);
                                JumpUtils.dataJump((Activity) mContext, SendActivity.class, bundle, false);

                            }
                        }

                    }
                });
                adapter.setNewData(item.OrderGoodsList);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString("orderid", item.OrderId + "");
                        JumpUtils.dataJump((Activity) mContext, MyOrderDetailsActivity.class, bundle, false);
                    }
                });
                break;
            case 1:
                helper.getView(R.id.tv_state1).setVisibility(View.GONE);
                helper.getView(R.id.recyclerView_order).setVisibility(View.GONE);
                helper.getView(R.id.iv_shop_logo).setVisibility(View.GONE);
                helper.setText(R.id.tv_shopName, "订单号：" + item.OrderNo);
                switch (item.OrderStates) {
                    case 0:
                        helper.setText(R.id.tv_state, "全部");
                        break;
                    case 1:
                        helper.setText(R.id.tv_state, "待付款");

                        break;
                    case 2:
                        helper.setText(R.id.tv_state, "待发货");
                        helper.setText(R.id.tv_state1, "确认发货");
                        helper.getView(R.id.tv_state1).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_state1).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cOrDOrder(item.OrderId, helper.getPosition(), 1, "发货成功");
                            }
                        });
                        break;
                    case 3:
                        helper.setText(R.id.tv_state, "待收货");
                        break;
                    case 4:
                        helper.setText(R.id.tv_state, "已完成");
                        break;
                    case 8:
                        helper.setText(R.id.tv_state, "已关闭");
                        break;
                }
                helper.setText(R.id.tv_youfei, "");
                helper.setText(R.id.tv_all_num, "共" + item.SumCommodity + "件商品 合计：");
                helper.setText(R.id.tv_TotalMoney1, "¥" + StrUtils.moenyToDH(item.OrderPrice + ""));
                ImageUtils.loadUrlImage(mContext, item.CommodityIconUrl, (ImageView) helper.getView(R.id.iv_product_logo));
                helper.setText(R.id.tv_product_Name, item.CommodityName);
                break;
            case 2:
                helper.getView(R.id.tv_state2).setVisibility(View.GONE);
                helper.getView(R.id.tv_state1).setVisibility(View.GONE);
                helper.getView(R.id.recyclerView_order).setVisibility(View.GONE);
                helper.getView(R.id.iv_shop_logo).setVisibility(View.GONE);
                helper.setText(R.id.tv_shopName, "订单号：" + item.OrderNo);
                switch (item.OrderStates) {
                    case 0:
                        helper.setText(R.id.tv_state, "全部");
                        break;
                    case 1:
                        helper.setText(R.id.tv_state, "待付款");
                        helper.getView(R.id.tv_state2).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_state1).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_state2, "取消");
                        helper.setText(R.id.tv_state1, "待支付");
                        helper.getView(R.id.tv_state1).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString("orderNo", item.OrderNo);
                                bundle.putDouble("totalMoney", item.OrderPrice);
                                JumpUtils.dataJump((Activity) mContext, PayActivity.class, bundle, false);

                            }
                        });
                        break;
                    case 2:
                        helper.setText(R.id.tv_state, "待发货");
                        helper.setText(R.id.tv_state1, "提醒发货");
                        helper.getView(R.id.tv_state1).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_state1).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                remindShipment(item.OrderId, helper.getPosition());
                            }
                        });
                        break;
                    case 3:
                        helper.setText(R.id.tv_state, "已发货");
                        helper.setText(R.id.tv_state1, "确认收货");
                        helper.getView(R.id.tv_state1).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_state1).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cOrDOrder(item.OrderId, helper.getPosition(), 2, "收货成功");
                            }
                        });
                        break;
                    case 4:
                        helper.setText(R.id.tv_state, "已完成");
                        break;
                    case 8:
                        helper.setText(R.id.tv_state, "已关闭");
                        break;
                }
                helper.setText(R.id.tv_youfei, "");
                helper.setText(R.id.tv_all_num, "共" + item.SumCommodity + "件商品 合计：");
                helper.setText(R.id.tv_TotalMoney1, "¥" + StrUtils.moenyToDH(item.OrderPrice + ""));
                ImageUtils.loadUrlImage(mContext, item.CommodityIconUrl, (ImageView) helper.getView(R.id.iv_product_logo));
                helper.setText(R.id.tv_product_Name, item.CommodityName);

                helper.getView(R.id.tv_state2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrder(item.OrderId, helper.getPosition());
                    }
                });
                break;
        }

        helper.addOnClickListener(R.id.ll_item);
    }

    private void setData(OrderBean bean) {
        map = new TreeMap<>();
        map.put("OrderId", bean.OrderId);
        map.put("UserId", bean.UserId);
        map.put("SupplierId", bean.SupplierId);
    }

    private void commit(final int positon) {
        RequestClient.ConfirmShipment(map, mContext, new NetSubscriber<BaseResultBean>(mContext, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                SPUtils.getInstance(mContext).savaBoolean(Contans.ORDER_IS_FRESH, true).commit();
                mData.remove(positon);

                Toast.makeText(mContext, "发货成功", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
                getCount(4);
            }
        });
    }

    private void cancelOrder(int orderId, final int position) {
        RequestClient.CancelOrder(orderId, mContext, new NetSubscriber<BaseResultBean>(mContext, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                mData.remove(position);
                Toast.makeText(mContext, "取消成功", Toast.LENGTH_SHORT).show();
                getCount(3);
                notifyDataSetChanged();
            }
        });
    }

    private void remindShipment(int orderId, final int position) {
        RequestClient.Remind(orderId, mContext, new NetSubscriber<BaseResultBean>(mContext, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                Toast.makeText(mContext, "提醒成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //确认收发货
    private void cOrDOrder(int orderId, final int position, final int type, final String content) {
        RequestClient.AffirmSSStates(orderId, type, mContext, new NetSubscriber<BaseResultBean>(mContext, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                mData.remove(position);
                SPUtils.getInstance(mContext).savaBoolean(Contans.ORDER_IS_FRESH, true).commit();
                Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
                getCount(type);
            }
        });
    }

    private void getCount(final int type) {
        RequestClient.GetSupplierNoticeUnreadCount(mContext, new NetSubscriber<BaseResultBean<NoticeNumBean>>(mContext) {
            @Override
            public void onResultNext(BaseResultBean<NoticeNumBean> model) {
                OrderFragment.UserDFH = model.data.UserDFH;
                OrderFragment.UserDZF = model.data.UserDZF;
                OrderFragment.CKDFH = model.data.CKDFH;
                OrderFragment.CKDZF = model.data.CKDZF;
                OrderFragment.JHDZF = model.data.JHDZF;
                OrderFragment.JHDSH = model.data.JHDSH;
                if (type == 1) {
                    OrderFragment.updataView(1, 2);
                }
                if (type == 2) {
                    OrderFragment.updataView(2, 3);
                }
                if(type ==3){
                    OrderFragment.updataView(2,1);
                }
                if(type==4){
                    OrderFragment.updataView(0,2);
                }
            }
        });
    }

    public void setType(int type) {
        this.type = type;
    }
}
