package com.snh.snhseller.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.OrderBean;
import com.snh.snhseller.bean.OrderGoodsBean;
import com.snh.snhseller.ui.order.FixPriceActivity;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.StrUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/21<p>
 * <p>changeTime：2019/2/21<p>
 * <p>version：1<p>
 */
public class OrderItemAdapter extends BaseQuickAdapter<OrderGoodsBean, BaseViewHolder> {

    private int type;

    public OrderItemAdapter(int layoutResId, @Nullable List<OrderGoodsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final OrderGoodsBean item) {
        helper.setText(R.id.tv_GoodsName, item.OrderGoodsName);
        helper.setText(R.id.tv_price, "￥" + StrUtils.moenyToDH(item.Price+""));
        if (StrUtils.isEmpty(item.NormName)) {
            helper.getView(R.id.tv_sku).setVisibility(View.GONE);
        } else {
            helper.setText(R.id.tv_sku, "规格："+item.NormName);
        }
        helper.setText(R.id.tv_Number1, "x" + item.Number);

        switch (type) {
            case 0:
                helper.getView(R.id.tv_state).setVisibility(View.GONE);
                helper.setText(R.id.tv_state, "改价");
                break;
            case 2:
                helper.getView(R.id.tv_state).setVisibility(View.GONE);
                break;
            case 3:
                helper.getView(R.id.tv_state).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_state, "物流信息");
                break;
            case 4:
                helper.getView(R.id.tv_state).setVisibility(View.GONE);
                break;
            case 5:
                helper.getView(R.id.tv_state).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_state, "协商历史");
                break;
        }
        ImageView logo = helper.getView(R.id.iv_product_logo1);
        ImageUtils.loadUrlImage(mContext, item.OrderGoodsIcon, logo);

        helper.getView(R.id.tv_state).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case 0:
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("data", item);
                        JumpUtils.dataJump((Activity) mContext, FixPriceActivity.class, bundle, false);
                        break;
                    case 3:
                        break;
                    case 5:
                        break;
                }
            }
        });
    }

    public void setType(int type) {
        this.type = type;
    }
}
