package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.supplierbean.SkuBean;
import com.snh.snhseller.utils.StrUtils;

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
        helper.setText(R.id.tv_01, item.NormName + " ");
        helper.setText(R.id.tv_03,  "价格 ¥" + StrUtils.moenyToDH(item.NormPrice+""));

        helper.setText(R.id.tv_02,"x"+item.NormNumber);
        helper.setText(R.id.tv_04,"小计："+StrUtils.moenyToDH(item.NormSumPrice+""));
        helper.setText(R.id.tv_sku, item.NormValue + "(" + item.Unit + "/组)");
    }
}
