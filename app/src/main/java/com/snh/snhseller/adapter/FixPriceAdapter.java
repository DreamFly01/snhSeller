package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.OrderGoodsBean;
import com.snh.snhseller.utils.ImageUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/4/9<p>
 * <p>changeTime：2019/4/9<p>
 * <p>version：1<p>
 */
public class FixPriceAdapter extends BaseQuickAdapter<OrderGoodsBean,BaseViewHolder> {
    public FixPriceAdapter(int layoutResId, @Nullable List<OrderGoodsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderGoodsBean item) {
        ImageUtils.loadUrlImage(mContext, item.shopLogo, (ImageView) helper.getView(R.id.iv_shop_logo));
        helper.setText(R.id.tv_shopName,item.shopName);
        helper.setText(R.id.tv_state,item.state);
        helper.setText(R.id.tv_GoodsName,item.OrderGoodsName);
        helper.setText(R.id.tv_Number1,"x" + item.Number);
        helper.setText(R.id.tv_price,"¥" + item.Price);
        ImageUtils.loadUrlImage(mContext, item.OrderGoodsIcon,  (ImageView) helper.getView(R.id.iv_product_logo1));
    }
}
