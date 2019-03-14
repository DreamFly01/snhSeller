package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BusinessBean;
import com.snh.snhseller.utils.ImageUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/11<p>
 * <p>changeTime：2019/3/11<p>
 * <p>version：1<p>
 */
public class BusinessAdapter extends BaseQuickAdapter<BusinessBean,BaseViewHolder> {
    public BusinessAdapter(int layoutResId, @Nullable List<BusinessBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BusinessBean item) {
        helper.setText(R.id.tv_product_Name,item.CommodityName);
        helper.setText(R.id.tv_inventory,"库存:"+item.Inventory);
        if(item.IsSetNorm){
            helper.setText(R.id.tv_state,"有规格");
        }else {
            helper.setText(R.id.tv_state,"无规格");
        }
        ImageUtils.loadUrlImage(mContext,item.CommodityIconUrl, (ImageView) helper.getView(R.id.iv_product_logo));
    }

    public void setDatas(List<BusinessBean> datas){
        mData = datas;
        notifyDataSetChanged();
    }
    public void addData(List<BusinessBean> datas){
        mData.addAll(datas);
        notifyDataSetChanged();
    }
}
