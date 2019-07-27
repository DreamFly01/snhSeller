package com.snh.moudle_coupons.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.library_base.utils.ImageUtils;
import com.snh.library_base.utils.StrUtils;
import com.snh.moudle_coupons.R;
import com.snh.moudle_coupons.bean.CouponsBean;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/5/30<p>
 * <p>changeTime：2019/5/30<p>
 * <p>version：1<p>
 */
public class StoreCouponsAdapter extends BaseQuickAdapter<CouponsBean,BaseViewHolder> {

    public StoreCouponsAdapter(int layoutResId, @Nullable List<CouponsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponsBean item) {
        helper.setText(R.id.coupons_tv_money, StrUtils.moenyToDH(item.CouponValue + ""));
        helper.setText(R.id.coupons_tv_condition, "满" + item.ConditionValue + "可用");
        helper.setText(R.id.coupons_item_tv_name, item.SupplierName);
        helper.setText(R.id.coupons_tv_02, "发行总张的" + item.TotalNum + "张");
//        helper.setText(R.id.coupons_tv_01, "每人限领" + item.MaxReceiveNum + "张");
        helper.setText(R.id.coupons_tv_01, "有效期：" + item.BeginDate + "-" + item.EndDate);
        helper.setText(R.id.coupons_tv_residue, item.StockNum + "");
        helper.setText(R.id.coupons_tv_recevie, item.ReceiveNum + "");
        helper.setText(R.id.coupons_tv_used, item.UsedNum + "");
        ImageView ivState1 = helper.getView(R.id.coupons_item_iv_state);
        ImageUtils.loadUrlImage(mContext,item.SupplierLogo, (ImageView) helper.getView(R.id.coupons_item_iv_logo));


        switch (item.IsAdd)
        {
            case 0:
                ivState1.setBackgroundResource(R.drawable.coupons_close_bg);
                break;
            case 1:
                ivState1.setBackgroundResource(R.drawable.coupons_open_bg);
                break;
        }
        helper.addOnClickListener(R.id.coupons_tv_03)
        .addOnClickListener(R.id.coupons_ll_on_off)
        ;
    }
}
