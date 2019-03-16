package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.salesBean.SalesCountBean;
import com.snh.snhseller.bean.salesBean.SalesRecodeBean;
import com.snh.snhseller.utils.ImageUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/13<p>
 * <p>changeTime：2019/3/13<p>
 * <p>version：1<p>
 */
public class SalestRecodeAdapter extends BaseQuickAdapter<SalesRecodeBean, BaseViewHolder> {
    public SalestRecodeAdapter(int layoutResId, @Nullable List<SalesRecodeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SalesRecodeBean item) {
        helper.setText(R.id.tv_time,item.CreatTime);
        helper.setText(R.id.tv_01, item.CommTenantName);
        helper.setText(R.id.tv_02,item.Remark);
        ImageUtils.loadUrlImage(mContext,item.Icon, (ImageView) helper.getView(R.id.iv_shop_logo));

    }
}
