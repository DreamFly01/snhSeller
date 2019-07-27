package com.snh.snhseller.ui.home.money;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.MyBankBean;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/4/3<p>
 * <p>changeTime：2019/4/3<p>
 * <p>version：1<p>
 */
public class WithdrawActivity extends BaseActivity {
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
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.ll_bank)
    LinearLayout llBank;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.ll_delete)
    LinearLayout llDelete;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.ll_02)
    LinearLayout ll02;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    private String money;
    private Bundle bundle;
    private DialogUtils dialogUtils;
    private boolean isFrist = true;
    private List<MyBankBean> banks = new ArrayList<>();

    private Map<String, Object> map = new TreeMap<>();
    private int id;
    private double adMoney,rate, minMoney, maxMoney;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_withdraw_layout);
        bundle = getIntent().getExtras();

        if (null != bundle) {
            money = bundle.getString("money");
            rate = bundle.getDouble("rate");
            minMoney = bundle.getDouble("minMoney");
            maxMoney = bundle.getDouble("maxMoney");
            adMoney = bundle.getDouble("adMoney");
        }
        dialogUtils = new DialogUtils(this);

    }

    @Override
    public void setUpViews() {
        heardTitle.setText("提现");
        btnCommit.setText("申请提现");
        btnCommit.setEnabled(false);
        IsBang.setImmerHeard(this, rlHead);
        tvMoney.setText(money);
        getMyBank();
    }

    @Override
    public void setUpLisener() {
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double money1 = 0;
                double money2 = 0;
                if (!StrUtils.isEmpty(etMoney.getText().toString().trim())) {
                    money1 = Double.parseDouble(etMoney.getText().toString().trim());
                    ll01.setVisibility(View.GONE);
                    ll02.setVisibility(View.VISIBLE);
                    if (money1 > maxMoney&&maxMoney<=Double.parseDouble(money)) {
                        tvState.setText("单笔最大提现金额为：" + maxMoney);
                        tvState.setTextColor(Color.parseColor("#FB4B2A"));
                        btnCommit.setEnabled(false);
                    } else if (money1 < minMoney&&minMoney<=Double.parseDouble(money)) {
                        tvState.setText("单笔最小提现金额为：" + minMoney);
                        tvState.setTextColor(Color.parseColor("#FB4B2A"));
                        btnCommit.setEnabled(false);
                    } else {
                        if (money1 > Double.parseDouble(money)) {
                            tvState.setText("输入金额已超过可提现余额");
                            tvState.setTextColor(Color.parseColor("#FB4B2A"));
                            btnCommit.setEnabled(false);
                        } else {
                            tvState.setTextColor(Color.parseColor("#6E6E6E"));
                            double freeMoney = adMoney - money1;
                            if (freeMoney >= 0) {
                                tvState.setText("此次提现免手续费");
                            } else {
                                tvState.setText(adMoney+"元免手续费，其余额外扣除¥" + StrUtils.moenyToDH((Math.abs(freeMoney * rate * 0.01)) + "") + "手续费（费率" + rate + "%）");
                            }
                            btnCommit.setEnabled(true);
                        }
                    }

                } else {
                    money1 = 0;
                    btnCommit.setEnabled(false);
                    ll01.setVisibility(View.VISIBLE);
                    ll02.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.heard_back, R.id.ll_bank, R.id.ll_delete, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.ll_bank:
                if (banks.size() > 0) {
                    showDialog(banks);
                } else {
                    isFrist = false;
                    getMyBank();
                }
                break;
            case R.id.ll_delete:
                etMoney.setText("");
                break;
            case R.id.btn_commit:
                if (check()) {
                    commit();
                }
                break;

        }
    }

    private boolean check() {
        if (StrUtils.equals(tvBankName.getText().toString(), "请选择")) {
            dialogUtils.noBtnDialog("请选择银行卡");
            return false;
        }
        if (StrUtils.isEmpty(etMoney.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入提现金额");
            return false;
        }
        return true;
    }

    private void getMyBank() {
        addSubscription(RequestClient.GetSupplierBankCards(this, new NetSubscriber<BaseResultBean<List<MyBankBean>>>(this, true) {
            @Override
            public void onResultNext(BaseResultBean<List<MyBankBean>> model) {
                if (model.data.size() > 0) {
                    if (!isFrist) {
                        showDialog(model.data);
                    } else {
                        banks = model.data;
                    }
                } else {
                    dialogUtils.simpleDialog("暂无已绑定银行卡，无法提现，是否前往添加？", new DialogUtils.ConfirmClickLisener() {
                        @Override
                        public void onConfirmClick(View v) {
                            dialogUtils.dismissDialog();
                            jumpActivity(AddBankActivity.class);
                        }
                    }, true);
                }

            }
        }));
    }

    private void showDialog(final List<MyBankBean> banks) {
        dialogUtils.myBankDiaolog(new DialogUtils.ConfirmMyBankLisener() {
            @Override
            public void onConfirmClick(MyBankBean bean, int position) {
                for (int i = 0; i < banks.size(); i++) {
                    if (i == position) {
                        banks.get(i).isChose = true;
                    } else {
                        banks.get(i).isChose = false;
                    }
                }
                tvBankName.setText(bean.BankTypeName);
                id = bean.Id;
                dialogUtils.dismissDialog();
            }
        }, banks, true);
    }

    private void commit() {
        map.put("BankCardId", id);
        map.put("Money", etMoney.getText().toString().trim());
        addSubscription(RequestClient.Withdrawal(map, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                showShortToast("提现成功");
                finish();
            }
        }));
    }
}
