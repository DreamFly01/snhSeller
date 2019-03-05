package com.snh.snhseller.ui.salesmanManagement.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.salebean.CommTenantBean;
import com.snh.snhseller.utils.Contans;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.DistanceUtils;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.SPUtils;
import com.snh.snhseller.utils.StrUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/4<p>
 * <p>changeTime：2019/3/4<p>
 * <p>version：1<p>
 */
public class OperationAdapter extends BaseQuickAdapter<CommTenantBean, BaseViewHolder> {
    private OnDeleteClickLister mDeleteClickListener;
    private boolean type = false;
    public OperationAdapter(int layoutResId, @Nullable List<CommTenantBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommTenantBean item) {
        helper.setText(R.id.tv_name, item.CommTenantName);

        helper.setText(R.id.tv_phone, "联系号码：" + item.BuinourPhoneNumber);
        helper.setText(R.id.tv_address, "地址：" + item.Address);

        helper.getView(R.id.btn_add).setVisibility(View.GONE);
        if (type) {
            helper.setText(R.id.tv_phone, "联系号码：" + item.BuinourPhoneNumber);
            helper.setText(R.id.tv_address, "地址：" + item.Address);
            View view = helper.getView(R.id.tv_delete);
            view.setTag(helper.getPosition());
            if (!view.hasOnClickListeners()) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDeleteClickListener != null) {
                            mDeleteClickListener.onDeleteClick(v, (Integer) v.getTag());
                        }
                    }
                });
            }
        } else {
            helper.setText(R.id.tv_phone, "(" + item.ManagerSalesmanUser + ")管理该商户");
            TextView tv = helper.getView(R.id.tv_phone);
            tv.setTextColor(Color.parseColor("#fc1a4e"));
            helper.setText(R.id.tv_address, "地址：" + item.Address);
            if(item.IsMyManager){
                helper.getView(R.id.btn_add).setVisibility(View.GONE);
            }else{
                helper.getView(R.id.btn_add).setVisibility(View.VISIBLE);
            }
        }
        if (!StrUtils.isEmpty(item.Latitude)) {
            LatLng latLng = new LatLng(Double.parseDouble(SPUtils.getInstance(mContext).getString(Contans.LATITUDE)), Double.parseDouble(SPUtils.getInstance(mContext).getString(Contans.LONGITUDE)));
            LatLng latLng1 = new LatLng( Double.parseDouble(item.Latitude), Double.parseDouble(item.Longitude));
//            double distans = DistanceUtils.getDistance(Double.parseDouble(SPUtils.getInstance(mContext).getString(Contans.LATITUDE)), Double.parseDouble(SPUtils.getInstance(mContext).getString(Contans.LONGITUDE))
//                    , Double.parseDouble(item.Latitude), Double.parseDouble(item.Longitude));
            double distans = DistanceUtil.getDistance(latLng,latLng1);
            TextView distance = helper.getView(R.id.tv_distance);
            distance.setText(DistanceUtils.distanceKMFormat(Double.parseDouble(DistanceUtils.distanceFormat(distans))));
            if (distans < 150) {
                helper.getView(R.id.btn_dk).setEnabled(true);
            }else {
                helper.getView(R.id.btn_dk).setEnabled(false);
            }
        } else {
            helper.getView(R.id.btn_dk).setEnabled(false);
        }
        ImageUtils.loadUrlImage(mContext, item.CommTenantIconUrl, (ImageView) helper.getView(R.id.iv_logo));
        helper.addOnClickListener(R.id.btn_dk);
        helper.addOnClickListener(R.id.btn_add);
    }
    public void setType(boolean type){
        this.type = type;
        notifyDataSetChanged();
    }

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
