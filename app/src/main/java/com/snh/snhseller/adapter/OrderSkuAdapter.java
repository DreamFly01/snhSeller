package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.supplierbean.SkuBean;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/11<p>
 * <p>changeTime：2019/3/11<p>
 * <p>version：1<p>
 */
public class OrderSkuAdapter extends BaseQuickAdapter<SkuBean,BaseViewHolder> {
    public OrderSkuAdapter(int layoutResId, @Nullable List<SkuBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SkuBean item) {
        helper.setText(R.id.tv_01, item.NormName + " " + item.NormValue + "(" + item.Unit + ")");
        helper.setText(R.id.tv_02,  "价格 ￥" + item.NormPrice);

        helper.setText(R.id.tv_03,"x"+item.NormNumber);
        helper.setText(R.id.tv_04,"小计："+item.NormSumPrice);
    }
}
