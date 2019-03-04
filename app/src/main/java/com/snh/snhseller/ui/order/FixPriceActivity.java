package com.snh.snhseller.ui.order;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.OrderGoodsBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.StrUtils;

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
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.iv_shop_logo)
    ImageView ivShopLogo;
    @BindView(R.id.tv_shopName)
    TextView tvShopName;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.iv_product_logo1)
    ImageView ivProductLogo1;
    @BindView(R.id.tv_GoodsName)
    TextView tvGoodsName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_sku)
    TextView tvSku;
    @BindView(R.id.tv_Number1)
    TextView tvNumber1;
    @BindView(R.id.tv_price1)
    TextView tvPrice1;
    @BindView(R.id.tv_yunfei)
    TextView tvYunfei;
    @BindView(R.id.tv_yunfei1)
    TextView tvYunfei1;
    @BindView(R.id.tv_price2)
    TextView tvPrice2;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.et_yunfei)
    EditText etYunfei;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    private Bundle bundle;
    private OrderGoodsBean bean;
    private double total;
    private String yunfei;
    private String price;

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
        ImageUtils.loadUrlImage(this, bean.shopLogo, ivShopLogo);
        tvShopName.setText(bean.shopName);
        tvState.setText(bean.state);
        tvGoodsName.setText(bean.OrderGoodsName);
        tvNumber1.setText("x" + bean.Number);
        tvPrice.setText("￥" + bean.Price);
        ImageUtils.loadUrlImage(this, bean.OrderGoodsIcon, ivProductLogo1);
        tvPrice1.setText("实收：￥" + bean.Price);
        tvYunfei.setText("运费：￥" + bean.Freight);
        etPrice.setText(bean.Price + "");
        etYunfei.setText(bean.Freight + "");
        yunfei = bean.Freight + "";
        price = bean.Price + "";
        if (Double.parseDouble(etYunfei.getText().toString()) > 0) {
            tvYunfei1.setText("（含运费：￥" + etYunfei.getText().toString() + "）");

        } else {
            tvYunfei1.setText("（包邮）");
        }
        total = Double.parseDouble(etPrice.getText().toString()) + Double.parseDouble(etYunfei.getText().toString());

        tvPrice2.setText("改后总价：￥" + total);
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
                        yunfei = etYunfei.getText().toString();
                        total = Double.parseDouble(etPrice.getText().toString()) + Double.parseDouble(etYunfei.getText().toString());
                    }
                }
                tvPrice2.setText("改后总价：￥" + total);
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
                tvPrice2.setText("改后总价：￥" + total);
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
        addSubscription(RequestClient.ChangePrice(bean.OrderId, bean.OrderGoodsId, price, yunfei, this, new NetSubscriber<BaseResultBean>(this,true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                showShortToast("修改价格成功");
                FixPriceActivity.this.finish();
            }
        }));
    }
}
