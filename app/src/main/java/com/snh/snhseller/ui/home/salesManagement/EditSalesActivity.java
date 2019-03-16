package com.snh.snhseller.ui.home.salesManagement;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.SalesSupplierAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.salesBean.SalesmanBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.Md5Utils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.wediget.RecycleViewDivider;

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
 * <p>creatTime：2019/3/12<p>
 * <p>changeTime：2019/3/12<p>
 * <p>version：1<p>
 */
public class EditSalesActivity extends BaseActivity {
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
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.et_age)
    EditText etAge;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_sh)
    TextView tvSh;
    @BindView(R.id.iv_eye)
    ImageView ivEye;

    private int type;
    private Bundle bundle;

    private List<String> options1Items = new ArrayList<>();

    private boolean isEdite = false;
    private boolean isClose = false;
    private SalesmanBean bean;
    private SalesSupplierAdapter adapter;

    private Map<String, Object> map = new TreeMap<>();
    private DialogUtils dialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_editsales_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            type = bundle.getInt("type");
            bean = bundle.getParcelable("data");
        }
        dialogUtils = new DialogUtils(this);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead);
        heardTitle.setText("业务员详情");
        options1Items.clear();
        options1Items.add("男");
        options1Items.add("女");
        setRecyclerView();
        if (type == 1) {
            heardTvMenu.setVisibility(View.GONE);
            btnCommit.setVisibility(View.VISIBLE);
            tvSh.setVisibility(View.GONE);
        }

        if (type == 2) {
            heardTvMenu.setText("编辑");
            btnCommit.setVisibility(View.GONE);
            etAddress.setText(bean.Address);
            etAge.setText(bean.Age + "");
            etName.setText(bean.SalesmanRealName);
            etPhone.setText(bean.PhoneNumber);
            tvSex.setText(bean.Sex);
            etPsw.setHint("重置业务员密码");
            setIsEdit(isEdite);
            adapter.setNewData(bean.ManagerSuppList);
        }
    }

    private String outStr = "";
    @Override
    public void setUpLisener() {

    }

    public void setRecyclerView() {
        adapter = new SalesSupplierAdapter(R.layout.item_sales_supplier_layout, null);
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

    @OnClick({R.id.heard_back, R.id.heard_tv_menu, R.id.tv_sex, R.id.btn_commit, R.id.iv_eye})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.heard_tv_menu:
                if (isEdite) {
                    if (check()) {
                        setData();
                        edit();
                    }
                } else {
                    heardTvMenu.setText("保存");
                    isEdite = true;
                    setIsEdit(isEdite);
                }
                break;
            case R.id.tv_sex:
                showPickView();
                break;
            case R.id.btn_commit:
                if (check()) {
                    setData();
                    add();
                }
                break;
            case R.id.iv_eye:
                if (isClose) {
                    ivEye.setBackgroundResource(R.drawable.eye_close_bg);
                    etPsw.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isClose = false;
                } else {
                    ivEye.setBackgroundResource(R.drawable.eye_open_bg);
                    etPsw.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL | InputType.TYPE_CLASS_TEXT);
                    isClose = true;
                }
                break;
        }
    }

    private void showPickView() {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(EditSalesActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1);
                tvSex.setText(tx);
            }
        }).setDecorView((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).build();
        pvOptions.setPicker(options1Items);
        pvOptions.show();
    }

    private void setIsEdit(boolean isEdit) {
        etAddress.setFocusable(isEdit);
        etAddress.setFocusableInTouchMode(isEdit);
        etAge.setFocusable(isEdit);
        etAge.setFocusableInTouchMode(isEdit);
        etName.setFocusable(isEdit);
        etName.setFocusableInTouchMode(isEdit);
        etPhone.setFocusable(isEdit);
        etPhone.setFocusableInTouchMode(isEdit);
        etPsw.setFocusable(isEdit);
        etPsw.setFocusableInTouchMode(isEdit);

        tvSex.setEnabled(isEdit);
    }

    private void clean() {
        etAddress.setText("");
        etAge.setText("");
        etName.setText("");
        etPhone.setText("");
        etPsw.setText("");
    }

    private void setData() {
        map.put("RealName", etName.getText().toString().trim());
        map.put("Pwd", Md5Utils.md5(etPsw.getText().toString().trim()));
        map.put("Sex", tvSex.getText().toString().trim());
        map.put("Age", etAge.getText().toString().trim());
        map.put("PhoneNumber", etPhone.getText().toString().trim());
        map.put("Address", etAddress.getText().toString().trim());
        if (type == 2) {
            map.put("SalesmanId", bean.SalesmanId);
        }
    }

    private boolean check() {
        if (StrUtils.isEmpty(etName.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入真实姓名");
            return false;
        }


        if (StrUtils.isEmpty(etAge.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入年龄");
            return false;
        }
        if(Integer.parseInt(etAge.getText().toString().trim())<16|Integer.parseInt(etAge.getText().toString().trim())>60){
            dialogUtils.noBtnDialog("年龄必须大于16，小于60");
            return false;
        }
        if (StrUtils.isEmpty(etPhone.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入手机号码");
            return false;
        }
        if (StrUtils.isEmpty(etAddress.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入地址");
            return false;
        }
        if (StrUtils.equals(tvSex.getText().toString().trim(), "请选择业务员性别")) {
            dialogUtils.noBtnDialog("请选择业务员性别");
            return false;
        }
        if (StrUtils.isEmpty(etPsw.getText().toString().trim()) && !StrUtils.isPsw(etPsw.getText().toString().trim())) {
            dialogUtils.noBtnDialog("密码不能为空，且必须为6-20数字字母组合而成");
            return false;
        }
        return true;
    }

    private void add() {
        addSubscription(RequestClient.AddSalesman(map, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.simpleDialog("添加成功", new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        clean();
                        dialogUtils.dismissDialog();
                    }
                }, false);
            }
        }));
    }

    private void edit() {
        addSubscription(RequestClient.EditSalesman(map, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.simpleDialog("编辑保存成功", new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        dialogUtils.dismissDialog();
                        EditSalesActivity.this.finish();
                    }
                }, false);
            }
        }));
    }
}
