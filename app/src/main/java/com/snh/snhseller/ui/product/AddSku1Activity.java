package com.snh.snhseller.ui.product;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.snh.library_base.db.DBManager;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.SkuBean;
import com.snh.snhseller.requestApi.RequestClient;
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
public class AddSku1Activity extends BaseActivity {
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
    private SkuBean bean;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_addsku1_layout);
        bundle = getIntent().getExtras();
        setImm(false);
        if (null != bundle) {
            ShopGoodsId = bundle.getInt("goodsId");
            bean = bundle.getParcelable("data");
        }
        dialogUtils = new DialogUtils(this);
    }

    @Override
    public void setUpViews() {
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        IsBang.setImmerHeard(this, rlHead,"#ffffff");
        heardTitle.setText("设置规格");
//        heardTvMenu.setText("保存");
        if (null != bean) {
            et01.setText(bean.NormName);
            et02.setText(bean.NormValue.substring(bean.NormValue.indexOf("*")+1,bean.NormValue.length()));
            et03.setText(StrUtils.moenyToDH(bean.Price + ""));
            et04.setText(StrUtils.moenyToDH(bean.RetailPrice + ""));
            et05.setText(bean.Inventory + "");
            if (!StrUtils.isEmpty(bean.Weight + "") && bean.Weight > 0) {
                et06.setText(bean.Weight + "");
            }
        }
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
//        if (StrUtils.isEmpty(et03.getText().toString().trim())) {
//            showLongToast("请填写二批价");
//            return false;
//        }
        if (!StrUtils.isEmpty(et03.getText().toString().trim())) {
            if (Double.parseDouble(et03.getText().toString().trim()) <= 0) {
                showLongToast("二批价必须大于零");
                return false;
            }
        }
//        if (StrUtils.isEmpty(et04.getText().toString().trim())) {
//            showLongToast("请填写终端价");
//            return false;
//        }
        if (!StrUtils.isEmpty(et04.getText().toString().trim())) {
            if (Double.parseDouble(et04.getText().toString().trim()) <= 0) {
                showLongToast("终端价必须大于零");
                return false;
            }
        }
        if (StrUtils.isEmpty(et03.getText().toString().trim()) && StrUtils.isEmpty(et04.getText().toString().trim())) {
            dialogUtils.noBtnDialog("二批价与终端价必须输入一个");
            return false;
        }
        if (!StrUtils.isEmpty(et03.getText().toString().trim()) && !StrUtils.isEmpty(et04.getText().toString().trim())) {
            if (Double.parseDouble(et03.getText().toString().trim()) > Double.parseDouble(et04.getText().toString().trim())) {
                showLongToast("二批价不能大于终端价");
                return false;
            }
        }
        if (StrUtils.isEmpty(et05.getText().toString().trim())) {
            showLongToast("请填写库存");
            return false;
        }

        if (!StrUtils.isEmpty(et06.getText().toString().trim())) {
            if (Double.parseDouble(et06.getText().toString().trim()) <= 0) {
                showLongToast("重量必须大于零");
                return false;
            }
        }
        return true;
    }

    private void setData() {
        if (null != bean) {
            map.put("NormId", bean.NormId);
            map.put("ShopGoodsId", ShopGoodsId);
        } else {

            map.put("SupplierId", DBManager.getInstance(this).getUseId());
        }
        map.put("ShopGoodsId", ShopGoodsId);
        map.put("NormName", et01.getText().toString().trim());
        map.put("NormValue", et02.getText().toString().trim());
        if (StrUtils.isEmpty(et03.getText().toString().trim())) {
            map.put("Price", et04.getText().toString().trim());
        } else {
            map.put("Price", et03.getText().toString().trim());
        }
        if (StrUtils.isEmpty(et04.getText().toString().trim())) {
            map.put("MarketPrice", et03.getText().toString().trim());
        } else {
            map.put("MarketPrice", et04.getText().toString().trim());
        }
        map.put("Inventory", et05.getText().toString().trim());
        if (!StrUtils.isEmpty(et06.getText().toString().trim())) {
            map.put("Weight", et06.getText().toString().trim());
        }
    }

    @OnClick({R.id.heard_back, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.btn_commit:
                if (check()) {
                    setData();
                    add();
                }
                break;
        }
    }

    private void add() {
        if (null != bean) {
            addSubscription(RequestClient.EditeNorm(map, this, new NetSubscriber<BaseResultBean>(this, true) {
                @Override
                public void onResultNext(BaseResultBean model) {
                    showShortToast("编辑成功！");
                    AddSku1Activity.this.finish();
                }
            }));
        } else {

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
                            AddSku1Activity.this.finish();
                        }
                    }, false);
                }
            }));
        }
    }


    private void clean() {
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
