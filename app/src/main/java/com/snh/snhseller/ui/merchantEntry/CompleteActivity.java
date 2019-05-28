package com.snh.snhseller.ui.merchantEntry;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.AllUserBean;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.UserBean;
import com.snh.snhseller.bean.salebean.SaleUserBean;
import com.snh.snhseller.db.DBManager;
import com.snh.snhseller.jpush.TagAliasOperatorHelper;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.loging.LogingActivity;
import com.snh.snhseller.ui.salesmanManagement.SalesmanMainActivity;
import com.snh.snhseller.utils.Contans;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.Md5Utils;
import com.snh.snhseller.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.snh.snhseller.jpush.TagAliasOperatorHelper.ACTION_DELETE;
import static com.snh.snhseller.jpush.TagAliasOperatorHelper.sequence;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/1/22<p>
 * <p>changeTime：2019/1/22<p>
 * <p>version：1<p>
 */
public class CompleteActivity extends BaseActivity {

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
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_psw)
    TextView tvPsw;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.ll_loging)
    LinearLayout llLoging;
    @BindView(R.id.tv_url)
    TextView tvUrl;

    private String phone;
    private String psw;
    private Bundle bundle;
    private String pcUrl;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_complete_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            phone = bundle.getString("phone");
            psw = bundle.getString("psw");
            pcUrl = bundle.getString("pcUrl");
        }
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead);
        heardTitle.setText("");
        tvPhone.setText("账户：" + phone);
        tvPsw.setText("密码：" + psw);
        tvUrl.setText(pcUrl);
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


    private long mExitTime = 0;
    public static final int EXIT_TIME = 2000;
    private Toast toast;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > EXIT_TIME) {
                toast = Toast.makeText(this, "再次点击退出应用", Toast.LENGTH_SHORT);
                toast.show();
                mExitTime = System.currentTimeMillis();
            } else {
                if (null != toast) {
                    toast.cancel();
                }
                finishAffinity();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.heard_back, R.id.ll_loging})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                JumpUtils.simpJump(this, MerchantLogingActivity.class, true);
                break;
            case R.id.ll_loging:
                DBManager.getInstance(this).cleanUser();
                NIMClient.getService(AuthService.class).logout();
                boolean isAliasAction = true;
                int action = ACTION_DELETE;
                TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
                tagAliasBean.action = action;
                sequence++;
                if (isAliasAction) {
                    tagAliasBean.alias = DBManager.getInstance(this).getUseId() + "";
                } else {
                }
                tagAliasBean.isAliasAction = isAliasAction;
                TagAliasOperatorHelper.getInstance().handleAction(this, sequence, tagAliasBean);
                loging();
                break;
        }
    }

    private void loging() {
//        addSubscription(RequestClient.LoginPhone(phone, "", Md5Utils.md5(psw), this, new NetSubscriber<BaseResultBean<UserBean>>(this, true) {
//            @Override
//            public void onResultNext(BaseResultBean<UserBean> model) {
//                DBManager.getInstance(CompleteActivity.this).logingSuccess(model.data, CompleteActivity.this);
//            }
//        }));
        addSubscription(RequestClient.Login(phone, Md5Utils.md5(psw), "", this, new NetSubscriber<BaseResultBean<AllUserBean>>(this, true) {
            @Override
            public void onResultNext(BaseResultBean<AllUserBean> model) {
                if (model.data.type == 1) {
                    SPUtils.getInstance(CompleteActivity.this).saveData(Contans.IS_FULL,model.data.isFull+"");
                    SPUtils.getInstance(CompleteActivity.this).savaBoolean(Contans.IS_REGIST,true).commit();
                    DBManager.getInstance(CompleteActivity.this).logingSuccess(model.data, CompleteActivity.this,psw,phone);
                }
            }
        }));
    }
}
