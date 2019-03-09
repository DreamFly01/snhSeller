package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.supplierbean.UrlBean;
import com.snh.snhseller.utils.ImageUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/9<p>
 * <p>changeTime：2019/3/9<p>
 * <p>version：1<p>
 */
public class AllSupplierItemAdapter extends BaseQuickAdapter<UrlBean,BaseViewHolder> {
    public AllSupplierItemAdapter(int layoutResId, @Nullable List<UrlBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UrlBean item) {
        ImageUtils.loadUrlImage(mContext,item.CommTenantLogo, (ImageView) helper.getView(R.id.iv_product_logo));
    }
}
