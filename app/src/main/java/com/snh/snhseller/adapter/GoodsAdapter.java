package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.supplierbean.GoodsBean;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.StrUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/7<p>
 * <p>changeTime：2019/3/7<p>
 * <p>version：1<p>
 */
public class GoodsAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {
    private int from;
    private int type;

    public GoodsAdapter(int layoutResId, @Nullable List<GoodsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {
        ImageUtils.loadUrlImage(mContext, item.CommodityLogo, (ImageView) helper.getView(R.id.iv_logo));
        helper.setText(R.id.tv_name, item.CommodityName);

            if (from != 2&&type==1) {
                helper.setText(R.id.tv_price, "￥" + StrUtils.moenyToDH(item.Price+""));
            } else {
                helper.getView(R.id.ll_price).setVisibility(View.GONE);
                helper.getView(R.id.tv_jh).setVisibility(View.GONE);
            }
        helper.getView(R.id.ll_price).setVisibility(View.GONE);
            helper.getView(R.id.tv_price).setVisibility(View.GONE);

        helper.setText(R.id.tv_price1, "￥" + StrUtils.moenyToDH(item.MarketPrice+""));
        helper.setText(R.id.tv_inventory, "库存：" + item.Repertory);
        if(item.Repertory>0){
            helper.getView(R.id.tv_jh).setEnabled(true);

        }else {
            helper.getView(R.id.tv_jh).setEnabled(false);
        }
    }

    public void setFrom(int from, int type) {
        this.from = from;
        this.type = type;
    }
}
