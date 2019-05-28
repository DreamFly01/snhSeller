package com.snh.snhseller.ui.home.money;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.WithdrawDetailsBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.utils.TimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/4/16<p>
 * <p>changeTime：2019/4/16<p>
 * <p>version：1<p>
 */
public class WithdrawDetailsActivity extends Activity {
    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_payState)
    TextView tvPayState;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_orderNo)
    TextView tvOrderNo;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_sxf)
    TextView tvSxf;
    @BindView(R.id.tv_bankCard)
    TextView tvBankCard;
    @BindView(R.id.tv_withdraw_time)
    TextView tvWithdrawTime;
    @BindView(R.id.tv_lsh)
    TextView tvLsh;
    @BindView(R.id.ll_02)
    LinearLayout ll02;
    @BindView(R.id.tv_myMoney)
    TextView tvMyMoney;
    @BindView(R.id.iv_process)
    ImageView ivProcess;
    @BindView(R.id.tv_pay_time)
    TextView tvPayTime;
    private int id;
    private int type;
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawdetails_layout);
        ButterKnife.bind(this);
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).titleBar(R.id.rl_head).init();
        bundle = getIntent().getExtras();
        if (null != bundle) {
            id = bundle.getInt("id");
            type = bundle.getInt("type");
        }
        setView();
        getData();
    }

    private void setView() {
        IsBang.setImmerHeard(this, rlHead, "#ffffff");
        if (type == 2 | type == 3) {
            heardTitle.setText("提现详情");
            ll02.setVisibility(View.VISIBLE);
            ll01.setVisibility(View.GONE);
        } else {
            heardTitle.setText("明细详情");
            ll01.setVisibility(View.VISIBLE);
            ll02.setVisibility(View.GONE);
        }
    }

    private void getData() {
        RequestClient.GetSupplierMoneyDetails(id + "", this, new NetSubscriber<BaseResultBean<WithdrawDetailsBean>>(this, true) {
            @Override
            public void onResultNext(BaseResultBean<WithdrawDetailsBean> model) {
                fillView(model.data);
            }
        });
    }

    private void fillView(WithdrawDetailsBean bean) {
        if (type == 2 || type == 3) {
            tvMoney.setText("¥" + StrUtils.moenyToDH(bean.Money + ""));
            tvSxf.setText("¥" + StrUtils.moenyToDH(bean.Poundage + ""));
            tvWithdrawTime.setText(TimeUtils.timeStamp2Date(bean.CreateTime + "", ""));
            tvBankCard.setText(bean.Bank + " 尾号" + bean.BankNo.substring(bean.BankNo.length() - 4, bean.BankNo.length()));
            switch (bean.CheckState) {
                case 0:
                    ivProcess.setBackgroundResource(R.drawable.withdraw_process_0);
                    break;
                case 1:
                    ivProcess.setBackgroundResource(R.drawable.withdraw_process_2);
                    break;
                case 2:
                    ivProcess.setBackgroundResource(R.drawable.withdraw_process_2);
                    break;

            }
        } else {
            ImageUtils.loadUrlImage(this, bean.Icon, ivLogo);
            tvPayState.setText(bean.PayMethods);
            tvContent.setText(bean.ShopGoodsName);
            tvOrderNo.setText(bean.OrderNo);
            tvTime.setText(TimeUtils.timeStamp2Date(bean.CreateTime + "", ""));
            tvPayTime.setText(TimeUtils.timeStamp2Date(bean.PayTime + "", ""));
            tvMyMoney.setText(bean.Money + "");
        }
    }

    @OnClick(R.id.heard_back)
    public void onClick() {
        this.finish();
    }
}
