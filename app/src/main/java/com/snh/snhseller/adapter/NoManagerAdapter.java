package com.snh.snhseller.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.salesBean.NoManagerBean;
import com.snh.snhseller.utils.ImageUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/13<p>
 * <p>changeTime：2019/3/13<p>
 * <p>version：1<p>
 */
public class NoManagerAdapter extends BaseQuickAdapter<NoManagerBean,BaseViewHolder> {
    public NoManagerAdapter(int layoutResId, @Nullable List<NoManagerBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoManagerBean item) {
        ImageUtils.loadUrlImage(mContext,item.CommtenantIconUrl, (ImageView) helper.getView(R.id.iv_shop_logo));
        helper.setText(R.id.tv_shopName,item.CommtenantName);
        if(item.SalesmanList.size()>0){
            StringBuffer str = new StringBuffer();
            for (int i = 0; i < item.SalesmanList.size(); i++) {
                str.append(item.SalesmanList.get(i).SalesmanName+",");
            }
            String str1 = str.toString().substring(0,str.length()-1);

            helper.setText(R.id.tv_01,"该商户有业务员（"+str1+"）管理");
        }else {
            TextView textView = helper.getView(R.id.tv_01);
            textView.setTextColor(Color.parseColor("#fc1a4e"));
            helper.setText(R.id.tv_01,"该商户未被业务员管理");
        }
    }
}
