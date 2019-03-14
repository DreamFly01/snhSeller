package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.salesBean.SalesmanBean;
import com.snh.snhseller.utils.ImageUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/12<p>
 * <p>changeTime：2019/3/12<p>
 * <p>version：1<p>
 */
public class SalesAdapter extends BaseQuickAdapter<SalesmanBean,BaseViewHolder> {
    public SalesAdapter(int layoutResId, @Nullable List<SalesmanBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SalesmanBean item) {
        helper.setText(R.id.tv_name,item.SalesmanRealName);
        helper.setText(R.id.tv_age,item.Age+"岁");
        helper.setText(R.id.tv_phone,item.PhoneNumber);
        if(item.States){
            helper.getView(R.id.tv_state).setVisibility(View.GONE);
            helper.setText(R.id.btnFreeze,"冻结");
        }else {
            helper.setText(R.id.btnFreeze,"解冻");
            helper.getView(R.id.tv_state).setVisibility(View.VISIBLE);
        }
        if(item.Sex.equals("男")) {
            helper.getView(R.id.iv_logo).setBackgroundResource(R.drawable.sales_man_bg);
        }else {
            helper.getView(R.id.iv_logo).setBackgroundResource(R.drawable.sales_women_bg);
        }
        helper.addOnClickListener(R.id.btnFreeze,R.id.btnDelete,R.id.ll_item);
    }
}
