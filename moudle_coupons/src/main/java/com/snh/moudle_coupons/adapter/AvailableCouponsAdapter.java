package com.snh.moudle_coupons.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.library_base.utils.StrUtils;
import com.snh.moudle_coupons.R;
import com.snh.moudle_coupons.bean.SupplierCouponBean;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/6/5<p>
 * <p>changeTime：2019/6/5<p>
 * <p>version：1<p>
 */
public class AvailableCouponsAdapter extends BaseQuickAdapter<SupplierCouponBean,BaseViewHolder> {
    public AvailableCouponsAdapter(int layoutResId, @Nullable List<SupplierCouponBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SupplierCouponBean item) {
//        helper.setText(R.id.coupons_item_tv_name,item.CouponName);
        helper.setText(R.id.coupons_item_tv_money,mContext.getResources().getString(R.string.moneyStr)+StrUtils.moenyToDH(item.TotalAmount+""));
    }
}
