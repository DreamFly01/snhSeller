package com.snh.snhseller.ui.home.set;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.snh.library_base.db.DBManager;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.jpush.TagAliasOperatorHelper;
import com.snh.snhseller.ui.loging.LogingActivity;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.GlideCacheUtil;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.snh.snhseller.jpush.TagAliasOperatorHelper.ACTION_DELETE;
import static com.snh.snhseller.jpush.TagAliasOperatorHelper.sequence;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/1/2<p>
 * <p>changeTime：2019/1/2<p>
 * <p>version：1<p>
 */
public class SetActivity extends BaseActivity {
    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.ll_02)
    LinearLayout ll02;
    @BindView(R.id.ll_03)
    LinearLayout ll03;
    @BindView(R.id.ll_04)
    LinearLayout ll04;
    @BindView(R.id.ll_05)
    LinearLayout ll05;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R.id.tv_cahe)
    TextView tvCahe;

    private DialogUtils dialogUtils;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_layout);
        dialogUtils = new DialogUtils(this);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead);
        heardTitle.setText("设置");
        tvCahe.setText("("+GlideCacheUtil.getInstance().getCacheSize(this) +")");
    }

    @Override
    public void setUpLisener() {

    }


    @OnClick({R.id.heard_back, R.id.ll_01, R.id.ll_02, R.id.ll_03, R.id.ll_04, R.id.ll_05, R.id.tv_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.ll_01:
                jumpActivity(AccountSafeActivity.class);
                break;
            case R.id.ll_02:
                jumpActivity(QRcodeActivity.class);
                break;
            case R.id.ll_03:
                jumpActivity(AboutActivity.class);
                break;
            case R.id.ll_04:
                jumpActivity(FeedActivity.class);
                break;
            case R.id.ll_05:
                dialogUtils.twoBtnDialog("确认清除缓存么？", new DialogUtils.ChoseClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        GlideCacheUtil.getInstance().clearImageAllCache(SetActivity.this);
                        tvCahe.setText("(0.00K)");
                        dialogUtils.dismissDialog();
                    }

                    @Override
                    public void onCancelClick(View v) {
                        dialogUtils.dismissDialog();
                    }
                }, true);
                break;
            case R.id.tv_exit:
                String phone = DBManager.getInstance(this).getUserInfo().ContactsTel;
                DBManager.getInstance(this).cleanUser();
                NIMClient.getService(AuthService.class).logout();
                Bundle bundle =new Bundle();
                bundle.putString("phone",phone);
                boolean isAliasAction = true;
                int action = ACTION_DELETE;
                TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
                tagAliasBean.action = action;
                sequence++;
                if(isAliasAction){
                    tagAliasBean.alias = DBManager.getInstance(this).getUseId()+"";
                }else{
//            tagAliasBean.tags = 1;
                }
                tagAliasBean.isAliasAction = isAliasAction;
                TagAliasOperatorHelper.getInstance().handleAction(this,sequence,tagAliasBean);
                JumpUtils.dataJump(this, LogingActivity.class, bundle,true);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
