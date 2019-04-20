package com.snh.snhseller.ui.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.OrderItemAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.OrderBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.ImageUtils;
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
 * <p>creatTime：2019/2/21<p>
 * <p>changeTime：2019/2/21<p>
 * <p>version：1<p>
 */
public class SendActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone1)
    TextView tvPhone1;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_shop_logo)
    ImageView ivShopLogo;
    @BindView(R.id.tv_shopName)
    TextView tvShopName;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.recyclerView_order)
    RecyclerView recyclerViewOrder;
    @BindView(R.id.tv_youfei)
    TextView tvYoufei;
    @BindView(R.id.tv_all_num)
    TextView tvAllNum;
    @BindView(R.id.tv_TotalMoney1)
    TextView tvTotalMoney1;
    @BindView(R.id.tv_fhfs)
    TextView tvFhfs;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.tv_kdgs)
    TextView tvKdgs;
    @BindView(R.id.ll_02)
    LinearLayout ll02;
    @BindView(R.id.et_kddh)
    EditText etKddh;
    @BindView(R.id.ll_03)
    LinearLayout ll03;
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
    @BindView(R.id.btn_commit)
    Button btnCommit;

    private Bundle bundle;
    private OrderBean bean;
    private OrderItemAdapter adapter;
    private List<String> options1Items = new ArrayList<>();
    private Map<String,Object> map = new TreeMap<>();
    private DialogUtils dialogUtils;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_send_layout);
        bundle = getIntent().getExtras();
        dialogUtils = new DialogUtils(this);
        if (null != bundle) {
            bean = bundle.getParcelable("data");
        }
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("发货");
        btnCommit.setText("提交");
        tvName.setText(bean.ReceivingName);
        tvPhone1.setText(bean.ReceivingPhone);
        tvAddress.setText(bean.ReceivingAddress);
        ImageUtils.loadUrlImage(this, bean.UserIcon, ivShopLogo);
        tvShopName.setText(bean.UserName);

        tvAllNum.setText("共" + bean.OrderGoodsList.size() + "件商品 合计：");
        double totalMoney = 0;
        for (int i = 0; i < bean.OrderGoodsList.size(); i++) {
            totalMoney = totalMoney + bean.OrderGoodsList.get(i).Price;
        }
        tvTotalMoney1.setText("￥" + totalMoney);
        if (bean.Freight > 0) {
            tvYoufei.setText("（含运费：￥" + bean.Freight + "）");
        } else {
            tvYoufei.setText("（包邮）");
        }
        adapter = new OrderItemAdapter(R.layout.item_order_item_layout, bean.OrderGoodsList);
        adapter.setType(2);
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrder.setAdapter(adapter);
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

    @OnClick({R.id.heard_back, R.id.ll_01, R.id.ll_02, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.ll_01:
                type = 0;
                showPickView();
                break;
            case R.id.ll_02:
                type = 1;
                showPickView();
                break;
            case R.id.btn_commit:
                if(check()){
                    setData();
                    commit();
                }
                break;
        }
    }

    private int type = 0;

    private void showPickView() {
        options1Items.clear();
        if (type == 0) {
            options1Items.add("自己联系物流");
            options1Items.add("快递上门取件");
        } else {
            options1Items.add("中通");
            options1Items.add("圆通");
            options1Items.add("申通");
            options1Items.add("顺丰");
            options1Items.add("韵达");
            options1Items.add("汇通");
            options1Items.add("天天");
            options1Items.add("德邦物流");
        }

        OptionsPickerView pvOptions = new OptionsPickerBuilder(SendActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1);
                if (type == 0) {
                    tvFhfs.setText(tx);
                } else {
                    tvKdgs.setText(tx);
                }
            }
        }).setDecorView((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content))
                .build();
        pvOptions.setPicker(options1Items);
        pvOptions.show();
    }
private boolean check(){
        if(StrUtils.equals(tvFhfs.getText().toString().trim(),"请选择物流")){
            dialogUtils.noBtnDialog("请选择物流");
            return false;
        }
        if(StrUtils.equals(tvKdgs.getText().toString().trim(),"请选择快递公司")){
            dialogUtils.noBtnDialog("请选择快递公司");
            return false;
        }
        if(StrUtils.isEmpty(etKddh.getText().toString().trim())){
            dialogUtils.noBtnDialog("请输入快递单号");
            return false;
        }
        return true;
}
    private void setData(){
        map.put("OrderId", bean.OrderId);
        map.put("UserId", bean.UserId);
        map.put("SupplierId", bean.SupplierId);
        map.put("UserName", bean.ReceivingName);
        map.put("PhoneNumber", bean.ReceivingPhone);
        map.put("Address", bean.ReceivingAddress);
        map.put("ShipmentsMethod", tvFhfs.getText().toString().trim());
        map.put("ExpressName", tvKdgs.getText().toString().trim());
        map.put("ExpressNo",etKddh.getText().toString().trim());
    }

    private void commit(){
        addSubscription(RequestClient.ConfirmShipment(map, this, new NetSubscriber<BaseResultBean>(this,true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                showShortToast("发货成功");
                SendActivity.this.finish();
            }
        }));
    }
}
