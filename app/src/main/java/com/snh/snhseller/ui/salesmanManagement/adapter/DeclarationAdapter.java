package com.snh.snhseller.ui.salesmanManagement.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.salebean.ApplyBean;
import com.snh.snhseller.utils.TimeUtils;

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
        helper.setText(R.id.tv_sales_name,item.SalesmanName+"的费用");
        helper.setText(R.id.tv_name,"项目名称："+item.CostName);
        helper.setText(R.id.tv_money,"费用预算："+item.Budget);
        helper.setText(R.id.tv_time,TimeUtils.stringToTime(item.CreateTime)+"前");
        TextView tvState = helper.getView(R.id.tv_state);
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

                tvState.setText("审批中");
                tvState.setBackgroundResource(R.drawable.shape_state_blue_bg);
                tvState.setTextColor(Color.parseColor("#2E8AFF"));
                break;
            case 2:
                tvState.setText("已通过");
                tvState.setBackgroundResource(R.drawable.shape_state_green_bg);
                tvState.setTextColor(Color.parseColor("#03D722"));
                break;
            case 3:
                tvState.setText("已驳回");
                tvState.setBackgroundResource(R.drawable.shape_state_red_bg);
                tvState.setTextColor(Color.parseColor("#fc1a4e"));
                break;
            case 4:
                tvState.setText("撤销");
                tvState.setBackgroundResource(R.drawable.shape_state_gray_bg);
                tvState.setTextColor(Color.parseColor("#6E6E6E"));
                break;
        }
    }
}
