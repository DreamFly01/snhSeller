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
import com.snh.snhseller.bean.beanDao.UserEntity;
import com.snh.snhseller.bean.supplierbean.SkuBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.db.DBManager;
import com.snh.snhseller.ui.merchantEntry.PerfectMyLocalActivity;
import com.snh.snhseller.utils.Contans;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.SPUtils;
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
    private String productName, SupplierName, SupplierIconUrl;
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
            SupplierName = bundle.getString("SupplierName");
            SupplierIconUrl = bundle.getString("SupplierIconUrl");
        }
        dialogUtils = new DialogUtils(this);
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("确认订单");
        IsBang.setImmerHeard(this, rlHead);
        tvNamePhone.setText(DBManager.getInstance(this).getUserInfo().ShopName + "   " + DBManager.getInstance(this).getUserInfo().ContactsTel);
        UserEntity userEntity = DBManager.getInstance(this).getUserInfo();
        tvAddress.setText(userEntity.Province + userEntity.City + userEntity.Area + " - " + userEntity.Address);
        ImageUtils.loadUrlImage(this, SupplierIconUrl, ivShopLogo);
        ImageUtils.loadUrlImage(this, url, ivProductLogo);
        tvProductName.setText(productName);
        tvShopName.setText(SupplierName);
        setRecyclerView();
        if (skudatas.get(0).Price > 0) {
            for (SkuBean bean : skudatas) {
                totalMoney += bean.total * bean.Price;
            }
        } else {
            for (SkuBean bean : skudatas) {
                totalMoney += bean.total * bean.MarketPrice;
            }
        }
        tvTotalMoney.setText(StrUtils.moenyToDH(totalMoney + ""));
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
        if (isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.tv_commit:
                if ("1".equals(SPUtils.getInstance(this).getString(Contans.IS_FULL))) {
                    postOrder();
                } else if ("0".equals(SPUtils.getInstance(this).getString(Contans.IS_FULL))) {
                    dialogUtils.twoBtnDialog("是否马上完善店铺信息", new DialogUtils.ChoseClickLisener() {
                        @Override
                        public void onConfirmClick(View v) {
                            dialogUtils.dismissDialog();
                            JumpUtils.simpJump(CommitOrderActivity.this, PerfectMyLocalActivity.class, false);
                        }

                        @Override
                        public void onCancelClick(View v) {
                            dialogUtils.dismissDialog();
                        }
                    }, true);
                }
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
                    }, false);
                }
            }
        }));
    }
}
