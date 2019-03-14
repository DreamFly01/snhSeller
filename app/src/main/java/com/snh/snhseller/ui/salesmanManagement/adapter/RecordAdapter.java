package com.snh.snhseller.ui.salesmanManagement.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.salebean.RecordBean;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/5<p>
 * <p>changeTime：2019/3/5<p>
 * <p>version：1<p>
 */
public class RecordAdapter extends BaseQuickAdapter<RecordBean,BaseViewHolder> {


    public RecordAdapter(int layoutResId, @Nullable List<RecordBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecordBean item) {
        helper.setText(R.id.tv_01, item.RouteTime.substring(11, 16));
        helper.setText(R.id.tv_02, item.CommtenantName);
        if (item.RouteStates == 1) {
            helper.setText(R.id.tv_03, "已拜访");
        }else {
            helper.setText(R.id.tv_03,"未拜访");
        }
    }
}
