package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.salesBean.SalesCountBean;
import com.snh.snhseller.bean.salesBean.SalesRecodeBean;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/13<p>
 * <p>changeTime：2019/3/13<p>
 * <p>version：1<p>
 */
public class SalestCountAdapter extends BaseQuickAdapter<SalesCountBean, BaseViewHolder> {
    public SalestCountAdapter(int layoutResId, @Nullable List<SalesCountBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SalesCountBean item) {
        helper.setText(R.id.tv_01, item.SalesmanName);
        helper.setText(R.id.tv_02,item.SalesmanPhone);
        if (item.Count > 0) {
            helper.setText(R.id.tv_03, "打卡次数：" + item.Count + "次");
        } else {
            helper.setText(R.id.tv_03, "未拜访");
        }
    }
}
