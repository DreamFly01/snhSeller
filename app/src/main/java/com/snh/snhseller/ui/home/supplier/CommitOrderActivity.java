package com.snh.snhseller.ui.home.supplier;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.OrderSukAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.NormsBean;
import com.snh.snhseller.bean.supplierbean.SkuBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DBManager;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.wediget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：提交订单页面<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/8<p>
 * <p>changeTime：2019/3/8<p>
 * <p>version：1<p>
 */
public class CommitOrderActivity extends BaseActivity {
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
    @BindView(R.id.tv_name_phone)
    TextView tvNamePhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_change_address)
    LinearLayout llChangeAddress;
    @BindView(R.id.iv_shop_logo)
    ImageView ivShopLogo;
    @BindView(R.id.tv_shopName)
    TextView tvShopName;
    @BindView(R.id.iv_product_logo)
    ImageView ivProductLogo;
    @BindView(R.id.tv_product_Name)
    TextView tvProductName;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_TotalMoney)
    TextView tvTotalMoney;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.et_msg)
    EditText etMsg;

    private String url;
    private String productName;
    private int goodsId;
    private int shopId;
    private Bundle bundle;
    private OrderSukAdapter adapter;
    private List<SkuBean> skudatas = new ArrayList<>();
    private String levelWord = "";
    private double totalMoney = 0;
    private String payMethod;
    private DialogUtils dialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_commitorder_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            goodsId = bundle.getInt("goodsId");
            shopId = bundle.getInt("shopId");
            url = bundle.getString("url");
            productName = bundle.getString("name");
            skudatas = bundle.getParcelableArrayList("data");
            payMethod = bundle.getString("payMethod");
        }
        dialogUtils = new DialogUtils(this);
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("确认订单");
        tvNamePhone.setText(DBManager.getInstance(this).getUserInfo().Contacts + "   " + DBManager.getInstance(this).getUserInfo().ContactsTel);
        tvAddress.setText(DBManager.getInstance(this).getUserInfo().Address);
        ImageUtils.loadUrlImage(this, DBManager.getInstance(this).getUserInfo().Logo, ivShopLogo);
        ImageUtils.loadUrlImage(this, url, ivProductLogo);
        tvProductName.setText(productName);
        tvShopName.setText(DBManager.getInstance(this).getUserInfo().ShopName);
        setRecyclerView();
        for (SkuBean bean : skudatas) {
            totalMoney += bean.total * bean.Price;
        }
        tvTotalMoney.setText(totalMoney + "");
    }

    @Override
    public void setUpLisener() {

    }

    private void setRecyclerView() {
        adapter = new OrderSukAdapter(R.layout.item_sku_order_layout, null);
        adapter.setNewData(skudatas);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL, R.drawable.line));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.heard_back, R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                break;
            case R.id.tv_commit:
                postOrder();
                break;
        }
    }

    private List<NormsBean> data = new ArrayList<>();
    private NormsBean normsBean;

    private void setData() {
        for (SkuBean bean : skudatas) {
            normsBean = new NormsBean();
            normsBean.NormId = bean.NormId;
            normsBean.NumberUnits = bean.total;
            normsBean.SumPrice = bean.Price * bean.total;
            data.add(normsBean);

        }
    }

    private void postOrder() {
        setData();
        levelWord = etMsg.getText().toString().trim();

        addSubscription(RequestClient.PostOrder(shopId, goodsId, totalMoney, levelWord, payMethod, data, this, new NetSubscriber<BaseResultBean<String>>(this, true) {
            @Override
            public void onResultNext(BaseResultBean<String> model) {
                if (StrUtils.isEmpty(payMethod)) {

                    bundle = new Bundle();
                    bundle.putString("orderNo", model.data);
                    bundle.putDouble("totalMoney", totalMoney);
                    JumpUtils.dataJump(CommitOrderActivity.this, PayActivity.class, bundle, true);
                } else {
                    dialogUtils.simpleDialog("下单成功", new DialogUtils.ConfirmClickLisener() {
                        @Override
                        public void onConfirmClick(View v) {
                            CommitOrderActivity.this.finish();
                        }
                    },false);
                }
            }
        }));
    }
}
