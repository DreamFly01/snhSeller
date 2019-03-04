package com.snh.snhseller.ui.home.account;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.beanDao.UserEntity;
import com.snh.snhseller.greendao.UserEntityDao;
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
 * <p>creatTime：2019/2/23<p>
 * <p>changeTime：2019/2/23<p>
 * <p>version：1<p>
 */
public class ModifInfoActivity extends BaseActivity {
    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_tv_menu)
    Button heardTvMenu;
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    private String name = "";
    private String phone = "";
    private String email = "";
    private String desc = "";
    private Bundle bundle;
    private int type = 0;
    private Map<String, Object> map = new TreeMap<>();
    private DialogUtils dialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_modifyinfo_layout);
        bundle = getIntent().getExtras();
        dialogUtils = new DialogUtils(this);
        if (null != bundle) {
            name = bundle.getString("name");
            phone = bundle.getString("phone");
            email = bundle.getString("email");
            desc = bundle.getString("desc");
            type = bundle.getInt("type");
        }
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead);
        if (type == 4) {
            etInfo.setHint("请输入店铺描述");
            heardTitle.setText("店铺描述");
            if (!StrUtils.isEmpty(desc)) {
                etInfo.setText(desc);
            }
        }

        if (!StrUtils.isEmpty(name)) {
            etInfo.setText(name);
            heardTitle.setText("店长姓名");
        }
        if (!StrUtils.isEmpty(phone)) {
            etInfo.setText(phone);
            heardTitle.setText("店长手机");
        }
        if (!StrUtils.isEmpty(email)) {
            heardTitle.setText("店长邮箱");
            etInfo.setText(email);
        }
    }

    @Override
    public void setUpLisener() {
        etInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                heardTvMenu.setEnabled(true);
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

    @OnClick({R.id.heard_back, R.id.heard_tv_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.heard_tv_menu:
                if (check()) {
                    if (type == 4) {
                        modifDesc();
                    } else {
                        setData();
                        modifInfo();
                    }
                }
                break;
        }
    }

    private void setData() {
        map.put("SupplierId", DBManager.getInstance(this).getUseId());
        if (!StrUtils.isEmpty(name)) {
            map.put("BuinourName", etInfo.getText().toString().trim());
        }
        if (!StrUtils.isEmpty(phone)) {
            map.put("BuinourPhone", etInfo.getText().toString().trim());
        }
        if (!StrUtils.isEmpty(email)) {
            map.put("BuinourQQ", etInfo.getText().toString().trim());
        }
    }

    private boolean check() {
        if (StrUtils.isEmpty(etInfo.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入信息");
            return false;
        }
        return true;
    }

    private void modifDesc() {
        addSubscription(RequestClient.ModifDesc(etInfo.getText().toString().trim(), this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.simpleDialog("修改成功", new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        UserEntityDao userEntityDao = DBManager.getInstance(ModifInfoActivity.this).getDaoSession().getUserEntityDao();
                        UserEntity userEntity = userEntityDao.queryBuilder().build().list().get(0);
                        userEntityDao.deleteAll();
                        userEntity.Introduction = etInfo.getText().toString().trim();
                        userEntityDao.insert(userEntity);
                        showShortToast("修改成功");
                        dialogUtils.dismissDialog();
                        ModifInfoActivity.this.finish();
                    }
                }, false);
            }
        }));
    }

    private void modifInfo() {
        addSubscription(RequestClient.ModifInfo(map, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.simpleDialog("修改成功", new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        UserEntityDao userEntityDao = DBManager.getInstance(ModifInfoActivity.this).getDaoSession().getUserEntityDao();
                        UserEntity userEntity = userEntityDao.queryBuilder().build().list().get(0);
                        userEntityDao.deleteAll();
                        switch (type) {
                            case 1:
                                userEntity.Contacts = etInfo.getText().toString().trim();
                                break;
                            case 2:
                                userEntity.ContactsTel = etInfo.getText().toString().trim();
                                break;
                            case 3:
                                userEntity.ContactsQQ = etInfo.getText().toString().trim();
                                break;
                            case 4:
                                userEntity.Introduction = etInfo.getText().toString().trim();
                                break;
                        }
                        userEntityDao.insert(userEntity);

                        dialogUtils.dismissDialog();
                        ModifInfoActivity.this.finish();
                    }
                }, false);
            }
        }));
    }
}
