package com.snh.snhseller.ui.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.FixPriceAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.OrderBean;
import com.snh.snhseller.bean.OrderGoodsBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.StrUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/21<p>
 * <p>changeTime：2019/2/21<p>
 * <p>version：1<p>
 */
public class FixPriceActivity extends BaseActivity {


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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_price1)
    TextView tvPrice1;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.tv_yunfei)
    TextView tvYunfei;
    @BindView(R.id.et_yunfei)
    EditText etYunfei;
    @BindView(R.id.tv_yunfei1)
    TextView tvYunfei1;
    @BindView(R.id.tv_price2)
    TextView tvPrice2;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    private Bundle bundle;
    private OrderBean bean;
    private double total;
    private String yunfei;
    private String price;

    private FixPriceAdapter adapter;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fixprice_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            bean = bundle.getParcelable("data");
        }
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("改价格");
        btnCommit.setText("提交");
        IsBang.setImmerHeard(this, rlHead);

        tvPrice1.setText("实收：¥" + bean.OrderPrice);
        tvYunfei.setText("运费：¥" + bean.Freight);
        etPrice.setText(bean.OrderPrice + "");
        etYunfei.setText(bean.Freight + "");
        etYunfei.setVisibility(View.GONE);
        tvYunfei1.setVisibility(View.GONE);
        if (Double.parseDouble(etYunfei.getText().toString()) > 0) {
            tvYunfei1.setText("（含运费：¥" + etYunfei.getText().toString() + "）");
        } else {
            tvYunfei1.setText("（到店自取）");
        }
        yunfei = bean.Freight + "";
        price = bean.OrderPrice + "";
        total = Double.parseDouble(etPrice.getText().toString()) + Double.parseDouble(etYunfei.getText().toString());
        tvPrice2.setText("改后总价：¥" + total);
        adapter = new FixPriceAdapter(R.layout.item_fixprice_layout, bean.OrderGoodsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void setUpLisener() {
        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StrUtils.isEmpty(etPrice.getText().toString())) {
                    price = "0";
                    if (StrUtils.isEmpty(etYunfei.getText().toString())) {
                        yunfei = "0";
                        total = 0;
                    } else {
                        yunfei = etYunfei.getText().toString();
                        total = Double.parseDouble(etYunfei.getText().toString());
                    }
                } else {
                    price = etPrice.getText().toString();
                    if (StrUtils.isEmpty(etYunfei.getText().toString())) {
                        yunfei = "0";
                        total = 0;
                    } else {
                        try{
                            yunfei = etYunfei.getText().toString();
                            total = Double.parseDouble(etPrice.getText().toString()) + Double.parseDouble(etYunfei.getText().toString());
                        }catch (Exception e)
                        {
                            showShortToast("请输入正确的金额");
                        }

                    }
                }
                tvPrice2.setText("改后总价：¥" + total);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etYunfei.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StrUtils.isEmpty(etYunfei.getText().toString())) {
                    yunfei = "0";
                    if (StrUtils.isEmpty(etPrice.getText().toString())) {
                        price = "0";
                        total = 0;
                    } else {
                        price = etPrice.getText().toString();
                        total = Double.parseDouble(etPrice.getText().toString());
                    }
                } else {
                    yunfei = etYunfei.getText().toString();
                    if (StrUtils.isEmpty(etPrice.getText().toString())) {
                        price = "0";
                        total = 0;
                    } else {
                        price = etPrice.getText().toString();
                        total = Double.parseDouble(etPrice.getText().toString()) + Double.parseDouble(etYunfei.getText().toString());
                    }
                }
                tvPrice2.setText("改后总价：¥" + total);
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

    @OnClick({R.id.heard_back, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.btn_commit:
                changePrice();
                break;
        }
    }

    private void changePrice() {
        addSubscription(RequestClient.ChangePrice(bean.OrderId, price, yunfei, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                showShortToast("修改价格成功");
                FixPriceActivity.this.finish();
            }
        }));
    }
}
