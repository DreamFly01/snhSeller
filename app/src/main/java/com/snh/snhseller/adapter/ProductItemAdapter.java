package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/5/20<p>
 * <p>changeTime：2019/5/20<p>
 * <p>version：1<p>
 */
public class ProductItemAdapter extends BaseQuickAdapter<ProductItemAdapter,BaseViewHolder> {
    public ProductItemAdapter(int layoutResId, @Nullable List<ProductItemAdapter> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductItemAdapter item) {

    }
}
