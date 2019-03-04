package com.snh.snhseller.adapter;

import android.content.Context;
import android.widget.TextView;

import com.snh.snhseller.R;
import com.snh.snhseller.bean.TypePriceBean;
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
public class TypePriceAdapter extends CommonAdapter<TypePriceBean> {
    public TypePriceAdapter(Context context, int layoutId, List<TypePriceBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TypePriceBean typePriceBean, int position) {
        TextView name = holder.getView(R.id.tv_name);
        TextView price1 = holder.getView(R.id.tv_price1);
        TextView price2 = holder.getView(R.id.tv_price2);

        name.setText(typePriceBean.name);
        price1.setText(typePriceBean.price1);
        price2.setText(typePriceBean.price2);


    }
}
