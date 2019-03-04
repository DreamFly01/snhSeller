package com.snh.snhseller.ui.home.set;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.utils.DBManager;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.StrUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/1/2<p>
 * <p>changeTime：2019/1/2<p>
 * <p>version：1<p>
 */
public class AccountSafeActivity extends BaseActivity {
    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.ll_02)
    LinearLayout ll02;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_accoutsafe_layout);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this,rlHead);
        heardTitle.setText("账号安全");
        if (!StrUtils.isEmpty(DBManager.getInstance(this).getUserInfo().ContactsTel)) {
            StringBuffer sbf = new StringBuffer(DBManager.getInstance(this).getUserInfo().ContactsTel);
            tvPhone.setText(sbf.replace(3, 7, "****").toString());
        }else {
            tvPhone.setText("");
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

    @OnClick({R.id.heard_back, R.id.ll_01, R.id.ll_02})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.ll_01:
                Intent intent = new Intent(this, ChangePhoneActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.ll_02:
                jumpActivity(ChangePswActivity.class);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            StringBuffer sbf = new StringBuffer(DBManager.getInstance(this).getUserInfo().ContactsTel);

            tvPhone.setText(sbf.replace(3, 7, "****").toString());
        }
    }
}
