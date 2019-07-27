package com.snh.moudle_coupons.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snh.library_base.BaseActivity;
import com.snh.library_base.utils.DialogUtils;
import com.snh.library_base.utils.StrUtils;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.moudle_coupons.R;
import com.snh.moudle_coupons.R2;
import com.snh.moudle_coupons.bean.CouponsBean;
import com.snh.moudle_coupons.netapi.RequestClient;

import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/6/4<p>
 * <p>changeTime：2019/6/4<p>
 * <p>version：1<p>
 */
public class EditCouponsActivity extends BaseActivity {
    @BindView(R2.id.heard_back)
    LinearLayout heardBack;
    @BindView(R2.id.heard_title)
    TextView heardTitle;
    @BindView(R2.id.heard_menu)
    ImageView heardMenu;
    @BindView(R2.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R2.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R2.id.rl_head)
    LinearLayout rlHead;
    @BindView(R2.id.coupons_tv_type)
    TextView couponsTvType;
    @BindView(R2.id.coupons_tv_money)
    TextView couponsTvMoney;
    @BindView(R2.id.coupons_tv_condition)
    TextView couponsTvCondition;
    @BindView(R2.id.coupons_ll_condition)
    LinearLayout couponsLlCondition;
    @BindView(R2.id.coupons_tv_num)
    TextView couponsTvNum;
    @BindView(R2.id.coupons_tv_startTime)
    TextView couponsTvStartTime;
    @BindView(R2.id.coupons_tv_endTime)
    TextView couponsTvEndTime;
    @BindView(R2.id.coupons_et_name)
    EditText couponsEtName;
    @BindView(R2.id.coupons_et_num)
    TextView couponsEtNum;
    @BindView(R2.id.coupons_iv_isOpen)
    ImageView couponsIvIsOpen;
    @BindView(R2.id.coupons_tv_commit)
    TextView couponsTvCommit;
    @BindView(R2.id.coupons_et_add_num)
    EditText couponsEtAddNum;
    private int IsDisplay;
    private CouponsBean couponsBean;
    private Bundle bundle;
    private Map<String, Object> map = new TreeMap<>();
    private DialogUtils dialogUtils;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.coupons_activity_editcoupons_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            couponsBean = bundle.getParcelable("data");
        }
        dialogUtils = new DialogUtils(this);
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("编辑优惠券");
        if (couponsBean.CouponWay == 1) {
            couponsTvType.setText("店铺优惠券");
            couponsLlCondition.setVisibility(View.GONE);
        } else {
            couponsTvType.setText("商品优惠券");
            couponsTvCondition.setText(StrUtils.moenyToDH(couponsBean.ConditionValue + ""));
        }
        couponsTvMoney.setText(StrUtils.moenyToDH(couponsBean.CouponValue + ""));
        if (couponsBean.MaxReceiveNum > 0) {
            couponsTvNum.setText(couponsBean.MaxReceiveNum + "");
        } else {
            couponsTvNum.setText("无限制");
        }
        couponsTvStartTime.setText(couponsBean.BeginDate);
        couponsTvEndTime.setText(couponsBean.EndDate);
        couponsEtName.setText(couponsBean.CouponName);
        couponsEtNum.setText(couponsBean.TotalNum + "");
        if (couponsBean.IsDisplay == 1) {
            couponsIvIsOpen.setBackgroundResource(R.drawable.coupons_open_bg);
            IsDisplay = 1;
        } else {
            couponsIvIsOpen.setBackgroundResource(R.drawable.coupons_close_bg);
            IsDisplay = 0;
        }
    }

    @Override
    public void setUpLisener() {

    }

    private boolean check(){
        if (StrUtils.isEmpty(couponsEtName.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入优惠劵名称");
            return false;
        }
        if(StrUtils.isEmpty(couponsEtNum.getText().toString().trim())){
            dialogUtils.noBtnDialog("请输入优惠劵数量");
            return false;
        }
        if(StrUtils.isEmpty(couponsEtAddNum.getText().toString().trim())){
            dialogUtils.noBtnDialog("增发数必须大于0");
            return false;
        }
        return true;
    }
    private void setData() {
        map.put("CouponId", couponsBean.CouponId);
        map.put("CouponName", couponsEtName.getText().toString().trim());
        map.put("TotalNum", Integer.parseInt(couponsEtNum.getText().toString().trim())+Integer.parseInt(couponsEtAddNum.getText().toString().trim()));
        map.put("IsDisplay", IsDisplay);
        map.put("IsPutaway",1);
    }

    private void editData(){
        addSubscription(RequestClient.EditCoupons(map, this, new NetSubscriber<BaseResultBean>(this,true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.simpleDialog("修改成功，是否退出当前页面", new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        EditCouponsActivity.this.finish();
                    }
                },true);
            }
        }));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R2.id.heard_back, R2.id.coupons_tv_commit,R2.id.coupons_iv_isOpen})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.heard_back) {
            this.finish();
        } else if (i == R.id.coupons_tv_commit) {
            if(check()){
                setData();
                editData();
            }

        } else if (i==R.id.coupons_iv_isOpen) {
            if(IsDisplay==1){
                IsDisplay =0;
                couponsIvIsOpen.setBackgroundResource(R.drawable.coupons_close_bg);
            }else {
                IsDisplay = 1;
                couponsIvIsOpen.setBackgroundResource(R.drawable.coupons_open_bg);
            }
        }
    }
}
