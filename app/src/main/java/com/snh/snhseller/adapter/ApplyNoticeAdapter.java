package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.ApplyBean;
import com.snh.snhseller.utils.ImageUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/12<p>
 * <p>changeTime：2019/3/12<p>
 * <p>version：1<p>
 */
public class ApplyNoticeAdapter extends BaseQuickAdapter<ApplyBean,BaseViewHolder> {
    public ApplyNoticeAdapter(int layoutResId, @Nullable List<ApplyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ApplyBean item) {
        if (item.Type==2) {
            helper.setText(R.id.tv_state,"有商家申请成为您的供应商");
        }
        if(item.Type == 1){
            helper.setText(R.id.tv_state,"有商家申请成为您的商户");
        }
        helper.setText(R.id.tv_time,item.CreateDateTime);
        helper.setText(R.id.tv_shopName,item.CommTenantName);
        helper.setText(R.id.tv_inventory,"总库存："+item.SumRepertory);
        ImageUtils.loadUrlImage(mContext,item.CommTenantIconUrl, (ImageView) helper.getView(R.id.iv_shop_logo));
        helper.addOnClickListener(R.id.tv_agree,R.id.tv_refuse);
    }
}
