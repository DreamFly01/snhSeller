package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.supplierbean.SkuBean;
import com.snh.snhseller.utils.StrUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/7<p>
 * <p>changeTime：2019/3/7<p>
 * <p>version：1<p>
 */
public class ProductSkuAdapter extends BaseQuickAdapter<SkuBean, BaseViewHolder> {
    public ProductSkuAdapter(int layoutResId, @Nullable List<SkuBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final SkuBean item) {
        helper.setText(R.id.tv_01, item.NormName + " " );
        helper.setText(R.id.tv_03,item.NormValue.substring(item.NormValue.indexOf("*")+1,item.NormValue.length()) + "(" + item.Unit + "/组)");
        helper.setText(R.id.tv_02, "库存 " + item.Inventory + "   价格 ￥" + StrUtils.moenyToDH(item.Price+""));
        final EditText num = helper.getView(R.id.et_num);
        final int[] numStr = {0};
        helper.getView(R.id.tv_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numStr[0] > 0) {
                    numStr[0] -= 1;
                    num.setText(numStr[0] + "");
                    item.total = numStr[0];
                }
            }
        });
        helper.getView(R.id.tv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numStr[0] < item.Inventory) {
                    numStr[0] += 1;
                    num.setText(numStr[0] + "");
                    item.total = numStr[0];
                }
            }
        });
        num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!StrUtils.isEmpty(num.getText().toString().trim())) {

                    if (Integer.parseInt(num.getText().toString().trim()) > item.Inventory) {
                        num.setText(item.Inventory + "");
                        num.setSelection((item.Inventory + "").length());

                    }
                    if (Integer.parseInt(num.getText().toString().trim()) < 0) {
                        num.setText(0 + "");
                        num.setSelection(0 +"".length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
