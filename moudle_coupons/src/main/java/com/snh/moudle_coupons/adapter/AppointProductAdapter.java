package com.snh.moudle_coupons.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.library_base.utils.ImageUtils;
import com.snh.moudle_coupons.R;
import com.snh.moudle_coupons.bean.CouponsProductIdBean;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/6/1<p>
 * <p>changeTime：2019/6/1<p>
 * <p>version：1<p>
 */
public class AppointProductAdapter extends BaseQuickAdapter<CouponsProductIdBean,BaseViewHolder> {

    public AppointProductAdapter(int layoutResId, @Nullable List<CouponsProductIdBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponsProductIdBean item) {
        ImageUtils.loadUrlImage(mContext,item.GoodsImg, (ImageView) helper.getView(R.id.coupons_iv_logo));
    }
}
