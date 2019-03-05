package com.snh.snhseller.ui.salesmanManagement.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.salebean.ApplyBean;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/5<p>
 * <p>changeTime：2019/3/5<p>
 * <p>version：1<p>
 */
public class DeclarationAdapter extends BaseQuickAdapter<ApplyBean,BaseViewHolder> {
    public DeclarationAdapter(int layoutResId, @Nullable List<ApplyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ApplyBean item) {
        helper.setText(R.id.tv_name,"项目名称："+item.CostName);
        helper.setText(R.id.tv_money,"费用预算："+item.Budget);
        switch (item.CostType)
        {
            case 1:
                helper.setText(R.id.tv_type,"费用类型：差旅费");

                break;
            case 2:
                helper.setText(R.id.tv_type,"费用类型：招待费");
                break;
            case 3:
                helper.setText(R.id.tv_type,"费用类型：市场营销");
                break;
            case 4:
                helper.setText(R.id.tv_type,"费用类型：其他");
                break;
        }
        switch (item.CostStates)
        {
            case 1:
                helper.setText(R.id.tv_state,"审批中");
                break;
            case 2:
                helper.setText(R.id.tv_state,"已通过");
                break;
            case 3:
                helper.setText(R.id.tv_state,"已驳回");
                break;
            case 4:
                helper.setText(R.id.tv_state,"撤销");
                break;
        }
    }
}
