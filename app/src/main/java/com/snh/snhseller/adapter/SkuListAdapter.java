package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.SkuBean;
import com.snh.snhseller.utils.StrUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/12<p>
 * <p>changeTime：2019/3/12<p>
 * <p>version：1<p>
 */
public class SkuListAdapter extends BaseQuickAdapter<SkuBean,BaseViewHolder> {
    public SkuListAdapter(int layoutResId, @Nullable List<SkuBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SkuBean item) {
        helper.setText(R.id.tv_sku_name,"规格名称："+item.NormName);
        helper.setText(R.id.tv_sku_value,"规格值："+item.NormValue+"(单位/"+item.MeasureUnit+")");
        helper.setText(R.id.tv_sku_sprice,"二批价：￥"+StrUtils.moenyToDH(item.Price+""));
        helper.setText(R.id.tv_sku_mprice,"终端价：￥"+StrUtils.moenyToDH(item.RetailPrice+""));
        helper.setText(R.id.tv_sku_inventory,"库存："+item.Inventory);

        helper.addOnClickListener(R.id.ll_delete);
    }
}
