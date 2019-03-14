package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.salesBean.SalesSupplierBean;
import com.snh.snhseller.utils.ImageUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/13<p>
 * <p>changeTime：2019/3/13<p>
 * <p>version：1<p>
 */
public class SalesSupplierAdapter extends BaseQuickAdapter<SalesSupplierBean,BaseViewHolder> {
    public SalesSupplierAdapter(int layoutResId, @Nullable List<SalesSupplierBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SalesSupplierBean item) {
        helper.setText(R.id.tv_name,item.ShopName);
        ImageUtils.loadUrlImage(mContext,item.SupplierIconUrl, (ImageView) helper.getView(R.id.iv_shop_logo));
    }
}
