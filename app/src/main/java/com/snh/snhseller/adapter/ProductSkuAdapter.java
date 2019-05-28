package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        helper.setText(R.id.tv_01, item.NormName + " ");
        final int normNum = Integer.parseInt(item.NormValue.substring(item.NormValue.indexOf("*") + 1, item.NormValue.length()));
        helper.setText(R.id.tv_03, item.NormValue.substring(item.NormValue.indexOf("*") + 1, item.NormValue.length()) + "(" + item.Unit + "/组)" + "   库存 " + item.Inventory);
        if (item.Price <= 0) {
            helper.setText(R.id.tv_02, "价格 ¥" + StrUtils.moenyToDH(item.MarketPrice + ""));
        } else if (item.Price == item.MarketPrice) {
            helper.setText(R.id.tv_02, "价格 ¥" + StrUtils.moenyToDH(item.MarketPrice + ""));
        } else {
            helper.setText(R.id.tv_02, "二批价 ¥" + StrUtils.moenyToDH(item.Price + "") + "   终端价 ¥" + StrUtils.moenyToDH(item.MarketPrice + ""));

        }
        final EditText num = helper.getView(R.id.et_num);
        num.setText(item.total + "");
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
                if (numStr[0] < Math.floor(item.Inventory / normNum)) {
                    numStr[0] += 1;
                    num.setText(numStr[0] + "");
                    item.total = numStr[0];
                } else {
                    Toast.makeText(mContext, "库存不足", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    if (Integer.parseInt(num.getText().toString().trim())<=0){
//                        num.setText("");
//                    }
//                }else {
//                    if (StrUtils.isEmpty(num.getText().toString().trim())){
//                        num.setText("0");
//                    }
//                }
//            }
//        });
//        num.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                try {
//                    if (!StrUtils.isEmpty(num.getText().toString().trim())) {
//                        if (Integer.parseInt(num.getText().toString().trim()) > Math.floor(item.Inventory/normNum)) {
//                            num.setText(item.Inventory + "");
//                            num.setSelection((item.Inventory + "").length());
//                        }
//                        if (Integer.parseInt(num.getText().toString().trim()) < 0) {
//                            num.setText(0 + "");
//                            num.setSelection(0 +"".length());
//                        }
//                    }
//                }catch (Exception e) {
//                    Toast.makeText(mContext,"输入有误",Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }
}
