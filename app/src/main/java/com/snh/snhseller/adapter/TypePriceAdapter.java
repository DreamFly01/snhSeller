package com.snh.snhseller.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.TypePriceBean;
import com.snh.snhseller.utils.StrUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/20<p>
 * <p>changeTime：2019/2/20<p>
 * <p>version：1<p>
 */
public class TypePriceAdapter extends BaseQuickAdapter<TypePriceBean,BaseViewHolder> {


    public TypePriceAdapter(int layoutResId, @Nullable List<TypePriceBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TypePriceBean item) {
        TextView name = helper.getView(R.id.tv_name);
        TextView price1 = helper.getView(R.id.tv_price1);
        TextView price2 = helper.getView(R.id.tv_price2);

        name.setText(item.name);
        price1.setText(item.price1);
        price2.setText(item.price2);
    }
}
