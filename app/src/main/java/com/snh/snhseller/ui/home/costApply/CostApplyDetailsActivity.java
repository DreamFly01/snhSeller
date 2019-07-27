package com.snh.snhseller.ui.home.costApply;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.CostApplyBean;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.StrUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/14<p>
 * <p>changeTime：2019/3/14<p>
 * <p>version：1<p>
 */
public class CostApplyDetailsActivity extends BaseActivity {
    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_name1)
    TextView tvName1;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.iv_01)
    ImageView iv01;
    @BindView(R.id.iv_02)
    ImageView iv02;
    @BindView(R.id.iv_03)
    ImageView iv03;
    @BindView(R.id.iv_state)
    ImageView ivState;
    @BindView(R.id.iv_logo1)
    ImageView ivLogo1;
    @BindView(R.id.tv_state1)
    TextView tvState1;
    @BindView(R.id.ll_edit)
    LinearLayout llEdit;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.ll_02)
    LinearLayout ll02;

    @BindView(R.id.ll_menu)
    LinearLayout llMenu;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.tv_supplier_mark)
    TextView tvSupplierMark;

    private Bundle bundle;
    private CostApplyBean bean;
    private DialogUtils dialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_costapplydetails_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            bean = bundle.getParcelable("data");
        }
        dialogUtils = new DialogUtils(this);
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("申请详情");
        IsBang.setImmerHeard(this, rlHead);
        ImageUtils.loadUrlImage(this, bean.SalesmanLogo, ivLogo);
        switch (bean.CostStates) {
            case 1:
                tvState.setText("审批中");
                tvState.setBackgroundResource(R.drawable.shape_state_blue_bg);
                tvState.setTextColor(Color.parseColor("#2E8AFF"));
                tvState1.setText(bean.SupplierName + ".审批中");
                ivState.setBackgroundResource(R.drawable.state1_bg);

                break;
            case 2:
                tvState.setText("已同意");
                if (null==bean.SuppRemark||bean.SuppRemark.length <= 0) {
                    llEdit.setVisibility(View.VISIBLE);
                }
                tvState.setBackgroundResource(R.drawable.shape_state_green_bg);
                tvState.setTextColor(Color.parseColor("#03D722"));
                tvState1.setText(bean.SupplierName + ".已同意");
                ivState.setBackgroundResource(R.drawable.state3_bg);
                llMenu.setVisibility(View.GONE);
//                llEdit.setVisibility(View.VISIBLE);
                break;
            case 3:
                tvState.setText("已驳回");
                    if (null == bean.SuppRemark||bean.SuppRemark.length <= 0) {
                        llEdit.setVisibility(View.VISIBLE);
                    }
                tvState.setBackgroundResource(R.drawable.shape_state_red_bg);
                tvState.setTextColor(Color.parseColor("#fc1a4e"));
                tvState1.setText(bean.SupplierName + ".已驳回");
                ivState.setBackgroundResource(R.drawable.state2_bg);
                ll01.setVisibility(View.GONE);
                ll02.setVisibility(View.GONE);
                break;
            case 4:
                tvState.setText("已撤销");
                tvState.setBackgroundResource(R.drawable.shape_state_gray_bg);
                tvState.setTextColor(Color.parseColor("#6E6E6E"));
                tvState1.setText(bean.SalesmanName);
                ivState.setBackgroundResource(R.drawable.state2_bg);
                llMenu.setVisibility(View.GONE);
                break;
        }
        tvOrder.setText(bean.ApprovalNo);
        switch (bean.CostType) {
            case 1:
                tvType.setText("差旅费");
                break;
            case 2:
                tvType.setText("招待费");
                break;
            case 3:
                tvType.setText("市场营销");
                break;
            case 4:
                tvType.setText("其他");
                break;
        }
        tvName.setText(bean.CostName);
        tvMoney.setText(bean.Budget + "元");
        tvTime.setText(bean.OccurDate);
        if (null != bean.ReMark) {
            tvDesc.setText(bean.ReMark[0]);
        }
        if (null != bean.SuppRemark) {
            if (bean.SuppRemark.length > 0) {
                tvSupplierMark.setText("备注：" + bean.SuppRemark[0]);
                llEdit.setVisibility(View.GONE);
            }
        }
        tvName1.setText(bean.SalesmanName);
        ImageUtils.loadUrlImage(this, bean.SupplierIconUrl, ivLogo1);
        List<String> pathList;
        if (!StrUtils.isEmpty(bean.ExpenseVoucher)) {
            pathList = Arrays.asList(StringUtils.split(bean.ExpenseVoucher, ","));
            switch (pathList.size()) {
                case 1:
                    ImageUtils.loadUrlImage(this, pathList.get(0).replace("H", "h"), iv01);
                    break;
                case 2:
                    ImageUtils.loadUrlImage(this, pathList.get(0).replace("H", "h"), iv01);
                    ImageUtils.loadUrlImage(this, pathList.get(1).replace("H", "h"), iv02);
                    break;
                case 3:
                    ImageUtils.loadUrlImage(this, pathList.get(0).replace("H", "h"), iv01);
                    ImageUtils.loadUrlImage(this, pathList.get(1).replace("H", "h"), iv02);
                    ImageUtils.loadUrlImage(this, pathList.get(2).replace("H", "h"), iv03);
                    break;
            }
        }
    }

    @Override
    public void setUpLisener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.heard_back, R.id.ll_edit, R.id.ll_01, R.id.ll_02})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.ll_edit:
                dialogUtils.editeDialog("添加备注", new DialogUtils.EditClickLisener() {
                    @Override
                    public void onCancelClick(View v) {
                        dialogUtils.dismissDialog();
                    }

                    @Override
                    public void onConfirmClick(View v, String content) {
                        addMark(content);
                        dialogUtils.dismissDialog();
                    }
                }, false);
                break;
            case R.id.ll_01:
                rejectOrAgree(2, "已驳回该申请");
                break;
            case R.id.ll_02:
                rejectOrAgree(1, "已同意该申请");
                break;
        }
    }

    private void rejectOrAgree(final int type, final String content) {
        addSubscription(RequestClient.ConsentCostApply(bean.CostId, bean.SalesmanId, type, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {

                dialogUtils.simpleDialog(content, new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        CostApplyDetailsActivity.this.finish();
                    }
                }, false);

            }
        }));
    }

    private void addMark(String mark) {
        addSubscription(RequestClient.AddReMark(bean.CostId, bean.SalesmanId, mark, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                showShortToast("添加备注成功");
                CostApplyDetailsActivity.this.finish();
            }
        }));
    }
}
