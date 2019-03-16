package com.snh.snhseller.ui.product;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DBManager;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.StrUtils;

import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/12<p>
 * <p>changeTime：2019/3/12<p>
 * <p>version：1<p>
 */
public class AddSkuActivity extends BaseActivity {
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
    @BindView(R.id.et_01)
    EditText et01;
    @BindView(R.id.et_02)
    EditText et02;
    @BindView(R.id.et_03)
    EditText et03;
    @BindView(R.id.et_04)
    EditText et04;
    @BindView(R.id.et_05)
    EditText et05;
    @BindView(R.id.et_06)
    EditText et06;

    private Map<String, Object> map = new TreeMap<>();
    private int ShopGoodsId;
    private Bundle bundle;
    private DialogUtils dialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_addsku_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            ShopGoodsId = bundle.getInt("goodsId");
        }
        dialogUtils = new DialogUtils(this);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead);
        heardTitle.setText("添加规格");
        heardTvMenu.setText("保存");
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

    private boolean check() {
        if (StrUtils.isEmpty(et01.getText().toString().trim())) {
            showLongToast("请填写规格名称");
            return false;
        }
        if (StrUtils.isEmpty(et02.getText().toString().trim())) {
            showLongToast("请填写规格值");
            return false;
        }
        if (StrUtils.isEmpty(et03.getText().toString().trim())) {
            showLongToast("请填写二批价");
            return false;
        }
        if (StrUtils.isEmpty(et04.getText().toString().trim())) {
            showLongToast("请填写终端价");
            return false;
        }
        if(StrUtils.isEmpty(et05.getText().toString().trim())){
            showLongToast("请填写库存");
            return false;
        }
        if (!StrUtils.isEmpty(et05.getText().toString().trim())) {
            if (Integer.parseInt(et05.getText().toString().trim()) <= 0) {
                showLongToast("库存数量必须大于零");
                return false;
            }
        }
        if (!StrUtils.isEmpty(et06.getText().toString().trim())) {
            if (Integer.parseInt(et06.getText().toString().trim()) <= 0) {
                showLongToast("运费必须大于零");
                return false;
            }
        }
        return true;
    }

    private void setData() {
        map.put("SupplierId", DBManager.getInstance(this).getUseId());
        map.put("ShopGoodsId", ShopGoodsId);
        map.put("NormName", et01.getText().toString().trim());
        map.put("NormValue", et02.getText().toString().trim());
        map.put("Price", et03.getText().toString().trim());
        map.put("MarketPrice", et04.getText().toString().trim());
        map.put("Inventory", et05.getText().toString().trim());
        map.put("Weight", et06.getText().toString().trim());
    }

    @OnClick({R.id.heard_back, R.id.heard_tv_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.heard_tv_menu:
                if (check()) {
                    setData();
                    add();
                }
                break;
        }
    }

    private void add() {
        addSubscription(RequestClient.AddNorm(map, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.twoBtnDialog("添加成功，是否继续添加", new DialogUtils.ChoseClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        clean();
                        dialogUtils.dismissDialog();
                    }

                    @Override
                    public void onCancelClick(View v) {
                        dialogUtils.dismissDialog();
                        AddSkuActivity.this.finish();
                    }
                },false);
//                dialogUtils.simpleDialog("添加成功", new DialogUtils.ConfirmClickLisener() {
//                    @Override
//                    public void onConfirmClick(View v) {
//                        clean();
//                        dialogUtils.dismissDialog();
//                    }
//                }, false);
            }
        }));
    }

    private void clean() {
//        et01.setText("");
        et02.setText("");
        et02.setFocusable(true);
        et02.setFocusableInTouchMode(true);
        et02.requestFocus();
        et03.setText("");
        et04.setText("");
        et05.setText("");
        et06.setText("");

    }
}
