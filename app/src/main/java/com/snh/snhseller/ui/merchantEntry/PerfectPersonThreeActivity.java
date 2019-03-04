package com.snh.snhseller.ui.merchantEntry;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.adapter.TypePriceAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.TypePriceBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/1/22<p>
 * <p>changeTime：2019/1/22<p>
 * <p>version：1<p>
 */
public class PerfectPersonThreeActivity extends BaseActivity {
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
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_commit)
    TextView tvCommit;

    private TypePriceBean bean;
    private List<TypePriceBean> datas = new ArrayList<>();
    private TypePriceAdapter adapter;
    private Bundle bundle;
    private Map<String, Object> dataMap;
    private int flag;
    private String psw;
    private String phone;
    private String name;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_perfectperson3_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            dataMap = (Map<String, Object>) bundle.getSerializable("data");
            flag = bundle.getInt("flag");
            psw = bundle.getString("psw");
            phone = bundle.getString("phone");
            name = bundle.getString("name");
        }
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("缴纳保证金");
        tvName.setText(name);
        setDatas();
        setAdapter();
    }

    @Override
    public void setUpLisener() {

    }

    @OnClick({R.id.heard_back, R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.tv_commit:
                commitData();
                break;
        }
    }

    private void setDatas() {
        bean = new TypePriceBean();
        bean.name = "类目";
        bean.price1 = "非海淘企业店铺";
        bean.price2 = "海淘企业店铺";
//@"类目",@"水果生鲜",@"美容个护",@"家具生活",@"母婴玩具",@"食品保健",@"虚拟商品",@"运动户外",@"数码电器",@"家纺家具"
        for (int i = 0; i < 8; i++) {
            bean = new TypePriceBean();
            switch (i) {
                case 0:
                    bean.name = "生鲜水果";
                    break;
                case 1:
                    bean.name = "美容个护";
                    break;
                case 2:
                    bean.name = "家具生活";
                    break;
                case 3:
                    bean.name = "母婴玩具";
                    break;
                case 4:
                    bean.name = "虚拟商品";
                    break;
                case 5:
                    bean.name = "运动户外";
                    break;
                case 6:
                    bean.name = "数码电器";
                    break;
                case 7:
                    bean.name = "家纺家具";
                    break;
            }
            bean.price1 = "2000";
            bean.price2 = "2000";
            datas.add(bean);
        }
    }

    private void setAdapter() {
        adapter = new TypePriceAdapter(this, R.layout.item_type_price_layout, datas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    private void commitData() {

        switch (flag) {
            case 1:
                addSubscription(RequestClient.MerchantPersonEnter(dataMap, this, new NetSubscriber<BaseResultBean>(this, true) {
                    @Override
                    public void onResultNext(BaseResultBean model) {
                        bundle = new Bundle();
                        bundle.putString("psw", psw);
                        bundle.putString("phone", (String) dataMap.get("PhoneNumber"));
                        JumpUtils.dataJump(PerfectPersonThreeActivity.this, CompleteActivity.class, bundle, false);
                    }
                }));
                break;
            case 2:
                addSubscription(RequestClient.MerchantCompanyEntry(dataMap, this, new NetSubscriber<BaseResultBean>(this, true) {
                    @Override
                    public void onResultNext(BaseResultBean model) {
                        bundle = new Bundle();
                        bundle.putString("psw", psw);
                        bundle.putString("phone", (String) dataMap.get("PhoneNumber"));
                        JumpUtils.dataJump(PerfectPersonThreeActivity.this, CompleteActivity.class, bundle, false);
                    }
                }));
                break;
            case 3:
                break;
        }

    }


}
